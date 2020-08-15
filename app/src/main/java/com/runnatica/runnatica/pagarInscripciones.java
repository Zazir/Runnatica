package com.runnatica.runnatica;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.mercadopago.android.px.core.MercadoPagoCheckout;
import com.mercadopago.android.px.model.Payment;
import com.mercadopago.android.px.model.exceptions.MercadoPagoError;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.runnatica.runnatica.Config.PaypalConfig;
import com.runnatica.runnatica.PDF.PlantillaPDF;
import com.runnatica.runnatica.poho.Usuario;
import com.runnatica.runnatica.repositorio.mercadopago.models.ExcludedPaymentType;
import com.runnatica.runnatica.repositorio.mercadopago.models.Item;
import com.runnatica.runnatica.repositorio.mercadopago.models.PagoDetalles;
import com.runnatica.runnatica.repositorio.mercadopago.models.Payer;
import com.runnatica.runnatica.repositorio.mercadopago.models.PaymentMethods;
import com.runnatica.runnatica.repositorio.mercadopago.models.ResponsePago;
import com.runnatica.runnatica.repositorio.mercadopago.retrofit.RetrofitApi;
import com.runnatica.runnatica.repositorio.mercadopago.retrofit.RetrofitClient;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.runnatica.runnatica.carrera_vista1.FECHA_COMPETENCIA;
import static com.runnatica.runnatica.carrera_vista1.IDS_FORANEOS;
import static com.runnatica.runnatica.carrera_vista1.ID_COMPETENCIA;
import static com.runnatica.runnatica.carrera_vista1.LUGAR_COMPETENCIA;
import static com.runnatica.runnatica.carrera_vista1.MONTO;
import static com.runnatica.runnatica.carrera_vista1.NOMBRE_COMPETENCIA;
import static com.runnatica.runnatica.carrera_vista1.ORGANIZADOR;

public class pagarInscripciones extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private Usuario usuario = Usuario.getUsuarioInstance();

    private Button paypal, MercadoPago;
    PlantillaPDF plantillaPDF = new PlantillaPDF(pagarInscripciones.this);

    BottomNavigationView MenuUsuario;

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)//Seleccionado el modo sandbox
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);

    Calendar calendar = Calendar.getInstance();
    final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
    Usuario usuario2 = Usuario.getUsuarioInstance();

    private String monto;
    private int total;
    private String ids_foraneos;
    private String id_competencia, NombreCompetencia, Fecha1, Lugar, Organizador;

    private String dominio;

    //MercadoPago
    private Retrofit retrofit;
    private RetrofitApi retrofitApi;
    private Disposable disposable;
    private static final String PUBLIC_KEY = "TEST-fda95f10-45da-49ec-89c3-f9d8ef10ad73"; //reemplazar por su public key
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //MercadoPago
        retrofit = RetrofitClient.getInstance();
        retrofitApi = retrofit.create(RetrofitApi.class);
        //MercadopAGO

        dominio = getString(R.string.ip);


        setContentView(R.layout.activity_pagar_inscripciones);

        obtenerPreferencias();

        paypal = (Button)findViewById(R.id.btnPaypal);
        MercadoPago = (Button) findViewById(R.id.btnMercadoPago);
        //mWalletFragment = (SupportWalletFragment)getSupportFragmentManager().findFragmentByTag(WALLET_FRAGMENT_ID);
        paypal.setEnabled(false);

        getLastViewData();
        //Toast.makeText(pagarInscripciones.this, ""+NombreCompetencia, Toast.LENGTH_SHORT).show();

        if (validarPermisos()) {
            paypal.setEnabled(true);
        } else {
            paypal.setEnabled(false);
        }

        // ------------------------------> Iniciar el servicio Paypal
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        // ------------------------------> PAYPAL

        paypal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerPago();
            }
        });MercadoPago.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                generarPago();
            }
        });
    }

    private void generarPago() {
        List<Item> list = new ArrayList<>();//lista con datos de la venta
        Item item = new Item();
        if (monto == null) {
            Toast.makeText(this, "Hubo un error al cargar la información de la competencia, vuelve a seleccionar la competenca", Toast.LENGTH_SHORT).show();
        } else if (ids_foraneos.length() < 1) {
            total = Integer.parseInt(monto);
        }else if (ids_foraneos.length() > 1){
            total = Integer.parseInt(monto) * (this.total+1);
        }
        if(total <300){
            Toast.makeText(this, "No se pueden hacer pagos con Tarjeta ni en efectivo por montos minimos de 300 MXN", Toast.LENGTH_SHORT).show();
        }else {
            item.setUnitPrice(Double.parseDouble(String.valueOf(total))); //precio unitario de producto
            item.setTitle(Organizador); //titulo de la venta del producto
            item.setQuantity(1); //cantidad de productos a vender
            item.setDescription(NombreCompetencia); //descripcion del producto
            item.setCurrencyId("MXN"); //moneda de pais del precio
            list.add(item); //agregamos los detalles a la lista

            Payer payer = new Payer(); //objeto con los datos de usuario comprador
            payer.setEmail(usuario2.getCorreo()); //email del usuario comprador


            List<ExcludedPaymentType> list1 = new ArrayList<>();//lista con datos de la venta
            ExcludedPaymentType itempay = new ExcludedPaymentType();
            itempay.setId("prepaid_card"); //EXCLUIR MEDIOS DE PAG
            list1.add(itempay); //agregamos los detalles a la lista


            PaymentMethods methods = new PaymentMethods();
            methods.setExcludedPaymentTypes(list1); //precio unitario de producto


            PagoDetalles pagoDetalles = new PagoDetalles(); //generamos el objeto para enviar a mercado pago
            pagoDetalles.setPayer(payer);
            pagoDetalles.setItems(list);
            pagoDetalles.setmPayment_methods(methods);


            disposable = retrofitApi.obtenerDatosPago(pagoDetalles).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(new DisposableObserver<ResponsePago>() {
                        @Override
                        public void onNext(ResponsePago responsePago) {
                            //una vez generada el id de compra lo mandamos a mercado pago y se genera la ventana de tarjetas
                            new MercadoPagoCheckout.Builder(PUBLIC_KEY, responsePago.getId())
                                    .build()
                                    .startPayment(pagarInscripciones.this, 12);
                        }

                        @Override
                        public void onError(Throwable e) {
                            Log.d("aca", "error = " + e.getMessage());

                        }

                        @Override
                        public void onComplete() {

                        }
                    });
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        //primeramente aca se borraron una linea
        super.onActivityResult(requestCode, resultCode, data);

        //MercadoPago
        if(requestCode == 12){
            if (resultCode == MercadoPagoCheckout.PAYMENT_RESULT_CODE) {
                final Payment payment = (Payment) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_PAYMENT_RESULT);//payment almacena los datos de la compra

                if(payment.getPaymentStatus().equals("approved")){

                    //compra aprobada
                    Toast.makeText(this, "Pago Realizado", Toast.LENGTH_SHORT).show();
                    //Petición al WS para mostrar en bd la inscripción
                    reflejarInscripcion(dominio + "inscribirUsuario.php?" +
                            "id_usuario="+ usuario.getId() +
                            "&id_competencia=" + id_competencia);

                    inscribirForaneos(dominio + "inscribirForaneo.php?" +
                            "id_usuario=" + usuario.getId() +
                            "&id_competencia=" + id_competencia+
                            "&id_foraneo=" + ids_foraneos.replaceAll(" ", "%20"));

                    crearPDF(currentDate);
                    Felicidades();

                }  else if(payment.getPaymentStatus().equals("pending")){
                    Toast.makeText(this, "Pago en espera de OXXO", Toast.LENGTH_SHORT).show();
                } else{
                    //compra no aprobada
                    Toast.makeText(this, "Fallo Pago", Toast.LENGTH_SHORT).show();

                }
            }
            else if (resultCode == RESULT_CANCELED) {

                if (data != null && data.getExtras() != null
                        && data.getExtras().containsKey(MercadoPagoCheckout.EXTRA_ERROR)) {
                    //error en algun paso de mercado pago
                    final MercadoPagoError mercadoPagoError =
                            (MercadoPagoError) data.getSerializableExtra(MercadoPagoCheckout.EXTRA_ERROR);
                    //el objeto mercadoPagoError contiene todos los datos de porque no se hombre la venta

                } else {
                    //compra cancelada

                }
            }


        }

        //MercadoPago

        if(requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                //Petición al WS para mostrar en bd la inscripción
                reflejarInscripcion(dominio + "inscribirUsuario.php?" +
                        "id_usuario="+ usuario.getId() +
                        "&id_competencia=" + id_competencia);

                inscribirForaneos(dominio + "inscribirForaneo.php?" +
                        "id_usuario=" + usuario.getId() +
                        "&id_competencia=" + id_competencia+
                        "&id_foraneo=" + ids_foraneos.replaceAll(" ", "%20"));

                crearPDF(currentDate);
                PaymentConfirmation confirmation = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
                if (confirmation != null){
                    try {
                        String DetallesPago = confirmation.toJSONObject().toString(4);
                        startActivity(new Intent(this, DetallesTransaccion.class).putExtra("PaymentDetails", DetallesPago)
                                .putExtra("PaymentAmount", monto));
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }else if (resultCode == Activity.RESULT_CANCELED)
                Toast.makeText(this, "Transaccion cancelada", Toast.LENGTH_SHORT).show();
        }else if (requestCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
            Toast.makeText(this, "Petición inválida", Toast.LENGTH_SHORT).show();

        }
    }




    private void obtenerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);

        usuario.setId(preferences.getInt(Login.ID_USUARIO_SESSION, 0));
        usuario.setNombre(preferences.getString(Login.NOMBRE_USUARIO_SESSION, "No_name"));
        usuario.setCorreo(preferences.getString(Login.CORREO_SESSION, "No_mail"));
        usuario.setFechaNacimiento(preferences.getInt(Login.NACIMIENTO_USUARIO_SESSION, 0));
    }

    private void getLastViewData() {
        /*Bundle extra = pagarInscripciones.this.getIntent().getExtras();
        id_competencia = extra.getString("ID_COMPENTENCIA");
        monto = extra.getString("monto");
        ids_foraneos = extra.getString("CANT_FORANEOS");
        NombreCompetencia = extra.getString("NOMBRE_COMPETENCIA");
        Fecha1 = extra.getString("FECHA");
        Lugar = extra.getString("LUGAR");
        Organizador = extra.getString("ORGANIZADOR");*/

        obtenerAutoguardado();

        ids_foraneos = ids_foraneos.trim();
        //Toast.makeText(this, ids_foraneos, Toast.LENGTH_SHORT).show();
        Log.i("ids_foraneos", "Estas son las ids: "+ids_foraneos);
        total = ids_foraneos.split(" ").length;
        //Toast.makeText(this, total+"", Toast.LENGTH_SHORT).show();
        Log.i("Cantidad_seleccionados", total+"Total");
    }

    private void obtenerAutoguardado() {
        SharedPreferences preferences = getSharedPreferences("Autoguardado_Inscripcion", Context.MODE_PRIVATE);

        id_competencia = preferences.getString(ID_COMPETENCIA, "");
        monto = preferences.getString(MONTO, "0");
        NombreCompetencia = preferences.getString(NOMBRE_COMPETENCIA, "");
        Fecha1 = preferences.getString(FECHA_COMPETENCIA, "");
        Lugar = preferences.getString(LUGAR_COMPETENCIA, "");
        Organizador = preferences.getString(ORGANIZADOR, "");
        ids_foraneos = preferences.getString(IDS_FORANEOS, "");
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    // --------------------------> PAYPAL INTEGRATION <------------------------------- //
    private void hacerPago() {
        if (monto == null) {
            Toast.makeText(this, "Hubo un error al cargar la información de la competencia, vuelve a seleccionar la competenca", Toast.LENGTH_SHORT).show();
        } else if (ids_foraneos.length() < 1) {
            total = Integer.parseInt(monto);
        }else if (ids_foraneos.length() > 1){
            total = Integer.parseInt(monto) * (this.total+1);
        }
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(total), "MXN", NombreCompetencia, PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    // --------------------------> PAYPAL INTEGRATION <------------------------------- //

    /*
     * Método con el cual se reflejará el pago en la bd
     * Recibe la url del WS y se lanza cuando el código de respuesta del
     * servicio de paypal está en "ok" o cuando conekta...
     * */
    private void reflejarInscripcion(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Hacer algo en la respuesta
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexión al servidor", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void inscribirForaneos(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Hacer algo en la respuesta
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexión al servidor", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void crearPDF(String Fecha) {
        plantillaPDF.crearArchivo();

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
           // String authorities = getApplicationContext().getPackageName()+".provider";
            //FileProvider.getUriForFile(this, authorities, plantillaPDF.archivoPDF);
        //}

        plantillaPDF.abrirArchivo();
        plantillaPDF.addMetadata("Carrera", "Inscripcion", "Runnatica");

        plantillaPDF.addHeaders("Runnatica", "Inscripcion Oficial", "");
        plantillaPDF.addParagraph("Informacion: " );
        plantillaPDF.addParagraph("Carrera: " + NombreCompetencia.toString());
        plantillaPDF.addParagraph("Nombre del Competidor"+ usuario.getNombre());
        plantillaPDF.addParagraph("Fecha de Inscripcion: "+ Fecha1);
        plantillaPDF.addParagraph("Monto Pagado: "+ total + " MXN");
        plantillaPDF.addParagraph("Direccion de Competencia: "+Lugar);
        plantillaPDF.addParagraph("Organizador: "+Organizador);
        //plantillaPDF.addParagraph("Código QR");
        plantillaPDF.cerrarDocumento();
        plantillaPDF.sendMail(usuario.getCorreo());
    }

    public boolean validarPermisos() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M){
            return true;
        }

        if ((checkSelfPermission(WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) &&
                (checkSelfPermission(READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)) {
            return true;
        }

        if ((shouldShowRequestPermissionRationale(WRITE_EXTERNAL_STORAGE)) ||
                (shouldShowRequestPermissionRationale(READ_EXTERNAL_STORAGE))) {
            cargarDialogoPermisos();
        }else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 100);
            }
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100) {
            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                paypal.setEnabled(true);
            } else {
                solicitarPermisosManual();
            }
        }
    }

    private void solicitarPermisosManual() {
        final CharSequence[] opciones = {"Si", "No"};
        final AlertDialog.Builder alertOptions = new AlertDialog.Builder(pagarInscripciones.this);
        alertOptions.setTitle("¿Quieres configurar manualmente los permisos?");
        alertOptions.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (opciones[which].equals("Si")){
                    Intent intent = new Intent();
                    intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", getPackageName(), null);
                    intent.setData(uri);
                    startActivity(intent);
                } else {
                    Toast.makeText(pagarInscripciones.this, "Los permisos no fueron aceptados", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
    }

    private void cargarDialogoPermisos() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(pagarInscripciones.this);
        dialog.setTitle("Permisos desactivados");
        dialog.setMessage("Debes aceptar los permisos para el funcionamiento correcto de la aplicación");

        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 100);
                }
            }
        });

        dialog.show();
    }

    private void Felicidades(){
        Intent next = new Intent(this, FelicidadesCompetidor.class);
        startActivity(next);
        finish();
    }

}

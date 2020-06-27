package com.runnatica.runnatica;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.runnatica.runnatica.Config.PaypalConfig;
import com.runnatica.runnatica.PDF.PlantillaPDF;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class pagarInscripciones extends AppCompatActivity {


    private Button paypal, conekta;
    PlantillaPDF plantillaPDF = new PlantillaPDF(pagarInscripciones.this);;

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)//Seleccionado el modo sandbox
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);

    Calendar calendar = Calendar.getInstance();
    final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
    Usuario usuario = Usuario.getUsuarioInstance();

    private String monto = "";
    private int multiplicar = 0;
    private String id_competencia, NombreCompetencia, Fecha1, Lugar, Organizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pagar_inscripciones);
        paypal = (Button)findViewById(R.id.btnPaypal);
        conekta = (Button)findViewById(R.id.btnConekta);
        paypal.setEnabled(false);

        getLastViewData();


        if (validarPermisos()) {
            paypal.setEnabled(true);
            conekta.setEnabled(true);
        } else {
            paypal.setEnabled(false);
            conekta.setEnabled(false);
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
        });
    }

    private void getLastViewData() {
        Bundle extra = pagarInscripciones.this.getIntent().getExtras();
        id_competencia = extra.getString("ID_COMPENTENCIA");
        monto = extra.getString("monto");
        multiplicar = extra.getInt("CANT_FORANEOS");
        NombreCompetencia = extra.getString("NOMBRE_COMPETENCIA");
        Fecha1 = extra.getString("FECHA");
        Lugar = extra.getString("LUGAR");
        Organizador = extra.getString("ORGANIZADOR");

        Toast.makeText(this, ""+multiplicar, Toast.LENGTH_SHORT).show();
    }

    // --------------------------> PAYPAL INTEGRATION <------------------------------- //
    private void hacerPago() {
        if (multiplicar > 0 && monto != null) {
            int total = Integer.valueOf(monto) * (multiplicar+1);
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(total), "MXN", "Prueba", PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        } else if (monto == null) {
            Toast.makeText(this, "Hubo un error al cargar la información de la competencia, vuelve a seleccionar la competenca", Toast.LENGTH_SHORT).show();
        }else {
            PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(monto)), "MXN", "Prueba", PayPalPayment.PAYMENT_INTENT_SALE);

            Intent intent = new Intent(this, PaymentActivity.class);
            intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
            intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
            startActivityForResult(intent, PAYPAL_REQUEST_CODE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                //Petición al WS para mostrar en bd la inscripción
                reflejarInscripcion("https://runnatica.000webhostapp.com/WebServiceRunnatica/inscribirUsuario.php?" +
                        "id_usuario="+ usuario.getId() +
                        "&id_competencia=" + id_competencia);
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
        }else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID)
            Toast.makeText(this, "Petición inválida", Toast.LENGTH_SHORT).show();

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

    private void crearPDF(String Fecha) {
        plantillaPDF.crearArchivo();

        //if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
           // String authorities = getApplicationContext().getPackageName()+".provider";
            //FileProvider.getUriForFile(this, authorities, plantillaPDF.archivoPDF);
        //}

        plantillaPDF.abrirArchivo();
        plantillaPDF.addMetadata("Carrera", "Inscripcion", "Runnatica");
        plantillaPDF.addHeaders(NombreCompetencia.toString(), "Marca", Fecha1.toString());
        plantillaPDF.addParagraph("Código QR");
        plantillaPDF.addParagraph(usuario.getNombre());
        plantillaPDF.addParagraph(Lugar.toString());
        plantillaPDF.addParagraph(Organizador.toString());
        plantillaPDF.cerrarDocumento();
        plantillaPDF.sendMail();
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
}

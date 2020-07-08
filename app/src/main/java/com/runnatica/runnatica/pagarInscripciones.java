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
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.support.design.widget.BottomNavigationView;
import android.view.Menu;
import android.view.MenuItem;


import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class pagarInscripciones extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private Button paypal;
    PlantillaPDF plantillaPDF = new PlantillaPDF(pagarInscripciones.this);

    BottomNavigationView MenuUsuario;

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)//Seleccionado el modo sandbox
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);

    /*private GoogleApiClient mGoogleApiClient;
    private SupportWalletFragment mWalletFragment;
    private SupportWalletFragment mXmlWalletFrafment;

    private MaskedWallet mMaskedWallet;
    private FullWallet mFullWallet;

    public static final int MAKED_WALLET_REQUEST_CODE = 888;
    public static final int FULL_WALLET_REQUEST_CODE = 889;

    public static final String WALLET_FRAGMENT_ID = "wallet_fragment";*/


    Calendar calendar = Calendar.getInstance();
    final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
    Usuario usuario = Usuario.getUsuarioInstance();

    private String monto;
    private int total;
    private String ids_foraneos;
    private String id_competencia, NombreCompetencia, Fecha1, Lugar, Organizador;

    private String dominio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*WalletFragmentInitParams startParams;
        WalletFragmentInitParams.Builder startParamsBuilder = WalletFragmentInitParams.newBuilder()
                .setMaskedWalletRequest(generateMaskedWalletRequest())
                .setMaskedWalletRequestCode(MAKED_WALLET_REQUEST_CODE);

        startParams = startParamsBuilder.build();

        if (mWalletFragment == null) {
            WalletFragmentStyle walletFragmentStyle = new WalletFragmentStyle()
                    .setBuyButtonText(BuyButtonText.BUY_WITH_GOOGLE)
                    .setBuyButtonWidth(Dimension.MATCH_PARENT);

            WalletFragmentOptions walletFragmentOptions = WalletFragmentOptions.newBuilder()
                    .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                    .setFragmentStyle(walletFragmentStyle)
                    .setTheme(WalletConstants.THEME_HOLO_DARK)
                    .setMode(WalletFragmentMode.BUY_BUTTON).build();

            mWalletFragment = SupportWalletFragment.newInstance(walletFragmentOptions);

            mWalletFragment.initialize(startParams);
        }

        getSupportFragmentManager().beginTransaction().replace(R.id.wallet_button_holder, mWalletFragment, WALLET_FRAGMENT_ID).commit();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Wallet.API, new Wallet.WalletOptions.Builder()
                        .setEnvironment(WalletConstants.ENVIRONMENT_SANDBOX)
                        .setTheme(WalletConstants.THEME_HOLO_DARK)
                        .build()).build();*/

        dominio = getString(R.string.ip);

        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

//Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);
        //


        MenuUsuario.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    home();
                }
                if (menuItem.getItemId() == R.id.menu_busqueda) {
                    Busqueda();
                }
                if (menuItem.getItemId() == R.id.menu_historial) {
                    Historial();
                }
                if (menuItem.getItemId() == R.id.menu_ajustes) {
                    Ajustes();
                }

                return true;
            }
        });


        setContentView(R.layout.activity_pagar_inscripciones);

        obtenerPreferencias();

        paypal = (Button)findViewById(R.id.btnPaypal);
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
        });
    }

    private void obtenerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);

        usuario.setId(preferences.getInt(Login.ID_USUARIO_SESSION, 0));
        usuario.setNombre(preferences.getString(Login.NOMBRE_USUARIO_SESSION, "No_name"));
        usuario.setCorreo(preferences.getString(Login.CORREO_SESSION, "No_mail"));
        usuario.setFechaNacimiento(preferences.getInt(Login.NACIMIENTO_USUARIO_SESSION, 0));
    }

    /*@Override
    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }*/

    private void getLastViewData() {
        Bundle extra = pagarInscripciones.this.getIntent().getExtras();
        id_competencia = extra.getString("ID_COMPENTENCIA");
        monto = extra.getString("monto");
        ids_foraneos = extra.getString("CANT_FORANEOS");
        NombreCompetencia = extra.getString("NOMBRE_COMPETENCIA");
        Fecha1 = extra.getString("FECHA");
        Lugar = extra.getString("LUGAR");
        Organizador = extra.getString("ORGANIZADOR");

        ids_foraneos = ids_foraneos.trim();
        Toast.makeText(this, ids_foraneos, Toast.LENGTH_SHORT).show();
        Log.i("ids_foraneos", "Estas son las ids: "+ids_foraneos);
        total = ids_foraneos.split(" ").length;
        Toast.makeText(this, total+"", Toast.LENGTH_SHORT).show();
        Log.i("Cantidad_seleccionados", total+"Total");
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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

        }/*else if (requestCode == MAKED_WALLET_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    mMaskedWallet = data.getParcelableExtra(WalletConstants.EXTRA_MASKED_WALLET);
                    break;
                case Activity.RESULT_CANCELED:
                     break;
                default:
                    Toast.makeText(this, "Transaccion cancelada", Toast.LENGTH_SHORT).show();
                    break;
            }
        }else if (requestCode == FULL_WALLET_REQUEST_CODE) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    mFullWallet = data.getParcelableExtra(WalletConstants.EXTRA_FULL_WALLET);
                    Toast.makeText(this, mFullWallet.getProxyCard().getPan(), Toast.LENGTH_SHORT).show();
                    Wallet.Payments.notifyTransactionStatus(mGoogleApiClient,
                            generateNotifyTransactionStatusRequest(mFullWallet.getGoogleTransactionId(),
                            NotifyTransactionStatusRequest.Status.SUCCESS));
                    break;
                default:
                    Toast.makeText(this, "Ocurrió un error", Toast.LENGTH_SHORT).show();
                    break;
            }
        }else if (requestCode == WalletConstants.RESULT_ERROR) {
            Toast.makeText(this, "Transaccion cancelada", Toast.LENGTH_SHORT).show();
        }*/
    }

    /*public static NotifyTransactionStatusRequest generateNotifyTransactionStatusRequest(String googleTransactionId, int status) {
        return NotifyTransactionStatusRequest.newBuilder()
                .setGoogleTransactionId(googleTransactionId)
                .setStatus(status)
                .build();
    }

    private MaskedWalletRequest generateMaskedWalletRequest() {
        MaskedWalletRequest maskedWalletRequest = MaskedWalletRequest.newBuilder()
                .setMerchantName("Runnatica")
                .setPhoneNumberRequired(true)
                .setShippingAddressRequired(true)
                .setCurrencyCode("MXN")
                .setShouldRetrieveWalletObjects(true)
                .setEstimatedTotalPrice(monto)
                .setCart(Cart.newBuilder()
                    .setCurrencyCode("MXN")
                    .setTotalPrice(monto)
                    .addLineItem(LineItem.newBuilder()
                            .setCurrencyCode("MXN")
                            .setQuantity("1")
                            .setUnitPrice(monto)
                            .setTotalPrice(monto)
                            .build())
                        .build())
                .build();

        return maskedWalletRequest;
    }

    private FullWalletRequest generateFullWalletRequest(String googleTransactionID) {
        FullWalletRequest fullWalletRequest = FullWalletRequest.newBuilder()
                .setCart(Cart.newBuilder()
                        .setCurrencyCode("MXN")
                        .setTotalPrice(monto)
                        .addLineItem(LineItem.newBuilder()
                                .setCurrencyCode("MXN")
                                .setQuantity("1")
                                .setUnitPrice(monto)
                                .setTotalPrice(monto)
                                .build())
                .addLineItem(LineItem.newBuilder()
                        .setCurrencyCode("MXN")
                        .setDescription("Competencia")
                        .setRole(LineItem.Role.TAX)
                        .setTotalPrice(monto)
                        .build()).build())
                .build();

        return fullWalletRequest;
    }

    public void requesFullWallet(View view) {
        if (mGoogleApiClient.isConnected()) {
            Wallet.Payments.loadFullWallet(mGoogleApiClient,
                    generateFullWalletRequest(mMaskedWallet.getGoogleTransactionId()), FULL_WALLET_REQUEST_CODE);
        }
    }*/

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
        plantillaPDF.addHeaders(NombreCompetencia.toString(), "Marca", Fecha1);
        plantillaPDF.addParagraph("Código QR");
        plantillaPDF.addParagraph(usuario.getNombre());
        plantillaPDF.addParagraph(Lugar);
        plantillaPDF.addParagraph(Organizador);
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

    public void requestFullWallet(View view) {

    }

    /*try {
            readyToPayRequest = IsReadyToPayRequest.fromJson(baseConfigurationJson().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Task<Boolean> task = paymentsClient.isReadyToPay(readyToPayRequest);
        task.addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
            @Override
            public void onComplete(@NonNull Task<Boolean> completeTask) {
                if (completeTask.isSuccessful()) {
                    showGooglePayButton(completeTask.getResult());
                }else {

                }
            }
        });*/

    /*Wallet.WalletOptions walletOptions = new Wallet.WalletOptions.Builder()
                .setEnvironment(WalletConstants.ENVIRONMENT_TEST).build();

        paymentsClient = Wallet.getPaymentsClient(pagarInscripciones.this, walletOptions);
        try {
            final PaymentDataRequest request = PaymentDataRequest.fromJson(paymentRequestJSON().toString());
            AutoResolveHelper.resolveTask(paymentsClient.loadPaymentData(request), this, LOAD_PAYMENT);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/

    /*private static JSONObject baseConfigurationJson() throws JSONException {
        return new JSONObject()
                .put("apiVersion", 2)
                .put("apiVersionMinor", 0)
                .put("allowedPaymentMethods", new JSONArray().put(getCardPaymentMethod()));
    }

    private void showGooglePayButton(Boolean userIsReadyToPay) {
        if (userIsReadyToPay) {
            // eg: googlePayButton.setVisibility(View.VISIBLE);
        }else {
            // Google pay is not supported
        }
    }

    private JSONObject paymentRequestJSON() throws JSONException {
        final JSONObject paymentRequestJson = baseConfigurationJson();
        paymentRequestJson.put("transactionInfo", new JSONObject()
            .put("totalPrice", monto)
            .put("totalPriceStatus", "FINAL")
            .put("currencyCode", "MXN"));

        paymentRequestJson.put("merchantInfo", new JSONObject()
            .put("merchantId", "0151068430315")
            .put("merchantName", "Chocoso"));

        return paymentRequestJson;
    }

    private static JSONObject getCardPaymentMethod() throws JSONException {
        final String[] networks = new String[] {"VISA", "AMEX"};
        final String[] authMethods = new String[] {"PAY_ONLY", "CRYPTOGRAM_3DS"};

        JSONObject card = new JSONObject();
        card.put("type", "CARD");
        card.put("tokenizationSpecification", getTokenizationSpec());
        card.put("parameters", new JSONObject()
            .put("allowedAuthMethods", new JSONArray(authMethods))
            .put("allowedCardNetworks", new JSONArray(networks)));

        return card;
    }*/
    private void home(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }
    private void Busqueda(){
        Intent next = new Intent(this, busqueda_competidor.class);
        startActivity(next);
    }
    private void Historial(){
        Intent next = new Intent(this, historial_competidor.class);
        startActivity(next);
    }
    private void Ajustes(){
        Intent next = new Intent(this, ajustes_competidor.class);
        startActivity(next);
    }

}

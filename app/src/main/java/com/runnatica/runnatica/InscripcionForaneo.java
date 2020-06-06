package com.runnatica.runnatica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.paypal.android.sdk.payments.PayPalConfiguration;
import com.paypal.android.sdk.payments.PayPalPayment;
import com.paypal.android.sdk.payments.PayPalService;
import com.paypal.android.sdk.payments.PaymentActivity;
import com.paypal.android.sdk.payments.PaymentConfirmation;
import com.runnatica.runnatica.Config.PaypalConfig;
import com.runnatica.runnatica.Fragmentos.Inscripcion_foraneo;
import com.runnatica.runnatica.PDF.PlantillaPDF;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONException;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.util.Calendar;

public class InscripcionForaneo extends AppCompatActivity {
    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)//Seleccionado el modo sandbox
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);

    private String monto = "";
    Calendar calendar = Calendar.getInstance();
    final String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
    Usuario usuario = Usuario.getUsuarioInstance();

    private Button btnCantidadDeForaneos, btnInscribir;
    private EditText CantidadForaneos;
    private Inscripcion_foraneo fragmentInscripcionForaneo;

    private String id_competencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscripcion_foraneo);
        fragmentInscripcionForaneo = new Inscripcion_foraneo();
        CantidadForaneos = (EditText)findViewById(R.id.etCantidadForaneos);
        btnCantidadDeForaneos = (Button)findViewById(R.id.btnPasarAInscripcionForaneo);

        // ------------------------------> Iniciar el servicio Paypal
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);
        // ------------------------------> PAYPAL

        getLastViewData();

        btnCantidadDeForaneos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validar5foraneos()) {
                    MandarCantidadUsuariosForaneos(Integer.parseInt(CantidadForaneos.getText().toString()));
                    FragmentTransaction transaction = getSupportFragmentManager().beginTransaction().add(R.id.contenedorForaneos, fragmentInscripcionForaneo);
                    transaction.commit();
                    btnCantidadDeForaneos.setEnabled(false);
                }
            }
        });

        btnInscribir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for ( int i=0 ; i == fragmentInscripcionForaneo.arregloID.length ; i++) {

                }
            }
        });
    }

    private void getLastViewData() {
        Bundle extra = InscripcionForaneo.this.getIntent().getExtras();
        id_competencia = extra.getString("ID_COMPENTENCIA");
        monto = extra.getString("monto");

        Toast.makeText(this, id_competencia, Toast.LENGTH_SHORT).show();
    }

    private void MandarCantidadUsuariosForaneos(int foraneos) {
        Bundle bundle = new Bundle();
        bundle.putInt("CANTIDADFORANEOS", foraneos);
        bundle.putString("ID_COMPENTENCIA", id_competencia);

        fragmentInscripcionForaneo.setArguments(bundle);
    }

    private boolean validar5foraneos() {
        boolean flag = false;
        if (Integer.parseInt(CantidadForaneos.getText().toString()) >= 6){
            CantidadForaneos.setError("No pueden ser más de 5 usuarios foráneos");
        }else if (Integer.parseInt(CantidadForaneos.getText().toString()) <= 0){
            CantidadForaneos.setError("No existen las antipersonas (No puedes poner números negativos)");
        }else flag = true;

        return flag;
    }

    // --------------------------> PAYPAL INTEGRATION <------------------------------- //
    private void hacerPago() {
        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(monto)), "MXN", "Prueba", PayPalPayment.PAYMENT_INTENT_SALE);

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
                reflejarInscripcion("");
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
    }

    private void crearPDF(String Fecha) {
        PlantillaPDF plantillaPDF = new PlantillaPDF(getApplicationContext());
        plantillaPDF.abrirArchivo();
        plantillaPDF.addMetadata("Carrera", "Inscripcion", "Runnatica");
        plantillaPDF.addHeaders("Nombre de la carrera", "Marca", Fecha);
        plantillaPDF.addParagraph("Código QR");
        plantillaPDF.addParagraph(usuario.getNombre());
        plantillaPDF.addParagraph("Ubicación del evento");
        plantillaPDF.addParagraph("Nomre del organizador");
        plantillaPDF.cerrarDocumento();
        plantillaPDF.sendPDF(this);
    }
}

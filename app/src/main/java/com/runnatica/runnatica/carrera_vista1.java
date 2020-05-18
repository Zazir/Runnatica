package com.runnatica.runnatica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;

public class carrera_vista1 extends AppCompatActivity {
    ImageView imgCompetencia;
    TextView txtNomCompe, txtOrganizador, txtFechaCompe, txtHoraCompe, txtLugarCompe, txtPrecioCompe, txtDescripcionCompe;
    Button btnInscripcion;

    private static final int PAYPAL_REQUEST_CODE = 7171;
    private static PayPalConfiguration config = new PayPalConfiguration().environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)//Seleccionado el modo sandbox
            .clientId(PaypalConfig.PAYPAL_CLIENT_ID);
    String monto = "";
    private String id;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrera_vista1);
        Spinner spinner = (Spinner)findViewById(R.id.spForo);
        imgCompetencia = (ImageView)findViewById(R.id.ivFotoCompetencia);
        txtNomCompe = (TextView)findViewById(R.id.tvNombreCompetencia);
        txtOrganizador = (TextView)findViewById(R.id.tvNombreOrganizador);
        txtFechaCompe = (TextView)findViewById(R.id.tvFechaCompetencia);
        txtHoraCompe = (TextView)findViewById(R.id.tvHoraCompetencia);
        txtLugarCompe = (TextView)findViewById(R.id.tvLugarCompetencia);
        txtPrecioCompe = (TextView)findViewById(R.id.tvPrecioCompetencia);
        txtDescripcionCompe = (TextView)findViewById(R.id.tvDescripcion);
        btnInscripcion = (Button)findViewById(R.id.btnIncribirse);

        id = getIntent().getStringExtra("id");
        cargarInfoCarrera("https://runnatica.000webhostapp.com/WebServiceRunnatica/obtenerCompetencia.php?idCompe=1");

        //Iniciar el servicio Paypal
        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);

        btnInscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hacerPago();
            }
        });
    }

    private void hacerPago() {
        monto = txtPrecioCompe.getText().toString();

        PayPalPayment payPalPayment = new PayPalPayment(new BigDecimal(String.valueOf(monto)), "MXN", "Prueba", PayPalPayment.PAYMENT_INTENT_SALE);

        Intent intent = new Intent(this, PaymentActivity.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payPalPayment);
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }

    private void cargarInfoCarrera(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject respuesta = jsonArray.getJSONObject(0);
                            txtNomCompe.setText(respuesta.optString("nom_comp"));
                            txtOrganizador.setText(respuesta.optString("id_usuario"));
                            txtFechaCompe.setText(respuesta.optString("fecha"));
                            txtHoraCompe.setText(respuesta.optString("hora"));
                            txtLugarCompe.setText(respuesta.optString("coordenadas"));
                            txtPrecioCompe.setText(respuesta.optString("precio"));
                            txtDescripcionCompe.setText(respuesta.optString("descripcion"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexión al servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == PAYPAL_REQUEST_CODE){
            if (resultCode == RESULT_OK){
                //Petición al WS para mostrar en bd la inscripción
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
}

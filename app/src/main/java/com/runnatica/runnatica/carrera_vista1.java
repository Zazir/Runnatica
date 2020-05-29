package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
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
import com.paypal.android.sdk.payments.PayPalService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class carrera_vista1 extends AppCompatActivity {
    ImageView imgCompetencia;
    TextView txtNomCompe, txtOrganizador, txtFechaCompe, txtHoraCompe, txtLugarCompe, txtPrecioCompe, txtDescripcionCompe, txtRegistrarse;
    Button btnInscripcion;

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
        txtRegistrarse = (TextView)findViewById(R.id.tvRegistrarse);

        getLastViewData();
        cargarInfoCarrera("https://runnatica.000webhostapp.com/WebServiceRunnatica/obtenerCompetencia.php?idCompe=" + id);

        btnInscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearInscripcion();
            }
        });

        txtRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void CrearInscripcion() {
        Intent intent = new Intent(carrera_vista1.this, crear_inscripcion.class);
        intent.putExtra("monto", txtPrecioCompe.getText().toString());
        startActivity(intent);
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

    private void getLastViewData() {
        Bundle extra = carrera_vista1.this.getIntent().getExtras();
        id = extra.getString("id");
    }

    private void ReCapcha(){

    }
}

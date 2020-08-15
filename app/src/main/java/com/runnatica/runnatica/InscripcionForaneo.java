package com.runnatica.runnatica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.runnatica.runnatica.adapter.inscripcionesForaneoAdapter;
import com.runnatica.runnatica.poho.Inscripciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.runnatica.runnatica.carrera_vista1.IDS_FORANEOS;

public class InscripcionForaneo extends AppCompatActivity {

    private String monto = "";

    private Button btnCrearForaneos, btnInscribir;
    private RecyclerView rvInscripcionesForaneos;

    private String id_competencia, NombreCompetencia, Fecha, Lugar, Organizador;
    private List<Inscripciones> inscripcionesList = new ArrayList<>();
    private inscripcionesForaneoAdapter inscripcionesAdapter;

    private String ids_foraneos;

    private static final String TAG = "JORGE";

    public static final String SITE_KEY = "6LfiMKkZAAAAAIIpYcZWdXd7TaRoya7OBKhi8EeQ";
    public static final String SITE_SECRET_KEY = "6LfiMKkZAAAAADbV7pR01TxTXQcZSZMDqx_8SCCM";
    String userResponseToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscripcion_foraneo);
        btnCrearForaneos = (Button)findViewById(R.id.btnPasarACrearForaneo);
        btnInscribir = (Button)findViewById(R.id.btnPagarInscripciones);
        rvInscripcionesForaneos = (RecyclerView)findViewById(R.id.rvInscripcionesForaneos);
        rvInscripcionesForaneos.setHasFixedSize(true);
        rvInscripcionesForaneos.setLayoutManager(new LinearLayoutManager(this));

        getLastViewData();
        String dominio = getString(R.string.ip);
        cargarInscripciones(dominio + "obtenerInscripciones.php?id_compentencia=" + id_competencia);

        btnCrearForaneos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aAgregarForaneos();
            }
        });

        btnInscribir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ids_foraneos = inscripcionesAdapter.idsForaneos;
                guardarDatosInscripcion();
                CrearInscripcion();
            }
        });

    }

    private void guardarDatosInscripcion() {
        SharedPreferences preferences = getSharedPreferences("Autoguardado_Inscripcion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(IDS_FORANEOS, inscripcionesAdapter.idsForaneos);

        editor.commit();
    }

    private void getLastViewData() {
        Bundle extra = InscripcionForaneo.this.getIntent().getExtras();
        id_competencia = extra.getString("ID_COMPENTENCIA");
        monto = extra.getString("monto");
        NombreCompetencia = extra.getString("NOMBRE_COMPETENCIA");
        Fecha = extra.getString("FECHA");
        Lugar = extra.getString("LUGAR");
        Organizador = extra.getString("ORGANIZADOR");
        //Toast.makeText(getApplicationContext(), ""+NombreCompetencia, Toast.LENGTH_SHORT).show();
    }

    private void aAgregarForaneos() {
        Intent intent = new Intent(InscripcionForaneo.this, agregarForaneo.class);
        /*intent.putExtra("monto", monto);
        intent.putExtra("ID_COMPENTENCIA", id_competencia);
        intent.putExtra("CANT_FORANEOS", ids_foraneos);
        intent.putExtra("NOMBRE_COMPETENCIA", NombreCompetencia);
        intent.putExtra("FECHA", Fecha);
        intent.putExtra("LUGAR", Lugar);
        intent.putExtra("ORGANIZADOR", Organizador);*/
        startActivity(intent);
        finish();
    }

    public void moveNewActivity() {
        Intent intent = new Intent(InscripcionForaneo.this, AvisoPago.class);
        intent.putExtra("monto", monto);
        intent.putExtra("ID_COMPENTENCIA", id_competencia);
        intent.putExtra("CANT_FORANEOS", ids_foraneos);
        intent.putExtra("NOMBRE_COMPETENCIA", NombreCompetencia);
        intent.putExtra("FECHA", Fecha);
        intent.putExtra("LUGAR", Lugar);
        intent.putExtra("ORGANIZADOR", Organizador);
        startActivity(intent);
    }

    private void CrearInscripcion() {

        //Toast.makeText(this, ""+NombreCompetencia, Toast.LENGTH_SHORT).show();
        SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                .addOnSuccessListener(this,
                        new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>()
                        {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response)
                            {

                                userResponseToken = response.getTokenResult();

                                if (!userResponseToken.isEmpty())
                                {
                                    sendRequest();
                                }
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        if (e instanceof ApiException)
                        {
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();

                            Log.e(TAG, "ERROR 1: " + CommonStatusCodes.getStatusCodeString(statusCode));
                        }
                        else
                        {
                            Log.e(TAG, "ERROR 2: " + e.getMessage());
                        }
                    }
                });

    }

    private void cargarInscripciones(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Hacer el string a json array object
                            JSONArray array = new JSONArray(response);

                            //Recorremos con un for lo que tiene el array
                            for (int i = 0; i < array.length(); i++) {
                                //Obtenemos los objetos tipo competencias del array
                                JSONObject objetoinscripcion = array.getJSONObject(i);

                                //Añadir valores a los correspondientes textview
                                inscripcionesList.add(new Inscripciones(
                                        objetoinscripcion.getInt("id_competencia"),
                                        objetoinscripcion.getString("nombre_inscripcion"),
                                        objetoinscripcion.getInt("cantidad_usuarios"),
                                        objetoinscripcion.getInt("cantidad_foraneos"),
                                        objetoinscripcion.getInt("edad_max"),
                                        objetoinscripcion.getInt("edad_min")
                                ));
                            }

                            //Creamos instancia del adapter
                            inscripcionesAdapter = new inscripcionesForaneoAdapter(InscripcionForaneo.this, inscripcionesList);

                            inscripcionesAdapter.setOnItemClick(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                    Toast.makeText(getApplicationContext(), parent.getItemIdAtPosition(position)+"", Toast.LENGTH_SHORT).show();
                                }
                            });

                            rvInscripcionesForaneos.setAdapter(inscripcionesAdapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conección con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    public void sendRequest()
    {
        String url = "https://www.google.com/recaptcha/api/siteverify";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("success").equals("true"))
                            {
                                Log.e(TAG, "EXITO");
                                moveNewActivity();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, "ERROR 3");
                        Toast.makeText(InscripcionForaneo.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("secret", SITE_SECRET_KEY);
                params.put("response", userResponseToken);
                return params;
            }
        };

        Controlador_Captcha.getInstance(this).addToRequestQueue(stringRequest);
    }
}

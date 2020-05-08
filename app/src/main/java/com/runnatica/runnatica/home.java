package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.adapter.MyAdapter;
import com.runnatica.runnatica.poho.Competencias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class home extends AppCompatActivity {
    RequestQueue requestQueue;
    LinearLayout llConfig;
    Button btnTest;

    private List<Competencias> competenciasList;
    private RecyclerView recyclerView;
    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Enlaces de elementos por id's
        recyclerView = (RecyclerView)findViewById(R.id.rcvCompetencia);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        llConfig = (LinearLayout)findViewById(R.id.ajustes);
        btnTest = (Button)findViewById(R.id.btnPrueba);

        //Inicializar arreglo de competencias
        competenciasList = new ArrayList<>();

        CargarCompetencias("https://runnatica.000webhostapp.com/WebServiceRunnatica/obtenerCompetencias.php");

        llConfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saltarACrearCompe();
            }
        });

        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TesteoPagoPaypal();
            }
        });
    }

    private void TesteoPagoPaypal() {
        Intent intent = new Intent(this, Donaciones.class);
        startActivity(intent);
    }

    /*@Override
    protected void onStart() {
        super.onStart();
        CargarCompetencias("http://192.168.137.1:811/WebServiceRunnatica/obtenerCompetencias.php");
    }*/

    private void CargarCompetencias(String URL) {
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
                                JSONObject competencia = array.getJSONObject(i);

                                //Añadir valores a los correspondientes textview
                                competenciasList.add(new Competencias(
                                        competencia.getInt("id_competencia"),
                                        competencia.getString("nom_comp"),
                                        competencia.getString("descripcion"),
                                        competencia.getInt("precio"),
                                        competencia.getString("foto")
                                ));
                            }

                            //Creamos instancia del adapter
                            adapter = new MyAdapter(home.this, competenciasList);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            Log.i("Adaptador", "Se va a la verga");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conección con el servidor", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
        /*JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject = null;
                for (int i = 0; i < response.length(); i++) {
                    try {
                        jsonObject = response.getJSONObject(i);
                        Competenciatxv.setText(jsonObject.getString("nombre"));
                        Descripciontxv.setText(jsonObject.getString("descripcion"));
                        Preciotxv.setText(jsonObject.getString("precio"));
                    } catch (JSONException e) {
                        Toast.makeText(getApplicationContext(), "Hubo un error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conección con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(jsonArrayRequest);*/
    }

    private void saltarACrearCompe() {
        Intent next = new Intent(this, crear_competencia.class);
        startActivity(next);
    }
}

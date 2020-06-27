package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.adapter.inscripcionesUsuarioAdapter;
import com.runnatica.runnatica.poho.Inscripciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InscripcionesCompetidor extends AppCompatActivity {
    private RecyclerView rvInscripciones;
    private Button btnNext;
    private List<Inscripciones> inscripcionesList = new ArrayList<>();
    private String id_competencia, monto, NombreCompetencia, Fecha, Lugar, Organizador;
    private inscripcionesUsuarioAdapter inscripcionesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscripciones_competidor);
        btnNext = (Button)findViewById(R.id.btnSegundoPaso);
        rvInscripciones = (RecyclerView)findViewById(R.id.rvInscripciones);
        rvInscripciones.setHasFixedSize(true);
        rvInscripciones.setLayoutManager(new LinearLayoutManager(this));

        getLastViewData();

        String dominio = getString(R.string.ip);
        cargarInscripciones(dominio + "obtenerInscripciones.php?id_compentencia=" + id_competencia);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearInscripcion();
            }
        });
    }

    private void getLastViewData() {
        Bundle extra = InscripcionesCompetidor.this.getIntent().getExtras();
        id_competencia = extra.getString("ID_COMPENTENCIA");
        monto = extra.getString("monto");
        NombreCompetencia = extra.getString("NOMBRE_COMPETENCIA");
        Fecha = extra.getString("FECHA");
        Lugar = extra.getString("LUGAR");
        Organizador = extra.getString("ORGANIZADOR");
    }

    private void CrearInscripcion() {
        Intent intent = new Intent(InscripcionesCompetidor.this, InscripcionForaneo.class);
        intent.putExtra("monto", monto);
        intent.putExtra("ID_COMPENTENCIA", id_competencia);
        intent.putExtra("NOMBRECOMPENTENCIA", NombreCompetencia);
        intent.putExtra("FECHA", Fecha);
        intent.putExtra("LUGAR", Lugar);
        intent.putExtra("ORGANIZADOR", Organizador);
        startActivity(intent);
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
                            inscripcionesAdapter = new inscripcionesUsuarioAdapter(InscripcionesCompetidor.this, inscripcionesList);
                            inscripcionesAdapter.notifyDataSetChanged();
                            rvInscripciones.setAdapter(inscripcionesAdapter);
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
}

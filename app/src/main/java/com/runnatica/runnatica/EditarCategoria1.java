package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.adapter.inscripcionesAdapter;
import com.runnatica.runnatica.poho.Inscripciones;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class EditarCategoria1 extends AppCompatActivity {
    private RecyclerView rvInscripciones;

    private String dominio, id_competencia;

    private List<Inscripciones> inscripcionesList = new ArrayList<>();
    private inscripcionesAdapter inscripcionesAdaptador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_categoria1);
        rvInscripciones = (RecyclerView)findViewById(R.id.rvInscripciones);
        rvInscripciones.setHasFixedSize(true);
        rvInscripciones.setLayoutManager(new LinearLayoutManager(this));

        getLastViewData();
        dominio = getString(R.string.ip);
        cargarInscripciones(dominio + "obtenerInscripciones.php?id_compentencia=" + id_competencia);
    }

    private void getLastViewData() {
        Bundle extra = EditarCategoria1.this.getIntent().getExtras();
        id_competencia = extra.getString("ID_COMPENTENCIA");
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
                                        objetoinscripcion.getInt("id_inscripcion"),
                                        objetoinscripcion.getInt("id_competencia"),
                                        objetoinscripcion.getString("nombre_inscripcion"),
                                        objetoinscripcion.getInt("cantidad_usuarios"),
                                        objetoinscripcion.getInt("cantidad_foraneos"),
                                        objetoinscripcion.getInt("edad_max"),
                                        objetoinscripcion.getInt("edad_min")
                                ));
                            }

                            //Creamos instancia del adapter
                            inscripcionesAdaptador= new inscripcionesAdapter(EditarCategoria1.this, inscripcionesList, new inscripcionesAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(int position) {
                                    launchCategoria(inscripcionesList.get(position).getId_categoria()+"");
                                }
                            });

                                    rvInscripciones.setAdapter(inscripcionesAdaptador);
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

    private void launchCategoria(String id_cat) {
        Intent intent = new Intent(EditarCategoria1.this, EditarCategorias2.class);
        intent.putExtra("ID_CAT", id_cat);
        startActivity(intent);
    }
}
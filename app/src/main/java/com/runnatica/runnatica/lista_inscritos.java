package com.runnatica.runnatica;

import android.annotation.SuppressLint;
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
import com.runnatica.runnatica.adapter.inscritosAdapter;
import com.runnatica.runnatica.poho.UsuariosInscritos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class lista_inscritos extends AppCompatActivity {

    private Button Atras;

    private List<UsuariosInscritos> usuariosInscritosList;
    private RecyclerView rvInscritos;
    private inscritosAdapter inscritosAdaptador;

    private String id_competencia;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_inscritos);
        rvInscritos = (RecyclerView) findViewById(R.id.rvInscritos);
        rvInscritos.setHasFixedSize(true);
        rvInscritos.setLayoutManager(new LinearLayoutManager(this));
        Atras = (Button)findViewById(R.id.btnBack);

        usuariosInscritosList = new ArrayList<>();

        getLastViewData();
        cargarUsuariosInscritos("https://runnatica.000webhostapp.com/WebServiceRunnatica/obtenerUsuariosInscritos.php?id_competencia="+id_competencia);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atras();
            }
        });
    }

    private void cargarUsuariosInscritos(String URL) {
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray array = new JSONArray(response);

                            for (int i = 0; i < array.length(); i++) {
                                JSONObject inscrito = array.getJSONObject(i);

                                usuariosInscritosList.add(new UsuariosInscritos(
                                        inscrito.optString("nombre"),
                                        inscrito.optString("correo"),
                                        inscrito.optString("f_nacimiento"),
                                        inscrito.optString("f_inscripciones")
                                ));
                            }

                            inscritosAdaptador = new inscritosAdapter(lista_inscritos.this, usuariosInscritosList, new inscritosAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(int position) {
                                    Toast.makeText(getApplicationContext(), usuariosInscritosList.get(position).getNombre(), Toast.LENGTH_SHORT).show();
                                    detallesInscrito(
                                            usuariosInscritosList.get(position).getNombre(),
                                            usuariosInscritosList.get(position).getCorreo(),
                                            usuariosInscritosList.get(position).getFecha_nacimiento(),
                                            usuariosInscritosList.get(position).getFecha_inscripcion()
                                    );
                                }
                            });
                            rvInscritos.setAdapter(inscritosAdaptador);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void detallesInscrito(String nombre, String correo, String nacimiento, String inscripcion) {
        Intent intent = new Intent(this, informacion_inscrito.class);
        intent.putExtra("NOMBRE", nombre);
        intent.putExtra("CORREO", correo);
        intent.putExtra("F_NACIMIENTO", nacimiento);
        intent.putExtra("F_INSCRIPCION", inscripcion);
        startActivity(intent);
    }

    private void getLastViewData() {
        Bundle extra = lista_inscritos.this.getIntent().getExtras();
        id_competencia = extra.getString("id_competencia");
    }

    void atras(){
        Intent next = new Intent(this, vista1_organizador.class);
        startActivity(next);
    }
}
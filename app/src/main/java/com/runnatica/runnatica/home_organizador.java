package com.runnatica.runnatica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.adapter.MyAdapter;
import com.runnatica.runnatica.poho.Competencias;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class home_organizador extends AppCompatActivity {
    private List<Competencias> competenciasList;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Usuario user = Usuario.getUsuarioInstance();
    private int[] id;

    BottomNavigationView MenuOrganizador;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_organizador);
        recyclerView = (RecyclerView) findViewById(R.id.rvAdministradorCompetencias);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        obtenerPreferencias();

        MenuOrganizador= (BottomNavigationView)findViewById(R.id.MenuOrganizador);

        MenuOrganizador.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    homeOrganizador();
                }
                if (menuItem.getItemId() == R.id.menu_historial) {
                    historialOrganizador();
                }
                if (menuItem.getItemId() == R.id.menu_ajustes) {
                    ajuestesOrganizador();
                }

                return true;
            }
        });

        competenciasList = new ArrayList<>();

        String dominio = getString(R.string.ip);
        cargarAdministradorCompetencias(dominio+"obtenerCompetencias.php?id_usuarioA="+user.getId());
    }

    private void obtenerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);

        user.setId(preferences.getInt(Login.ID_USUARIO_SESSION, 0));
        user.setNombre(preferences.getString(Login.NOMBRE_USUARIO_SESSION, "No_name"));
        user.setCorreo(preferences.getString(Login.CORREO_SESSION, "No_mail"));
        user.setFechaNacimiento(preferences.getInt(Login.NACIMIENTO_USUARIO_SESSION, 0));
    }

    private void cargarAdministradorCompetencias(String URL) {
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
                                id = new int[array.length()];

                                //Crear y añadir objeto de tipo Competencias a una Lista
                                competenciasList.add(new Competencias(
                                        competencia.getInt("id_competencia"),
                                        competencia.getString("nom_comp"),
                                        competencia.getString("descripcion"),
                                        competencia.getString("precio"),
                                        competencia.getString("foto")
                                ));
                                id[i] = competenciasList.get(i).getId();
                            }

                            //Creamos instancia del adapter
                            adapter = new MyAdapter(home_organizador.this, competenciasList, new MyAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(int position) {
                                    String idS = new String("" + competenciasList.get(position).getId());
                                    int price = Integer.parseInt(competenciasList.get(position).getPrecioCompetencia());
                                    launchCompetenciaView(idS, price);
                                }
                            });
                            recyclerView.setAdapter(adapter);
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
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void launchCompetenciaView(String id, int Precio) {
        Intent intent = new Intent(home_organizador.this, vista1_organizador.class);
        intent.putExtra("id", id);
        intent.putExtra("precio", Precio);
        startActivity(intent);
    }


    private void homeOrganizador(){
        Intent next = new Intent(this, home_organizador.class);
        startActivity(next);
    }
    private void historialOrganizador(){
        Intent next = new Intent(this, historial_organizador.class);
        startActivity(next);
    }
    private void ajuestesOrganizador(){
        Intent next = new Intent(this, ajustes_organizador.class);
        startActivity(next);
    }
}


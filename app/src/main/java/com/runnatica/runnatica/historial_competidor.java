package com.runnatica.runnatica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
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

public class historial_competidor extends AppCompatActivity {
    private List<Competencias> competenciasList;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Usuario usuario = Usuario.getUsuarioInstance();

    BottomNavigationView MenuUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historial_competidor);
        recyclerView = (RecyclerView) findViewById(R.id.rvHistorialCompetencias);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        MenuUsuario=(BottomNavigationView)findViewById(R.id.bottomNavigation);

        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(2);
        menuItem.setChecked(true);

        MenuUsuario.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.menu_home){
                    home();
                }
                if(menuItem.getItemId() == R.id.menu_busqueda){
                    Busqueda();
                }
                if(menuItem.getItemId() == R.id.menu_historial){
                    Historial();
                }
                if(menuItem.getItemId() == R.id.menu_ajustes){
                    Ajustes();
                }

                return true;
            }
        });

        competenciasList = new ArrayList<>();

        String dominio = getString(R.string.ip);
        cargarHistorialCompetencias(dominio+"obtenerCompetencias.php?id_usuario="+usuario.getId());
    }

    private void obtenerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);

        usuario.setId(preferences.getInt(Login.ID_USUARIO_SESSION, 0));
        usuario.setNombre(preferences.getString(Login.NOMBRE_USUARIO_SESSION, "No_name"));
        usuario.setCorreo(preferences.getString(Login.CORREO_SESSION, "No_mail"));
        usuario.setFechaNacimiento(preferences.getInt(Login.NACIMIENTO_USUARIO_SESSION, 0));
    }

    private void cargarHistorialCompetencias(String URL) {
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

                                //Crear y añadir objeto de tipo Competencias a una Lista
                                competenciasList.add(new Competencias(
                                        competencia.getInt("id_competencia"),
                                        competencia.getString("nom_comp"),
                                        competencia.getString("descripcion"),
                                        competencia.getString("precio"),
                                        competencia.getString("foto")
                                ));
                            }

                            //Creamos instancia del adapter
                            adapter = new MyAdapter(historial_competidor.this, competenciasList, new MyAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(int position) {}
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


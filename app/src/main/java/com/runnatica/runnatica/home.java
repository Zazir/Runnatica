package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class home extends AppCompatActivity {
    RequestQueue requestQueue;
    TextView Competenciatxv, Descripciontxv, Preciotxv;
    ImageView imgCompetencia;
    LinearLayout llConfig;
    Button btnTest,botontemporal;
    BottomNavigationView MenuUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Enlaces de elementos por id's
        Competenciatxv = (TextView)findViewById(R.id.txvTitulo);
        Descripciontxv = (TextView)findViewById(R.id.txvDescripcion);
        Preciotxv = (TextView)findViewById(R.id.txvPrecio);
        imgCompetencia = (ImageView)findViewById(R.id.imgvCompetencia);
        btnTest = (Button)findViewById(R.id.btnPrueba);
        botontemporal=(Button)findViewById(R.id.botondeprueva);

        MenuUsuario=(BottomNavigationView)findViewById(R.id.MenuUsuario);

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

        botontemporal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), crear_competencia.class);
                startActivity(i);
            }
        });

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



    private void TesteoPagoPaypal() {
        Intent intent = new Intent(this, Donaciones.class);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        CargarCompetencias("http://192.168.137.1:811/WebServiceRunnatica/obtenerCompetencias.php");
    }

    private void CargarCompetencias(String URL) {
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(URL, new Response.Listener<JSONArray>() {
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
        requestQueue.add(jsonArrayRequest);
    }

    private void saltarACrearCompe() {
        Intent next = new Intent(this, crear_competencia.class);
        startActivity(next);
    }
}

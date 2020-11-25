package com.runnatica.runnatica;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.runnatica.runnatica.adapter.MyAdapter;
import com.runnatica.runnatica.poho.Competencias;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class home extends AppCompatActivity {
    BottomNavigationView MenuUsuario;
    TextView NombreCiudad, txtAdvertencia;
    Button Estado, Pais;
    int bandera = 0;

    private List<Competencias> competenciasList;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private int[] id;
    String Localizacion;
    double latitud, longitud;
    private String dominio;

    private Usuario user = Usuario.getUsuarioInstance();
    private String fcmToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        obtenerPreferencias();
        fcmToken = FirebaseInstanceId.getInstance().getToken();

        //Enlaces de elementos por id's
        txtAdvertencia = (TextView)findViewById(R.id.tvAdvertencia);
        recyclerView = (RecyclerView) findViewById(R.id.rcvCompetencia);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        NombreCiudad = (TextView) findViewById(R.id.tvNombreCiudad);

        dominio = getString(R.string.ip);

        //Toast.makeText(this, user.getId()+"", Toast.LENGTH_SHORT).show();
        Localizacion();
        guardarPreferencias();
        saveFCMToken(dominio + "notificaciones.php?id_usuario="+ user.getId() + "&token_fcm=" + fcmToken);
        CargarCompetencias(dominio + "home.php?id_usuario="+user.getId()+"&Estado="+Localizacion);

        //Inicializar arreglo de competencias
        competenciasList = new ArrayList<>();

        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        //Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);
        //

        MenuUsuario.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {

                }
                if (menuItem.getItemId() == R.id.menu_busqueda) {
                    Busqueda();
                }
                if (menuItem.getItemId() == R.id.menu_historial) {
                    Historial();
                }
                if (menuItem.getItemId() == R.id.menu_ajustes) {
                    Ajustes();
                }

                return true;
            }
        });

    }
    private void guardarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString("latitud", latitud+"");
        editor.putString("longitud", longitud+"");
        editor.commit();

    }

    private void obtenerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);

        user.setId(preferences.getInt(Login.ID_USUARIO_SESSION, 0));
        user.setNombre(preferences.getString(Login.NOMBRE_USUARIO_SESSION, "No_name"));
        user.setCorreo(preferences.getString(Login.CORREO_SESSION, "No_mail"));
        user.setFechaNacimiento(preferences.getString(Login.NACIMIENTO_USUARIO_SESSION, "0"));
    }

    public void Localizacion(){
        //Localizacion GPS para buscar el nombre de la ciudad
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 1000);
        } else {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            try {
                String city = hereLocation(location.getLatitude(), location.getLongitude());
                NombreCiudad.setText(city);
                Localizacion = city;
            } catch (Exception e) {
                e.printStackTrace();
                Nogps();
                Toast.makeText(home.this, "Tu GPS esta desactivado, activelo para poder continuar", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1000: {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    try {
                        String city = hereLocation(location.getLatitude(), location.getLongitude());

                        NombreCiudad.setText(city);
                    }catch(Exception e){
                        e.printStackTrace();
                        Toast.makeText(home.this, "No funciona", Toast.LENGTH_SHORT).show();

                    }
                }else{
                    Toast.makeText(this,"No has dado permisos aun", Toast.LENGTH_SHORT).show();
                }
                break;
            }
        }
    }

    private String hereLocation(double lat, double lon){
        String cityName = "";
        latitud = lat;
        longitud = lon;
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(lat, lon, 20);
            if(addresses.size() > 0){
                for(Address adr: addresses){
                    if(adr.getAdminArea() != null && adr.getAdminArea().length() > 0){
                        cityName = adr.getAdminArea();
                    }
                    break;
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return cityName;
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
                                id = new int[array.length()];

                                //Añadir valores a los correspondientes textview
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
                            adapter = new MyAdapter(home.this, competenciasList, new MyAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(int position) {
                                    String idS = new String("" + competenciasList.get(position).getId());
                                    launchCompetenciaView(idS);
                                }
                            });
                            if (adapter.getItemCount() <= 0) {
                                txtAdvertencia.setVisibility(View.VISIBLE);
                            }else
                                recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void saveFCMToken(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void launchCompetenciaView(String id) {
        Intent intent = new Intent(home.this, carrera_vista1.class);
        intent.putExtra("id", id);
        intent.putExtra("BanderaHistorial", false);
        startActivity(intent);
    }

    private void Nogps(){
        Intent intent = new Intent(home.this, Nogps.class);
        startActivity(intent);
    }
}
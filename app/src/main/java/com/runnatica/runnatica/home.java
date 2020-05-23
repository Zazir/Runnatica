package com.runnatica.runnatica;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.adapter.MyAdapter;
import com.runnatica.runnatica.poho.Competencias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class home extends AppCompatActivity {
    BottomNavigationView MenuUsuario;
    TextView NombreCiudad;

    private List<Competencias> competenciasList;
    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private int[] id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Enlaces de elementos por id's
        recyclerView = (RecyclerView) findViewById(R.id.rcvCompetencia);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //botongps = (Button) findViewById(R.id.botongps);
        NombreCiudad = (TextView) findViewById(R.id.tvNombreCiudad);

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
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(home.this, "No funciona", Toast.LENGTH_SHORT).show();
            }
        }



        //Inicializar arreglo de competencias
        competenciasList = new ArrayList<>();

        CargarCompetencias("https://runnatica.000webhostapp.com/WebServiceRunnatica/obtenerCompetencias.php");

        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        MenuUsuario.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    home();
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
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        try{
            addresses = geocoder.getFromLocation(lat, lon, 10);
            if(addresses.size() > 0){
                for(Address adr: addresses){
                    if(adr.getLocality() != null && adr.getLocality().length() > 0){
                        cityName = adr.getLocality();
                        break;
                    }
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
                                    Log.i("Position", "el valor de la posición es: "+position);
                                    String idS = new String("" + id[position]);
                                    Log.i("Position", "el valor de la posición es: "+id[position]);
                                    launchCompetenciaView("1");
                                }
                            });
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
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
    }

    private void launchCompetenciaView(String id) {
        Intent intent = new Intent(home.this, carrera_vista1.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}

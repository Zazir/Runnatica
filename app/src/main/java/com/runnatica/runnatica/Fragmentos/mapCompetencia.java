package com.runnatica.runnatica.Fragmentos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.runnatica.runnatica.R;
import com.runnatica.runnatica.crear_competencia;

import androidx.fragment.app.FragmentActivity;

public class mapCompetencia extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private double Latitud = 0.0, Longitud = 0.0;
    private String Competencia_nombre = "";
    private GoogleMap mMap;
    private MarkerOptions marker = new MarkerOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLastViewData();
        setContentView(R.layout.activity_map_competencia);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity)getApplicationContext(), 10);
            dialog.show();
        }

    }

    private void getLastViewData() {
        Bundle extras = mapCompetencia.this.getIntent().getExtras();
        assert extras != null;
        String Lat = extras.getString("Latitud");
        String Lng = extras.getString("Longitud");

        Competencia_nombre = extras.getString("nombre_competencia");
        Latitud = Double.parseDouble(Lat);
        Longitud = Double.parseDouble(Lng);

        Log.i("Coordenadas", "Latitud: "+Latitud);
        Log.i("Coordenadas", "Longitud: "+Longitud);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (!Competencia_nombre.equals("")) {
            createMark(googleMap);
        } else {
            googleMap.setOnMapLongClickListener(this);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {//Es el metodo para recibir la longitud y la latitud LatLng
        Log.i("Coords selected", latLng.latitude+"");//Aqui mostramos en la consola la latitud
        Log.i("Coords selected", latLng.longitude+"");//Aqui Mostramos en la consola la longitud

        if (marker != null) {// Validacion si existe un marcador
            mMap.clear();// Aqui eliminamos los marcadores
            marker = new MarkerOptions();
        }

        mMap.addMarker(marker.position(latLng).title(latLng.latitude + ","+latLng.longitude));//Añadimos el marcador
        String coords = latLng.latitude + " " + latLng.longitude;
        Log.i("Coords to send", coords);//Mostramos las coordenadas
        //regresarCoordenadas(coords);
    }

    private void createMark(GoogleMap googleMap) {//Metodo que se activa cuando se accede desde la vista de la competncia 1
        LatLng startCom = new LatLng(Latitud, Longitud);// Creamos el objeto para crear latitud y longitud
        googleMap.addMarker(new MarkerOptions().position(startCom)
                .title("Inicio de "+Competencia_nombre));//Agregamos un marcador al mapa
        float zoom = 16;

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startCom, zoom));//Movemos el mapa a la latitud y longitud que tiene la carrera
    }

    private void regresarCoordenadas(String Coordenadas) {
        Intent intent = new Intent(mapCompetencia.this, crear_competencia.class);
        intent.putExtra("Coordenadas", Coordenadas);
    }
}

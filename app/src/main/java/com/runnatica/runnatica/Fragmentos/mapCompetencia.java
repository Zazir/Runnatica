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
    public void onMapLongClick(LatLng latLng) {
        Log.i("Coords selected", latLng.latitude+"");
        Log.i("Coords selected", latLng.longitude+"");

        if (marker != null) {
            mMap.clear();
            marker = new MarkerOptions();
        }

        mMap.addMarker(marker.position(latLng).title("Aquí iniciará la carrera"));
        String coords = latLng.latitude + " " + latLng.longitude;
        Log.i("Coords to send", coords);
        regresarCoordenadas(coords);
    }

    private void createMark(GoogleMap googleMap) {
        LatLng startCom = new LatLng(Latitud, Longitud);
        googleMap.addMarker(new MarkerOptions().position(startCom)
                .title("Inicio de "+Competencia_nombre));
        float zoom = 16;

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startCom, zoom));
    }

    private void regresarCoordenadas(String Coordenadas) {
        Intent intent = new Intent(mapCompetencia.this, crear_competencia.class);
        intent.putExtra("Coordenadas", Coordenadas);
    }
}

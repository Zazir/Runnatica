package com.runnatica.runnatica.Fragmentos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.runnatica.runnatica.R;
import com.runnatica.runnatica.crear_competencia;

import androidx.fragment.app.FragmentActivity;

import static com.runnatica.runnatica.crear_competencia.COORDENADAS_COMPETENCIA;

public class mapCompetencia extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private Button btnSelectCoords;

    private String Coordenadas = "";

    private GoogleMap mMap;
    private MarkerOptions marker = new MarkerOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_competencia);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        btnSelectCoords = (Button)findViewById(R.id.btnMandarCordenadas);

        btnSelectCoords.setEnabled(false);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        }else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity)getApplicationContext(), 10);
            dialog.show();
        }

        btnSelectCoords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                regresarCoordenadas();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);

        googleMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {//Es el metodo para recibir la longitud y la latitud LatLng
        if (marker != null) {// Validacion si existe un marcador
            mMap.clear();// Aqui eliminamos los marcadores
            marker = new MarkerOptions();
        }

        mMap.addMarker(marker.position(latLng).title(latLng.latitude + ","+latLng.longitude));//Añadimos el marcador
        Log.i("Coords to send", latLng.toString());//Mostramos las coordenadas
        Coordenadas = latLng.latitude+","+latLng.longitude;
        guardarCoordenadasArchivo(Coordenadas);
        btnSelectCoords.setEnabled(true);
    }

    private void regresarCoordenadas() {
        Intent intent = new Intent(mapCompetencia.this, crear_competencia.class);
        startActivity(intent);
    }

    private void guardarCoordenadasArchivo(String Coordenadas) {
        SharedPreferences preferences = getSharedPreferences("Autoguardado_Competencia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(COORDENADAS_COMPETENCIA, Coordenadas);

        editor.commit();
    }
}

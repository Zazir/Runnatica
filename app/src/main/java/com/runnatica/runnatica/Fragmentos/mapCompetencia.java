package com.runnatica.runnatica.Fragmentos;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.fragment.app.FragmentActivity;

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

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import static com.runnatica.runnatica.crear_competencia.CALLE_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.CIUDAD_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.COLONIA_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.COORDENADAS_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.ESTADO_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.PAIS_COMPETENCIA;

public class mapCompetencia extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private Button btnSelectCoords;

    private String Coordenadas = "", ciudad, estado, pais, colonia, calle;

    private GoogleMap mMap;
    private MarkerOptions marker = new MarkerOptions();
    private List<Address> address;
    private Geocoder geocoder;
    private LatLng Mexico;

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_competencia);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        obtenerPreferencias();

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
        //mMap.moveCamera();
        googleMap.setOnMapLongClickListener(this);

        //mMap = googleMap;
        // googleMapOptions.mapType(googleMap.MAP_TYPE_HYBRID)
        //    .compassEnabled(true);

        // Add a marker in Sydney and move the camera
        //LatLng Mexico = new LatLng(19.686081, -98.871635);
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(Mexico));
        //mMap.setOnMapLongClickListener(this);

        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Mexico));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

    }

    @Override
    public void onMapLongClick(LatLng latLng) {//Es el metodo para recibir la longitud y la latitud LatLng
        if (marker != null) {// Validacion si existe un marcador
            mMap.clear();// Aqui eliminamos los marcadores
            marker = new MarkerOptions();
        }

        mMap.addMarker(marker.position(latLng).title(latLng.latitude + ","+latLng.longitude));//Añadimos el marcador
        Coordenadas = latLng.latitude+","+latLng.longitude;

        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);

            String addres = address.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String addressPart[] = addres.split(",");

            ciudad = address.get(0).getLocality();
            estado = address.get(0).getAdminArea();
            pais = address.get(0).getCountryName();
            colonia = address.get(0).getSubLocality();
            calle = addressPart[0];
        } catch (IOException e) {
            e.printStackTrace();
        }

        guardarCoordenadasArchivo(Coordenadas);
        btnSelectCoords.setEnabled(true);
    }

    private void regresarCoordenadas() {
        Intent intent = new Intent(mapCompetencia.this, crear_competencia.class);
        startActivity(intent);
    }
    private void obtenerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);
        String latitud= preferences.getString("latitud", "");
        String longitud= preferences.getString("longitud", "");

        Mexico = new LatLng(Double.parseDouble(latitud), Double.parseDouble(longitud));
    }

    private void guardarCoordenadasArchivo(String Coordenadas) {
        SharedPreferences preferences = getSharedPreferences("Autoguardado_Competencia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(COORDENADAS_COMPETENCIA, Coordenadas);
        editor.putString(PAIS_COMPETENCIA, pais);
        editor.putString(ESTADO_COMPETENCIA, estado);
        editor.putString(CIUDAD_COMPETENCIA, ciudad);
        editor.putString(COLONIA_COMPETENCIA, colonia);
        editor.putString(CALLE_COMPETENCIA, calle);

        editor.commit();
    }
}

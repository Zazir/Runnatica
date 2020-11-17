package com.runnatica.runnatica.Fragmentos;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

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

import java.util.List;
import java.util.Locale;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

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

        btnSelectCoords = (Button) findViewById(R.id.btnMandarCordenadas);

        btnSelectCoords.setEnabled(false);

        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());
        if (status == ConnectionResult.SUCCESS) {

            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

        } else {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity) getApplicationContext(), 10);
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
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        //mMap.moveCamera();

        mMap = googleMap;
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Mexico));
        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

        googleMap.setOnMapLongClickListener(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {//Es el metodo para recibir la longitud y la latitud LatLng
        if (marker != null) {// Validacion si existe un marcador
            mMap.clear();// Aqui eliminamos los marcadores
            marker = new MarkerOptions();
        }

        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            address = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            String Municipio;

            String addres = address.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
            String addressPart[] = addres.split(",");

            ciudad = address.get(0).getLocality();
            estado = address.get(0).getAdminArea();
            pais = address.get(0).getCountryName();
            colonia = address.get(0).getSubLocality();
            calle = addressPart[0];
            Municipio= address.get(0).getSubAdminArea();

            Log.i("Point_selected", addres);
            Log.i("Point_selected", Municipio + "");
            Log.i("Point_selected", calle + "");

            if (colonia == null && calle.equals(pais) || calle=="Unnamed Road" || calle.equals(Municipio)) {
                Toast.makeText(this, "Debes seleccionar un punto real", Toast.LENGTH_SHORT).show();
            }else {

                mMap.addMarker(marker.position(latLng).title(latLng.latitude + "," + latLng.longitude));//Añadimos el marcador
                Coordenadas = latLng.latitude + "," + latLng.longitude;

                guardarCoordenadasArchivo(Coordenadas);
                btnSelectCoords.setEnabled(true);

            }

        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Tenemos problemas con esa dirección, selecciona alguna un poco separada", Toast.LENGTH_LONG).show();
        }
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

package com.runnatica.runnatica;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //Enlaces de elementos por id's
        Competenciatxv = (TextView)findViewById(R.id.txvTitulo);
        Descripciontxv = (TextView)findViewById(R.id.txvDescripcion);
        Preciotxv = (TextView)findViewById(R.id.txvPrecio);
        imgCompetencia = (ImageView)findViewById(R.id.imgvCompetencia);

    }

    @Override
    protected void onStart() {
        super.onStart();
        CargarCompetencias("http://192.168.137.1:8080/WebServiceRunnatica/obtenerCompetencias.php");
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
}

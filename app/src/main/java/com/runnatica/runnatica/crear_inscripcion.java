package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class crear_inscripcion extends AppCompatActivity{
    private EditText Nombre, CantidadNormal, CantidadForaneos, DesdeAnos, HastaAnos;
    private Button CrearInscripcion, btnFinalizarInscripciones;
    private TextView txtcantidadInscripciones;

    private String idCompetencia;
    private int contInscripciones = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_inscripcion);

        Nombre = (EditText) findViewById(R.id.etNombreInscripcion);
        CantidadNormal = (EditText) findViewById(R.id.etCantidadNormal);
        CantidadForaneos = (EditText) findViewById(R.id.etCantidadForaneos);
        DesdeAnos = (EditText) findViewById(R.id.etDesdeAnos);
        HastaAnos = (EditText) findViewById(R.id.etHastaAnos);
        CrearInscripcion = (Button) findViewById(R.id.btnCrearInscripcion);
        btnFinalizarInscripciones = (Button)findViewById(R.id.btnFinalizarCompetencia);
        txtcantidadInscripciones = (TextView)findViewById(R.id.tvCantidadInscripciones);

        idCompetenciaFromLastView();

        CrearInscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validaciones()){
                String ip = getString(R.string.ip);
                crearInscripcion(ip + "agregarInscripciones.php?" +
                        "nombreInscripcion=" + Nombre.getText().toString().replaceAll(" ", "%20") +
                        "&id_competencia=" + idCompetencia +
                        "&cant_usuarios=" + CantidadNormal.getText().toString() +
                        "&cant_foraneos=" + CantidadForaneos.getText().toString() +
                        "&edadMin=" + DesdeAnos.getText().toString() +
                        "&edadMax=" + HastaAnos.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnFinalizarInscripciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAlHome();
            }
        });
    }

    private void volverAlHome() {
        Intent intent = new Intent(crear_inscripcion.this, home.class);
        startActivity(intent);
        finish();
    }

    private void idCompetenciaFromLastView() {
        Bundle bundle = crear_inscripcion.this.getIntent().getExtras();
        idCompetencia = bundle.getString("ID_COMPETENCIA");
    }

    private void crearInscripcion(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Inscripcion Creada")){
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            contInscripciones++;
                            txtcantidadInscripciones.setText("Has definido " + contInscripciones + " inscripciones");
                            btnFinalizarInscripciones.setEnabled(true);
                            Nombre.setText("");
                            CantidadNormal.setText("");
                            CantidadForaneos.setText("");
                            DesdeAnos.setText("");
                            HastaAnos.setText("");
                        } else
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Hubo un error con el servidor: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private Boolean Validaciones() {
        Boolean siguiente = false;
        if (Nombre.getText().toString().length() <= 0) {
            Nombre.setError("Debes de poner el nombre de la inscripcion");
        }
        else if (CantidadNormal.getText().toString().length() <= 0 || CantidadNormal.getText().toString().length() >= 5) {
            CantidadNormal.setError("Ingresa una Cantidad Valida");
        }
        else if (CantidadForaneos.getText().toString().length() <= 0 || CantidadForaneos.getText().toString().length() >= 5) {
            CantidadForaneos.setError("Ingresa una Cantidad Valida");
        }
        else if (DesdeAnos.getText().toString().length() <= 0 || DesdeAnos.getText().toString().length() >= 3) {
            DesdeAnos.setError("Ingresa un Año Valido");
        }
        else if (HastaAnos.getText().toString().length() <= 0 || HastaAnos.getText().toString().length() >= 3) {
            HastaAnos.setError("Ingresa un Año Valido");
        }else siguiente = true;

        return siguiente;
    }
}
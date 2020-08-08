package com.runnatica.runnatica;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.PDF.PlantillaPDF;

import androidx.appcompat.app.AppCompatActivity;

public class AsignarUsuarios extends AppCompatActivity {
    EditText idUsuario, NombreUsuario, idCompetencia, NombreCompetencia;
    Button btnAsignar;

    private String correo, nombreUsuario, nombreCompetencia, lugar;

    private String dominio;
    private PlantillaPDF plantillaPDF = new PlantillaPDF(AsignarUsuarios.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_usuarios);

        idUsuario = (EditText)findViewById(R.id.etIdUsuario);
        NombreUsuario = (EditText)findViewById(R.id.etNombreUsuario);
        idCompetencia = (EditText)findViewById(R.id.idCompetencia);
        NombreCompetencia = (EditText)findViewById(R.id.etNombreCompetencia);
        btnAsignar = (Button)findViewById(R.id.btnAsignarUsuarios);

        dominio = getString(R.string.ip);

        btnAsignar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reflejarInscripcion(dominio + "inscribirUsuario.php?" +
                        "id_usuario="+ idUsuario.getText().toString() +
                        "&id_competencia=" + idCompetencia.getText().toString());

                crearPDF();
            }
        });
    }

    private void reflejarInscripcion(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Hacer algo en la respuesta
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexión al servidor", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void obtenerDatosPdf(String URL) {
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexión al servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }

    private void crearPDF() {
        plantillaPDF.crearArchivo();

        plantillaPDF.abrirArchivo();
        plantillaPDF.addMetadata("Carrera", "Inscripcion", "Runnatica");
        plantillaPDF.addHeaders(nombreCompetencia, "Marca", "");
        plantillaPDF.addParagraph("Código QR");
        plantillaPDF.addParagraph(nombreUsuario);
        plantillaPDF.addParagraph(lugar);
        plantillaPDF.addParagraph("Organizador");
        plantillaPDF.cerrarDocumento();
        plantillaPDF.sendMail(correo);
    }
}
package com.runnatica.runnatica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class editar_competencia2 extends AppCompatActivity {

    EditText EditarNombre, EditarDescripcion;
    Button SeleccionarFoto, Siguiente;
    private ImageView imgCompetencia;

    private String path, dominio, id_competencia;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_competencia2);

        EditarNombre = (EditText)findViewById(R.id.etNombreCompetencia);
        EditarDescripcion = (EditText)findViewById(R.id.etEditarDescripcion);
        SeleccionarFoto = (Button)findViewById(R.id.btnImagenCompetencia);
        Siguiente = (Button)findViewById(R.id.btnGuardarEditar);
        imgCompetencia = (ImageView)findViewById(R.id.imgCompetencia);

        dominio = getString(R.string.ip);
        getLastViewData();

        SeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarImagen();
            }
        });

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActualizarCompetencia(dominio + "actualizarCompetencia.php?" +
                        "nombre_competencia=" + EditarNombre.getText().toString().replaceAll(" ", "%20") +
                        "&descripcion=" + EditarDescripcion.getText().toString().replaceAll(" ", "%20") +
                        "&Foto=" + path +
                        "&id_competencia=" + id_competencia);

                Salir();
            }
        });
    }

    private void ActualizarCompetencia(String URL) {
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), "Competencia actualizada con éxito", Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Hubo un error con la conexión", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private void Salir(){
        Intent next = new Intent(this, vista1_organizador.class);
        startActivity(next);
        finish();
    }

    private void getLastViewData() {
        Bundle extra = editar_competencia2.this.getIntent().getExtras();
        id_competencia = extra.getString("id_competencia");
    }

    private void subirImagenCompetencia(String URL) {
        ProgressDialog progreso = new ProgressDialog(editar_competencia2.this);
        progreso.setMessage("Guardando Imagen...");
        progreso.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progreso.dismiss();
                        Log.i("Respuesta_img", response);
                        if (response.equals("Error al subir")) {
                            Toast.makeText(getApplicationContext(), "La imagen no se pudo subir con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            path = response;
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progreso.dismiss();
                        Toast.makeText(getApplicationContext(), "Hubo un error con la conexión", Toast.LENGTH_SHORT).show();
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String imagen = getStringImage(bitmap);
                String nombreFoto = (System.currentTimeMillis()/1000)+"";

                Map<String, String> parametros = new HashMap<>();
                parametros.put("Foto", imagen);
                parametros.put("NombreFoto", nombreFoto);

                return parametros;
            }
        };

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private String getStringImage(Bitmap bitmap) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imgBytes = array.toByteArray();

        return Base64.encodeToString(imgBytes, Base64.DEFAULT);
    }

    private void CargarImagen() {//funcion de tipo void para hacer un proceso
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            //imgCarrera.setImageURI(path);//poner la imagen que busco
            Uri path = data.getData();//valor que te devuelve el metodo sobrecargado (el data es la imagen de la galeria)

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                imgCompetencia.setImageBitmap(bitmap);//Setear bitmap de la imagen en el ImageView
                subirImagenCompetencia(dominio+"guardarImagen.php?");
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
package com.runnatica.runnatica;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class editar_competencia2 extends AppCompatActivity {

    EditText EditarNombre, EditarDescripcion, EditarPrecio;
    Button SeleccionarFoto, Siguiente, EditarCategoria;
    private ImageView imgCompetencia;
    BottomNavigationView MenuOrganizador;
    private StringRequest request;

    private String path, dominio, id_competencia;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_competencia2);
        MenuOrganizador= (BottomNavigationView)findViewById(R.id.MenuOrganizador);

        EditarNombre = (EditText)findViewById(R.id.etNombreCompetencia);
        EditarDescripcion = (EditText)findViewById(R.id.etEditarDescripcion);
        EditarPrecio = (EditText)findViewById(R.id.etEditarPrecio);
        SeleccionarFoto = (Button)findViewById(R.id.btnImagenCompetencia);
        Siguiente = (Button)findViewById(R.id.btnGuardarEditar);
        imgCompetencia = (ImageView)findViewById(R.id.imgCompetencia);
        EditarCategoria = (Button)findViewById(R.id.btnEditarCategoria);

        dominio = getString(R.string.ip);
        getLastViewData();

        Menu menu = MenuOrganizador.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);

        SeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarImagen();
            }
        });

        EditarCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarCategoria();
            }
        });

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Salir();
                /*if (Validaciones()) {
                }else
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();*/
                }
        });

        EditarNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ActualizarCompetencia(dominio + "actualizarCompetencia.php?" +
                        "nombre_competencia=" + EditarNombre.getText().toString().replaceAll(" ", "%20") +
                        "&id_competencia=" + id_competencia);
            }
        });
        EditarPrecio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ActualizarCompetencia(dominio + "actualizarCompetencia.php?" +
                        "Precio=" + EditarPrecio.getText().toString().replaceAll(" ", "%20") +
                        "&id_competencia=" + id_competencia);
            }
        });

        EditarDescripcion.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ActualizarCompetencia(dominio + "actualizarCompetencia.php?" +
                        "descripcion=" + EditarDescripcion.getText().toString().replaceAll(" ", "%20") +
                        "&id_competencia=" + id_competencia);
            }
        });
    }

    private void ActualizarCompetencia(String URL) {
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getApplicationContext(), "", Toast.LENGTH_SHORT).show();
                        //Salir();
                        Log.i("Respuesta", response);
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
        Intent next = new Intent(this, home_organizador.class);
        startActivity(next);
        finish();
    }

    private void getLastViewData() {
        Bundle extra = editar_competencia2.this.getIntent().getExtras();
        id_competencia = extra.getString("id_competencia");
        ConsultarNombre();
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
                        if (response.equals("Error al subir")) {
                            Toast.makeText(getApplicationContext(), "La imagen no se pudo subir con éxito", Toast.LENGTH_SHORT).show();
                        } else {
                            path = response;
                            ActualizarCompetencia(dominio + "actualizarCompetencia.php?" +
                                    "Foto=" + path +
                                    "&id_competencia=" + id_competencia);
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
    private void ConsultarNombre(){
        String URL = dominio + "obtenerDatosCompetencia.php?id_competencia="+id_competencia+"&consulta=4";
        request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        EditarNombre.setText(response);

                        //txtTotalUsuarios.setText();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(editar_competencia2.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }

    private Boolean Validaciones() {
        Boolean siguiente = false;


        return siguiente;
    }

    private void EditarCategoria(){
        Intent intent = new Intent(editar_competencia2.this, EditarCategoria1.class);
        intent.putExtra("ID_COMPENTENCIA5", id_competencia);
        startActivity(intent);
    }
}
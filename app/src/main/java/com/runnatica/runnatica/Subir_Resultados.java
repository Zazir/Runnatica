package com.runnatica.runnatica;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Subir_Resultados extends AppCompatActivity {

    Button btnImagen, btnSubirResultados;
    private ImageView img;
    private Bitmap bitmap;
    private ProgressDialog progreso;
    BottomNavigationView MenuOrganizador;
    private String path = "xxx";
    boolean validar = false;

    private String dominio, id_competencia;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir__resultados);

        btnImagen = (Button)findViewById(R.id.btnSeleccioarResultados);
        btnSubirResultados = (Button)findViewById(R.id.btnSubirResultados);
        img = (ImageView)findViewById(R.id.ivFotosResultados);
        MenuOrganizador= (BottomNavigationView)findViewById(R.id.MenuOrganizador);

        Menu menu = MenuOrganizador.getMenu();
        MenuItem menuItem= menu.getItem(2);
        menuItem.setChecked(true);

        MenuOrganizador.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    homeOrganizador();
                }
                if (menuItem.getItemId() == R.id.menu_historial) {
                    historialOrganizador();
                }
                if (menuItem.getItemId() == R.id.menu_ajustes) {
                    ajuestesOrganizador();
                }
                if (menuItem.getItemId() == R.id.menu_regresar) {
                    home();
                }

                return true;
            }
        });

        dominio = getString(R.string.ip);
        getLastViewData();

        cargarImagen(dominio+ "resultadosCompetencia.php?id_competencia="+ id_competencia);

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarImagen();
            }
        });

        btnSubirResultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validar==true){
                actualizarResultadosCompetencia(dominio+"actualizarCompetencia.php?"+
                        "PathFoto=" + path +
                        "&id_competencia=" + id_competencia);
                }else{
                    Toast.makeText(getApplicationContext(), "Debes de subir una foto primero", Toast.LENGTH_SHORT).show();
                    btnSubirResultados.setError("No se ha seleccionado una imagen");
                }
            }
        });
    }

    private void getLastViewData() {
        Bundle extra = Subir_Resultados.this.getIntent().getExtras();
        id_competencia = extra.getString("id_competencia");
    }

    private String getStringImage(Bitmap btm) {// se recive el parametro
        ByteArrayOutputStream array = new ByteArrayOutputStream(); //Declaramos el objeto que le da el formato de texto a la imagen
        btm.compress(Bitmap.CompressFormat.JPEG, 100, array);//Comprimimos el bitmap de la imagen
        byte[] imgBytes = array.toByteArray();//creamos un arreglo de bites
        String encodeImg = Base64.encodeToString(imgBytes, Base64.DEFAULT);//Creamos un sting el cual guarda la imagen codificada de forma de cadena de texto

        return encodeImg;//retornamos el sting que es la imagen codificada
    }

    private void subirImagenResultados(String URL) {
        progreso = new ProgressDialog(Subir_Resultados.this);
        progreso.setMessage("Guardando Imagen...");
        progreso.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progreso.hide();
                        if (response.equals("Error al subir")) {
                            Toast.makeText(getApplicationContext(), "La imagen no se pudo subir", Toast.LENGTH_SHORT).show();
                        } else {
                            //Aqui recibo el URL de la Imagen
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

    private void CargarImagen() {//funcion de tipo void para hacer un proceso
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);//Intent se inicalisa (action pick formato para buscar en la galeria) y entrar imagenes
        intent.setType("image/");//el tipo de archivo que va a buscar va a ser imagen.
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), 10);//inicia la actividad y recibe el intent (createChooser muestra las opciones para mostrtar imagenes)
    }

    private void cargarImagen(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject respuesta = jsonArray.getJSONObject(0);

                            if(respuesta.optString("resultados").equals("xxx")){
                                return;
                            }else {
                                Glide.with(Subir_Resultados.this).load(respuesta.optString("resultados")).into(img);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexión al servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){//si el usuario ya selecciono la imagen
            Uri path = data.getData();//valor que te devuelve el metodo sobrecargado (el data es la imagen de la galeria)
            img.setImageURI(path);//poner la imagen que busco

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), path);//metodo para convetir la imagen a bitmap, el path es la imagen que obtuviste del metodo sobrecargado
                img.setImageBitmap(bitmap);//Se obtiene el bipmap
                subirImagenResultados(dominio + "guardarImagen.php?");
                validar = true;

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void actualizarResultadosCompetencia(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (response.equals("Resultados registrados")) {
                    Toast.makeText(getApplicationContext(), "Se guardaron los resultados exitosamente", Toast.LENGTH_SHORT).show();
                    //img.setImageResource(android.R.color.transparent);
                    //homeOrganizador();
                    finish();
                }
                else Toast.makeText(getApplicationContext(), "Hubo un problema con el servidor", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Hubo un error con la conexión", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void homeOrganizador(){
        Intent next = new Intent(this, home_organizador.class);
        startActivity(next);
    }
    private void historialOrganizador(){
        Intent next = new Intent(this, historial_organizador.class);
        startActivity(next);
    }
    private void ajuestesOrganizador(){
        Intent next = new Intent(this, ajustes_organizador.class);
        startActivity(next);
    }
    private void home(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }

}
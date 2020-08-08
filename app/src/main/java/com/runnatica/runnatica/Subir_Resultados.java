package com.runnatica.runnatica;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Subir_Resultados extends AppCompatActivity {

    Button btnImagen, btnSubirResultados;
    private ImageView img;
    private Bitmap bitmap;
    private ProgressDialog progreso;
    BottomNavigationView MenuOrganizador;
    private String path = "xxx";

    private String dominio;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir__resultados);

        btnImagen = (Button)findViewById(R.id.btnSeleccioarResultados);
        btnSubirResultados = (Button)findViewById(R.id.btnSubirResultados);

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

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CargarImagen();
            }
        });

        btnSubirResultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubirImagen(dominio + "actualizarCompetencia.php?");
            }
        });


    }

    private String getStringImage(Bitmap btm) {// se recive el parametro
        ByteArrayOutputStream array = new ByteArrayOutputStream(); //Declaramos el objeto que le da el formato de texto a la imagen
        btm.compress(Bitmap.CompressFormat.JPEG, 100, array);//Comprimimos el bitmap de la imagen
        byte[] imgBytes = array.toByteArray();//creamos un arreglo de bites
        String encodeImg = Base64.encodeToString(imgBytes, Base64.DEFAULT);//Creamos un sting el cual guarda la imagen codificada de forma de cadena de texto

        return encodeImg;//retornamos el sting que es la imagen codificada
    }

    private void SubirImagen(String URL) {//recibimos la url
        progreso = new ProgressDialog(Subir_Resultados.this);//creas dialogo de proceso
        progreso.setMessage("Subiendo Imagen...");//el mensaje
        progreso.show();//se lanza

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>(){
            @Override
            public void onResponse(String response) {
                progreso.dismiss();
                if (response.equals("")){//Si es diferente de nulo entonces se subio la imagen
                    Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    peticion(dominio + "subirImagen.php?urlimagen=" + response);
                } else if (Integer.parseInt(response) >= 0){//si la respuesta es mayor o iguala cero (ya que retornamos el id de la competenbcia) si creamos una competenbcia va a ser mayopr a cero

                }
            }//resibimos el parametro, el metodo de peticion
        }, new Response.ErrorListener() {//dice si hubo un error en el web servic
            @Override
            public void onErrorResponse(VolleyError error) {// Cuando hay un problema en la conexion.
                progreso.dismiss();
                Toast.makeText(getApplicationContext(), "Hubo un error con la conexión", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                String Foto = getStringImage(bitmap);
                Map<String, String> parametros = new HashMap<>();//nombre que recibes
                Random rand = new Random(System.currentTimeMillis());
                String cadena="";
                parametros.put("Foto", Foto);
                parametros.put("NombreFoto", cadena+rand);
                parametros.put("id_competencia", cadena+rand);
                return parametros; //retornamos los parametros enlazados
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);//creamos la peticion hacemos la peticion
        requestQueue.add(stringRequest);//hacemos la peticion

    }
    private void subirImagenCompetencia(String URL) {
        progreso = new ProgressDialog(Subir_Resultados.this);
        progreso.setMessage("Guardando Imagen...");
        progreso.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progreso.hide();
                        Log.i("Respuesta_img", response);
                        if (response.equals("Error al subir")) {
                            Toast.makeText(getApplicationContext(), "La imagen no se pudo subir con éxito", Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){//si el usuario ya selecciono la imagen
            Uri path = data.getData();//valor que te devuelve el metodo sobrecargado (el data es la imagen de la galeria)
            img.setImageURI(path);//poner la imagen que busco

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), path);//metodo para convetir la imagen a bitmap, el path es la imagen que obtuviste del metodo sobrecargado
                img.setImageBitmap(bitmap);//Se obtiene el bipmap
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void peticion(String url){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

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
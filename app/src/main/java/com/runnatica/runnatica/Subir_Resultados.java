package com.runnatica.runnatica;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Subir_Resultados extends AppCompatActivity {

    Button Atras,btnImagen, btnSubirResultados;
    private ImageView img;
    private Bitmap bitmap;
    private ProgressDialog progreso;

    private String dominio;


    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir__resultados);

        Atras = (Button)findViewById(R.id.btnAtras);
        btnImagen = (Button)findViewById(R.id.btnSeleccioarResultados);
        btnSubirResultados = (Button)findViewById(R.id.btnSubirResultados);

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

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atras();
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
                if (response.equals("error al crear el competencia")){//mensaje desde el web service, si el respose es igual a "error al crear competencia"
                    Toast.makeText(getApplicationContext(), "Hubo un error al Imagen", Toast.LENGTH_SHORT).show();
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


    void atras(){
        Intent next = new Intent(this, vista1_organizador.class);
        startActivity(next);
    }
}
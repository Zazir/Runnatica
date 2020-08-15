package com.runnatica.runnatica;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.poho.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import static com.runnatica.runnatica.Login.CORREO_SESSION;
import static com.runnatica.runnatica.Login.ID_USUARIO_SESSION;
import static com.runnatica.runnatica.Login.NACIMIENTO_USUARIO_SESSION;
import static com.runnatica.runnatica.Login.NOMBRE_USUARIO_SESSION;


public class registro_usuario extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //Mínimo 1 digito
                    "(?=.*[a-z])" +         //Mínimo 1 minúscula
                    "(?=.*[A-Z])" +         //Mínimo 1 mayúscula
                    ".{8,}" +               //Mínimo 8 caracteres
                    "$");

    Button Hombre, Mujer, Foto, Registrarse, Fecha;
    EditText Nombre, Ciudad;
    CheckBox Terminos;
    TextView Condiciones, MostrarFecha;
    private Spinner Estado, Pais;

    int flagTerminos = 0, flagFecha = 0;
    private String genero = "";
    private String dominio, Correo, Contrasena;
    private String FechaNacimiento, pais, estado;
    private Calendar calendar;
    private DatePickerDialog picker;
    private String path = "xxx";
    private ProgressDialog progreso;
    private Bitmap bitmap;
    private ImageView FotoUsuario;
    private Usuario user = Usuario.getUsuarioInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        dominio = getString(R.string.ip);

        //Enlaces de elementos por id's
        Hombre = (Button)findViewById(R.id.btnHombre);
        Mujer = (Button)findViewById(R.id.btnMujer);
        Foto = (Button)findViewById(R.id.btnFoto);
        Registrarse = (Button)findViewById(R.id.btnRegistrarse);
        Terminos = (CheckBox)findViewById(R.id.checkTerminos);
        Nombre = (EditText)findViewById(R.id.etNombre);
        Fecha = (Button) findViewById(R.id.btnSeleccionNacimiento);
        MostrarFecha = (TextView) findViewById(R.id.tvMostrarFecha);
        Condiciones = (TextView)findViewById(R.id.tvCondiciones);
        Ciudad = (EditText)findViewById(R.id.etCiudad);
        Estado = (Spinner) findViewById(R.id.spEstado);
        Pais = (Spinner) findViewById(R.id.spPais);
        FotoUsuario = (ImageView)findViewById(R.id.ivFotoUsuarioRegistro);

        getLastViewData();
        cargarSpinnerEstado();
        cargarSpinnerPais();

        Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validaciones()) {
                    Log.i("Problema", "entra");
                    SubirUsuario(dominio + "agregarUsuario.php?" +
                            "NombreYApellido=" + Nombre.getText().toString().replaceAll(" ", "%20") +
                            "&Email=" + Correo +
                            "&Contrasena=" + Contrasena +
                            "&Sexo=" + genero +
                            "&FechaNacimiento=" + FechaNacimiento +
                            "&Telefono=0" +
                            "&Terminos=" + flagTerminos +
                            "&Ciudad=" + Ciudad.getText().toString().replaceAll(" ", "%20") +
                            "&Estado=" + estado.replaceAll(" ", "%20") +
                            "&Pais=" + pais.replaceAll(" ", "%20") +
                            "&PathFoto=" + path);
                }
                else
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();


            }
        });

        Foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarImagen();
            }
        });

        Terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagTerminos = 1;
                Toast.makeText(registro_usuario.this, "Aceptaste los Terminos y condiciones" , Toast.LENGTH_SHORT).show();
            }
        });

        Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int ano = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(registro_usuario.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String MesTemp = month+1+ "";
                        if(MesTemp.length() ==1){
                            MesTemp = "0" + MesTemp;
                            FechaNacimiento = dayOfMonth + "" + MesTemp + "" + year + "";
                            MostrarFecha.setText(FechaNacimiento);
                            flagFecha = 1;
                        }
                    }
                }, ano, mes, dia);

                picker.show();

            }
        });

        Hombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genero = "Hombre";
                Toast.makeText(getApplicationContext(), "Eres un atlético " + genero, Toast.LENGTH_SHORT).show();
                Mujer.setBackgroundColor(Color.GRAY);
                Hombre.setBackgroundColor(Color.RED);
            }
        });

        Mujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genero = "Mujer";
                Toast.makeText(getApplicationContext(), "Eres una " + genero + " muy fit", Toast.LENGTH_SHORT).show();
                Hombre.setBackgroundColor(Color.GRAY);
                Mujer.setBackgroundColor(Color.RED);
            }
        });

        Condiciones.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), terminosycondiciones.class);
                startActivity(i);
            }
        });
    }

    private void cargarSpinnerEstado() {
        ArrayAdapter<CharSequence> opcionesSpCat = ArrayAdapter.createFromResource(this, R.array.estados, android.R.layout.simple_spinner_item);
        Estado.setAdapter(opcionesSpCat);

        Estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estado = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void cargarSpinnerPais() {
        ArrayAdapter<CharSequence> opcionesSpCat = ArrayAdapter.createFromResource(this, R.array.paises, android.R.layout.simple_spinner_item);
        Pais.setAdapter(opcionesSpCat);

        Pais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pais = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri path = data.getData();//valor que te devuelve el metodo sobrecargado (el data es la imagen de la galeria)
            FotoUsuario.setImageURI(path);//poner la imagen que busco

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                FotoUsuario.setImageBitmap(bitmap);//Se obtiene el bipmap
                subirImagen(dominio + "guardarImagen.php?");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    * Función diseñada para mandar
    * */

    private void subirImagen(String URL) {
        progreso = new ProgressDialog(registro_usuario.this);
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
                            Log.i("RutaImagen", response);
                            path = response;
                            Toast.makeText(getApplicationContext(), "Imagen Subida con Exito", Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), 10);
    }



    private void SubirUsuario(String URL) {
        //stringRequest es el objeto el cual almacena los datos.
        //Declaramos un StringRequest definiendo el método que utilizamos, en este caso es GET

        Log.i("Problema", URL);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {//Aqui recibimos un objeto como parametro, es la respuesta a un listener
            @Override
            public void onResponse(String response) {//Operacion exitosa a travez del web service
                int temp;
                try{
                    temp = Integer.parseInt(response);
                    if(temp >= 0){
                        Toast.makeText(getApplicationContext(), "Usuario Creado", Toast.LENGTH_SHORT).show();

                        user.setId(temp);
                        user.setFechaNacimiento(FechaNacimiento);
                        user.setCorreo(Correo);
                        user.setNombre(Nombre.getText().toString());

                        guardarPreferencias();
                        alHome();
                    }
                }catch(Exception e){
                    Log.i("Tipo", e.toString());
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {// Cuando hay un problema en la conexion.
                Toast.makeText(getApplicationContext(), "Hubo un error" + error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void guardarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(ID_USUARIO_SESSION, user.getId());
        editor.putString(NOMBRE_USUARIO_SESSION, user.getNombre());
        editor.putString(CORREO_SESSION, user.getCorreo());
        editor.putString(NACIMIENTO_USUARIO_SESSION, user.getFechaNacimiento());

        editor.commit();
    }

    private Boolean Validaciones() {
        Boolean siguiente = false;

        if (Nombre.getText().toString().length() <= 0){
            Nombre.setError("Debes poner tu nombre");
        }else if (genero.length() == 0){
            Toast.makeText(this, "Selecciona tu sexo", Toast.LENGTH_SHORT).show();
        }else if (flagTerminos == 0){
            Terminos.setError("Debes de Aceptar terminos y condiciones");
        }else if (flagFecha == 0){
             Fecha.setError("Debes de seleccionar una fecha");
        }else if (Ciudad.getText().toString().length() <= 0) {
            Ciudad.setError("Agrega tu ciudad");
        }else if (estado.length() <= 0){
            siguiente = false;
        }else if (pais.length() <= 0){
            siguiente = false;
        }else siguiente = true;

        return siguiente;
    }


    private void getLastViewData() {
        Bundle extra = registro_usuario.this.getIntent().getExtras();
        Correo = extra.getString("Correo");
        Contrasena = extra.getString("Contrasena");
    }

    private void alHome(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }
    private String getStringImage(Bitmap btm) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        btm.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imgBytes = array.toByteArray();
        String encodeImg = Base64.encodeToString(imgBytes, Base64.DEFAULT);

        return encodeImg;
    }
}

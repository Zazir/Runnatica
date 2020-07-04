package com.runnatica.runnatica;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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

public class crear_competencia extends AppCompatActivity {

    private TextView txtFecha, txtHora;
    private EditText Nombre, Precio, GradosUbicacion, Ciudad, Colonia, Calle, Descripcion;
    private Button Informacion, btnImagen, btnDate, Aval, Guardar, btnHora;
    private Spinner Pais, Estado;
    private RadioButton SiReembolso, NoReembolso;
    private ImageView img;
    private Usuario usuario = Usuario.getUsuarioInstance();

    private Bitmap bitmap;
    private ProgressDialog progreso;

    private Calendar calendar;
    private DatePickerDialog picker;
    private TimePickerDialog timePicker;
    private String fecha;
    private String hora;
    private String Reembolso;

    private String dominio;



    int imagen=0;
    private String path = "xxx", estado, pais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_competencia);

        dominio = getString(R.string.ip);
        obtenerPreferencias();

        Nombre=(EditText)findViewById(R.id.etNombreCompetencia);
        Precio = (EditText)findViewById(R.id.etPrecioCompetencia);
        Informacion = (Button)findViewById(R.id.btnInformacionPrecio);
        btnHora = (Button) findViewById(R.id.btnHoraCompetencia);
        btnImagen = (Button) findViewById(R.id.btnImagenCompetencia);
        btnDate = (Button)findViewById(R.id.btnSeleccionarFecha);
        img = (ImageView)findViewById(R.id.imgCompetencia);
        Ciudad = (EditText)findViewById(R.id.etCiudadCompetencia);
        Colonia = (EditText)findViewById(R.id.etColoniaCompetencia);
        Pais = (Spinner) findViewById(R.id.spPaisCompetencia);
        Estado = (Spinner) findViewById(R.id.spEstadoCompetencia);
        Calle = (EditText)findViewById(R.id.etCalleCompetencia);
        Aval = (Button) findViewById(R.id.btnAvalCompetencia);
        Descripcion = (EditText)findViewById(R.id.etDescripcionCompetencia);
        SiReembolso = (RadioButton) findViewById(R.id.rbSiReembolso);
        NoReembolso = (RadioButton) findViewById(R.id.rbNoReembolso);
        Guardar = (Button) findViewById(R.id.btnGuardarCompetencia);
        txtFecha = (TextView)findViewById(R.id.tvFechaPicker);
        txtHora = (TextView)findViewById(R.id.tvHoraCompetencia);

        Toast.makeText(this, usuario.getId()+"", Toast.LENGTH_SHORT).show();

        cargarSpinnerEstado();
        cargarSpinnerPais();

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Guardar imagen
                CargarImagen();
                imagen = 1;
            }
        });
        Aval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar la imagen del aval

            }
        });

        btnHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int horas = calendar.get(Calendar.HOUR_OF_DAY);
                int minuto = calendar.get(Calendar.MINUTE);

                timePicker = new TimePickerDialog(crear_competencia.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        hora = hourOfDay + ":" + minute;
                        txtHora.setText(hora);
                    }
                }, horas, minuto, true);

                timePicker.show();
            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int ano = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(crear_competencia.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fecha = year + "-" + (month+1) + "-" + dayOfMonth;
                        txtFecha.setText(fecha);
                    }
                }, ano, mes, dia);

                picker.show();
            }
        });

        Informacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Informacion();
            }
        });

        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //subirImagenCompetencia("http://192.168.137.1:811/WebServiceRunnatica/agregarCompetencia.php?");
                if (Validaciones()){
                    //subirImagenCompetencia("https://runnatica.000webhostapp.com/WebServiceRunnatica/agregarCompetencia.php?");
                    subirImagenCompetencia(dominio + "uploadImg.php?");
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void obtenerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);

        usuario.setId(preferences.getInt(Login.ID_USUARIO_SESSION, 0));
        usuario.setNombre(preferences.getString(Login.NOMBRE_USUARIO_SESSION, "No_name"));
        usuario.setCorreo(preferences.getString(Login.CORREO_SESSION, "No_mail"));
        usuario.setFechaNacimiento(preferences.getInt(Login.NACIMIENTO_USUARIO_SESSION, 0));
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

    private void SubirCompetencia(String URL) {
        progreso = new ProgressDialog(crear_competencia.this);
        progreso.setMessage("Creando competencia...");
        progreso.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.dismiss();
                if (response.equals("NO")) {
                    Toast.makeText(getApplicationContext(), "Hoy ya creaste una competencia", Toast.LENGTH_SHORT).show();
                }else if (response.equals("error al crear el competencia")){
                    Toast.makeText(getApplicationContext(), "Hubo un error al crear la competencia", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(response) >= 0){
                    alCrearInscripcion(response);
                }
            }
        }, new Response.ErrorListener() {//dice si hubo un error en el web servic
            @Override
            public void onErrorResponse(VolleyError error) {// Cuando hay un problema en la conexion.
                progreso.dismiss();
                Toast.makeText(getApplicationContext(), "Hubo un error con la conexión", Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);//creamos la peticion hacemos la peticion
        requestQueue.add(stringRequest);//hacemos la peticion
    }

    private void subirImagenCompetencia(String URL) {
        progreso = new ProgressDialog(crear_competencia.this);
        progreso.setMessage("Guardando Imagen...");
        progreso.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progreso.hide();
                        path = response;
                        SubirCompetencia(dominio + "agregarCompetencia.php?" +
                                "Id_usuario=" + usuario.getId() +
                                "&Descripcion=" +Descripcion.getText().toString().replaceAll(" ", "%20")+
                                "&Aval=Aval" +
                                "&Coordenadas=95959595959" +
                                "&Nombre_competencia=" + Nombre.getText().toString().replaceAll(" ", "%20")+
                                "&Pais=" + pais +
                                "&Colonia=" +Colonia.getText().toString().replaceAll(" ", "%20")+
                                "&Calle=" +Calle.getText().toString().replaceAll(" ", "%20")+
                                "&Ciudad=" +Ciudad.getText().toString().replaceAll(" ", "%20")+
                                "&Fecha=" +fecha+
                                "&Hora=" +hora+
                                "&Estado=" + estado +
                                "&Reembolso=N" +
                                "&Precio=" +Precio.getText().toString()+
                                "&path="+path);
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
            Uri path = data.getData();//valor que te devuelve el metodo sobrecargado (el data es la imagen de la galeria)
            img.setImageURI(path);//poner la imagen que busco

            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                img.setImageBitmap(bitmap);//Se obtiene el bipmap
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

        private Boolean Validaciones() {
        Boolean siguiente = false;

        if (Nombre.getText().toString().length() <= 0) {
            Nombre.setError("Debes de poner el nombre de la competencia");
        }else if (Precio.getText().toString().length() <= 0) {
            Precio.setError("Debes de poner el el precio de la inscripcion");
        }
        else if(hora.length() <= 0) {
            btnHora.setBackgroundColor(600);
        }
        else if(Ciudad.getText().toString().length() <= 0) {
            Ciudad.setError("Debes de poner la Ciudad en donde se realizara la Competencia");
        }
        else if(Colonia.getText().toString().length() <= 0) {
            Colonia.setError("Debes de poner la Colonia en donde se realizara la Competencia");
        }
        else if(Calle.getText().toString().length() <= 0) {
            Calle.setError("Debes de poner la Calle en donde se realizara la Competencia");
        }
        else if(Descripcion.getText().toString().length() <= 0) {
            Descripcion.setError("Debes de poner la inscripción de la Competencia");
        }
        else if(imagen == 0){
            btnImagen.setError("Debes seleccionar una imagen");
        }
        else if(SiReembolso.isChecked()== false && NoReembolso.isChecked() == false){
            Toast.makeText(getApplicationContext(), "Verifica el Reembolso", Toast.LENGTH_SHORT).show();
        }else siguiente = true;

        return siguiente;
    }

    private void alCrearInscripcion(String id) {
        Intent next = new Intent(this, crear_inscripcion.class);
        next.putExtra("ID_COMPETENCIA", id);
        startActivity(next);
    }

    private void Informacion(){
        Intent next = new Intent(this, Informacion.class);
        startActivity(next);
    }

}

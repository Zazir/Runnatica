package com.runnatica.runnatica;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.runnatica.runnatica.Fragmentos.mapCompetencia;
import com.runnatica.runnatica.poho.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class crear_competencia extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private TextView txtFecha, txtHora;
    private EditText Nombre, Precio, GradosUbicacion, Ciudad, Colonia, Calle, Descripcion, Coordenadas;
    private Button Informacion, btnImagen, btnDate, Aval, Guardar, btnHora, btnLugar;
    private Spinner Pais, Estado;
    private ImageView img;
    private Usuario usuario = Usuario.getUsuarioInstance();

    private Bitmap bitmap;
    private ProgressDialog progreso;

    private GoogleMap mMap;
    private MarkerOptions marker = new MarkerOptions();

    private Calendar calendar;
    private DatePickerDialog picker;
    private TimePickerDialog timePicker;
    private String fecha;
    private String hora;
    private String Reembolso;

    private String dominio;

    BottomNavigationView MenuUsuario;



    int imagen=0, dia, mes, ano;
    private String path = "xxx", estado, pais;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            //Toast.makeText(this, "Estas listo", Toast.LENGTH_SHORT).show();
            setContentView(R.layout.activity_crear_competencia);
        }

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
        Guardar = (Button) findViewById(R.id.btnGuardarCompetencia);
        txtFecha = (TextView)findViewById(R.id.tvFechaPicker);
        txtHora = (TextView)findViewById(R.id.tvHoraCompetencia);
        btnLugar = (Button)findViewById(R.id.btnSeleccionarLugar);
        Coordenadas = (EditText)findViewById(R.id.etCoordenadas);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapCompetencia);
        mapFragment.getMapAsync(this);

        cargarSpinnerEstado();
        cargarSpinnerPais();

        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);
        //

        MenuUsuario.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    home();
                }
                if (menuItem.getItemId() == R.id.menu_busqueda) {
                    Busqueda();
                }
                if (menuItem.getItemId() == R.id.menu_historial) {
                    Historial();
                }
                if (menuItem.getItemId() == R.id.menu_ajustes) {
                    Ajustes();
                }

                return true;
            }
        });

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
                dia = calendar.get(Calendar.DAY_OF_MONTH);
                mes = calendar.get(Calendar.MONTH);
                ano = calendar.get(Calendar.YEAR);


                picker = new DatePickerDialog(crear_competencia.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fecha = year + "-" + (month+1) + "-" + dayOfMonth;
                        txtFecha.setText(fecha);
                    }
                }, ano, mes, dia);

                picker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
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
                    subirImagenCompetencia("http://45.15.24.210/WebServiceRunnatica/guardarImagen.php?");
                if (true){//Validaciones()){
                    /*SubirCompetencia(dominio + "agregarCompetencia.php?" +
                            "Id_usuario=" + usuario.getId() +
                            "&Descripcion=" +Descripcion.getText().toString().replaceAll(" ", "%20")+
                            "&Aval=Aval" +
                            "&Coordenadas=" +Coordenadas.getText().toString().replaceAll(" ", "%20")+
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
                            "&path="+path);*/
                    //subirImagenCompetencia(dominio + "uploadImg.php?");
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mapa();
            }
        });
    }

    private void mapa() {
        Intent intent = new Intent(crear_competencia.this, mapCompetencia.class);
        intent.putExtra("Latitud", "0.0");
        intent.putExtra("Longitud", "0.0");
        intent.putExtra("nombre_competencia", "");
        startActivity(intent);
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
                        Log.i("Respuesta img", response);
                        if (response.equals("Error al subir")) {
                            Toast.makeText(getApplicationContext(), "La imagen no se pudo subir con éxito", Toast.LENGTH_SHORT).show();
                        } else {
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
        /*else if(imagen == 0){
            btnImagen.setError("Debes seleccionar una imagen");
        }*/else siguiente = true;

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
    private void home(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }
    private void Busqueda(){
        Intent next = new Intent(this, busqueda_competidor.class);
        startActivity(next);
    }
    private void Historial(){
        Intent next = new Intent(this, historial_competidor.class);
        startActivity(next);
    }
    private void Ajustes(){
        Intent next = new Intent(this, ajustes_competidor.class);
        startActivity(next);
    }

    //Google maps integration
    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int status = api.isGooglePlayServicesAvailable(this);

        if (status == ConnectionResult.SUCCESS) {
            return true;

        }else if (api.isUserResolvableError(status)){
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity)getApplicationContext(), 10);
            dialog.show();

        }else {
            Toast.makeText(this, "Error con los servicios de Google Play", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Geocoder geo = new Geocoder(this);
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Log.i("Coords selected", latLng.latitude+"");
        Log.i("Coords selected", latLng.longitude+"");

        if (marker != null) {
            mMap.clear();
            marker = new MarkerOptions();
        }

        mMap.addMarker(marker.position(latLng).title(latLng.latitude + ","+latLng.longitude));
        String coords = latLng.latitude + " " + latLng.longitude;
        Log.i("Coords to send", coords);
    }
}

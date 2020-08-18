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
import android.text.Editable;
import android.text.TextWatcher;
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
import com.runnatica.runnatica.Fragmentos.mapCompetencia;
import com.runnatica.runnatica.poho.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class crear_competencia extends AppCompatActivity implements TextWatcher {

    // Constantes para códigos
    public static final String NOMBRE_COMPETENCIA = "nombre.competencia.guardado";
    public static final String PRECIO_COMPETENCIA = "precio.competencia.guardado";
    public static final String FECHA_COMPETENCIA = "fecha.competencia.guardado";
    public static final String HORA_COMPETENCIA = "hora.competencia.guardado";
    public static final String IMAGEN_DE_COMPETENCIA = "imagen.competencia.guardado";
    public static final String PAIS_COMPETENCIA = "pais.competencia.guardado";
    public static final String ESTADO_COMPETENCIA = "estado.competencia.guardado";
    public static final String CIUDAD_COMPETENCIA = "ciudad.competencia.guardado";
    public static final String COLONIA_COMPETENCIA = "colonia.competencia.guardado";
    public static final String CALLE_COMPETENCIA = "calle.competencia.guardado";
    public static final String COORDENADAS_COMPETENCIA = "coordenadas.competencia.guardado";
    public static final String AVAL_DE_COMPETENCIA = "aval.competencia.guardado";
    public static final String DESCRIPCION_COMPETENCIA = "descripcion.competencia.guardado";

    private TextView txtFecha, txtHora, txtCoordenadas;
    private EditText Nombre, Precio, Ciudad, Colonia, Calle, Descripcion;
    private Button Informacion, btnImagen, btnDate, Aval, Guardar, btnHora, btnLugar;
    private Spinner Pais, Estado;
    private ImageView imgCarrera, imgAval;
    private Usuario usuario = Usuario.getUsuarioInstance();

    // Útiles para imágenes
    private Bitmap bitmap;
    private ProgressDialog progreso;
    private boolean diferenciar_imagen;

    // Útiles para fecha y hora
    private Calendar calendar;
    private DatePickerDialog picker;
    private TimePickerDialog timePicker;
    private String Reembolso;

    private String dominio;

    BottomNavigationView MenuUsuario;
    DatePickerDialog datePickerDialog;
    int imagen=0, dia, mes, ano;

    // Miembros para guardar los valores de los datos de los EditText
    private String nombreCompetencia="", fecha="", hora="", pathCompetencia="", pais="", estado="", ciudad="", colonia="", calle="", coordenadas="", pathAval="", descripcion="";
    private int precio = 0;

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
        imgCarrera = (ImageView)findViewById(R.id.imgCompetencia);
        imgAval = (ImageView)findViewById(R.id.imgAval);
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
        txtCoordenadas = (TextView)findViewById(R.id.tvCoordenadas);

        obtenerAutoguardado();
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
                //Guardar imagen de la competencia
                diferenciar_imagen = true;
                CargarImagen();
            }
        });

        Aval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Guardar la imagen del aval
                diferenciar_imagen = false;
                CargarImagen();
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
                        autoguardadoOportuno();
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
                        autoguardadoOportuno();
                    }
                }, ano, mes, dia);

                picker.getDatePicker().setMinDate(System.currentTimeMillis() + 10000000);
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
                if (Validaciones()){
                    SubirCompetencia(dominio + "agregarCompetencia.php?" +
                            "Id_usuario=" + usuario.getId() +
                            "&Descripcion=" + descripcion.replaceAll(" ", "%20") +
                            "&Aval=" + pathAval +
                            "&Coordenadas=" + coordenadas +
                            "&Nombre_competencia=" + nombreCompetencia.replaceAll(" ", "%20") +
                            "&Pais=" + pais +
                            "&Colonia=" + colonia.replaceAll(" ", "%20") +
                            "&Calle=" + calle.replaceAll(" ", "%20") +
                            "&Ciudad=" + ciudad.replaceAll(" ", "%20") +
                            "&Fecha=" +fecha+
                            "&Hora=" +hora+
                            "&Estado=" + estado +
                            "&Reembolso=N" +
                            "&Precio=" + precio +
                            "&path="+pathCompetencia);
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnLugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoguardadoOportuno();
                mapa();
                finish();
            }
        });

        //Métodos para un autoguardado en tiempo real
        Nombre.addTextChangedListener(this);
        Descripcion.addTextChangedListener(this);
        Colonia.addTextChangedListener(this);
        Colonia.addTextChangedListener(this);
        Calle.addTextChangedListener(this);
        Ciudad.addTextChangedListener(this);
        Precio.addTextChangedListener(this);
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {
        autoguardadoOportuno();
    }

    private void mapa() {
        Intent intent = new Intent(crear_competencia.this, mapCompetencia.class);
        startActivity(intent);
    }

    private void obtenerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);

        usuario.setId(preferences.getInt(Login.ID_USUARIO_SESSION, 0));
        usuario.setNombre(preferences.getString(Login.NOMBRE_USUARIO_SESSION, "No_name"));
        usuario.setCorreo(preferences.getString(Login.CORREO_SESSION, "No_mail"));
        usuario.setFechaNacimiento(preferences.getString(Login.NACIMIENTO_USUARIO_SESSION, "0"));
    }

    private void cargarSpinnerEstado() {
        ArrayAdapter<CharSequence> opcionesSpCat = ArrayAdapter.createFromResource(this, R.array.estados, android.R.layout.simple_spinner_item);
        Estado.setAdapter(opcionesSpCat);

        Estado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estado = parent.getItemAtPosition(position).toString();
                autoguardadoOportuno();
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
                autoguardadoOportuno();
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

    /**
     * Este método crea una petición post hacia un web service (especificado con el parámetro que esta recibiendo)
     *
     * Descompone una imagen seleccionada para obtener su string y después enviarla
     *
     * La respuesta de la petición entrega el path de la imagen guardada en el servidor o en su defecto
     * entrega el error al intentar subir la imagen al servidor (controles de errores definidos en el web service)
     *
     * @param URL
     */
    private void subirImagenCompetencia(String URL) {
        progreso = new ProgressDialog(crear_competencia.this);
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
                            if (diferenciar_imagen) {
                                Log.i("path_imagen", response);
                                pathCompetencia = response;
                            } else {
                                Log.i("path_imagen", response);
                                pathAval = response;
                            }
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

            //La imagen se cargar desde el botón de imagen competencia
            if (diferenciar_imagen) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                    imgCarrera.setImageBitmap(bitmap);//Setear bitmap de la imagen en el ImageView
                    subirImagenCompetencia(dominio+"guardarImagen.php?");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if (!diferenciar_imagen) {
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), path);
                    imgAval.setImageBitmap(bitmap);//Setear bitmap de la imagen en el ImageView
                    subirImagenCompetencia(dominio+"guardarImagen.php?");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    /**
     * Metodo que valida el formulario para su posterior envio
     * @return
     */
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

    /**
     * Método que crea un autoguardado para la competencia que se está creando por si no se termina de crear la competencia
     */
    private void autoguardadoOportuno() {
        SharedPreferences preferences = getSharedPreferences("Autoguardado_Competencia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        nombreCompetencia = Nombre.getText().toString();
        descripcion = Descripcion.getText().toString();
        coordenadas = txtCoordenadas.getText().toString();
        colonia = Colonia.getText().toString();
        calle = Calle.getText().toString();
        ciudad = Ciudad.getText().toString();
        if (Precio.getText().toString().equals("")) {
            precio = 0;
        }else {
            precio = Integer.parseInt(Precio.getText().toString());
        }

        editor.putString(NOMBRE_COMPETENCIA, nombreCompetencia);
        editor.putInt(PRECIO_COMPETENCIA, precio);
        editor.putString(FECHA_COMPETENCIA, fecha);
        editor.putString(HORA_COMPETENCIA, hora);
        editor.putString(IMAGEN_DE_COMPETENCIA, pathCompetencia);
        editor.putString(PAIS_COMPETENCIA, pais);
        //editor.putString(ESTADO_COMPETENCIA, estado);
        editor.putString(CIUDAD_COMPETENCIA, ciudad);
        editor.putString(COLONIA_COMPETENCIA, colonia);
        editor.putString(CALLE_COMPETENCIA, calle);
        editor.putString(COORDENADAS_COMPETENCIA, coordenadas);
        editor.putString(AVAL_DE_COMPETENCIA, pathAval);
        editor.putString(DESCRIPCION_COMPETENCIA, descripcion);

        editor.commit();
    }

    private void obtenerAutoguardado() {
        SharedPreferences preferences = getSharedPreferences("Autoguardado_Competencia", Context.MODE_PRIVATE);

        nombreCompetencia = preferences.getString(NOMBRE_COMPETENCIA, "");
        precio = preferences.getInt(PRECIO_COMPETENCIA, 0);
        fecha = preferences.getString(FECHA_COMPETENCIA, "");
        hora = preferences.getString(HORA_COMPETENCIA, "");
        pathCompetencia = preferences.getString(IMAGEN_DE_COMPETENCIA, "");
        pais = preferences.getString(PAIS_COMPETENCIA, "");
        //estado = preferences.getString(ESTADO_COMPETENCIA, "");
        ciudad = preferences.getString(CIUDAD_COMPETENCIA, "");
        colonia = preferences.getString(COLONIA_COMPETENCIA, "");
        calle = preferences.getString(CALLE_COMPETENCIA, "");
        coordenadas = preferences.getString(COORDENADAS_COMPETENCIA, "");
        pathAval = preferences.getString(AVAL_DE_COMPETENCIA, "");
        descripcion = preferences.getString(DESCRIPCION_COMPETENCIA, "");

        Nombre.setText(nombreCompetencia);
        Precio.setText(precio+"");
        txtFecha.setText(fecha);
        txtHora.setText(hora);
        Ciudad.setText(ciudad);
        Colonia.setText(colonia);
        Calle.setText(calle);
        txtCoordenadas.setText(coordenadas);
        Descripcion.setText(descripcion);
    }
}

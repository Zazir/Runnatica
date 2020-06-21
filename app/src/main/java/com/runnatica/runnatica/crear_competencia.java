package com.runnatica.runnatica;

import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
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

public class crear_competencia extends AppCompatActivity {

    private TextView txtFecha;
    private EditText Nombre, Precio, Hora, GradosUbicacion, Ciudad, Colonia, Calle, Descripcion;
    private Button Informacion, btnImagen, btnDate, Aval, Guardar;
    private Spinner Pais, Estado;
    private RadioButton SiReembolso, NoReembolso;
    private ImageView img;
    private String cadenaVacia;
    private Usuario usuario = Usuario.getUsuarioInstance();

    private Bitmap bitmap;
    private ProgressDialog progreso;

    private Calendar calendar;
    private DatePickerDialog picker;
    private String fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_competencia);

        Nombre=(EditText)findViewById(R.id.etNombreCompetencia);
        Precio = (EditText)findViewById(R.id.etPrecioCompetencia);
        Informacion = (Button)findViewById(R.id.btnInformacionPrecio);
        Hora = (EditText)findViewById(R.id.etHoraCompetencia);
        btnImagen = (Button) findViewById(R.id.btnImagenCompetencia);
        btnDate = (Button)findViewById(R.id.btnSeleccionarFecha);
        img = (ImageView)findViewById(R.id.imgCompetencia);
        GradosUbicacion = (EditText)findViewById(R.id.etGradosUbicacion);
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

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Guardar imagen
                CargarImagen();
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

        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validaciones()){

                    SubirCompetencia("https://runnatica.000webhostapp.com/WebServiceRunnatica/agregarCompetencia.php?");
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getStringImage(Bitmap btm) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        btm.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imgBytes = array.toByteArray();
        String encodeImg = Base64.encodeToString(imgBytes, Base64.DEFAULT);

        return encodeImg;
    }

    private void SubirCompetencia(String URL) {
        progreso = new ProgressDialog(crear_competencia.this);
        progreso.setMessage("Creando competencia, tus chingaderas...");
        progreso.show();

        //stringRequest es el objeto el cual almacena los datos.
        //Declaramos un StringRequest definiendo el método que utilizamos, en este caso es GET
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//Operacion exitosa a travez del web service
                progreso.dismiss();
                if (response.equals("error al crear el competencia")){
                    Toast.makeText(getApplicationContext(), "Hubo un error al crear la competencia", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(response) >= 0){
                    progreso.dismiss();
                    alCrearInscripcion(response);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {// Cuando hay un problema en la conexion.
                progreso.dismiss();
                Toast.makeText(getApplicationContext(), "Hubo un error con el servidor: " + error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                String id_user = ""+usuario.getId();
                String Foto = getStringImage(bitmap);
                String NombreImg = System.currentTimeMillis()/1000+"";
                String DescripcionCompe = Descripcion.getText().toString();
                String Aval = "X";
                String Coordenadas = GradosUbicacion.getText().toString();
                String NomCompetencia = Nombre.getText().toString();
                String Pais = "Mexico";
                String ColoniaS = Colonia.getText().toString();
                String CalleS = Calle.getText().toString();
                String CiudadS = Ciudad.getText().toString();
                String Fecha = fecha;
                String HoraS = Hora.getText().toString();
                String Estado = "X";
                String Reembolso = "X";
                String PrecioS = Precio.getText().toString();

                Map<String, String> parametros = new HashMap<>();
                parametros.put("Id_usuario", id_user);
                parametros.put("Foto", Foto);
                parametros.put("NombreFoto", NombreImg);
                parametros.put("Descripcion", DescripcionCompe);
                parametros.put("Aval", Aval);
                parametros.put("Coordenadas", Coordenadas);
                parametros.put("Nombre_competencia", NomCompetencia);
                parametros.put("Pais", Pais);
                parametros.put("Colonia", ColoniaS);
                parametros.put("Calle", CalleS);
                parametros.put("Ciudad", CiudadS);
                parametros.put("Fecha", Fecha);
                parametros.put("Hora", HoraS);
                parametros.put("Estado", Estado);
                parametros.put("Reembolso", Reembolso);
                parametros.put("Precio", PrecioS);

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void CargarImagen() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent, "Seleccione la aplicación"), 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            Uri path = data.getData();
            img.setImageURI(path);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), path);
                img.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private Boolean Validaciones() {
        Boolean siguiente = false;

        if (Nombre.getText().toString().length() <= 0) {
            Nombre.setError("Debes de poner el nombre de la inscripcion");
        } else if (Precio.getText().toString().length() <= 0) {
            Precio.setError("Debes de poner el el precio de la inscripcion");
        }
        else if(GradosUbicacion.getText().toString().length() <= 0) {
            GradosUbicacion.setError("Debes de poner los grados de Google Maps de la Competencia");
        }
        else if(Hora.getText().toString().length() <= 0) {
            Hora.setError("Debes de poner la hora de la Competencia");
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

}

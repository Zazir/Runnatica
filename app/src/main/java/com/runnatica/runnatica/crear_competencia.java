package com.runnatica.runnatica;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.poho.Usuario;

import java.io.ByteArrayOutputStream;

public class crear_competencia extends AppCompatActivity {

    private EditText Nombre, Precio, Dia, Mes, Ano, Hora, GradosUbicacion, Ciudad, Colonia, Calle, Descripcion;
    private Button Informacion, btnImagen, Aval, Guardar;
    private Spinner Pais, Estado;
    private RadioButton SiReembolso, NoReembolso;
    private ImageView img;
    private String cadenaVacia;
    private Usuario usuario = Usuario.getUsuarioInstance();

    private Bitmap bitmap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_competencia);

        Nombre=(EditText)findViewById(R.id.etNombreCompetencia);
        Precio = (EditText)findViewById(R.id.etPrecioCompetencia);
        Informacion = (Button)findViewById(R.id.btnInformacionPrecio);
        Dia = (EditText)findViewById(R.id.etDiaCompetencia);
        Mes = (EditText)findViewById(R.id.etMesCompetencia);
        Ano = (EditText)findViewById(R.id.ttAnoCompetencia);
        Hora = (EditText)findViewById(R.id.etHoraCompetencia);
        btnImagen = (Button) findViewById(R.id.btnImagenCompetencia);
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

        btnImagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Guardar imagen
                CargarImagen();
            }
        });

        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validaciones()){
                    String fecha = fechadeCompetencia();
                    SubirCompetencia("https://runnatica.000webhostapp.com/WebServiceRunnatica/agregarCompetencia.php?" +
                    "Id_usuario="+ usuario.getId() +
                    "&Foto=" + getStringImage(bitmap) +
                    "&NombreFoto=" + System.currentTimeMillis()/1000+".jpg" +
                    "&Descripcion=" + Descripcion.getText().toString().replaceAll(" ", "%20")+
                    "&Aval=X" +
                    "&Coordenadas=" + GradosUbicacion.getText().toString() +
                    "&Nombre_competencia=" + Nombre.getText().toString().replaceAll(" ", "%20") +
                    "&Pais=X" +
                    "&Colonia=" + Colonia.getText().toString().replaceAll(" ", "%20") +
                    "&Calle=" + Calle.getText().toString().replaceAll(" ", "%20") +
                    "&Ciudad=" + Ciudad.getText().toString().replaceAll(" ", "%20") +
                    "&Fecha="+ fecha+
                    "&Hora=" + Hora.getText().toString() +
                    "&Estado=X" +
                    "&Reembolso=X" +
                    "&Precio=" + Precio.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String getStringImage(Bitmap btm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        btm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imgBytes = baos.toByteArray();
        String encodeImg = Base64.encodeToString(imgBytes, Base64.DEFAULT);

        return encodeImg;
    }

    private void SubirCompetencia(String URL) {
        final ProgressDialog cargando = ProgressDialog.show(this, "Creando tu competencia", "Espera por favor...");
        //stringRequest es el objeto el cual almacena los datos.
        //Declaramos un StringRequest definiendo el método que utilizamos, en este caso es GET
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {//Operacion exitosa a travez del web service
                cargando.dismiss();
                if (response.equals("error al crear el competencia")){
                    Toast.makeText(getApplicationContext(), "Hubo un error al crear la competencia", Toast.LENGTH_SHORT).show();
                } else if (Integer.parseInt(response) >= 0)
                    alCrearInscripcion(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {// Cuando hay un problema en la conexion.
                cargando.dismiss();
                Toast.makeText(getApplicationContext(), "Hubo un error con el servidor: " + error, Toast.LENGTH_SHORT).show();
            }
        });
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
        }
    }

    private String fechadeCompetencia() {
        String date = Dia.getText().toString() + Mes.getText().toString() + Ano.getText().toString();
        return date;
    }

    private Boolean Validaciones() {
        Boolean siguiente = false;

        if (Nombre.getText().toString().length() <= 0) {
            Nombre.setError("Debes de poner el nombre de la inscripcion");
        } else if (Precio.getText().toString().length() <= 0) {
            Precio.setError("Debes de poner el el precio de la inscripcion");
        }else if(Dia.getText().toString().length() !=2){
            Dia.setError("Formato del Dia: DD");
        }
        else if(Mes.getText().toString().length() !=2){
            Mes.setError("Formato del Mes: MM");
        }
        else if(Ano.getText().toString().length() !=4){
            Ano.setError("Formato del Año: YYYY");
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

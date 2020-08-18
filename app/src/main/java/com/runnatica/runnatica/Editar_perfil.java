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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.runnatica.runnatica.poho.Usuario;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Editar_perfil extends AppCompatActivity {

    Button MujerEditar, HombreEditar, btnFotoEditar, EditarContrasena, btnGuardar, btnFechaNacimiento, btnEditarCorreo;
    EditText CiudadEditar, EstadoEditar, PaisEditar, NombreEditar;
    TextView MostrarFecha;
    ImageView img;
    String NombreUsuario1, CorreoUsuario1, SexoUsuario1, FechaNacimientoUsuario1, CiudadUsuario1, EstadoUsuario1, PaisUsuario1, FotoUsuario;
    BottomNavigationView MenuUsuario;
    private String genero = "";
    private String FechaNacimiento;

    private Bitmap bitmap;
    private ProgressDialog progreso;

    private Calendar calendar;
    private DatePickerDialog picker;

    Boolean flagFecha = false;
    Boolean flagFoto = false;

    private String dominio;
    private Usuario usuario = Usuario.getUsuarioInstance();
    private String path = "xxx";
    private ImageView FotoUsuarioNueva;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        MujerEditar = (Button)findViewById(R.id.btnMujerEditar);
        MostrarFecha = (TextView)findViewById(R.id.tvMostrarFechaEditar) ;
        HombreEditar = (Button)findViewById(R.id.btnHombreEditar);
        btnFotoEditar = (Button)findViewById(R.id.btnFotoEditar);
        EditarContrasena = (Button)findViewById(R.id.btnEditarContrasena);
        btnEditarCorreo = (Button)findViewById(R.id.btnEditarCorreo);
        btnFechaNacimiento = (Button)findViewById(R.id.btnSeleccionNacimiento);
        CiudadEditar = (EditText) findViewById(R.id.etCiudadEditar);
        EstadoEditar = (EditText) findViewById(R.id.etEstadoEditar);
        PaisEditar = (EditText) findViewById(R.id.etPaisEditar);
        NombreEditar = (EditText) findViewById(R.id.etNombreEditar);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        //img = (ImageView)findViewById(R.id.imgFotoPerfil);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        FotoUsuarioNueva = (ImageView)findViewById(R.id.ivFotoUsuarioNueva);

        getLastViewData();

        String fecha = FechaNacimientoUsuario1;
        String dia = fecha.substring(0, 2);
        String mes = fecha.substring(3, 5);
        String ano = fecha.substring(6);

        path = FotoUsuario;
        Glide.with(Editar_perfil.this).load(FotoUsuario).into(FotoUsuarioNueva);

        FechaNacimiento = dia + "" + mes + "" + ano + "";
        String FechaMostrar =  dia + "/" + mes + "/" + ano + "/";
        MostrarFecha.setText(FechaMostrar);

        NombreEditar.setText(NombreUsuario1);
        CiudadEditar.setText(CiudadUsuario1);
        EstadoEditar.setText(EstadoUsuario1);
        PaisEditar.setText(PaisUsuario1);
        //FechaNacimiento = FechaNacimientoUsuario1;
        //MostrarFecha.setText(FechaNacimientoUsuario1);

        if(SexoUsuario1.equals("Hombre")){
            genero = "Hombre";
            MujerEditar.setBackgroundColor(Color.GRAY);
            HombreEditar.setBackgroundColor(Color.RED);
        }

        if(SexoUsuario1.equals("Mujer")){
            genero = "Mujer";
            MujerEditar.setBackgroundColor(Color.RED);
            HombreEditar.setBackgroundColor(Color.GRAY);
        }



        //Toast.makeText(getApplicationContext(), "Fecha: " + FechaNacimientoUsuario1, Toast.LENGTH_SHORT).show();


        //Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);
        //
        btnFechaNacimiento.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int ano = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(Editar_perfil.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String MesTemp = month+1+ "";
                        String DayTemp = dayOfMonth+ "";

                        if(MesTemp.length() <=1){
                            MesTemp = "0" + MesTemp;
                            Log.i("FechaNacimiento", MesTemp);
                        }

                        if(dayOfMonth <=9){
                            DayTemp = "0" + dayOfMonth;
                            Log.i("FechaNacimiento", DayTemp);
                        }

                        FechaNacimiento = DayTemp + "" + MesTemp + "" + year + "";
                        String FechaMostrar2 =  DayTemp + "/" + MesTemp + "/" + year + "/";
                        MostrarFecha.setText(FechaMostrar2);
                        flagFecha = true;
                    }
                }, ano, mes, dia);

                picker.getDatePicker().setMaxDate(System.currentTimeMillis()-100000000);
                picker.show();

            }
        });
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

        obtenerPreferencias();
        dominio = getString(R.string.ip);

        EditarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditarContrasena();
            }
        });

        HombreEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genero = "Hombre";
                Toast.makeText(getApplicationContext(), "Eres un atlético " + genero, Toast.LENGTH_SHORT).show();
                MujerEditar.setBackgroundColor(Color.GRAY);
                HombreEditar.setBackgroundColor(Color.RED);
            }
        });

        MujerEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genero = "Mujer";
                Toast.makeText(getApplicationContext(), "Eres una " + genero + " muy fit", Toast.LENGTH_SHORT).show();
                HombreEditar.setBackgroundColor(Color.GRAY);
                MujerEditar.setBackgroundColor(Color.RED);
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validaciones()){
                    actualizarPerfil(dominio + "actualizarPerfil.php?" +
                            "id_usuario=" + usuario.getId() +
                            "&nombre=" + NombreEditar.getText().toString().replaceAll(" ", "%20") +
                            "&sexo=" + genero +
                            "&f_nacimiento=" + FechaNacimiento +
                            "&ciudad=" + CiudadEditar.getText().toString().replaceAll(" ", "%20") +
                            "&estado=" + EstadoEditar.getText().toString().replaceAll(" ", "%20") +
                            "&pais=" + PaisEditar.getText().toString().replaceAll(" ", "%20") +
                            "&path=" +path);
                    Atras();
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                };
            }
        });

        btnEditarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditarCorreo();
            }
        });

        btnFotoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarImagen();
            }
        });
    }

    private void subirImagen(String URL) {
        progreso = new ProgressDialog(Editar_perfil.this);
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
                            //Aqui recibo el URL de la Imagen
                            flagFoto = true;
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


    private void obtenerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);

        usuario.setId(preferences.getInt(Login.ID_USUARIO_SESSION, 0));
        usuario.setNombre(preferences.getString(Login.NOMBRE_USUARIO_SESSION, "No_name"));
        usuario.setCorreo(preferences.getString(Login.CORREO_SESSION, "No_mail"));
        usuario.setFechaNacimiento(preferences.getString(Login.NACIMIENTO_USUARIO_SESSION, "0"));
    }

    private void goToEditarCorreo() {
        Intent next = new Intent(this, EditarCorreo.class);
        startActivity(next);
    }

    private void goToEditarContrasena(){
        Intent next = new Intent(this, EditarContrasena.class);
        startActivity(next);
    }

    private boolean validaciones(){
        Boolean siguiente = false;
        if (NombreEditar.getText().toString().length() <= 0) {
            NombreEditar.setError("Debes de poner un nombre");
        }else if(CiudadEditar.getText().toString().length() <= 0) {
            CiudadEditar.setError("Debes de poner una ciudad");
        }else if(EstadoEditar.getText().toString().length() <= 0) {
            EstadoEditar.setError("Debes de poner un Estado");
        }else if(PaisEditar.getText().toString().length() <= 0) {
            PaisEditar.setError("Debes de poner un Pais");
        }else siguiente = true;

        return siguiente;
    }

    private String getStringImage(Bitmap btm) {
        ByteArrayOutputStream array = new ByteArrayOutputStream();
        btm.compress(Bitmap.CompressFormat.JPEG, 100, array);
        byte[] imgBytes = array.toByteArray();
        String encodeImg = Base64.encodeToString(imgBytes, Base64.DEFAULT);

        return encodeImg;
    }

    private void actualizarPerfil(String URL) {
        progreso = new ProgressDialog(Editar_perfil.this);
        progreso.setMessage("Actualizando perfil...");
        progreso.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.dismiss();
                if (response.equals("Exito")){
                    NombreEditar.setText("");
                    CiudadEditar.setText("");
                    EstadoEditar.setText("");
                    PaisEditar.setText("");
                    //img.setImageResource(android.R.color.transparent);
                    Toast.makeText(getApplicationContext(), "Perfil actualizado con éxito", Toast.LENGTH_SHORT).show();
                } else if (response.equals("Error")){
                    Toast.makeText(getApplicationContext(), "Algo salió mal, vuelve a intentarlo", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.dismiss();
                Toast.makeText(getApplicationContext(), "Hubo un error con la conexión", Toast.LENGTH_SHORT).show();
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
            FotoUsuarioNueva.setImageURI(path);

            try {
                bitmap = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), path);
                FotoUsuarioNueva.setImageBitmap(bitmap);
                subirImagen(dominio + "guardarImagen.php?");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    private void getLastViewData() {
        Bundle extra = Editar_perfil.this.getIntent().getExtras();
        NombreUsuario1 = extra.getString("NombreUsuario");
        CorreoUsuario1 = extra.getString("CorreoUsuario");
        SexoUsuario1 = extra.getString("SexoUsuario");
        FechaNacimientoUsuario1 = extra.getString("FechaNacimientoUsuario");
        CiudadUsuario1 = extra.getString("CiudadUsuario");
        EstadoUsuario1 = extra.getString("EstadoUsuario");
        PaisUsuario1 = extra.getString("PaisUsuario");
        FotoUsuario = extra.getString("FotoUsuario");
    }
    private void home(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }
    private void Atras(){
        Intent next = new Intent(this, VerPerfil.class);
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
}

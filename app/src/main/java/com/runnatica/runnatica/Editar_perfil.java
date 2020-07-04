package com.runnatica.runnatica;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
import java.util.HashMap;
import java.util.Map;

public class Editar_perfil extends AppCompatActivity {

    Button MujerEditar, HombreEditar, btnFotoEditar, EditarContrasena, btnGuardar;
    EditText DiaEditar, CiudadEditar, EstadoEditar, PaisEditar, AnoEditar, NombreEditar, CorreoEditar, MesEditar;
    ImageView img;
    private String genero = "";
    private String fechaNacimiento;

    private Bitmap bitmap;
    private ProgressDialog progreso;

    private String dominio;
    private Usuario usuario = Usuario.getUsuarioInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        MujerEditar = (Button)findViewById(R.id.btnMujerEditar);
        HombreEditar = (Button)findViewById(R.id.btnHombreEditar);
        btnFotoEditar = (Button)findViewById(R.id.btnFotoEditar);
        EditarContrasena = (Button)findViewById(R.id.btnEditarContrasena);
        DiaEditar = (EditText) findViewById(R.id.etDiaEditar);
        CiudadEditar = (EditText) findViewById(R.id.etCiudadEditar);
        EstadoEditar = (EditText) findViewById(R.id.etEstadoEditar);
        PaisEditar = (EditText) findViewById(R.id.etPaisEditar);
        AnoEditar = (EditText) findViewById(R.id.etAnoEditar);
        NombreEditar = (EditText) findViewById(R.id.etNombreEditar);
        CorreoEditar = (EditText) findViewById(R.id.etCorreoEditar);
        MesEditar = (EditText) findViewById(R.id.etMesEditar);
        btnGuardar = (Button)findViewById(R.id.btnGuardar);
        img = (ImageView)findViewById(R.id.imgFotoPerfil);

        obtenerPreferencias();
        dominio = getString(R.string.ip);

        EditarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarContrasena();
            }
        });

        MujerEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genero = "Mujer";
                Toast.makeText(getApplicationContext(), "Eres una " + genero + " muy fit", Toast.LENGTH_SHORT).show();
            }
        });

        HombreEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genero = "Hombre";
                Toast.makeText(getApplicationContext(), "Eres un atlético " + genero, Toast.LENGTH_SHORT).show();
            }
        });


        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validaciones()){
                    fechaNacimiento = DiaEditar.getText().toString() + MesEditar.getText().toString() + AnoEditar.getText().toString();
                    actualizarFotoPerfil(dominio + "uploadImg.php?");
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                };
            }
        });

        btnFotoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarImagen();
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

    private void EditarContrasena(){
        Intent next = new Intent(this, EditarContrasena.class);
        startActivity(next);
    }

    private boolean validaciones(){
        Boolean siguiente = false;
        if (NombreEditar.getText().toString().length() <= 0) {
            NombreEditar.setError("Debes de poner un nombre");
        }else if(CorreoEditar.getText().toString().length() <= 0) {
            CorreoEditar.setError("Debes de poner un correo");
        }else if (DiaEditar.getText().toString().length() <= 0 || DiaEditar.getText().toString().length() >= 3) {
            DiaEditar.setError("Ingresa un Dia Valido");
        }else if (MesEditar.getText().toString().length() <= 0 || MesEditar.getText().toString().length() >= 3) {
            MesEditar.setError("Ingresa un Mes Valido");
        }else if (MesEditar.getText().toString().length() <= 0 || MesEditar.getText().toString().length() >= 5) {
            MesEditar.setError("Ingresa un Año Valido");
        }else if(CiudadEditar.getText().toString().length() <= 0) {
            CiudadEditar.setError("Debes de poner una ciudad");
        }else if(EstadoEditar.getText().toString().length() <= 0) {
            EstadoEditar.setError("Debes de poner un correo");
        }else if(PaisEditar.getText().toString().length() <= 0) {
            PaisEditar.setError("Debes de poner un correo");
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
        progreso.setMessage("Cargando...");
        progreso.show();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progreso.dismiss();
                if (response.equals("Exito")){
                    NombreEditar.setText("");
                    CorreoEditar.setText("");
                    CiudadEditar.setText("");
                    EstadoEditar.setText("");
                    PaisEditar.setText("");
                    img.setImageResource(android.R.color.transparent);
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

    private void actualizarFotoPerfil(String URL) {
        progreso = new ProgressDialog(Editar_perfil.this);
        progreso.setMessage("Cargando...");
        progreso.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        progreso.hide();
                        actualizarPerfil(dominio + "actualizarPerfil.php?" +
                            "id_usuario=" + usuario.getId() +
                            "&nombre=" + NombreEditar.getText().toString().replaceAll(" ", "%20") +
                            "&correo=" + CorreoEditar.getText().toString() +
                            "&sexo=" + genero +
                            "&f_nacimiento=" + fechaNacimiento +
                            "&ciudad=" + CiudadEditar.getText().toString().replaceAll(" ", "%20") +
                            "&estado=" + EstadoEditar.getText().toString().replaceAll(" ", "%20") +
                            "&pais=" + PaisEditar.getText().toString().replaceAll(" ", "%20") +
                            "&urlFoto="+response);
                        Toast.makeText(getApplicationContext(), "Perfil actualizado" + response, Toast.LENGTH_SHORT).show();
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
}

package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class VerPerfil extends AppCompatActivity {

    private ImageView imgUsuario;
    private TextView NombreUsuario, CorreoUsuario, SexoUsuario, FechaNacimientoUsuario, CiudadUsuario, EstadoUsuario, PaisUsuario, EditarPerfil;
    private Usuario usuario = Usuario.getUsuarioInstance();
    private String dominio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        NombreUsuario = (TextView)findViewById(R.id.tvNombreUsuario);
        CorreoUsuario = (TextView)findViewById(R.id.tvCorreoUsuario);
        SexoUsuario = (TextView)findViewById(R.id.tvSexoUsuario);
        FechaNacimientoUsuario = (TextView)findViewById(R.id.tvFechaNacimientoUsuario);
        CiudadUsuario = (TextView)findViewById(R.id.tvCiudadUsuario);
        EstadoUsuario = (TextView)findViewById(R.id.tvEstadoUsuario);
        PaisUsuario = (TextView)findViewById(R.id.tvPaisUsuario);
        EditarPerfil = (TextView)findViewById(R.id.tvEditarUsuario);
        imgUsuario = (ImageView)findViewById(R.id.ivFotoPerfilUsuario);

        dominio = getString(R.string.ip);

        EditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarPerfil();
            }
        });

        CargarInfoUsuario(dominio + "obtenerUsuario.php?id_usuario=" + usuario.getId());
    }

    private void CargarInfoUsuario(String URL) {
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray userArray = new JSONArray(response);
                            JSONObject usuario = userArray.getJSONObject(0);

                            String fecha = usuario.optString("f_nacimiento");
                            String dia = fecha.substring(0, 2);
                            String mes = fecha.substring(2, 4);
                            String ano = fecha.substring(4);
                            String dateBorn = dia+"-"+mes+"-"+ano;

                            Glide.with(VerPerfil.this).load(usuario.optString("foto_usuario")).into(imgUsuario);
                            NombreUsuario.setText(usuario.optString("nombre"));
                            CorreoUsuario.setText(usuario.optString("correo"));
                            SexoUsuario.setText(usuario.optString("sexo"));
                            FechaNacimientoUsuario.setText(dateBorn);
                            CiudadUsuario.setText(usuario.optString("ciudad"));
                            EstadoUsuario.setText(usuario.optString("estado"));
                            PaisUsuario.setText(usuario.optString("pais"));

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexión al servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }

    private void EditarPerfil(){
        Intent next = new Intent(this, Editar_perfil.class);
        startActivity(next);
    }
}

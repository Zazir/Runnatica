package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONException;
import org.json.JSONObject;


public class VerPerfil extends AppCompatActivity {

    private ImageView imgUsuario;
    private TextView NombreUsuario, CorreoUsuario, SexoUsuario, FechaNacimientoUsuario, CiudadUsuario, EstadoUsuario, PaisUsuario;
    private Usuario usuario = Usuario.getUsuarioInstance();
    private String dominio, fotourl;
    private Button EditarPerfil;
    BottomNavigationView MenuUsuario;

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
        EditarPerfil = (Button) findViewById(R.id.btnEditarUsuario);
        imgUsuario = (ImageView)findViewById(R.id.ivFotoPerfilUsuario);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        //Posicionar el icono del menu
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
                            JSONObject usuario = new JSONObject(response);

                            String fecha = usuario.optString("f_nacimiento");
                            String dia = fecha.substring(0, 2);
                            String mes = fecha.substring(2, 4);
                            String ano = fecha.substring(4);
                            String dateBorn = dia+"/"+mes+"/"+ano;

                            Glide.with(VerPerfil.this).load(usuario.optString("foto_usuario")).into(imgUsuario);
                            fotourl = usuario.optString("foto_usuario");
                            NombreUsuario.setText(usuario.optString("nombre"));
                            CorreoUsuario.setText(usuario.optString("correo"));
                            SexoUsuario.setText(usuario.optString("sexo"));
                            FechaNacimientoUsuario.setText(dateBorn);
                            CiudadUsuario.setText(usuario.optString("ciudad"));
                            EstadoUsuario.setText(usuario.optString("estado"));
                            PaisUsuario.setText(usuario.optString("pais"));
                            Toast.makeText(getApplicationContext(), fotourl, Toast.LENGTH_SHORT).show();

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

    private void EditarPerfil(){
        Intent intent = new Intent(VerPerfil.this, Editar_perfil.class);
        intent.putExtra("NombreUsuario", NombreUsuario.getText());
        intent.putExtra("CorreoUsuario", CorreoUsuario.getText());
        intent.putExtra("SexoUsuario", SexoUsuario.getText());
        intent.putExtra("FechaNacimientoUsuario", FechaNacimientoUsuario.getText());
        intent.putExtra("CiudadUsuario", CiudadUsuario.getText());
        intent.putExtra("EstadoUsuario", EstadoUsuario.getText());
        intent.putExtra("PaisUsuario", PaisUsuario.getText());
        intent.putExtra("FotoUsuario", fotourl);

        startActivity(intent);
        finish();
    }
}

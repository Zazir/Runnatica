package com.runnatica.runnatica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.poho.Usuario;

import java.util.regex.Pattern;

public class EditarContrasena extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //Mínimo 1 digito
                    "(?=.*[a-z])" +         //Mínimo 1 minúscula
                    "(?=.*[A-Z])" +         //Mínimo 1 mayúscula
                    ".{8,}" +               //Mínimo 8 caracteres
                    "$");

    Button CambiarContraseña;
    EditText ContrasenaActual, ContrasenaNueva, RepetirContrasena;
    BottomNavigationView MenuUsuario;
    private String dominio;
    private Usuario usuario = Usuario.getUsuarioInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contrasena);

        CambiarContraseña = (Button)findViewById(R.id.btnCambiarContrasena);
        ContrasenaActual = (EditText) findViewById(R.id.etContrasenaActual);
        ContrasenaNueva = (EditText)findViewById(R.id.etNuevaContrasena);
        RepetirContrasena = (EditText)findViewById(R.id.etRepeticionContrasena);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        obtenerPreferencias();
        dominio = getString(R.string.ip);

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

        CambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validaciones()){
                    cambiarContrasena(dominio + "actualizarPerfil.php?"+
                            "id_usuario=" + usuario.getId() +
                            "&antigua_contra=" + ContrasenaActual.getText().toString().replaceAll(" ", "%20") +
                            "&nueva_contra=" + ContrasenaNueva.getText().toString().replaceAll(" ", "%20"));
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

    private Boolean Validaciones() {
        Boolean siguiente = false;
        if (ContrasenaActual.getText().toString().length() <= 0){
            ContrasenaActual.setError("Debes poner tu nombre");
        }else if (!PASSWORD_PATTERN.matcher(ContrasenaNueva.getText().toString()).matches()){
            ContrasenaNueva.setError("Usa 8 Caracteres, 1 Letra Mayuscula y 1 Numero como minimo");
        }else if(ContrasenaNueva.getText().toString().equals(RepetirContrasena.getText().toString())){
            siguiente = true;
        }else{
            RepetirContrasena.setError("Contraseñas Diferentes");
        }

        return siguiente;
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

    private void cambiarContrasena(String URL) {
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(EditarContrasena.this, response, Toast.LENGTH_SHORT).show();
                        if (response.equals("Contraseña actualizada con éxito")) {
                            ContrasenaActual.setText("");
                            ContrasenaNueva.setText("");
                            RepetirContrasena.setText("");
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EditarContrasena.this, "Verifica tu conexión", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }

    private void Regresar(){
        Intent next = new Intent(this, VerPerfil.class);
        startActivity(next);
    }
}

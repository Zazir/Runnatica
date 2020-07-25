package com.runnatica.runnatica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
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


public class EditarCorreo extends AppCompatActivity {

    BottomNavigationView MenuUsuario;
    EditText CorreoNuevo, confirmarContrasena;
    Button VerificarCorreo;

    private Usuario usuario = Usuario.getUsuarioInstance();
    private String dominio;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
         dominio = getString(R.string.ip);

        setContentView(R.layout.activity_editar_correo);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        CorreoNuevo = (EditText)findViewById(R.id.etCorreoNuevo);
        confirmarContrasena = (EditText)findViewById(R.id.confirmContrasena);
        VerificarCorreo = (Button)findViewById(R.id.btnVerificarCorreo);

        //Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);
        //

        obtenerPreferencias();

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

        VerificarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(true){
                    verificarContrasena(dominio+ "actualizarPerfil.php?" + "id_usuario=" + usuario.getId()+
                            "&contrasena=" + confirmarContrasena.getText().toString().replaceAll(" ", "%20"));

                }else{
                    Toast.makeText(getApplicationContext(), "Verifica el Correo", Toast.LENGTH_SHORT).show();
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
    private boolean Validaciones(){
        Boolean siguiente = false;

        if (!Patterns.EMAIL_ADDRESS.matcher(CorreoNuevo.getText().toString()).matches()){
            CorreoNuevo.setError("Ese no es un correo válido");
        }else{
            siguiente = true;
        }
        return siguiente;
    }


    private void verificarContrasena(String URL){
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                        if (response.equals("Correcto")) {

                            Intent intent = new Intent(EditarCorreo.this, Verificar_CorreoNuevo.class);
                            intent.putExtra("Correo", CorreoNuevo.getText().toString());
                            startActivity(intent);
                            finish();

                        }else if(response.equals("Incorrecto")){
                            Toast.makeText(getApplicationContext(), "Contraseña Incorrecta, Vuelve a Intentar", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Verifica tu conexión", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }
}
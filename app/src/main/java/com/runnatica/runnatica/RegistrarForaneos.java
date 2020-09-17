package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.runnatica.runnatica.poho.Usuario;
import com.runnatica.runnatica.poho.UsuarioForaneo;

import java.util.ArrayList;
import java.util.List;

public class RegistrarForaneos extends AppCompatActivity {

    private EditText txtNombre, txtCorreo, txtEdad;
    private RadioButton rbHombre, rbMujer;
    private Button btnCrearForaneo;
    private RecyclerView rvForaneos;
    private BottomNavigationView MenuUsuario;

    private String id_competencia;
    private Usuario usuario = Usuario.getUsuarioInstance();
    private int index = 0;
    private String sexo, ip;
    private List<UsuarioForaneo> foraneoList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_foraneos);
        txtNombre = (EditText)findViewById(R.id.etNombreForaneo);
        txtCorreo = (EditText)findViewById(R.id.etCorreoForaneo);
        txtEdad = (EditText)findViewById(R.id.etEdadForaneo);
        rbHombre = (RadioButton) findViewById(R.id.rbtnHombre);
        rbMujer = (RadioButton) findViewById(R.id.rbtnMujer);
        btnCrearForaneo = (Button)findViewById(R.id.btnCreateForaneo);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);


        //Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);
        //

        ip = getString(R.string.ip);
        foraneoList = new ArrayList<>();
        //peticion();
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

        btnCrearForaneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidarCorreo();

            }
        });
    }
    private void setSexo() {
        if (rbMujer.isChecked())
            sexo = "Mujer";
        else if (rbHombre.isChecked())
            sexo = "Hombre";
    }

    private void crearUsuarioForaneo(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //llenarListView(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrarForaneos.this, "Error de conección con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void ValidarCorreo(){
        String dominio = getString(R.string.ip);
        String url = dominio + "sesion.php?correo=" + txtCorreo.getText().toString()+ "&operacion=1";


        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Existe")) {
                    txtCorreo.setError("Este correo ya esta Registrado");
                    Toast.makeText(getApplicationContext(), "Este correo ya esta Registrado en Runnatica", Toast.LENGTH_SHORT).show();
                    return;
                }else if(response.equals("Noexiste")) {
                    AgregarForaneo();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Hubo un problema "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private boolean Validaciones(){
        Boolean siguiente = false;
        String edadS= txtEdad.getText().toString();
        int EdadInt = Integer.parseInt(edadS);

        if (txtNombre.getText().toString().length() <= 0){
            txtNombre.setError("Debes poner un nombre");
        }else if (txtCorreo.getText().toString().length() <= 0){
            txtCorreo.setError("Debes poner un Correo");
        }else if (!Patterns.EMAIL_ADDRESS.matcher(txtCorreo.getText().toString()).matches()){
            txtCorreo.setError("Ese correo no es válido");
        }else if (txtEdad.getText().toString().length() <= 0){
            txtEdad.setError("Debes poner una Edad");
        } else if (EdadInt <=3 || EdadInt >=90){
            txtEdad.setError("La edad no es valida");
        }else if (rbHombre.isChecked() || rbMujer.isChecked()){
            siguiente = true;
        }else siguiente = false;

        return siguiente;
    }

    private void AgregarForaneo(){
        try{
            if(Validaciones()){
                setSexo();
                crearUsuarioForaneo(ip + "agregarUsuarioForaneo.php?" +
                        "id_usuario=" + usuario.getId() +
                        "&nombre=" + txtNombre.getText().toString().replaceAll(" ", "%20") +
                        "&correo=" + txtCorreo.getText().toString() +
                        "&sexo=" + sexo +
                        "&edad=" + txtEdad.getText().toString());
                Toast.makeText(getApplicationContext(), "El usuario Foraneo " + txtNombre.getText().toString() + "se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                Regresar();
                txtNombre.setText("");
                txtCorreo.setText("");
                txtEdad.setText("");
                rbHombre.setChecked(false);
                rbMujer.setChecked(false);
            }else{
                Toast.makeText(RegistrarForaneos.this, "Complete los Campos", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){

        }
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
    private void Regresar(){
        Intent next = new Intent(this, ListaForaneos.class);
        startActivity(next);
    }

}
package com.runnatica.runnatica;

import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contrasena);

        CambiarContraseña = (Button)findViewById(R.id.btnCambiarContrasena);
        ContrasenaActual = (EditText) findViewById(R.id.etContrasenaActual);
        ContrasenaNueva = (EditText)findViewById(R.id.etRepeticionContrasena);
        RepetirContrasena = (EditText)findViewById(R.id.etRepeticionContrasena);
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


        CambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validaciones()){

                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
}

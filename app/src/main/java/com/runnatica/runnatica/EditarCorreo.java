package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


public class EditarCorreo extends AppCompatActivity {

    BottomNavigationView MenuUsuario;
    EditText CorreoNuevo;
    Button VerificarCorreo;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_correo);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        CorreoNuevo = (EditText)findViewById(R.id.etCorreoNuevo);
        VerificarCorreo = (Button)findViewById(R.id.btnVerificarCorreo);

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

        VerificarCorreo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validaciones()){
                    Intent intent = new Intent(EditarCorreo.this, Verificar_CorreoNuevo.class);
                    intent.putExtra("Correo", CorreoNuevo.getText().toString());
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica el Correo", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
}
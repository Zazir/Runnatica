package com.runnatica.runnatica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class Cerrar_sesion extends AppCompatActivity {

    Button CerrarSesion, NoCerrarSesion;
    BottomNavigationView MenuUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerrar_sesion);
        CerrarSesion = (Button)findViewById(R.id.btnCerrarSesion);
        NoCerrarSesion = (Button)findViewById(R.id.btnNoCerrarSesion);

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesion();
            }
        });
        NoCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoCerrarSesion();
            }
        });

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
    }

    private void CerrarSesion(){
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(Login.ID_USUARIO_SESSION, 0);
        editor.putString(Login.CORREO_SESSION, "No_name");

        editor.commit();

        Intent next = new Intent(Cerrar_sesion.this, Login.class);
        startActivity(next);
        finish();
    }

    private void NoCerrarSesion(){
        Intent next = new Intent(this, ajustes_competidor.class);
        startActivity(next);
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

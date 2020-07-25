package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Correo_Editado extends AppCompatActivity {
    BottomNavigationView MenuUsuario;
    private String Correo;
    TextView MostrarCorreo;
    Button Adelante;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correo__editado);

        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        MostrarCorreo = (TextView)findViewById(R.id.tvMostrarCorreo);
        Adelante = (Button)findViewById(R.id.btnAdelante);
        MostrarCorreo.setText(Correo);

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

        Adelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Siguiente();
            }
        });
    }
    private void getLastViewData() {
        Bundle extra = Correo_Editado.this.getIntent().getExtras();
        Correo = extra.getString("Correo");
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
    private void Siguiente(){
        Intent next = new Intent(this, VerPerfil.class);
        startActivity(next);
    }
}
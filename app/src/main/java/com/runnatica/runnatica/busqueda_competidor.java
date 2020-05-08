package com.runnatica.runnatica;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class busqueda_competidor extends AppCompatActivity {

    BottomNavigationView MenuUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_competidor);

        MenuUsuario=(BottomNavigationView)findViewById(R.id.MenuUsuario);

        MenuUsuario.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.menu_home){
                    home();
                }
                if(menuItem.getItemId() == R.id.menu_busqueda){
                    Busqueda();
                }
                if(menuItem.getItemId() == R.id.menu_historial){
                    Historial();
                }
                if(menuItem.getItemId() == R.id.menu_ajustes){
                    Ajustes();
                }

                return true;
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
}


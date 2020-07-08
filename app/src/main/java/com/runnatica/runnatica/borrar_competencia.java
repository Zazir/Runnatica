package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

public class borrar_competencia extends AppCompatActivity {

    BottomNavigationView MenuOrganizador;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_borrar_competencia);

        MenuOrganizador= (BottomNavigationView)findViewById(R.id.MenuOrganizador);

        Menu menu = MenuOrganizador.getMenu();
        MenuItem menuItem= menu.getItem(2);
        menuItem.setChecked(true);
        MenuOrganizador.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    homeOrganizador();
                }
                if (menuItem.getItemId() == R.id.menu_historial) {
                    historialOrganizador();
                }
                if (menuItem.getItemId() == R.id.menu_ajustes) {
                    ajuestesOrganizador();
                }

                return true;
            }
        });

    }
    private void homeOrganizador(){
        Intent next = new Intent(this, home_organizador.class);
        startActivity(next);
    }
    private void historialOrganizador(){
        Intent next = new Intent(this, historial_organizador.class);
        startActivity(next);
    }
    private void ajuestesOrganizador(){
        Intent next = new Intent(this, ajustes_organizador.class);
        startActivity(next);
    }
}
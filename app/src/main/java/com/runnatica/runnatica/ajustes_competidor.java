package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.runnatica.runnatica.services.MainActivity;

import java.util.ArrayList;

public class ajustes_competidor extends AppCompatActivity {

    ListView listview;
    BottomNavigationView MenuUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_competidor);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        listview = (ListView)findViewById(R.id.lvAjustesCompetidor);

        final ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Perfil Competidor");
        arrayList.add("Crear Una Competencia");
        arrayList.add("Centro de Ayuda");
        arrayList.add("Aviso de Privacidad");
        arrayList.add("Administrador de Competencia");
        arrayList.add("Usuarios Foraneos");
        arrayList.add("Notificación servicio");
        arrayList.add("Cerrar Sesión");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        listview.setAdapter(arrayAdapter);

        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);

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
                return true;
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    Perfil();
                }
                if(i == 1){
                    CrearCompetencia();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 2){
                    Ayuda();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 3){
                    AvisoPrivacidad();
                    Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 4){
                    AdministrarCarrera();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 5){
                    Foraneos();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 6){
                    mainActivity();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 7){
                    CerrarSesion();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void CrearCompetencia(){
        Intent next = new Intent(this, crear_competencia.class);
        startActivity(next);
    }
    private void Perfil(){
        Intent next = new Intent(this, VerPerfil.class);
        startActivity(next);
    }
    private void Notificaciones(){
        Intent next = new Intent(this, Notificaciones.class);
        startActivity(next);
    }
    private void Ayuda(){
        Intent next = new Intent(this, Centrodeayuda.class);
        startActivity(next);
    }
    private void Ubicacion(){
        Intent next = new Intent(this, Ubicacion.class);
        startActivity(next);
    }
    private void AvisoPrivacidad(){
        Intent next = new Intent(this, AvisoPrivacidad.class);
        startActivity(next);
    }
    private void AdministrarCarrera(){
        Intent next = new Intent(this, home_organizador.class);
        startActivity(next);

    }
    private void CerrarSesion(){
        Intent next = new Intent(this, Cerrar_sesion.class);
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
    private void Foraneos(){
        Intent next = new Intent(this, ListaForaneos.class);
        startActivity(next);
    }
    private void mainActivity(){
        Intent next = new Intent(this, MainActivity.class);
        startActivity(next);
    }
}


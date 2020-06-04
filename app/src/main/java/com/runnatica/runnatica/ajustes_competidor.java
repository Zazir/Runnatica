package com.runnatica.runnatica;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ajustes_competidor extends AppCompatActivity {

    ListView listview;
    BottomNavigationView MenuUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_competidor);

        listview = (ListView)findViewById(R.id.lvAjustesCompetidor);
        MenuUsuario=(BottomNavigationView)findViewById(R.id.bottomNavigation);

        final ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Perfil Competidor");
        arrayList.add("Notificaciones");
        arrayList.add("Crear Una Competencia");
        arrayList.add("Centro de Ayuda");
        arrayList.add("Ubicación");
        arrayList.add("Aviso de Privacidad");
        arrayList.add("Administrador de Competencia");
        arrayList.add("Cerrar Sesión");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i == 0){
                    Perfil();
                }
                if(i == 1){
                    Notificaciones();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 2){
                    CrearCompetencia();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 3){
                    Ayuda();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 4){
                    Ubicacion();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 5){
                    AvisoPrivacidad();
                    Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 6){
                    AdministrarCarrera();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }
                if(i == 7){
                    CerrarSesion();
                    //Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
                }

            }
        });


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
}


package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class VistaAdministrador extends AppCompatActivity {
    ListView listview;
    BottomNavigationView MenuUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_administrador);

        listview = (ListView)findViewById(R.id.lvOpcionesAdministrador);

        final ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Asignar Usuarios a Carreras");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        listview.setAdapter(arrayAdapter);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        MenuUsuario.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_Regresar) {
                    aLogin();
                }
                return true;
            }
        });


        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i == 0){
                    AsignarUsuarios();
                }
            }
        });

    }
    private void AsignarUsuarios(){
        Intent next = new Intent(this, AsignarUsuarios.class);
        startActivity(next);
    }
    private void aLogin(){
        Intent next = new Intent(this, Login.class);
        startActivity(next);
    }
}
package com.runnatica.runnatica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class ajustes_competidor extends AppCompatActivity {

    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_competidor);

        listview = (ListView)findViewById(R.id.lvAjustesCompetidor);

        final ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Editar Perfil");
        arrayList.add("Notificaciones");
        arrayList.add("Crear Una Competencia");
        arrayList.add("Centro de Ayuda");
        arrayList.add("Ubicación");
        arrayList.add("Cerrar Sesión");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ajustes_competidor.this,"clicked item:"+i+" "+arrayList.get(i).toString(),Toast.LENGTH_SHORT).show();
            }
        });
    }
}

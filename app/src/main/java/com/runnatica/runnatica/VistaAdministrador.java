package com.runnatica.runnatica;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class VistaAdministrador extends AppCompatActivity {
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista_administrador);

        listview = (ListView)findViewById(R.id.lvAjustesCompetidor);

        final ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Asignar Usuarios a Carreras");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);

        listview.setAdapter(arrayAdapter);

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
        //Intent next = new Intent(this, historial_competidor.class);
        //startActivity(next);
    }
}
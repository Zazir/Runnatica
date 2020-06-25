package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ajustes_organizador extends AppCompatActivity {
    ListView listview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajustes_competidor);

        listview = (ListView)findViewById(R.id.lvAjustesCompetidor);

        final ArrayList<String> arrayList=new ArrayList<>();
        arrayList.add("Posponer Competencia");
        arrayList.add("Cancelar Competencia");
        arrayList.add("Borrar Ayuda");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,arrayList);
        listview.setAdapter(arrayAdapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                if(i == 0){
                    PosponerCompetencia();
                }
                if(i == 1){
                    CancelarCompetencia();
                }
                if(i == 2){
                    BorrarCompetencia();
                }
            }
        });
    }
    private void PosponerCompetencia(){
        Intent next = new Intent(this, editar_competencia.class);
        startActivity(next);
    }
    private void CancelarCompetencia(){
        Intent next = new Intent(this, editar_competencia.class);
        startActivity(next);
    }
    private void BorrarCompetencia(){
        Intent next = new Intent(this, editar_competencia.class);
        startActivity(next);
    }
}
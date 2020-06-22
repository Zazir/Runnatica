package com.runnatica.runnatica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class vista1_organizador extends AppCompatActivity {

    Button ListaInscritos, FotosResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista1_organizador);

        ListaInscritos = (Button)findViewById(R.id.btnListaInscritos);
        FotosResultados = (Button)findViewById(R.id.btnFotosResultados);

        ListaInscritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
        FotosResultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
    void ListaInscritos(){
        Intent next = new Intent(this, lista_inscritos.class);
        startActivity(next);
    }
    void FotosResultados(){
        Intent next = new Intent(this, Subir_Resultados.class);
        startActivity(next);
    }
}
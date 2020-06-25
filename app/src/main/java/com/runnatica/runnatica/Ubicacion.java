package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class Ubicacion extends AppCompatActivity {

    Button Estado, Pais;
    TextView Atras;
    int bandera;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubicacion);

        Estado = (Button)findViewById(R.id.btnEstado);
        Pais = (Button)findViewById(R.id.btnPais);
        Atras = (TextView)findViewById(R.id.btnAtrasUbi);

        Estado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bandera = 0 ;
            }
        });

        Pais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bandera = 1;
            }
        });

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atras();
            }
        });

    }
    void atras(){
        Intent next = new Intent(this, ajustes_competidor.class);
        startActivity(next);
    }
}

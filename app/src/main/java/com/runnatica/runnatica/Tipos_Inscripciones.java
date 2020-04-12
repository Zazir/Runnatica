package com.runnatica.runnatica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Tipos_Inscripciones extends AppCompatActivity {
    private TextView Nombre, EdadMinima, EdadMaxima;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tipos__inscripciones);

        Nombre = (TextView)findViewById(R.id.tvNombreCategoria);
        EdadMinima = (TextView)findViewById(R.id.tvEdadMinima);
        EdadMaxima = (TextView)findViewById(R.id.tvEdadMaxima);

    }
}

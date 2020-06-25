package com.runnatica.runnatica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class posponer_competencia extends AppCompatActivity {

    private Button btnDate, Guardar;
    private EditText Hora;
    private TextView Fecha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posponer_competencia);

        btnDate = (Button)findViewById(R.id.btnSeleccionarFecha);
        Guardar = (Button)findViewById(R.id.btnGuardar);
        Hora = (EditText)findViewById(R.id.etHora);
        Fecha = (TextView)findViewById(R.id.tvFecha);

    }
}
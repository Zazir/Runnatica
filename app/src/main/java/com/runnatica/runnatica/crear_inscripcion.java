package com.runnatica.runnatica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class crear_inscripcion extends AppCompatActivity {

    private EditText Nombre, CantidadNormal, CantidadForaneos, DesdeAnos, HastaAnos;
    private Button CrearInscripcion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_inscripcion);

        Nombre = (EditText) findViewById(R.id.etNombreInscripcion);
        CantidadNormal = (EditText) findViewById(R.id.etCantidadNormal);
        CantidadForaneos = (EditText) findViewById(R.id.etCantidadForaneos);
        DesdeAnos = (EditText) findViewById(R.id.etDesdeAnos);
        HastaAnos = (EditText) findViewById(R.id.etHastaAnos);
        CrearInscripcion = (Button) findViewById(R.id.btnCrearInscripcion);

    }
}
package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class editar_competencia2 extends AppCompatActivity {

    EditText EditarNombre, EditarDescripcion;
    Button SeleccionarFoto, Siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_competencia2);

        EditarNombre = (EditText)findViewById(R.id.etNombreCompetencia);
        EditarDescripcion = (EditText)findViewById(R.id.etEditarDescripcion);
        SeleccionarFoto = (Button)findViewById(R.id.btnImagenCompetencia);
        Siguiente = (Button)findViewById(R.id.btnGuardarEditar);



        SeleccionarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Salir();
            }
        });
    }
    private void Salir(){
        Intent next = new Intent(this, ajustes_organizador.class);
        startActivity(next);
    }
}
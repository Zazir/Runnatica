package com.runnatica.runnatica;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;



public class EditarCategorias2 extends AppCompatActivity {

    private EditText NombreCategoria, CompetidoresCategorias, ForaneosCategorias, MinimoEdad, MaximoEdad;
    private Button Finalizar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_categorias2);

        NombreCategoria = (EditText)findViewById(R.id.etNombreInscripcion);
        CompetidoresCategorias = (EditText)findViewById(R.id.etCantidadNormal);
        ForaneosCategorias = (EditText)findViewById(R.id.etCantidadForaneos);
        MinimoEdad = (EditText)findViewById(R.id.etDesdeAnos);
        MaximoEdad = (EditText)findViewById(R.id.etHastaAnos);
        Finalizar = (Button) findViewById(R.id.btnFinalizarCompetencia);
    }
}
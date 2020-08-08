package com.runnatica.runnatica;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class AsignarUsuarios extends AppCompatActivity {
    EditText idUsuario, NombreUsuario, idCompetencia, NombreCompetencia;
    Button Asignar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_asignar_usuarios);

        idUsuario = (EditText)findViewById(R.id.etIdUsuario);
        NombreUsuario = (EditText)findViewById(R.id.etNombreUsuario);
        idCompetencia = (EditText)findViewById(R.id.idCompetencia);
        NombreCompetencia = (EditText)findViewById(R.id.etNombreCompetencia);
        Asignar = (Button)findViewById(R.id.btnAsignarUsuarios);



    }
}
package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Editar_perfil extends AppCompatActivity {

    Button MujerEditar, HombreEditar, FotoEditar, EditarContrasena;
    EditText DiaEditar, CiudadEditar, EstadoEditar, PaisEditar, AnoEditar, NombreEditar, CorreoEditar, MesEditar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);

        MujerEditar = (Button)findViewById(R.id.btnMujerEditar);
        HombreEditar = (Button)findViewById(R.id.btnHombreEditar);
        FotoEditar = (Button)findViewById(R.id.btnFotoEditar);
        EditarContrasena = (Button)findViewById(R.id.btnEditarContrasena);
        DiaEditar = (EditText) findViewById(R.id.etDiaEditar);
        CiudadEditar = (EditText) findViewById(R.id.etCiudadEditar);
        EstadoEditar = (EditText) findViewById(R.id.etEstadoEditar);
        PaisEditar = (EditText) findViewById(R.id.etPaisEditar);
        AnoEditar = (EditText) findViewById(R.id.etAnoEditar);
        NombreEditar = (EditText) findViewById(R.id.etNombreEditar);
        CorreoEditar = (EditText) findViewById(R.id.etCorreoEditar);
        MesEditar = (EditText) findViewById(R.id.etMesEditar);

        EditarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarContrasena();
            }
        });
    }
    private void EditarContrasena(){
        Intent next = new Intent(this, EditarContrasena.class);
        startActivity(next);
    }
}

package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Editar_perfil extends AppCompatActivity {

    Button MujerEditar, HombreEditar, FotoEditar, EditarContrasena, Guardar;
    EditText DiaEditar, CiudadEditar, EstadoEditar, PaisEditar, AnoEditar, NombreEditar, CorreoEditar, MesEditar;
    private String genero = "";

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
        Guardar = (Button)findViewById(R.id.btnGuardar);


        EditarContrasena.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarContrasena();
            }
        });

        MujerEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genero = "Mujer";
                Toast.makeText(getApplicationContext(), "Eres una " + genero + " muy fit", Toast.LENGTH_SHORT).show();
            }
        });

        HombreEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                genero = "Hombre";
                Toast.makeText(getApplicationContext(), "Eres un atlético " + genero, Toast.LENGTH_SHORT).show();
            }
        });


        Guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validaciones()){


                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                };
            }
        });
    }
    private void EditarContrasena(){
        Intent next = new Intent(this, EditarContrasena.class);
        startActivity(next);
    }

    private boolean validaciones(){
        Boolean siguiente = false;
        if (NombreEditar.getText().toString().length() <= 0) {
            NombreEditar.setError("Debes de poner un nombre");
        }else if(CorreoEditar.getText().toString().length() <= 0) {
            CorreoEditar.setError("Debes de poner un correo");
        }else if (DiaEditar.getText().toString().length() <= 0 || DiaEditar.getText().toString().length() >= 3) {
            DiaEditar.setError("Ingresa un Dia Valido");
        }else if (MesEditar.getText().toString().length() <= 0 || MesEditar.getText().toString().length() >= 3) {
            MesEditar.setError("Ingresa un Mes Valido");
        }else if (MesEditar.getText().toString().length() <= 0 || MesEditar.getText().toString().length() >= 5) {
            MesEditar.setError("Ingresa un Año Valido");
        }else if(CiudadEditar.getText().toString().length() <= 0) {
            CiudadEditar.setError("Debes de poner una ciudad");
        }else if(EstadoEditar.getText().toString().length() <= 0) {
            EstadoEditar.setError("Debes de poner un correo");
        }else if(PaisEditar.getText().toString().length() <= 0) {
            PaisEditar.setError("Debes de poner un correo");
        }else siguiente = true;

        return siguiente;
    }
}

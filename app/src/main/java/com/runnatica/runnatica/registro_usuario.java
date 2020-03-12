package com.runnatica.runnatica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class registro_usuario extends AppCompatActivity {

    Button Hombre, Mujer, Foto, Registrarse;
    EditText Nombre, Correo;
    CheckBox Terminos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);
        Hombre = (Button)findViewById(R.id.btnHombre);
        Mujer = (Button)findViewById(R.id.btnMujer);
        Foto = (Button)findViewById(R.id.btnRegistrarse);
        Nombre = (EditText)findViewById(R.id.etNombre);
        Correo = (EditText)findViewById(R.id.etCorreo);
        Terminos = (CheckBox)findViewById(R.id.checkTerminos);
    }
}

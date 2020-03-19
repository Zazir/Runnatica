package com.runnatica.runnatica;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class registro_usuario extends AppCompatActivity {

    Button Hombre, Mujer, Foto, Registrarse;
    EditText Nombre, Correo, Contrasena, contrasena2, Ciudad, Estado, Pais;
    CheckBox Terminos;
    TextView Condiciones;

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
        Contrasena = (EditText)findViewById(R.id.etContrasena);
        contrasena2 = (EditText)findViewById(R.id.etContrasenaseguridad);
        Condiciones = (TextView)findViewById(R.id.tvCondiciones);
        Ciudad = (EditText)findViewById(R.id.etCiudad);
        Estado = (EditText)findViewById(R.id.etEstado);
        Pais = (EditText)findViewById(R.id.etPais);


        Condiciones.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), terminosycondiciones.class);
                startActivity(i);
            }
        });
    }

}

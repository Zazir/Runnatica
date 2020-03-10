package com.runnatica.runnatica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity {
    Button Entrar, Registro;
    EditText Usuariotxt, Contrasenatxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Entrar = (Button) findViewById(R.id.btnEntrar);
        Registro= (Button) findViewById(R.id.btnRegistrarse);
        Usuariotxt= (EditText) findViewById(R.id.etUsuario);
        Contrasenatxt= (EditText) findViewById(R.id.etContraseña);

        Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                
            }
        });
    }


}

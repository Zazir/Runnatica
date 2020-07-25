package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class registro_basico extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //Mínimo 1 digito
                    "(?=.*[a-z])" +         //Mínimo 1 minúscula
                    "(?=.*[A-Z])" +         //Mínimo 1 mayúscula
                    ".{8,}" +               //Mínimo 8 caracteres
                    "$");

    EditText Correo, Contrasena, VerificarContrasena;
    Button Siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_basico);

        Correo = (EditText)findViewById(R.id.etUsuario);
        Contrasena = (EditText)findViewById(R.id.etContrasena);
        VerificarContrasena = (EditText)findViewById(R.id.etVerificarContrasena);
        Siguiente = (Button)findViewById(R.id.btnSiguiente);

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validaciones()){
                    Intent intent = new Intent(registro_basico.this, verificar_correo.class);
                    intent.putExtra("Correo", Correo.getText().toString());
                    intent.putExtra("Contrasena", Contrasena.getText().toString());
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private boolean Validaciones(){
        Boolean siguiente = false;

        if (!Patterns.EMAIL_ADDRESS.matcher(Correo.getText().toString()).matches()){
            Correo.setError("Ese no es un correo válido");
        }else if (!PASSWORD_PATTERN.matcher(Contrasena.getText().toString()).matches()){
            Contrasena.setError("Usa 8 Caracteres, 1 Letra Mayuscula y 1 Numero como minimo ");
        }else if (Contrasena.getText().toString().equals(VerificarContrasena.getText().toString())) {
            siguiente = true;
        }else{
            VerificarContrasena.setError("Contraseñas Diferentes");
        }
        return siguiente;
    }
}
package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.regex.Pattern;

public class RegistroMedio extends AppCompatActivity {
    private String Correo, Contrasena, Nombre, Foto;


    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //Mínimo 1 digito
                    "(?=.*[a-z])" +         //Mínimo 1 minúscula
                    "(?=.*[A-Z])" +         //Mínimo 1 mayúscula
                    ".{8,}" +               //Mínimo 8 caracteres
                    "$");

    EditText etCorreo, etContrasena, VerificarContrasena;
    Button Siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_medio);

        etCorreo = (EditText)findViewById(R.id.etUsuario);
        etContrasena = (EditText)findViewById(R.id.etContrasena);
        VerificarContrasena = (EditText)findViewById(R.id.etVerificarContrasena);
        Siguiente = (Button)findViewById(R.id.btnSiguiente);
        getLastViewData();

        etCorreo.setText(Correo);
        etCorreo.setEnabled(false);

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validaciones()){
                    Intent intent = new Intent(RegistroMedio.this, registro_usuario.class);
                    intent.putExtra("Correo", etCorreo.getText().toString());
                    intent.putExtra("Contrasena", etContrasena.getText().toString());
                    intent.putExtra("Nombre", Nombre);
                    startActivity(intent);
                    finish();
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void getLastViewData() {
        Bundle extra = RegistroMedio.this.getIntent().getExtras();
        Correo = extra.getString("Correo");
        Nombre = extra.getString("Nombre");
    }
    private boolean Validaciones(){
        Boolean siguiente = false;

        if (!Patterns.EMAIL_ADDRESS.matcher(etCorreo.getText().toString()).matches()){
            etCorreo.setError("Ese no es un correo válido");
        }else if (!PASSWORD_PATTERN.matcher(etContrasena.getText().toString()).matches()){
            etContrasena.setError("Usa 8 Caracteres, 1 Letra Mayuscula y 1 Numero como minimo ");
        }else if (etContrasena.getText().toString().equals(VerificarContrasena.getText().toString())) {
            siguiente = true;
        }else{
            VerificarContrasena.setError("Contraseñas Diferentes");
        }
        return siguiente;
    }
}
package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class registro_basico extends AppCompatActivity {


    EditText Correo, Contrasena, VerificarContrasena;
    Button Siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_basico);

        Correo = (EditText)findViewById(R.id.etUsuario);
        Siguiente = (Button)findViewById(R.id.btnSiguiente);

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validaciones()){
                    Intent intent = new Intent(registro_basico.this, verificar_correo.class);
                    intent.putExtra("Correo", Correo.getText().toString());
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
        }else{
            siguiente = true;
        }
        return siguiente;
    }
}
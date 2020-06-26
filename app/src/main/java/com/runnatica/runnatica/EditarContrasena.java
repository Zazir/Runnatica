package com.runnatica.runnatica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class EditarContrasena extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //Mínimo 1 digito
                    "(?=.*[a-z])" +         //Mínimo 1 minúscula
                    "(?=.*[A-Z])" +         //Mínimo 1 mayúscula
                    ".{8,}" +               //Mínimo 8 caracteres
                    "$");

    Button CambiarContraseña;
    EditText ContrasenaActual, ContrasenaNueva, RepetirContrasena;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_contrasena);

        CambiarContraseña = (Button)findViewById(R.id.btnCambiarContrasena);
        ContrasenaActual = (EditText) findViewById(R.id.etContrasenaActual);
        ContrasenaNueva = (EditText)findViewById(R.id.etRepeticionContrasena);
        RepetirContrasena = (EditText)findViewById(R.id.etRepeticionContrasena);

        CambiarContraseña.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validaciones()){

                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    private Boolean Validaciones() {
        Boolean siguiente = false;
        if (ContrasenaActual.getText().toString().length() <= 0){
            ContrasenaActual.setError("Debes poner tu nombre");
        }else if (!PASSWORD_PATTERN.matcher(ContrasenaNueva.getText().toString()).matches()){
            ContrasenaNueva.setError("La contraseña es debil");
        }else if (!PASSWORD_PATTERN.matcher(RepetirContrasena.getText().toString()).matches()){
            RepetirContrasena.setError("La contraseña es debil");
        }else if(RepetirContrasena.getText() != ContrasenaNueva.getText()){
            RepetirContrasena.setError("No se parecen las Contraseñas");
        }else siguiente = true;

        return siguiente;
    }
}

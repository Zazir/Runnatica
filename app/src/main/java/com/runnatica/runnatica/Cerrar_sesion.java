package com.runnatica.runnatica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Cerrar_sesion extends AppCompatActivity {

    Button CerrarSesion, NoCerrarSesion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cerrar_sesion);
        CerrarSesion = (Button)findViewById(R.id.btnCerrarSesion);
        NoCerrarSesion = (Button)findViewById(R.id.btnNoCerrarSesion);

        CerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CerrarSesion();
            }
        });
        NoCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NoCerrarSesion();
            }
        });
    }

    private void CerrarSesion(){
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(Login.ID_USUARIO_SESSION, 0);
        editor.putString(Login.CORREO_SESSION, "No_name");

        editor.commit();

        Intent next = new Intent(Cerrar_sesion.this, Login.class);
        startActivity(next);
        finish();
    }

    private void NoCerrarSesion(){
        Intent next = new Intent(this, ajustes_competidor.class);
        startActivity(next);
    }
}

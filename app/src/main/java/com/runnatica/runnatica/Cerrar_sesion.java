package com.runnatica.runnatica;

import android.content.Intent;
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
        Intent next = new Intent(this, Login.class);
        startActivity(next);
    }
    private void NoCerrarSesion(){
        Intent next = new Intent(this, ajustes_competidor.class);
        startActivity(next);
    }
}

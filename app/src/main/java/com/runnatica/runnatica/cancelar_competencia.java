package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class cancelar_competencia extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final Button SiCancelar, NoCancelar;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cancelar_competencia);

        SiCancelar = (Button)findViewById(R.id.btnSiCancelar);
        NoCancelar = (Button)findViewById(R.id.btnNoCancelar);

        NoCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoCancelar();
            }
        });
        SiCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SiCancelar();
            }
        });
    }
    private void NoCancelar(){
        Intent next = new Intent(this, ajustes_organizador.class);
        startActivity(next);
    }
    private void SiCancelar(){

        //Poner codigo para cancelar la competencia

    }

}
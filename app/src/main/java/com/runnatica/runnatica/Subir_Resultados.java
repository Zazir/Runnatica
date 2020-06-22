package com.runnatica.runnatica;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Subir_Resultados extends AppCompatActivity {

    Button Atras;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subir__resultados);

        Atras = (Button)findViewById(R.id.btnAtras);

        Atras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                atras();
            }
        });
    }

    void atras(){
        Intent next = new Intent(this, vista1_organizador.class);
        startActivity(next);
    }
}
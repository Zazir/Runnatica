package com.runnatica.runnatica;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class Felicidades_Inscripcion extends AppCompatActivity {

    private Button Regresar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_felicidades__inscripcion);

        Regresar = (Button)findViewById(R.id.btnRegresar_Inscripcion);
    }
}

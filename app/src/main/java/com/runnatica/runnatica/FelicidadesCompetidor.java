package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class FelicidadesCompetidor extends AppCompatActivity {

    TextView alHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_felicidades_competidor);

        alHome = (TextView)findViewById(R.id.alHome);


        alHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alHome();
            }
        });

    }
    private void alHome(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }
}


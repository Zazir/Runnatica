package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class logo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logo);

        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override

            public void run() {
                paginaDeLogin();
                // Do something after 5s = 5000ms
            }
        }, 3000);

    }

    void paginaDeLogin() {
        Intent i=new Intent(this, Login.class);
        startActivity(i);
    }
}

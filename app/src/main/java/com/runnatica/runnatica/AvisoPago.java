package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

public class AvisoPago extends AppCompatActivity {

    private String monto = "";

    private String id_competencia, NombreCompetencia, Fecha, Lugar, Organizador, ids_foraneos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aviso_pago);

        getLastViewData();

        final Handler handler = new Handler();//que es y para que se utiliza el Handler
        handler.postDelayed(new Runnable() {
            @Override

            public void run() {
                moveNewActivity();
                finish();
                // Do something after 5s = 5000ms
            }
        }, 7000);

    }
    private void getLastViewData() {
        Bundle extra = AvisoPago.this.getIntent().getExtras();
        id_competencia = extra.getString("ID_COMPENTENCIA");
        monto = extra.getString("monto");
        NombreCompetencia = extra.getString("NOMBRE_COMPETENCIA");
        Fecha = extra.getString("FECHA");
        Lugar = extra.getString("LUGAR");
        Organizador = extra.getString("ORGANIZADOR");
        ids_foraneos = extra.getString("CANT_FORANEOS");
    }
    public void moveNewActivity(){
        Intent intent = new Intent(AvisoPago.this, pagarInscripciones.class);
        intent.putExtra("monto", monto);
        intent.putExtra("ID_COMPENTENCIA", id_competencia);
        intent.putExtra("CANT_FORANEOS", ids_foraneos);
        intent.putExtra("NOMBRE_COMPETENCIA", NombreCompetencia);
        intent.putExtra("FECHA", Fecha);
        intent.putExtra("LUGAR", Lugar);
        intent.putExtra("ORGANIZADOR", Organizador);
        startActivity(intent);
    }
}
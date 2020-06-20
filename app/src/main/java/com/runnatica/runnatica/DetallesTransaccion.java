package com.runnatica.runnatica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

public class DetallesTransaccion extends AppCompatActivity {

    TextView txtid, txtEstatus, txtMonto;
    Button btnContinuar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_transaccion);

        txtid = (TextView)findViewById(R.id.tvid);
        txtEstatus = (TextView)findViewById(R.id.tvEstatus);
        txtMonto = (TextView)findViewById(R.id.tvMonto);
        btnContinuar = (Button)findViewById(R.id.btnContinuar);

        Intent intent = getIntent();
        try {
            JSONObject jsonObject = new JSONObject(intent.getStringExtra("PaymentDetails"));
            verDetalles(jsonObject.getJSONObject("response"), intent.getStringExtra("PaymentAmount"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        btnContinuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FelicidadesInscribir();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FelicidadesInscribir();
    }

    private void FelicidadesInscribir() {
        Intent intent = new Intent(this, FelicidadesCompetidor.class);
        startActivity(intent);
        finish();
    }

    private void verDetalles(JSONObject response, String monto) {
        //Setear los parámetros obtenidos a través del intent
        try {
            txtid.setText(response.getString("id"));
            txtEstatus.setText(response.getString("state"));
            txtMonto.setText("$" + monto);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}

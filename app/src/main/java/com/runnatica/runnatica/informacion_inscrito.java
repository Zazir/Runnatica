package com.runnatica.runnatica;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class informacion_inscrito extends AppCompatActivity {
    private Button Atras;
    private TextView txtNombre, txtCorreo, txtF_inscrito, txtF_nacimiento;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_inscrito);
        txtNombre = (TextView)findViewById(R.id.tvNombreinscrito);
        txtCorreo = (TextView)findViewById(R.id.tvCorreoInscrito);
        txtF_inscrito = (TextView)findViewById(R.id.tvFechaInscripcion);
        txtF_nacimiento = (TextView)findViewById(R.id.tvNacimientoUsuario);

        Atras = findViewById(R.id.btnAtras);

        getLastViewData();

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

    private void getLastViewData() {
        Bundle extra = informacion_inscrito.this.getIntent().getExtras();
        txtNombre.setText(extra.getString("NOMBRE"));
        txtCorreo.setText(extra.getString("CORREO"));
        txtF_inscrito.setText(extra.getString("F_INSCRIPCION"));
        txtF_nacimiento.setText(extra.getString("F_NACIMIENTO"));
        //id_competencia = extra.getString("id");
    }
}
package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.runnatica.runnatica.Remote.Recapcha;

public class VerPerfil extends AppCompatActivity {

    TextView NombreUsuario, CorreoUsuario, SexoUsuario, FechaNacimientoUsuario, CiudadUsuario, EstadoUsuario, PaisUsuario, EditarPerfil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_perfil);

        NombreUsuario = (TextView)findViewById(R.id.tvNombreUsuario);
        CorreoUsuario = (TextView)findViewById(R.id.tvCorreoUsuario);
        SexoUsuario = (TextView)findViewById(R.id.tvSexoUsuario);
        FechaNacimientoUsuario = (TextView)findViewById(R.id.tvFechaNacimientoUsuario);
        CiudadUsuario = (TextView)findViewById(R.id.tvCiudadUsuario);
        EstadoUsuario = (TextView)findViewById(R.id.tvEstadoUsuario);
        PaisUsuario = (TextView)findViewById(R.id.tvPaisUsuario);
        EditarPerfil = (TextView)findViewById(R.id.tvEditarUsuario);

        EditarPerfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarPerfil();
            }
        });
    }
    private void EditarPerfil(){
        Intent next = new Intent(this, Editar_perfil.class);
        startActivity(next);
    }
}

package com.runnatica.runnatica;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class informacion_inscrito extends AppCompatActivity {
    private Button Atras;
    private TextView txtNombre, txtCorreo, txtF_inscrito, txtF_nacimiento;
    BottomNavigationView MenuOrganizador;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_informacion_inscrito);
        txtNombre = (TextView)findViewById(R.id.tvNombreinscrito);
        txtCorreo = (TextView)findViewById(R.id.tvCorreoInscrito);
        txtF_inscrito = (TextView)findViewById(R.id.tvFechaInscripcion);
        txtF_nacimiento = (TextView)findViewById(R.id.tvNacimientoUsuario);

        MenuOrganizador= (BottomNavigationView)findViewById(R.id.MenuOrganizador);

        Menu menu = MenuOrganizador.getMenu();
        MenuItem menuItem= menu.getItem(2);
        menuItem.setChecked(true);

        MenuOrganizador.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    homeOrganizador();
                }
                if (menuItem.getItemId() == R.id.menu_historial) {
                    historialOrganizador();
                }
                if (menuItem.getItemId() == R.id.menu_ajustes) {
                    ajuestesOrganizador();
                }

                return true;
            }
        });



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
    private void homeOrganizador(){
        Intent next = new Intent(this, home_organizador.class);
        startActivity(next);
    }
    private void historialOrganizador(){
        Intent next = new Intent(this, historial_organizador.class);
        startActivity(next);
    }
    private void ajuestesOrganizador(){
        Intent next = new Intent(this, ajustes_organizador.class);
        startActivity(next);
    }
}
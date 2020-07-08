package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.poho.Usuario;

public class agregarForaneo extends AppCompatActivity {
    BottomNavigationView MenuUsuario;
    private EditText txtNombre, txtCorreo, txtEdad;
    private RadioButton rbHombre, rbMujer;
    private Button btnCrearForaneo, btnVolverInscripcion;

    private String sexo;
    private Usuario usuario = Usuario.getUsuarioInstance();
    private String monto = "";
    private String id_competencia, NombreCompetencia, Fecha, Lugar, Organizador;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_foraneo);
        txtNombre = (EditText)findViewById(R.id.etNombreForaneo);
        txtCorreo = (EditText)findViewById(R.id.etCorreoForaneo);
        txtEdad = (EditText)findViewById(R.id.etEdadForaneo);
        rbHombre = (RadioButton)findViewById(R.id.rbtnHombre);
        rbMujer = (RadioButton)findViewById(R.id.rbtnMujer);
        btnCrearForaneo = (Button)findViewById(R.id.btnCreateForaneo);
        btnVolverInscripcion = (Button)findViewById(R.id.btnVolver);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        //Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);
        //

        MenuUsuario.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    home();
                }
                if (menuItem.getItemId() == R.id.menu_busqueda) {
                    Busqueda();
                }
                if (menuItem.getItemId() == R.id.menu_historial) {
                    Historial();
                }
                if (menuItem.getItemId() == R.id.menu_ajustes) {
                    Ajustes();
                }

                return true;
            }
        });


        final String ip = getString(R.string.ip);

        btnCrearForaneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    setSexo();
                    crearUsuarioForaneo(ip + "agregarUsuarioForaneo.php?" +
                            "id_usuario=" + usuario.getId() +
                            "&nombre=" + txtNombre.getText().toString().replaceAll(" ", "%20") +
                            "&correo=" + txtCorreo.getText().toString() +
                            "&sexo=" + sexo +
                            "&edad=" + txtEdad.getText().toString());
            }
        });

        btnVolverInscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAInscripcion();
            }
        });
    }

    private void volverAInscripcion() {
        Intent intent = new Intent(agregarForaneo.this, InscripcionForaneo.class);
        intent.putExtra("monto", monto);
        intent.putExtra("ID_COMPENTENCIA", id_competencia);
        intent.putExtra("NOMBRECOMPENTENCIA", NombreCompetencia);
        intent.putExtra("FECHA", Fecha);
        intent.putExtra("LUGAR", Lugar);
        intent.putExtra("ORGANIZADOR", Organizador);
        startActivity(intent);
        finish();
    }

    private void getLastViewData() {
        Bundle extra = agregarForaneo.this.getIntent().getExtras();
        id_competencia = extra.getString("ID_COMPENTENCIA");
        monto = extra.getString("monto");
        NombreCompetencia = extra.getString("NOMBRE_COMPETENCIA");
        Fecha = extra.getString("FECHA");
        Lugar = extra.getString("LUGAR");
        Organizador = extra.getString("ORGANIZADOR");
        Toast.makeText(this, id_competencia, Toast.LENGTH_SHORT).show();
    }

    private void setSexo() {
        if (rbMujer.isChecked())
            sexo = "Mujer";
        else if (rbHombre.isChecked())
            sexo = "Hombre";
    }

    private void crearUsuarioForaneo(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(agregarForaneo.this, response, Toast.LENGTH_SHORT);
                        txtNombre.setText("");
                        txtCorreo.setText("");
                        txtEdad.setText("");
                        rbHombre.setChecked(false);
                        rbMujer.setChecked(false);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(agregarForaneo.this, "Error de conección con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(agregarForaneo.this).add(stringRequest);
    }
    private void home(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }
    private void Busqueda(){
        Intent next = new Intent(this, busqueda_competidor.class);
        startActivity(next);
    }
    private void Historial(){
        Intent next = new Intent(this, historial_competidor.class);
        startActivity(next);
    }
    private void Ajustes(){
        Intent next = new Intent(this, ajustes_competidor.class);
        startActivity(next);
    }

}

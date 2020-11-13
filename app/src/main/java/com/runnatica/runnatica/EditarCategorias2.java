package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;


public class EditarCategorias2 extends AppCompatActivity {

    private EditText NombreCategoria, CompetidoresCategorias, ForaneosCategorias, MinimoEdad, MaximoEdad;
    private Button Finalizar;
    BottomNavigationView MenuOrganizador;
    private String dominio, id_categoria,id_competencia="";;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_categorias2);

        dominio = getString(R.string.ip);
        getLastViewData();

        NombreCategoria = (EditText)findViewById(R.id.etNombreInscripcion);
        CompetidoresCategorias = (EditText)findViewById(R.id.etCantidadNormal);
        ForaneosCategorias = (EditText)findViewById(R.id.etCantidadForaneos);
        MinimoEdad = (EditText)findViewById(R.id.etDesdeAnos);
        MaximoEdad = (EditText)findViewById(R.id.etHastaAnos);
        Finalizar = (Button) findViewById(R.id.btnFinalizarCompetencia2);
        MenuOrganizador= (BottomNavigationView)findViewById(R.id.MenuOrganizador);

        Menu menu = MenuOrganizador.getMenu();
        MenuItem menuItem= menu.getItem(0);
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
                if (menuItem.getItemId() == R.id.menu_regresar) {
                    home();
                }
                return true;
            }
        });

        Finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Categoria Editada Exitosamente", Toast.LENGTH_SHORT).show();
                    peticion(dominio + "actualizarCategoria.php?edad_max="+MaximoEdad.getText().toString()+"&id_categoria="+id_categoria);
                    peticion(dominio + "actualizarCategoria.php?edad_min="+MinimoEdad.getText().toString()+"&id_categoria="+id_categoria);
                    peticion(dominio + "actualizarCategoria.php?cantidad_usuarios"+CompetidoresCategorias.getText().toString()+"&id_categoria="+id_categoria);
                    peticion(dominio + "actualizarCategoria.php?cantidad_foraneos="+ForaneosCategorias.getText().toString()+"&id_categoria="+id_categoria);
                    peticion(dominio + "actualizarCategoria.php?nombre_categoria="+NombreCategoria.getText().toString().replaceAll(" ", "%20")+"&id_categoria="+id_categoria);
                    MaximoEdad.setText("");
                    MinimoEdad.setText("");
                    CompetidoresCategorias.setText("");
                    ForaneosCategorias.setText("");
                    NombreCategoria.setText("");
                    Atras();
            }
        });
    }

    private void getLastViewData() {
        Bundle extra = this.getIntent().getExtras();
        id_categoria = extra.getString("ID_CAT");
        id_competencia = extra.getString("ID_COMPENTENCIA5");
    }

    private void peticion(String URL) {

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        Log.i("Mensaje",response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error de conección con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private boolean validaciones() {

        return true;
    }
    private void homeOrganizador() {
        Intent next = new Intent(this, home_organizador.class);
        startActivity(next);
    }
    private void historialOrganizador(){
        Intent next = new Intent(this, historial_organizador.class);
        startActivity(next);
    }
    private void home(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }
    private void Atras(){
        Intent next = new Intent(this, EditarCategoria1.class);
        next.putExtra("ID_COMPENTENCIA5", id_competencia);
        startActivity(next);
    }
}
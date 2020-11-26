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
    String Nombre, Competidores, Foraneos, MinEdad, MaxEdad;


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

        NombreCategoria.setText(Nombre);
        CompetidoresCategorias.setText(Competidores);
        ForaneosCategorias.setText(Foraneos);
        MinimoEdad.setText(MinEdad);
        MaximoEdad.setText(MaxEdad);

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
                if(validaciones()) {
                    Toast.makeText(getApplicationContext(), "Categoria Editada Exitosamente", Toast.LENGTH_SHORT).show();
                    peticion(dominio + "actualizarCategoria.php?edad_max=" + MaximoEdad.getText().toString() + "&id_categoria=" + id_categoria);
                    peticion(dominio + "actualizarCategoria.php?edad_min=" + MinimoEdad.getText().toString() + "&id_categoria=" + id_categoria);
                    peticion(dominio + "actualizarCategoria.php?cantidad_usuarios=" + CompetidoresCategorias.getText().toString() + "&id_categoria=" + id_categoria);
                    peticion(dominio + "actualizarCategoria.php?cantidad_foraneos=" + ForaneosCategorias.getText().toString() + "&id_categoria=" + id_categoria);
                    peticion(dominio + "actualizarCategoria.php?nombre_categoria=" + NombreCategoria.getText().toString().replaceAll(" ", "%20") + "&id_categoria=" + id_categoria);
                    MaximoEdad.setText("");
                    MinimoEdad.setText("");
                    CompetidoresCategorias.setText("");
                    ForaneosCategorias.setText("");
                    NombreCategoria.setText("");
                    Atras();
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getLastViewData() {
        Bundle extra = this.getIntent().getExtras();
        id_categoria = extra.getString("ID_CAT");
        id_competencia = extra.getString("ID_COMPENTENCIA5");
        Nombre = extra.getString("Nombre");
        Competidores = extra.getString("Competidores");
        Foraneos = extra.getString("Foraneos");
        MinEdad = extra.getString("MinEdad");
        MaxEdad = extra.getString("MaxEdad");
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
        Boolean siguiente = false;

        try {
            int VerificarEdad = Integer.parseInt(MinimoEdad.getText().toString());
            int VerificarEdad2 = Integer.parseInt(MaximoEdad.getText().toString());
            int VerificarCantidad = Integer.parseInt(CompetidoresCategorias.getText().toString());

            if(NombreCategoria.getText().toString().length() <= 0){
                NombreCategoria.setError("Debes de poner el nombre de la inscripcion");
            }else if (CompetidoresCategorias.getText().toString().length() <= 0 || CompetidoresCategorias.getText().toString().length() >= 5) {
                CompetidoresCategorias.setError("Ingresa una Cantidad Valida");
            }else if (ForaneosCategorias.getText().toString().length() <= 0 || ForaneosCategorias.getText().toString().length() >= 5) {
                ForaneosCategorias.setError("Ingresa una Cantidad Valida");
            }else if (VerificarEdad <= 3 || VerificarEdad >= 99) {
                MinimoEdad.setError("La edad no es valida");
            } else if (VerificarEdad2 <= 3 || VerificarEdad2 >= 99) {
                MaximoEdad.setError("La edad no es valida");
            } else if (VerificarEdad >= VerificarEdad2) {
                MinimoEdad.setError("No puede ser mayor o igual la edad minima a la edad Maxima");
            } else if (VerificarEdad2 <= VerificarEdad) {
                MaximoEdad.setError("No puede ser menor o igual la edad maxima a la edad Minima");
            }else if (MaximoEdad.getText().toString().length() <= 0 || MaximoEdad.getText().toString().length() >= 3) {
                MaximoEdad.setError("Ingresa un Año Valido");
            } else if (VerificarCantidad <= 0) {
                CompetidoresCategorias.setError("Ingresa una Cantidad Valida");
            } else siguiente = true;


        }catch(Exception e){

        }
        return siguiente;
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
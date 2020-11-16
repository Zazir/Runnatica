package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class editarForaneos extends AppCompatActivity {
    BottomNavigationView MenuUsuario;
    private Button EditarForaneo;
    private EditText EditarNombreForaneo, EditarCorreoForaneo, EditarEdadForaneo;
    private RadioButton EditarHombre, EditarMujer;
    private String dominio, id_Foraneo, Nombre_Foraneo, Edad_Foraneo, Sexo_Foraneo, Correo_Foraneo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_foraneos);

        EditarNombreForaneo = (EditText)findViewById(R.id.etEditarNombreForaneo);
        EditarCorreoForaneo = (EditText)findViewById(R.id.etEditarCorreoForaneo);
        EditarEdadForaneo = (EditText)findViewById(R.id.etEditarEdadForaneo);
        EditarHombre = (RadioButton)findViewById(R.id.rbtnEditarHombre);
        EditarMujer = (RadioButton)findViewById(R.id.rbtnEditarMujer);
        EditarForaneo = (Button)findViewById(R.id.rbtnEditarMujer);
        EditarForaneo = (Button)findViewById(R.id.btnCreateForaneo);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        //Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);
        //
        getLastViewData();
        dominio = getString(R.string.ip);

        EditarNombreForaneo.setText(Nombre_Foraneo);
        EditarCorreoForaneo.setText(Correo_Foraneo);
        EditarEdadForaneo.setText(Edad_Foraneo);
        if(Sexo_Foraneo.equals("Hombre")){
            EditarHombre.setChecked(true);
        }else{
            EditarMujer.setChecked(true);
        }

        EditarNombreForaneo.setEnabled(false);
        EditarCorreoForaneo.setEnabled(false);
        EditarHombre.setEnabled(false);
        EditarMujer.setEnabled(false);




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

        EditarForaneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(editarForaneos.this, ListaForaneos.class);
                startActivity(intent);
                finish();
            }
        });


        EditarNombreForaneo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EditarForaneo(dominio + "actualizarForaneo.php?id_foraneo=" + id_Foraneo + "&nombreForaneo=" + EditarNombreForaneo.getText().toString().replaceAll(" ", "%20"));
            }
        });

        EditarCorreoForaneo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                EditarForaneo(dominio + "actualizarForaneo.php?id_foraneo=" + id_Foraneo + "&correoForaneo=" + EditarCorreoForaneo.getText().toString());
            }
        });

        EditarEdadForaneo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    int EdadIngresada = Integer.parseInt(EditarEdadForaneo.getText().toString());

                    if (EdadIngresada >= 4 && EdadIngresada <= 99) {
                        EditarForaneo(dominio + "actualizarForaneo.php?id_foraneo=" + id_Foraneo + "&EdadForaneo=" + EditarEdadForaneo.getText().toString());
                    } else {
                        EditarEdadForaneo.setError("La edad no es valida");
                    }
                }catch (Exception e){
                    
                }
            }
        });

        EditarHombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarForaneo(dominio + "actualizarForaneo.php?id_foraneo=" + id_Foraneo + "&sexoForaneo=Hombre");
            }
        });

        EditarMujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarForaneo(dominio + "actualizarForaneo.php?id_foraneo=" + id_Foraneo + "&sexoForaneo=Mujer");
            }
        });
    }

    private void getLastViewData() {
    Bundle nombre = getIntent().getExtras();
    id_Foraneo = nombre.getString("id");
    Nombre_Foraneo = nombre.getString("Nombre");
    Edad_Foraneo = nombre.getString("Edad");
    Correo_Foraneo = nombre.getString("Correo");
    Sexo_Foraneo = nombre.getString("Sexo");

    }

    private void EditarForaneo(String URL){
        Log.i("URL", URL);
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.equals("Exito")){

                        }else{
                            Toast.makeText(getApplicationContext(), "Espera un momento", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Hubo un error con la conexión", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(request);

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
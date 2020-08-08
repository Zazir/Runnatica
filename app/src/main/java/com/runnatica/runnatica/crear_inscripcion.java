package com.runnatica.runnatica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import static com.runnatica.runnatica.crear_competencia.AVAL_DE_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.CALLE_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.CIUDAD_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.COLONIA_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.COORDENADAS_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.DESCRIPCION_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.ESTADO_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.FECHA_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.HORA_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.IMAGEN_DE_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.NOMBRE_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.PAIS_COMPETENCIA;
import static com.runnatica.runnatica.crear_competencia.PRECIO_COMPETENCIA;

public class crear_inscripcion extends AppCompatActivity{
    private EditText Nombre, CantidadNormal, CantidadForaneos, DesdeAnos, HastaAnos;
    private Button CrearInscripcion, btnFinalizarInscripciones;
    private TextView txtcantidadInscripciones;

    private String idCompetencia;
    private int contInscripciones = 0;

    BottomNavigationView MenuUsuario;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_inscripcion);

        Nombre = (EditText) findViewById(R.id.etNombreInscripcion);
        CantidadNormal = (EditText) findViewById(R.id.etCantidadNormal);
        CantidadForaneos = (EditText) findViewById(R.id.etCantidadForaneos);
        DesdeAnos = (EditText) findViewById(R.id.etDesdeAnos);
        HastaAnos = (EditText) findViewById(R.id.etHastaAnos);
        CrearInscripcion = (Button) findViewById(R.id.btnCrearInscripcion);
        btnFinalizarInscripciones = (Button)findViewById(R.id.btnFinalizarCompetencia);
        txtcantidadInscripciones = (TextView)findViewById(R.id.tvCantidadInscripciones);

        idCompetenciaFromLastView();

        CrearInscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validaciones()){
                String ip = getString(R.string.ip);
                crearInscripcion(ip + "agregarInscripciones.php?" +
                        "nombreInscripcion=" + Nombre.getText().toString().replaceAll(" ", "%20") +
                        "&id_competencia=" + idCompetencia +
                        "&cant_usuarios=" + CantidadNormal.getText().toString() +
                        "&cant_foraneos=" + CantidadForaneos.getText().toString() +
                        "&edadMin=" + DesdeAnos.getText().toString() +
                        "&edadMax=" + HastaAnos.getText().toString());
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });

        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

//Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(3);
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


        btnFinalizarInscripciones.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volverAlHome();
            }
        });
    }

    private void volverAlHome() {
        Intent intent = new Intent(crear_inscripcion.this, home.class);
        startActivity(intent);
        finish();
    }

    private void idCompetenciaFromLastView() {
        Bundle bundle = crear_inscripcion.this.getIntent().getExtras();
        idCompetencia = bundle.getString("ID_COMPETENCIA");
    }

    private void crearInscripcion(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Inscripcion Creada")){
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                            contInscripciones++;
                            txtcantidadInscripciones.setText("Has definido " + contInscripciones + " inscripciones");
                            btnFinalizarInscripciones.setEnabled(true);
                            Nombre.setText("");
                            CantidadNormal.setText("");
                            CantidadForaneos.setText("");
                            DesdeAnos.setText("");
                            HastaAnos.setText("");

                            limpiarArchivoCompetencia();
                        } else
                            Toast.makeText(getApplicationContext(), response, Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Hubo un error con el servidor: " + error, Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void limpiarArchivoCompetencia() {
        SharedPreferences preferences = getSharedPreferences("Autoguardado_Competencia", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(NOMBRE_COMPETENCIA, "");
        editor.putInt(PRECIO_COMPETENCIA, 0);
        editor.putString(FECHA_COMPETENCIA, "");
        editor.putString(HORA_COMPETENCIA, "");
        editor.putString(IMAGEN_DE_COMPETENCIA, "");
        editor.putString(PAIS_COMPETENCIA, "");
        editor.putString(ESTADO_COMPETENCIA, "");
        editor.putString(CIUDAD_COMPETENCIA, "");
        editor.putString(COLONIA_COMPETENCIA, "");
        editor.putString(CALLE_COMPETENCIA, "");
        editor.putString(COORDENADAS_COMPETENCIA, "Coordenadas");
        editor.putString(AVAL_DE_COMPETENCIA, "");
        editor.putString(DESCRIPCION_COMPETENCIA, "");

        editor.commit();
    }

    private Boolean Validaciones() {
        Boolean siguiente = false;
        if (Nombre.getText().toString().length() <= 0) {
            Nombre.setError("Debes de poner el nombre de la inscripcion");
        }
        else if (CantidadNormal.getText().toString().length() <= 0 || CantidadNormal.getText().toString().length() >= 5) {
            CantidadNormal.setError("Ingresa una Cantidad Valida");
        }
        else if (CantidadForaneos.getText().toString().length() <= 0 || CantidadForaneos.getText().toString().length() >= 5) {
            CantidadForaneos.setError("Ingresa una Cantidad Valida");
        }
        else if (DesdeAnos.getText().toString().length() <= 0 || DesdeAnos.getText().toString().length() >= 3) {
            DesdeAnos.setError("Ingresa un Año Valido");
        }
        else if (HastaAnos.getText().toString().length() <= 0 || HastaAnos.getText().toString().length() >= 3) {
            HastaAnos.setError("Ingresa un Año Valido");
        }else siguiente = true;

        return siguiente;
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
package com.runnatica.runnatica;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.adapter.MyAdapter;
import com.runnatica.runnatica.poho.Competencias;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class busqueda_competidor extends AppCompatActivity {

    private Button btnfecha, btnKm, btnEstado, btnPais;
    private Spinner spPais, spEstado;
    private EditText txtKM;
    private RecyclerView rvFiltro;
    private TextView LaFecha;
    BottomNavigationView MenuUsuario;

    private String estado, pais;

    private List<Competencias> listCompetencias;
    private MyAdapter adapter;

    private int[] id;
    private String fecha;

    private String dominio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busqueda_competidor);

        dominio = getString(R.string.ip);

        btnfecha = (Button)findViewById(R.id.btnFiltroFecha);
        btnEstado = (Button)findViewById(R.id.btnBuscarEstado);
        btnPais = (Button)findViewById(R.id.btnBuscarPais);
        spPais = (Spinner) findViewById(R.id.spFiltroPais);
        spEstado = (Spinner)findViewById(R.id.spFiltroEstado);
        LaFecha = (TextView)findViewById(R.id.xx);
        rvFiltro = (RecyclerView) findViewById(R.id.rvFiltroCompetencias);
        rvFiltro.setHasFixedSize(true);
        rvFiltro.setLayoutManager(new LinearLayoutManager(this));

        listCompetencias = new ArrayList<>();

        cargarSpinnerEstado();
        cargarSpinnerPais();

        MenuUsuario=(BottomNavigationView)findViewById(R.id.bottomNavigation);

        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(1);
        menuItem.setChecked(true);

        MenuUsuario.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if(menuItem.getItemId() == R.id.menu_home){
                    home();
                }
                if(menuItem.getItemId() == R.id.menu_busqueda){
                    Busqueda();
                }
                if(menuItem.getItemId() == R.id.menu_historial){
                    Historial();
                }
                if(menuItem.getItemId() == R.id.menu_ajustes){
                    Ajustes();
                }

                return true;
            }
        });

        /*btnKm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    int kilometros = Integer.parseInt(txtKM.getText().toString());
                    CargarCompetencias(dominio + "filtroCompetencias.php?kilometros=" + kilometros);
                }catch (NumberFormatException e){
                    txtKM.setError("Este dato tiene que ser un número");
                }
            }
        });*/

        btnfecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int ano = calendar.get(Calendar.YEAR);

                DatePickerDialog picker = new DatePickerDialog(busqueda_competidor.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fecha = year + "-" + (month+1) + "-" + dayOfMonth;
                        LaFecha.setText(fecha);
                        CargarCompetencias(dominio + "filtroCompetencias.php?fecha=" + fecha);

                    }
                }, ano, mes, dia);

                picker.show();

            }
        });

        btnEstado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarCompetencias(dominio + "filtroCompetencias.php?estado=" + estado);
            }
        });

        btnPais.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CargarCompetencias(dominio + "filtroCompetencias.php?pais=" + pais);
            }
        });
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

    private void cargarSpinnerEstado() {
        ArrayAdapter<CharSequence> opcionesSpCat = ArrayAdapter.createFromResource(this, R.array.estados, android.R.layout.simple_spinner_item);
        spEstado.setAdapter(opcionesSpCat);

        spEstado.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                estado = parent.getItemAtPosition(position).toString().replaceAll(" ", "%20");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void cargarSpinnerPais() {
        ArrayAdapter<CharSequence> opcionesSpCat = ArrayAdapter.createFromResource(this, R.array.paises, android.R.layout.simple_spinner_item);
        spPais.setAdapter(opcionesSpCat);

        spPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pais = parent.getItemAtPosition(position).toString().replaceAll(" ", "%20");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void CargarCompetencias(String URL) {
        if (listCompetencias != null)
            listCompetencias.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Hacer el string a json array object
                            JSONArray array = new JSONArray(response);

                            //Recorremos con un for lo que tiene el array
                            for (int i = 0; i < array.length(); i++) {
                                //Obtenemos los objetos tipo competencias del array
                                JSONObject competencia = array.getJSONObject(i);
                                id = new int[array.length()];

                                //Añadir valores a los correspondientes textview
                                listCompetencias.add(new Competencias(
                                        competencia.getInt("id_competencia"),
                                        competencia.getString("nom_comp"),
                                        competencia.getString("descripcion"),
                                        competencia.getString("precio"),
                                        competencia.getString("foto")
                                ));
                                id[i] = listCompetencias.get(i).getId();
                            }

                            //Creamos instancia del adapter
                            adapter = new MyAdapter(busqueda_competidor.this, listCompetencias, new MyAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(int position) {
                                    //Log.i("Position", "el valor de la posición es: "+ competenciasList.get(position).getId());
                                    String idS = new String("" + listCompetencias.get(position).getId());
                                    launchCompetenciaView(idS);
                                }
                            });
                            rvFiltro.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void launchCompetenciaView(String id) {
        Intent intent = new Intent(busqueda_competidor.this, carrera_vista1.class);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}


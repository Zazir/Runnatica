package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.BarChart;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class vista1_organizador extends AppCompatActivity {
    BarChart graficaBarras;
    List<String> ListaFechas;
    Button ListaInscritos, FotosResultados, btnEditarCompetencia, PosponerEliminarCompetencia;
    TextView txtVendidasUsuarios, txtTotalUsuarios, txtVendidosForaneos, txtTotalForaneos, txtIngresoTotal;
    BottomNavigationView MenuOrganizador;


    private String id_competencia;
    private StringRequest request;
    private int precio, vendidosUsuario, vendidosForaneo;

    private String dominio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista1_organizador);

        ListaInscritos = (Button)findViewById(R.id.btnListaInscritos);
        FotosResultados = (Button)findViewById(R.id.btnFotosResultados);
        txtTotalUsuarios = (TextView)findViewById(R.id.tvTotalInscripciones);
        txtVendidasUsuarios = (TextView)findViewById(R.id.tvInscripcionesVendidas);
        txtVendidosForaneos = (TextView)findViewById(R.id.tvForaneasVendidas);
        txtTotalForaneos = (TextView)findViewById(R.id.tvTotalForaneos);
        txtIngresoTotal = (TextView)findViewById(R.id.ingreso);
        btnEditarCompetencia = (Button)findViewById(R.id.btnEditarCompetencia);
        PosponerEliminarCompetencia = (Button)findViewById(R.id.btnPosponerEliminarCompetencia);
        getLastViewData();

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
                if (menuItem.getItemId() == R.id.menu_regresar) {
                    home();
                }

                return true;
            }
        });

        ListaFechas = new ArrayList<>();
        //ObtenerTabla(dominio + "obtenerDatosTabla.php?id_competencia="+id_competencia);


        ListaInscritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaInscritos();
            }
        });

        FotosResultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FotosResultados();
            }
        });

        btnEditarCompetencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToEditarCompetencia();
            }
        });

        PosponerEliminarCompetencia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToPosponerCompetencia();
            }
        });
    }

    private void getLastViewData() {
        dominio = getString(R.string.ip);
        boolean flag;

        Bundle extra = vista1_organizador.this.getIntent().getExtras();
        id_competencia = extra.getString("id");
        precio = extra.getInt("precio");
        flag = extra.getBoolean("resultados");

        if (flag) {
            FotosResultados.setEnabled(flag);
        }else {
            FotosResultados.setEnabled(flag);
        }

        consultarTotalInscripciones();
        consultarInscritos();
    }

    /*private void ObtenerTabla(String url) {

        StringRequest Datos = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            JSONArray Arreglo = new JSONArray(response);
                            for(int a= 0; a < Arreglo.length();a++){

                                JSONObject Valor = Arreglo.getJSONObject(a);
                                ListaFechas.add(Valor.getString("f_inscripciones"));
                            }
                            LlenarTabla();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(vista1_organizador.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(vista1_organizador.this).add(Datos);
    }*/

    //private void LlenarTabla(){
        //List<BarEntry> entradas = new ArrayList<>();
       // int Contador = 0;
        //String temp = ListaFechas.get(0);
        //Hashtable<String, Integer> fechasRepetidas = new Hashtable<>();

        /*fechasRepetidas.put(ListaFechas.get(0), Contador);

        for (int i=0 ; i <= TamanoLista ; i++) {
            if (fechasRepetidas.containsKey(ListaFechas.get(i)) == ) {

            }
        }*/

        //for(int b = 0 ; b < ListaFechas.size() ; b++){
            //Log.i("Lista_valor", ListaFechas.get(b));
            //Log.i("Lista_tempora", temp);

            //if(temp.equals(ListaFechas.get(b))){
               // Contador++;
           // }else{
               // entradas.add(new BarEntry(10f,Contador));
               // temp = ListaFechas.get(b);
                //Contador = 0;
           // }
       // }
        //Log.i("Ultima_barra", String.valueOf(Contador));
        //entradas.add(new BarEntry(2,Contador));


        /*entradas.add(new BarEntry(1,2));
        entradas.add(new BarEntry(5,3));
        entradas.add(new BarEntry(6,5));*/

   // BarDataSet datos = new BarDataSet(entradas, "Grafica de Barras");
        //BarData data = new BarData(datos);

    //}

    private void consultarInscritos() {
        String URL = dominio + "obtenerDatosCompetencia.php?id_competencia="+id_competencia+"&consulta=1";
        request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        txtVendidasUsuarios.setText(response);
                        try {
                            vendidosUsuario = Integer.parseInt(response);
                        }catch (Exception e) {}
                        consultarForaneosInscritos();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(vista1_organizador.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }

    private void consultarForaneosInscritos() {
        String URL = dominio + "obtenerDatosCompetencia.php?id_competencia="+id_competencia+"&consulta=2";
        request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        txtVendidosForaneos.setText(response);
                        try {
                            vendidosForaneo = Integer.parseInt(response);
                        }catch (Exception e) {}
                        int TotalGanancia = (precio * vendidosForaneo) + (precio * vendidosUsuario);
                        txtIngresoTotal.setText(String.valueOf(TotalGanancia));
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(vista1_organizador.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }

    private void consultarTotalInscripciones() {
        String URL = dominio + "obtenerDatosCompetencia.php?id_competencia="+id_competencia+"&consulta=3";
        request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray res = new JSONArray(response);
                            JSONObject totalInscripciones = res.getJSONObject(0);

                            txtTotalUsuarios.setText(totalInscripciones.optString("Total_usuarios"));
                            txtTotalForaneos.setText(totalInscripciones.optString("Total_foraneos"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //txtTotalUsuarios.setText();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(vista1_organizador.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }

    private void ListaInscritos(){
        Intent next = new Intent(this, lista_inscritos.class);
        next.putExtra("id_competencia", id_competencia);
        startActivity(next);
    }

    void FotosResultados(){
        Intent next = new Intent(this, Subir_Resultados.class);
        next.putExtra("id_competencia", id_competencia);
        startActivity(next);
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

    private void home(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }

    private void goToEditarCompetencia() {
        Intent intent = new Intent(this, editar_competencia2.class);
        intent.putExtra("id_competencia", id_competencia);
        startActivity(intent);
    }

    private void goToPosponerCompetencia() {
        Intent intent = new Intent(this, editar_competencia.class);
        intent.putExtra("id_competencia", id_competencia);
        startActivity(intent);
    }
}
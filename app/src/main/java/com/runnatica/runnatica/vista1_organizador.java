package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class vista1_organizador extends AppCompatActivity {
    BarChart graficaBarras;
    List<String> ListaFechas;
    Button ListaInscritos, FotosResultados;
    TextView txtVendidasUsuarios, txtTotalUsuarios, txtVendidosForaneos, txtTotalForaneos, txtIngresoTotal;

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
        getLastViewData();

        dominio = getString(R.string.ip);

        ListaFechas = new ArrayList<>();
        ObtenerTabla(dominio + "obtenerDatosTabla.php?id_competencia="+id_competencia);


        ListaInscritos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ListaInscritos();
            }
        });

        FotosResultados.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void getLastViewData() {
        Bundle extra = vista1_organizador.this.getIntent().getExtras();
        id_competencia = extra.getString("id");
        precio = extra.getInt("precio");

        consultarTotalInscripciones();
        consultarInscritos();
    }

    private void ObtenerTabla(String url) {

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
    }

    private void LlenarTabla(){
        List<BarEntry> entradas = new ArrayList<>();
        int Contador = 0;
        String temp = ListaFechas.get(0);
        //Hashtable<String, Integer> fechasRepetidas = new Hashtable<>();

        /*fechasRepetidas.put(ListaFechas.get(0), Contador);

        for (int i=0 ; i <= TamanoLista ; i++) {
            if (fechasRepetidas.containsKey(ListaFechas.get(i)) == ) {

            }
        }*/

        for(int b = 0 ; b < ListaFechas.size() ; b++){
            Log.i("Lista_valor", ListaFechas.get(b));
            Log.i("Lista_tempora", temp);

            if(temp.equals(ListaFechas.get(b))){
                Contador++;
            }else{
                entradas.add(new BarEntry(10f,Contador));
                temp = ListaFechas.get(b);
                Contador = 0;
            }
<<<<<<< Updated upstream
        }
        Log.i("Ultima_barra", String.valueOf(Contador));
        entradas.add(new BarEntry(2,Contador));
=======
        }*/

        /*entradas.add(new BarEntry(1,2));
        entradas.add(new BarEntry(5,3));
        entradas.add(new BarEntry(6,5));*/
>>>>>>> Stashed changes

        BarDataSet datos = new BarDataSet(entradas, "Grafica de Barras");
        BarData data = new BarData(datos);

    }

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
        startActivity(next);
    }
}
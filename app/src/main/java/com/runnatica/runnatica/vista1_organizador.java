package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
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

    int TamañoLista = 0;

    List<String> ListaFechas;

    Button ListaInscritos, FotosResultados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vista1_organizador);

        ListaInscritos = (Button)findViewById(R.id.btnListaInscritos);
        FotosResultados = (Button)findViewById(R.id.btnFotosResultados);
        graficaBarras = findViewById(R.id.graficaBarras);

        ListaFechas = new ArrayList<>();
        ObtenerTabla("https://runnatica.000webhostapp.com/WebServiceRunnatica/obtenerDatosTabla.php?id_competencia=68");


        /*List<BarEntry> entradas = new ArrayList<>();
        entradas.add(new BarEntry(0f,2));
        entradas.add(new BarEntry(0f,2));
        entradas.add(new BarEntry(0f,2));*/



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
                                TamañoLista++;

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
        String temp = "";

        /*for(int b = 0 ; b <= TamañoLista; b++){


            if(ListaFechas.get(b).equals(ListaFechas.get(b++))){
                //temp = ListaFechas.get(b);
                Contador++;
            }else{
                entradas.add(new BarEntry(10,Contador));
                Contador = 0;
            }


        }*/

        entradas.add(new BarEntry(1,2));
        entradas.add(new BarEntry(5,3));
        entradas.add(new BarEntry(6,5));

        BarDataSet datos = new BarDataSet(entradas, "Grafica de Barras");
        BarData data = new BarData(datos);


        data.setBarWidth(0.9f);
        graficaBarras.setData(data);

        graficaBarras.setFitBars(true);

        graficaBarras.invalidate();
    }

    void ListaInscritos(){
        Intent next = new Intent(this, lista_inscritos.class);
        startActivity(next);
    }
    void FotosResultados(){
        Intent next = new Intent(this, Subir_Resultados.class);
        startActivity(next);
    }
}
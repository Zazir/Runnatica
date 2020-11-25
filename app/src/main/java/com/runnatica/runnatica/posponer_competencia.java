package com.runnatica.runnatica;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;

public class posponer_competencia extends AppCompatActivity {

    private Button btnDate, btnPosponer, btnCancelar, btnHora;
    private TextView txtFecha, txtFechaPosponer, txtHoraPosponer;

    private String id_competencia;


    BottomNavigationView MenuOrganizador;

    private Calendar calendar = Calendar.getInstance();
    int dia = calendar.get(Calendar.DAY_OF_MONTH);
    int mes = calendar.get(Calendar.MONTH);
    int ano = calendar.get(Calendar.YEAR);

    private DatePickerDialog picker;
    private String fecha;
    private TimePickerDialog timePicker;
    private String dominio;
    private String  hora="";
    private int DiaM, MesM, AnoM, HoraM, MinutosM;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posponer_competencia);

        dominio = getString(R.string.ip);

        btnDate = (Button)findViewById(R.id.btnPosponerFecha);
        btnPosponer = (Button)findViewById(R.id.btnActualizar);
        btnCancelar = (Button)findViewById(R.id.btnActualizarC);
        txtFecha = (TextView)findViewById(R.id.tvFechaActual);
        txtFechaPosponer = (TextView)findViewById(R.id.tvFechaPosponer);
        txtHoraPosponer = (TextView)findViewById(R.id.tvHoraPosponer);

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

        getLastViewData();
        obtenerFechaCompetencia(dominio + "obtenerCompetencia.php?idCompe=" + id_competencia);

        txtFecha.setText(dia+"/"+(mes+1)+"/"+ano);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hora();
                Fecha();
            }
        });

        btnPosponer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (fecha.equals("") || hora.equals("")) {
                        btnDate.setError("No se ha seleccionado una fecha correcta");
                    } else {
                        launchPosponer();
                    }
                }catch(Exception e){
                    Toast.makeText(getApplicationContext(), "Error en la fecha, Intentalo de nuevo", Toast.LENGTH_LONG).show();
                }
            }
        });

        btnCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchCancelar();
            }
        });
    }

    private void getLastViewData() {
        Bundle extra = posponer_competencia.this.getIntent().getExtras();
        assert extra != null;
        id_competencia = extra.getString("id_competencia");
    }

    private void posponerCompetencia(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Pospuesta")) {
                            Toast.makeText(getApplicationContext(), "La competencia ha sido pospuesta exitosamente, le informaremos a tus competidores", Toast.LENGTH_LONG).show();
                            Salir();
                        }else if (response.equals("Error en la operacion"))
                            Toast.makeText(getApplicationContext(), "Error al procesar la petición", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void cancelarCompetencia(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Cancelada")) {
                            Toast.makeText(getApplicationContext(), "La competencia ha sido cancelada exitosamente, le informaremos a tus competidores", Toast.LENGTH_LONG).show();
                            Salir();
                        }else if (response.equals("Error en la operacion"))
                            Toast.makeText(getApplicationContext(), "Error al procesar la petición", Toast.LENGTH_LONG).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void launchPosponer() {

        AlertDialog.Builder alerta = new AlertDialog.Builder(posponer_competencia.this);
        alerta.setTitle("Posponer competencia");
        alerta.setMessage("¿Seguro que quieres hacer esto?");
        alerta.setPositiveButton("Posponer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                posponerCompetencia(dominio + "actualizarCompetencia.php?" +
                        "id_competencia=" + id_competencia +
                        "&fecha_posponer=" + fecha +" " + hora +":00"+
                        "&operacion=1");
            }
        });
        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    txtFechaPosponer.setText("Posponer competencia");
                    fecha = "";
                    hora = "";
            }
        });

        alerta.show();
    }

    private void launchCancelar() {

        AlertDialog.Builder alerta = new AlertDialog.Builder(posponer_competencia.this);
        alerta.setTitle("Cancelar competencia");
        alerta.setMessage("Es una operación irreversible");
        alerta.setPositiveButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelarCompetencia(dominio + "actualizarCompetencia.php?" +
                        "id_competencia=" + id_competencia +
                        "&fecha_posponer=" + fecha +
                        "&operacion=3");
                Salir();
                Toast.makeText(getApplicationContext(), "La carrera se ha eliminado satisfactoriamente", Toast.LENGTH_SHORT).show();
            }
        });
        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txtFechaPosponer.setText("Posponer competencia");
                fecha = "";
                hora= "";
            }
        });

        alerta.show();
    }

    private void obtenerFechaCompetencia(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject respuesta = jsonArray.getJSONObject(0);

                            txtFecha.setText(respuesta.optString("fecha"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void Fecha(){
        picker = new DatePickerDialog(posponer_competencia.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                fecha = year + "-" + (month+1) + "-" + dayOfMonth;
                txtFechaPosponer.setText("Se pospondrá al: " + dayOfMonth + "/" + (month+1) + "/" + year);
            }

        }, ano, mes, dia);

        picker.getDatePicker().setMinDate(System.currentTimeMillis() +100050000);
        picker.show();
        picker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        picker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
    }
    private void Hora(){

        calendar = Calendar.getInstance();
        int horas = calendar.get(Calendar.HOUR_OF_DAY);
        int minuto = calendar.get(Calendar.MINUTE);

        timePicker = new TimePickerDialog(posponer_competencia.this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                hora = hourOfDay + ":" + minute;
                txtHoraPosponer.setText("A las " + hora);
            }
        }, horas, minuto, true);

        timePicker.show();
        timePicker.getButton(DatePickerDialog.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
        timePicker.getButton(DatePickerDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
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

    private void Salir(){
        Intent next = new Intent(this, home_organizador.class);
        startActivity(next);
        finish();
    }
}
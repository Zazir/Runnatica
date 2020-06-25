package com.runnatica.runnatica;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;

public class posponer_competencia extends AppCompatActivity {

    private Button btnDate, btnPosponer, btnCancelar;
    private EditText Hora;
    private TextView txtFecha, txtFechaPosponer;

    private String id_competencia;

    private Calendar calendar = Calendar.getInstance();
    int dia = calendar.get(Calendar.DAY_OF_MONTH);
    int mes = calendar.get(Calendar.MONTH);
    int ano = calendar.get(Calendar.YEAR);
    private DatePickerDialog picker;
    private String fecha;

    private String dominio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_posponer_competencia);

        btnDate = (Button)findViewById(R.id.btnPosponerFecha);
        btnPosponer = (Button)findViewById(R.id.btnActualizar);
        btnCancelar = (Button)findViewById(R.id.btnActualizarC);
        Hora = (EditText)findViewById(R.id.etHora);
        txtFecha = (TextView)findViewById(R.id.tvFechaActual);
        txtFechaPosponer = (TextView)findViewById(R.id.tvFechaPosponer);

        getLastViewData();

        txtFecha.setText(dia+"/"+(mes+1)+"/"+ano);
        dominio = getString(R.string.ip);

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                picker = new DatePickerDialog(posponer_competencia.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        fecha = year + "-" + (month+1) + "-" + dayOfMonth;
                        txtFechaPosponer.setText("Se pospondrá al " + dayOfMonth + "-" + (month+1) + "-" + year);
                    }
                }, ano, mes, dia);

                picker.show();
            }
        });

        btnPosponer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchPosponer();
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
        id_competencia = extra.getString("id");
    }

    private void posponerCompetencia(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Pospuesta"))
                            Toast.makeText(getApplicationContext(), "La competencia ha sido pospuesta exitosamente, le informaremos a tus competidores", Toast.LENGTH_LONG).show();
                        else if (response.equals("Error en la operacion"))
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
                        if (response.equals("Cancelada"))
                            Toast.makeText(getApplicationContext(), "La competencia ha sido cancelada exitosamente, le informaremos a tus competidores", Toast.LENGTH_LONG).show();
                        else if (response.equals("Error en la operacion"))
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
                        "&fecha_posponer=" + fecha +
                        "&operacion=1");
            }
        });
        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    txtFechaPosponer.setText("Posponer competencia");
                    fecha = "";
            }
        });

        alerta.show();
    }

    private void launchCancelar() {
        final CharSequence[] opciones = {"Aceptar", "Cancelar"};

        AlertDialog.Builder alerta = new AlertDialog.Builder(posponer_competencia.this);
        alerta.setTitle("Cancelar competencia");
        alerta.setMessage("Es una operación irreversible");
        alerta.setPositiveButton("Posponer", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cancelarCompetencia(dominio + "actualizarCompetencia.php?" +
                        "id_competencia=" + id_competencia +
                        "&fecha_posponer=" + fecha +
                        "&operacion=2");
            }
        });
        alerta.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                txtFechaPosponer.setText("Posponer competencia");
                fecha = "";
            }
        });

        alerta.show();
    }
}
package com.runnatica.runnatica;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Calendar;
import java.util.regex.Pattern;

public class registro_usuario extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //Mínimo 1 digito
                    "(?=.*[a-z])" +         //Mínimo 1 minúscula
                    "(?=.*[A-Z])" +         //Mínimo 1 mayúscula
                    ".{8,}" +               //Mínimo 8 caracteres
                    "$");

    Button Hombre, Mujer, Foto, Registrarse, Fecha;
    EditText Nombre, Ciudad, Estado, Pais;
    CheckBox Terminos;
    TextView Condiciones, MostrarFecha;
    int flagTerminos = 0, flagFecha = 0;
    private String genero = "";
    private String dominio, Correo, Contrasena;
    private String FechaNacimiento;
    private Calendar calendar;
    private DatePickerDialog picker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        dominio = getString(R.string.ip);

        //Enlaces de elementos por id's
        Hombre = (Button)findViewById(R.id.btnHombre);
        Mujer = (Button)findViewById(R.id.btnMujer);
        Foto = (Button)findViewById(R.id.btnRegistrarse);
        Registrarse = (Button)findViewById(R.id.btnRegistrarse);
        Terminos = (CheckBox)findViewById(R.id.checkTerminos);
        Nombre = (EditText)findViewById(R.id.etNombre);
        Fecha = (Button) findViewById(R.id.btnSeleccionNacimiento);
        MostrarFecha = (TextView) findViewById(R.id.tvMostrarFecha);
        Condiciones = (TextView)findViewById(R.id.tvCondiciones);
        Ciudad = (EditText)findViewById(R.id.etCiudad);
        Estado = (EditText)findViewById(R.id.etEstado);
        Pais = (EditText)findViewById(R.id.etPais);
        getLastViewData();

        Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Validaciones())
                SubirUsuario(dominio + "agregarUsuario.php?" +
                        "NombreYApellido=" + Nombre.getText().toString().replaceAll(" ", "%20") +
                        "&Email=" + Correo +
                        "&Contrasena=" + Contrasena +
                        "&Sexo=" + genero +
                        "&FechaNacimiento=" + FechaNacimiento +
                        "&Telefono=0" +
                        "&Terminos=" + flagTerminos +
                        "&Ciudad=" + Ciudad.getText().toString().replaceAll(" ", "%20") +
                        "&Estado=" + Estado.getText().toString().replaceAll(" ", "%20") +
                        "&Pais=" + Pais.getText().toString().replaceAll(" ", "%20"));
                else
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();


            }
        });

        Terminos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagTerminos = 1;
                Toast.makeText(registro_usuario.this, "Aceptaste los Terminos y condiciones" , Toast.LENGTH_SHORT).show();
            }
        });

        Fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                int dia = calendar.get(Calendar.DAY_OF_MONTH);
                int mes = calendar.get(Calendar.MONTH);
                int ano = calendar.get(Calendar.YEAR);

                picker = new DatePickerDialog(registro_usuario.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        FechaNacimiento = year + "-" + (month+1) + "-" + dayOfMonth;
                        MostrarFecha.setText(FechaNacimiento);
                        flagFecha = 1;
                    }
                }, ano, mes, dia);

                picker.show();

            }
        });

        Hombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genero = "Hombre";
                Toast.makeText(getApplicationContext(), "Eres un atlético " + genero, Toast.LENGTH_SHORT).show();
                Mujer.setBackgroundColor(Color.GRAY);
                Hombre.setBackgroundColor(Color.RED);
            }
        });

        Mujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genero = "Mujer";
                Toast.makeText(getApplicationContext(), "Eres una " + genero + " muy fit", Toast.LENGTH_SHORT).show();
                Hombre.setBackgroundColor(Color.GRAY);
                Mujer.setBackgroundColor(Color.RED);
            }
        });

        Condiciones.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), terminosycondiciones.class);
                startActivity(i);
            }
        });
    }

    /*
    * Función diseñada para mandar
    * */
    private void SubirUsuario(String URL) {
        //stringRequest es el objeto el cual almacena los datos.
        //Declaramos un StringRequest definiendo el método que utilizamos, en este caso es GET
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {//Aqui recibimos un objeto como parametro, es la respuesta a un listener
            @Override
            public void onResponse(String response) {//Operacion exitosa a travez del web service
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
                alHome();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {// Cuando hay un problema en la conexion.
                Toast.makeText(getApplicationContext(), "Hubo un error" + error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private Boolean Validaciones() {
        Boolean siguiente = false;

        if (Nombre.getText().toString().length() <= 0){
            Nombre.setError("Debes poner tu nombre");
        }else if (genero.length() == 0){
            Toast.makeText(this, "Selecciona tu sexo", Toast.LENGTH_SHORT).show();
        }else if (flagTerminos == 0){
            Terminos.setError("Debes de Aceptar terminos y condiciones ");
        }else if (flagFecha == 0){
             Fecha.setError("Debes de seleccionar una fecha");
        }else if (Ciudad.getText().toString().length() <= 0) {
            Ciudad.setError("Agrega tu ciudad");
        }else if (Estado.getText().toString().length() <= 0){
            Estado.setError("Agrega tu estado");
        }else if (Pais.getText().toString().length() <= 0){
            Pais.setError("Agrega tu pais");
        }else siguiente = true;

        return siguiente;
    }
    private void getLastViewData() {
        Bundle extra = registro_usuario.this.getIntent().getExtras();
        Correo = extra.getString("Correo");
        Contrasena = extra.getString("Contrasena");
    }

    private void alHome(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }
}

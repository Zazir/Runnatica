package com.runnatica.runnatica;

import android.support.v7.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class registro_usuario extends AppCompatActivity {

    Button Hombre, Mujer, Foto, Registrarse;
    EditText Nombre, Correo, Contrasena, contrasena2, Ciudad, Estado, Pais, Dia, Mesedt, Ano;
    CheckBox Terminos;
    TextView Condiciones;
    private String flagTerminos = "0";
    private String genero = "";
    private String fechaDeNacimiento = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_usuario);

        //Enlaces de elementos por id's
        Hombre = (Button)findViewById(R.id.btnHombre);
        Mujer = (Button)findViewById(R.id.btnMujer);
        Foto = (Button)findViewById(R.id.btnRegistrarse);
        Registrarse = (Button)findViewById(R.id.btnRegistrarse);
        Terminos = (CheckBox)findViewById(R.id.checkTerminos);
        Nombre = (EditText)findViewById(R.id.etNombre);
        Correo = (EditText)findViewById(R.id.etCorreo);
        Contrasena = (EditText)findViewById(R.id.etContrasena);
        contrasena2 = (EditText)findViewById(R.id.etContrasenaseguridad);
        Dia = (EditText)findViewById(R.id.etDia);
        Mesedt = (EditText)findViewById(R.id.Mes);
        Ano = (EditText)findViewById(R.id.etAno);
        Condiciones = (TextView)findViewById(R.id.tvCondiciones);
        Ciudad = (EditText)findViewById(R.id.etCiudad);
        Estado = (EditText)findViewById(R.id.etEstado);
        Pais = (EditText)findViewById(R.id.etPais);

        Registrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Terminos.equals(1)){
                    flagTerminos = "1";
                }
                fechaDeNacimiento = fechaNacimiento();SubirUsuario("http://192.168.137.1:811/login/agregarUsuario.php");

            }
        });

        Hombre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genero = "Hombre";
                Toast.makeText(getApplicationContext(), "Eres un atlético " + genero, Toast.LENGTH_SHORT).show();
            }
        });

        Mujer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                genero = "Mujer";
                Toast.makeText(getApplicationContext(), "Eres una " + genero + " muy fit", Toast.LENGTH_SHORT).show();
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
        //Declaramos un StringRequest definiendo el método que utilizamos, en este caso es GET
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Toast.makeText(getApplicationContext(), "OPERACION EXITOSA", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Hubo un error", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> parametros = new HashMap<String,String>();
                //Mapeo de los valores del usuario para mandarse a través de Volley
                parametros.put("NombreYApellido", Nombre.getText().toString());
                parametros.put("Email", Correo.getText().toString());
                parametros.put("Contrasena", Contrasena.getText().toString());
                parametros.put("Sexo", genero);
                parametros.put("FechaNacimiento", fechaDeNacimiento);
                parametros.put("Telefono", "0");
                parametros.put("Terminos", flagTerminos);
                parametros.put("Ciudad", Ciudad.getText().toString());
                parametros.put("Estado", Estado.getText().toString());
                parametros.put("Pais", Pais.getText().toString());

                return parametros;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String fechaNacimiento() {
        String date;
        return date = Dia.getText().toString() + Mesedt.getText().toString() + Ano.getText().toString();
    }
}

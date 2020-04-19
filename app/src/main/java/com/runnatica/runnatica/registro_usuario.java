package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.regex.Pattern;

public class registro_usuario extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    "(?=.*[0-9])" +         //Mínimo 1 digito
                    "(?=.*[a-z])" +         //Mínimo 1 minúscula
                    "(?=.*[A-Z])" +         //Mínimo 1 mayúscula
                    ".{8,}" +               //Mínimo 8 caracteres
                    "$");

    Button Hombre, Mujer, Foto, Registrarse;
    EditText Nombre, Correo, Contrasena, contrasena2, Ciudad, Estado, Pais, Dia, Mesedt, Ano;
    CheckBox Terminos;
    TextView Condiciones;
    private String flagTerminos = "0";
    private String genero = "";
    //private String fechaDeNacimiento = "";


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
                if (Validaciones())
                SubirUsuario("http://192.168.137.1:811/WebServiceRunnatica/agregarUsuario.php?" +
                        "NombreYApellido=" + Nombre.getText().toString() +
                        "&Email=" + Correo.getText().toString() +
                        "&Contrasena=" + Contrasena.getText().toString() +
                        "&Sexo=" + genero +
                        "&FechaNacimiento=" + fechaNacimiento() +
                        "&Telefono=0" +
                        "&Terminos=" + flagTerminos +
                        "&Ciudad=" + Ciudad.getText().toString() +
                        "&Estado=" + Estado.getText().toString() +
                        "&Pais=" + Pais.getText().toString());
                else
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(getApplicationContext(), "Eres una" + genero + " muy fit", Toast.LENGTH_SHORT).show();
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
                alHome();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Hubo un error" + error, Toast.LENGTH_SHORT).show();
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private String fechaNacimiento() {
        String date;
        return date = Dia.getText().toString() + Mesedt.getText().toString() + Ano.getText().toString();
    }

    private Boolean Validaciones() {
        Boolean siguiente = false;

        if (Nombre.getText().toString().length() <= 0){
            Nombre.setError("Debes poner tu nombre");
        }else if (!Patterns.EMAIL_ADDRESS.matcher(Correo.getText().toString()).matches()){
            Correo.setError("Ese no es un correo válido");
        }else if (!PASSWORD_PATTERN.matcher(Contrasena.getText().toString()).matches()){
            Contrasena.setError("La contraseña es debil");
        }else if (genero.length() == 0){
            Toast.makeText(this, "Selecciona tu sexo", Toast.LENGTH_SHORT).show();
        }else if (fechaNacimiento().length() != 8){
            if (Dia.getText().toString().length() != 2)
                Dia.setError("Formato del día: DD");
            else if (Mesedt.getText().toString().length() != 2)
                Mesedt.setError("Formato del mes: MM");
            else if (Ano.getText().toString().length() != 4)
                Ano.setError("Formato del año: YYYY");
            Toast.makeText(this, "Tienes un error en tu fecha de nacimiento", Toast.LENGTH_SHORT).show();
        }/*else if (flagTerminos == "0"){
            Toast.makeText(this, "Tienes que aceptar los términos y condiciones para acceder", Toast.LENGTH_SHORT).show();
        }*/else if (Ciudad.getText().toString().length() <= 0) {
            Ciudad.setError("Agrega tu ciudad");
        }else if (Estado.getText().toString().length() <= 0){
            Estado.setError("Agrega tu estado");
        }else if (Pais.getText().toString().length() <= 0){
            Pais.setError("Agrega tu pais");
        }else siguiente = true;

        return siguiente;
    }

    private void alHome() {
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }
}

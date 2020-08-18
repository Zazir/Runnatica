package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class registro_basico extends AppCompatActivity {


    EditText Correo, Contrasena, VerificarContrasena;
    Button Siguiente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro_basico);

        Correo = (EditText)findViewById(R.id.etUsuario);
        Siguiente = (Button)findViewById(R.id.btnSiguiente);

        Siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Validaciones()){
                    ValidarCorreo();
                }else{
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void ValidarCorreo(){
        String dominio = getString(R.string.ip);
        String url = dominio + "sesion.php?correo=" + Correo.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.equals("Existe")) {
                    Correo.setError("Este correo ya esta Registrado");
                    Toast.makeText(getApplicationContext(), "Este correo ya esta Registrado en Runnatica", Toast.LENGTH_SHORT).show();
                    return;
                }else if(response.equals("Noexiste")) {
                    Intent intent = new Intent(registro_basico.this, verificar_correo.class);
                    intent.putExtra("Correo", Correo.getText().toString());
                    startActivity(intent);
                    finish();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Hubo un problema "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private boolean Validaciones(){
        Boolean siguiente = false;

        if (!Patterns.EMAIL_ADDRESS.matcher(Correo.getText().toString()).matches()){
            Correo.setError("Ese no es un correo válido");
        }else{
            siguiente = true;
        }
        return siguiente;
    }
}
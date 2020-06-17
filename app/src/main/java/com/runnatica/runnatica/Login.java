package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {
    Button Entrar, Registro;
    EditText Usuariotxt, Contrasenatxt;
    RequestQueue rq;
    JsonRequest jrq;
    Usuario user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Entrar = (Button) findViewById(R.id.btnEntrar);
        Registro= (Button) findViewById(R.id.btnRegistrarse);
        Usuariotxt= (EditText) findViewById(R.id.etUsuario);
        Contrasenatxt= (EditText) findViewById(R.id.etContrasena);
        rq = Volley.newRequestQueue(this);

        Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iniciarSesion();
            }
        });

        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), registro_usuario.class);
                startActivity(i);
            }
        });

    }

    private void iniciarSesion() {
        String url = "https://runnatica.000webhostapp.com/WebServiceRunnatica/sesion.php?user="+Usuariotxt.getText().toString()+"&pwd="+Contrasenatxt.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                user = Usuario.getUsuarioInstance();
                Toast.makeText(getApplicationContext(), "Bienvenido "+Usuariotxt.getText().toString(), Toast.LENGTH_SHORT).show();
                try {
                    JSONArray jsonarray = new JSONArray(response);
                    JSONObject jsonobject;
                    /*if (jsonarray.get(0).equals("[")){
                        Toast.makeText(getApplicationContext(), "Error en las credenciales", Toast.LENGTH_SHORT).show();
                    }else if (jsonarray.length() > 1){*/
                        jsonobject = jsonarray.getJSONObject(0);
                        user.setId(Integer.parseInt(jsonobject.getString("id_usuarios")));
                        user.setTipoUsuario(jsonobject.optString("tipo_usr"));
                        user.setFechaNacimiento(jsonobject.optInt("f_nacimiento"));
                        user.setCorreo(jsonobject.optString("correo"));
                        user.setNombre(jsonobject.optString("nombre"));
                    //}
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                alHome();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Hubo un problema "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void alHome() {
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }
}

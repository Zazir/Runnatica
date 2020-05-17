package com.runnatica.runnatica;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity implements Response.Listener<JSONObject>, Response.ErrorListener {
    Button Entrar, Registro;
    EditText Usuariotxt, Contrasenatxt;
    RequestQueue rq;
    JsonRequest jrq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Entrar = (Button) findViewById(R.id.btnEntrar);
        Registro= (Button) findViewById(R.id.btnRegistrarse);
        Usuariotxt= (EditText) findViewById(R.id.etUsuario);
        Contrasenatxt= (EditText) findViewById(R.id.etContraseña);
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

    @Override
    public void onResponse(JSONObject response) {
        Usuario user = new Usuario();
        Toast.makeText(getApplicationContext(), "Bienvenido "+Usuariotxt.getText().toString(), Toast.LENGTH_SHORT).show();

        JSONArray jsonarray = response.optJSONArray("datos");
        JSONObject jsonobject;

        try {
            jsonobject = jsonarray.getJSONObject(0);
            user.setCorreo(jsonobject.optString("user"));
            //user.setContrasena(jsonobject.optString("pwd"));
            user.setNombre(jsonobject.optString("nombre"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Intent next = new Intent(this, home.class);
        startActivity(next);
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        Toast.makeText(getApplicationContext(), "Hubo un problema "+error.toString(), Toast.LENGTH_SHORT).show();
    }

    private void iniciarSesion() {
        String url = "https://runnatica.000webhostapp.com/WebServiceRunnatica/sesion.php?user="+Usuariotxt.getText().toString()+"&pwd="+Contrasenatxt.getText().toString();

        jrq = new JsonObjectRequest(Request.Method.GET, url, null, this, this);
        rq.add(jrq);
    }
}

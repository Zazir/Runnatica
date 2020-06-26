package com.runnatica.runnatica;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
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
    RadioButton rbSesion;
    Button Entrar, Registro;
    EditText Usuariotxt, Contrasenatxt;

    private RequestQueue rq;
    private JsonRequest jrq;
    private Usuario user = Usuario.getUsuarioInstance();
    private boolean flagRadio;
    private static final String ID_PREFERENCES_SESSION = "com.runnatica.runnatica";
    private static final String ESTADO_SESSION = "estado.boton";
    private static final String ID_USUARIO_SESSION = "id.usuario.session";
    private static final String NOMBRE_USUARIO_SESSION = "nombre.usuario.session";
    private static final String CORREO_SESSION = "correo.session";
    private static final String NACIMIENTO_USUARIO_SESSION = "fecha.nacimiento.session";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /*if (obtenerEstado()
                && user.getId() != 0
                && user.getNombre() != null
                && user.getFechaNacimiento() != 0
                && user.getCorreo() != null) {
            alHome();
            finish();
            Toast.makeText(getApplicationContext(), "Boton en true", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "Boton en false", Toast.LENGTH_SHORT).show();
        }*/

        Entrar = (Button) findViewById(R.id.btnEntrar);
        Registro= (Button) findViewById(R.id.btnRegistrarse);
        Usuariotxt= (EditText) findViewById(R.id.etUsuario);
        Contrasenatxt= (EditText) findViewById(R.id.etContrasena);
        rbSesion = (RadioButton)findViewById(R.id.radioSesion);
        rq = Volley.newRequestQueue(this);

        flagRadio = rbSesion.isChecked();

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

        rbSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flagRadio)
                    rbSesion.setChecked(false);
                flagRadio = rbSesion.isChecked();
            }
        });
    }

    private void iniciarSesion() {
        String url = "https://runnatica.000webhostapp.com/WebServiceRunnatica/sesion.php?user="+Usuariotxt.getText().toString()+"&pwd="+Contrasenatxt.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    JSONArray jsonarray = new JSONArray(response);
                    JSONObject jsonobject;
                    if (response.equals("[]")){
                        Toast.makeText(getApplicationContext(), "Error en las credenciales", Toast.LENGTH_SHORT).show();
                        return;
                    }else {
                        jsonobject = jsonarray.getJSONObject(0);
                        user.setId(Integer.parseInt(jsonobject.getString("id_usuarios")));
                        user.setTipoUsuario(jsonobject.optString("tipo_usr"));
                        user.setFechaNacimiento(jsonobject.optInt("f_nacimiento"));
                        user.setCorreo(jsonobject.optString("correo"));
                        user.setNombre(jsonobject.optString("nombre"));
                        //guardarEstado();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                alHome();
                finish();
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

    private void guardarEstado() {
        SharedPreferences preferences = getSharedPreferences(ID_PREFERENCES_SESSION, MODE_PRIVATE);
        preferences.edit().putBoolean(ESTADO_SESSION, rbSesion.isChecked()).apply();
        preferences.edit().putInt(ID_USUARIO_SESSION, user.getId()).apply();
        preferences.edit().putString(NOMBRE_USUARIO_SESSION, user.getNombre()).apply();
        preferences.edit().putString(CORREO_SESSION, user.getCorreo()).apply();
        preferences.edit().putInt(NACIMIENTO_USUARIO_SESSION, user.getFechaNacimiento()).apply();
    }

    private boolean obtenerEstado() {
        SharedPreferences preferences = getSharedPreferences(ID_PREFERENCES_SESSION, MODE_PRIVATE);
        return preferences.getBoolean(ESTADO_SESSION, false);
    }
}

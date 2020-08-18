package com.runnatica.runnatica;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    int RC_SIGN_IN = 0;
    RadioButton rbSesion;
    Button Entrar, Registro;
    EditText Usuariotxt, Contrasenatxt;
    SignInButton signInButton;
    String personEmail, personalName, UriURL;
    Uri personPhoto;
    GoogleSignInClient mGoogleSignClient;

    private RequestQueue rq;
    private JsonRequest jrq;
    private Usuario user = Usuario.getUsuarioInstance();
    private boolean flagRadio;
    public static final String ID_USUARIO_SESSION = "id.usuario.session";
    public static final String NOMBRE_USUARIO_SESSION = "nombre.usuario.session";
    public static final String CORREO_SESSION = "correo.session";
    public static final String NACIMIENTO_USUARIO_SESSION = "fecha.nacimiento.session";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        obtenerPreferencias();

        if (user.getId() != 0 && user.getCorreo() != "No_mail") {
            alHome();
            finish();
            Toast.makeText(getApplicationContext(), "Preferencias guardadas", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getApplicationContext(), "Preferencias no guardadas", Toast.LENGTH_SHORT).show();
        }

        Entrar = (Button) findViewById(R.id.btnEntrar);
        Registro= (Button) findViewById(R.id.btnRegistrarse);
        Usuariotxt= (EditText) findViewById(R.id.etUsuario);
        Contrasenatxt= (EditText) findViewById(R.id.etContrasena);
        rbSesion = (RadioButton)findViewById(R.id.radioSesion);
        rq = Volley.newRequestQueue(this);
        signInButton = findViewById(R.id.btnEntrarGoogle);

                flagRadio = rbSesion.isChecked();

        //Configuracion del perfil de google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();

        mGoogleSignClient = GoogleSignIn.getClient(this,gso);

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });

        Entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String usuario = getString(R.string.credencial_usuario);
                String contrasena = getString(R.string.credencial_contrasena);

                if (Usuariotxt.getText().toString().equals(usuario) && Contrasenatxt.getText().toString().equals(contrasena)) {
                    Administrador();
                } else
                    iniciarSesion();
            }
        });

        Registro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), registro_basico.class);
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
        String dominio = getString(R.string.ip);
        String url = dominio + "sesion.php?user="+Usuariotxt.getText().toString()+"&pwd="+Contrasenatxt.getText().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                if (response.length() <= 1) {
                    Toast.makeText(getApplicationContext(), "Error en las credenciales", Toast.LENGTH_SHORT).show();
                    return;
                }else if (response.contains("borrado")) {
                    alertaEliminado();
                    Toast.makeText(getApplicationContext(), "Tu usuario se borró por no subir los resultados de las competencias", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    try {
                        JSONArray jsonarray = new JSONArray(response);
                        JSONObject jsonobject;

                        jsonobject = jsonarray.getJSONObject(0);
                        user.setId(Integer.parseInt(jsonobject.getString("id_usuarios")));
                        user.setTipoUsuario(jsonobject.optString("tipo_usr"));
                        user.setFechaNacimiento(jsonobject.optString("f_nacimiento"));
                        user.setCorreo(jsonobject.optString("correo"));
                        user.setNombre(jsonobject.optString("nombre"));

                        guardarPreferencias();
                        alHome();
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        //Result returned from lauching the intent from GoogleSignInClient.getSignInIntent
        if(requestCode == RC_SIGN_IN){
            //The Task returnet from this call is always completed, no need to attach
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void alHome() {
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }

    private void guardarPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putInt(ID_USUARIO_SESSION, user.getId());
        editor.putString(NOMBRE_USUARIO_SESSION, user.getNombre());
        editor.putString(CORREO_SESSION, user.getCorreo());
        editor.putString(NACIMIENTO_USUARIO_SESSION, user.getFechaNacimiento());

        editor.commit();
    }

    private void obtenerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);

        user.setId(preferences.getInt(ID_USUARIO_SESSION, 0));
        user.setNombre(preferences.getString(NOMBRE_USUARIO_SESSION, "No_name"));
        user.setCorreo(preferences.getString(CORREO_SESSION, "No_mail"));
        user.setFechaNacimiento(preferences.getString(NACIMIENTO_USUARIO_SESSION, "0"));
    }

    private void Administrador() {
        Intent intent = new Intent(Login.this, VistaAdministrador.class);
        startActivity(intent);
    }

    private void alertaEliminado() {
        AlertDialog.Builder alerta = new AlertDialog.Builder(Login.this);

        alerta.setTitle("Cuenta Eliminada");
        alerta.setMessage("Razón: Por no subir los resultados de las competencias");

        alerta.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alerta.show();
    }
    private void signIn(){
        Intent signInIntent = mGoogleSignClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private void handleSignInResult(Task<GoogleSignInAccount> completedTask){

        try{
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            //Signed is succesfully. show authenticated.UI
            getCorreo();
            SiguienteGoogle();
        }catch (ApiException e){
            //The ApiException status code indicates the detailed falture reason
            Log.w("Google Sign In Error", "signInResults: failed code= "+ e.getStatusCode());
            Toast.makeText(Login.this, "Error al entrar", Toast.LENGTH_LONG).show();
        }
    }
    private void getCorreo(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Login.this);
        if(acct != null){
            personEmail = acct.getEmail();
            //Poner validacion de correo

        }
    }
    private void SiguienteGoogle(){
        GoogleSignInAccount acct = GoogleSignIn.getLastSignedInAccount(Login.this);
        personalName = acct.getDisplayName();
        personPhoto = acct.getPhotoUrl();
        UriURL = personPhoto.toString();

        Intent intent = new Intent(Login.this, RegistroMedio.class);
        intent.putExtra("Correo", personEmail);
        intent.putExtra("Nombre", personalName);
        intent.putExtra("Foto", UriURL);
        startActivity(intent);
        finish();
    }
}

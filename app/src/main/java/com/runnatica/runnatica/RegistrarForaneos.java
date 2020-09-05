package com.runnatica.runnatica;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.runnatica.runnatica.adapter.editarForaneoAdapter;
import com.runnatica.runnatica.poho.Usuario;
import com.runnatica.runnatica.poho.UsuarioForaneo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RegistrarForaneos extends AppCompatActivity {

    private EditText txtNombre, txtCorreo, txtEdad;
    private RadioButton rbHombre, rbMujer;
    private Button btnCrearForaneo;
    private RecyclerView rvForaneos;
    private BottomNavigationView MenuUsuario;

    private String id_competencia;
    private Usuario usuario = Usuario.getUsuarioInstance();
    private int index = 0;
    private String sexo, ip;
    private List<UsuarioForaneo> foraneoList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar_foraneos);
        txtNombre = (EditText)findViewById(R.id.etNombreForaneo);
        txtCorreo = (EditText)findViewById(R.id.etCorreoForaneo);
        txtEdad = (EditText)findViewById(R.id.etEdadForaneo);
        rbHombre = (RadioButton) findViewById(R.id.rbtnHombre);
        rbMujer = (RadioButton) findViewById(R.id.rbtnMujer);
        btnCrearForaneo = (Button)findViewById(R.id.btnCreateForaneo);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        rvForaneos = (RecyclerView) findViewById(R.id.rcvForaneos);
        rvForaneos.setHasFixedSize(true);
        rvForaneos.setLayoutManager(new LinearLayoutManager(this));

        //Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);
        //

        ip = getString(R.string.ip);
        foraneoList = new ArrayList<>();
        CargarForaneos(ip + "obtenerForaneosTabla.php?id_usuario=" + usuario.getId());
        //peticion();
        MenuUsuario.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                if (menuItem.getItemId() == R.id.menu_home) {
                    home();
                }
                if (menuItem.getItemId() == R.id.menu_busqueda) {
                    Busqueda();
                }
                if (menuItem.getItemId() == R.id.menu_historial) {
                    Historial();
                }
                if (menuItem.getItemId() == R.id.menu_ajustes) {
                    Ajustes();
                }

                return true;
            }
        });

        btnCrearForaneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Validaciones()){
                    setSexo();
                    crearUsuarioForaneo(ip + "agregarUsuarioForaneo.php?" +
                            "id_usuario=" + usuario.getId() +
                            "&nombre=" + txtNombre.getText().toString().replaceAll(" ", "%20") +
                            "&correo=" + txtCorreo.getText().toString() +
                            "&sexo=" + sexo +
                            "&edad=" + txtEdad.getText().toString());
                    Toast.makeText(getApplicationContext(), "El usuario Foraneo " + txtNombre.getText().toString() + "se ha registrado correctamente", Toast.LENGTH_SHORT).show();
                    txtNombre.setText("");
                    txtCorreo.setText("");
                    txtEdad.setText("");
                    rbHombre.setChecked(false);
                    rbMujer.setChecked(false);
                }else
                    Toast.makeText(getApplicationContext(), "Verifica los campos", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void setSexo() {
        if (rbMujer.isChecked())
            sexo = "Mujer";
        else if (rbHombre.isChecked())
            sexo = "Hombre";
    }

    private void crearUsuarioForaneo(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //llenarListView(response);
                        CargarForaneos(ip + "obtenerForaneosTabla.php?id_usuario=" + usuario.getId());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(RegistrarForaneos.this, "Error de conección con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private boolean Validaciones(){
        Boolean siguiente = false;
        String edadS= txtEdad.getText().toString();
        int EdadInt = Integer.parseInt(edadS);

        if (txtNombre.getText().toString().length() <= 0){
            txtNombre.setError("Debes poner un nombre");
        }else if (txtCorreo.getText().toString().length() <= 0){
            txtCorreo.setError("Debes poner un Correo");
        }else if (!Patterns.EMAIL_ADDRESS.matcher(txtCorreo.getText().toString()).matches()){
            txtCorreo.setError("Ese correo no es válido");
        }else if (txtEdad.length() == 0){
            txtEdad.setError("Debes poner una Edad");
        } else if (EdadInt <=3 || EdadInt >=90){
            txtEdad.setError("La edad no es valida");
        }else if (rbHombre.isChecked() || rbMujer.isChecked()){
            siguiente = true;
        }else siguiente = false;

        return siguiente;
    }

    private void CargarForaneos(String URL) {
        foraneoList.clear();

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Hacer el string a json array object
                            JSONArray array = new JSONArray(response);

                            //Recorremos con un for lo que tiene el array
                            for (int i = 0; i < array.length(); i++) {
                                //Obtenemos los objetos tipo competencias del array
                                JSONObject foraneo = array.getJSONObject(i);

                                //Añadir valores a los correspondientes textview
                                foraneoList.add(new UsuarioForaneo(
                                        foraneo.getInt("id_foraneo"),
                                        foraneo.getString("nombre"),
                                        foraneo.getString("correo"),
                                        "alguno",
                                        foraneo.getInt("edad")
                                ));
                            }

                            //Creamos instancia del adapter
                            editarForaneoAdapter adapterForaneo = new editarForaneoAdapter(RegistrarForaneos.this, foraneoList, new editarForaneoAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(int position) {
                                    String idS = new String("" + foraneoList.get(position).getId_foreign());
                                    String Nombre = new String("" + foraneoList.get(position).getNombre());
                                    String Correo = new String("" + foraneoList.get(position).getCorreon());
                                    String Edad = new String("" + foraneoList.get(position).getEdad());
                                    String Sexo = new String("" + foraneoList.get(position).getSexo());
                                    modificarForaneo(idS, Nombre, Correo, Edad, Sexo);
                                }
                            });

                            rvForaneos.setAdapter(adapterForaneo);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void modificarForaneo(String id_foreign, String Nombre, String Correo, String Edad, String Sexo) {
        Intent intent = new Intent(RegistrarForaneos.this, editarForaneos.class);
        intent.putExtra("id", id_foreign);
        intent.putExtra("Nombre", Nombre);
        intent.putExtra("Correo", Correo);
        intent.putExtra("Edad", Edad);
        intent.putExtra("Sexo", Sexo);

        startActivity(intent);
        finish();
        //Toast.makeText(RegistrarForaneos.this, id_foreign, Toast.LENGTH_SHORT).show();
    }

    private void home(){
        Intent next = new Intent(this, home.class);
        startActivity(next);
    }

    private void Busqueda(){
        Intent next = new Intent(this, busqueda_competidor.class);
        startActivity(next);
    }

    private void Historial(){
        Intent next = new Intent(this, historial_competidor.class);
        startActivity(next);
    }

    private void Ajustes(){
        Intent next = new Intent(this, ajustes_competidor.class);
        startActivity(next);
    }


}
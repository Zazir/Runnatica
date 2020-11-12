package com.runnatica.runnatica;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

public class ListaForaneos extends AppCompatActivity {

    private String sexo, ip;
    private EditText txtNombre, txtCorreo, txtEdad;
    private RadioButton rbHombre, rbMujer;
    private Button btnAgregarForaneo;
    private RecyclerView rvForaneos;
    private BottomNavigationView MenuUsuario;

    private String id_competencia;
    private Usuario usuario = Usuario.getUsuarioInstance();
    private int index = 0;
    private List<UsuarioForaneo> foraneoList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_foraneos);

        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        rvForaneos = (RecyclerView) findViewById(R.id.rcvForaneos);
        rvForaneos.setHasFixedSize(true);
        rvForaneos.setLayoutManager(new LinearLayoutManager(this));
        btnAgregarForaneo = (Button) findViewById(R.id.btnAgregarForaneo);
        txtNombre = (EditText)findViewById(R.id.etNombreForaneo);
        txtCorreo = (EditText)findViewById(R.id.etCorreoForaneo);
        txtEdad = (EditText)findViewById(R.id.etEdadForaneo);
        rbHombre = (RadioButton) findViewById(R.id.rbtnHombre);
        rbMujer = (RadioButton) findViewById(R.id.rbtnMujer);
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);

        //Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(3);
        menuItem.setChecked(true);
        //


        ip = getString(R.string.ip);
        CargarForaneos(ip + "obtenerForaneosTabla.php?id_usuario=" + usuario.getId());
        foraneoList = new ArrayList<>();

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

        btnAgregarForaneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgregarForaneo();
            }
        });

    }
    private void CargarForaneos(String URL) {
        //foraneoList.clear();

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
                            editarForaneoAdapter adapterForaneo = new editarForaneoAdapter(ListaForaneos.this, foraneoList, new editarForaneoAdapter.OnItemClickListener() {
                                @Override
                                public void OnItemClick(int position) {
                                    String idS = new String("" + foraneoList.get(position).getId_foreign());
                                    String Nombre = new String("" + foraneoList.get(position).getNombre());
                                    String Correo = new String("" + foraneoList.get(position).getCorreon());
                                    String Edad = new String("" + foraneoList.get(position).getEdad());
                                    String Sexo = new String("" + foraneoList.get(position).getSexo());
                                    SeleccionarOpcion(idS, Nombre, Correo, Edad, Sexo);
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
        Intent intent = new Intent(ListaForaneos.this, editarForaneos.class);
        intent.putExtra("id", id_foreign);
        intent.putExtra("Nombre", Nombre);
        intent.putExtra("Correo", Correo);
        intent.putExtra("Edad", Edad);
        intent.putExtra("Sexo", Sexo);

        startActivity(intent);
        finish();
        //Toast.makeText(RegistrarForaneos.this, id_foreign, Toast.LENGTH_SHORT).show();
    }
    private void SeleccionarOpcion(String idS, String Nombre, String Correo, String Edad, String Sexo){

        String[] opciones = {"Editar Usuario", "Eliminar Usuario"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("¿Que deseas hacer?");
        builder.setItems(opciones, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which == 0){
                    modificarForaneo(idS, Nombre, Correo, Edad, Sexo);
                }else if(which == 1){
                    EliminarForaneo(ip+"actualizarForaneo.php?id_foraneo="+idS);

                }
            }
        });
        builder.show();
    }
    private void EliminarForaneo(String URL){
        AlertDialog.Builder alertaComentario = new AlertDialog.Builder(this);
        alertaComentario.setTitle("Eliminar Usuario");
        alertaComentario.setMessage("Esta acción es irreversible");
        alertaComentario.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest request = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Exito")) {
                                    Toast.makeText(ListaForaneos.this, "Usuario Eliminado", Toast.LENGTH_SHORT).show();
                                    foraneoList.clear();
                                    CargarForaneos(ip + "obtenerForaneosTabla.php?id_usuario=" + usuario.getId());
                                }else if (response.equals("Error"))
                                    Toast.makeText(ListaForaneos.this, "Vuelve a intentarlo en un poco de tiempo", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ListaForaneos.this, "Hubo un error con el servidor", Toast.LENGTH_SHORT).show();
                            }
                        });
                Volley.newRequestQueue(getApplicationContext()).add(request);
            }
        });
        alertaComentario.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        alertaComentario.show();
    }

    private void AgregarForaneo(){
        Intent next = new Intent(this, RegistrarForaneos.class);
        startActivity(next);
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
package com.runnatica.runnatica;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.bumptech.glide.Glide;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.paypal.android.sdk.payments.PayPalService;
import com.runnatica.runnatica.adapter.comentariosAdapter;
import com.runnatica.runnatica.poho.Comentarios;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class carrera_vista1 extends AppCompatActivity implements OnMapReadyCallback {
    BottomNavigationView MenuUsuario;
    private ImageView imgCompetencia, imgAval, imgResultados;
    private TextView txtNomCompe, txtOrganizador, txtFechaCompe, txtHoraCompe,
            txtLugarCompe, txtPrecioCompe, txtDescripcionCompe, txtRegistrarse,
            txtComentario;
    private Button btnInscripcion, btnEnviarComentario;
    private Spinner spCategorias, spFiltro;
    private RecyclerView ForoRecycler;

    private List<Comentarios> comentariosList = new ArrayList<>();
    private Usuario user = Usuario.getUsuarioInstance();
    private String id_competencia="";
    private String id_organizador;

    private comentariosAdapter adaptador;

    private String monto;
    private String categoria;
    private String dominio;
    private String coordenadas, nombreCompe;

    private GoogleMap mMap;
    private MarkerOptions marker = new MarkerOptions();

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (googleServicesAvailable()) {
            setContentView(R.layout.activity_carrera_vista1);
        }

        spCategorias = (Spinner)findViewById(R.id.spRespuestaCategoria);
        spFiltro = (Spinner)findViewById(R.id.spForo);
        imgCompetencia = (ImageView)findViewById(R.id.ivFotoCompetencia);
        txtNomCompe = (TextView)findViewById(R.id.tvNombreCompetencia);
        txtOrganizador = (TextView)findViewById(R.id.tvNombreOrganizador);
        txtFechaCompe = (TextView)findViewById(R.id.tvFechaCompetencia);
        txtHoraCompe = (TextView)findViewById(R.id.tvHoraCompetencia);
        txtLugarCompe = (TextView)findViewById(R.id.tvLugarCompetencia);
        txtPrecioCompe = (TextView)findViewById(R.id.tvPrecioCompetencia);
        txtDescripcionCompe = (TextView)findViewById(R.id.tvDescripcion);
        btnInscripcion = (Button)findViewById(R.id.btnIncribirse);
        txtComentario = (TextView)findViewById(R.id.etRespuestaForo);
        btnEnviarComentario = (Button)findViewById(R.id.btnEnviarForo);
        ForoRecycler = (RecyclerView)findViewById(R.id.rvForo);
        ForoRecycler.setHasFixedSize(true);
        ForoRecycler.setLayoutManager(new LinearLayoutManager(this));
        MenuUsuario = (BottomNavigationView) findViewById(R.id.bottomNavigation);
        imgAval = (ImageView)findViewById(R.id.ivAval);
        imgResultados = (ImageView)findViewById(R.id.ivResultados);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapaFragment);
        mapFragment.getMapAsync(this);

        //Posicionar el icono del menu
        Menu menu = MenuUsuario.getMenu();
        MenuItem menuItem= menu.getItem(0);
        menuItem.setChecked(true);

        //
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

        obtenerPreferencias();
        dominio = getString(R.string.ip);
        getLastViewData();

        cargarInfoCarrera(dominio + "obtenerCompetencia.php?idCompe=" + id_competencia);

        cargarSpinnerComentar();
        cargarSpinnerComentarios();

        btnInscripcion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CrearInscripcion();
            }
        });

        btnEnviarComentario.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                comentarForo(dominio + "agregarComentario.php?" +
                        "id_usuario="+ user.getId() +
                        "&id_competencia=" + id_competencia +
                        "&mensaje="+txtComentario.getText().toString().replaceAll(" ", "%20") +
                        "&tipo_mensaje=" + categoria);
            }

        });
    }

    private void obtenerPreferencias() {
        SharedPreferences preferences = getSharedPreferences("Datos_usuario", Context.MODE_PRIVATE);

        user.setId(preferences.getInt(Login.ID_USUARIO_SESSION, 0));
        user.setNombre(preferences.getString(Login.NOMBRE_USUARIO_SESSION, "No_name"));
        user.setCorreo(preferences.getString(Login.CORREO_SESSION, "No_mail"));
        user.setFechaNacimiento(preferences.getInt(Login.NACIMIENTO_USUARIO_SESSION, 0));
    }

    private void CrearInscripcion() {
        Intent intent = new Intent(carrera_vista1.this, InscripcionesCompetidor.class);
        intent.putExtra("NOMBRE_COMPETENCIA", txtNomCompe.getText());
        intent.putExtra("monto", monto);
        intent.putExtra("ID_COMPENTENCIA", id_competencia);
        intent.putExtra("FECHA", txtFechaCompe.getText());
        intent.putExtra("LUGAR", txtLugarCompe.getText());
        intent.putExtra("ORGANIZADOR", txtOrganizador.getText());
        startActivity(intent);
        finish();
    }

    private void cargarSpinnerComentar() {
        ArrayAdapter<CharSequence> opcionesSpCat = ArrayAdapter.createFromResource(this, R.array.categorias, android.R.layout.simple_spinner_item);
        spCategorias.setAdapter(opcionesSpCat);

        spCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoria = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void cargarSpinnerComentarios() {
        ArrayAdapter<CharSequence> opcionesSpCat = ArrayAdapter.createFromResource(this, R.array.filtroCategorias, android.R.layout.simple_spinner_item);
        spFiltro.setAdapter(opcionesSpCat);

        spFiltro.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cargarComentarios(dominio + "obtenerComentarios.php?id_compentencia=" + id_competencia +
                        "&tipo_comentario=" + parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void cargarInfoCarrera(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject respuesta = jsonArray.getJSONObject(0);
                            Glide.with(carrera_vista1.this).load(respuesta.optString("foto")).into(imgCompetencia);
                            Glide.with(carrera_vista1.this).load(respuesta.optString("aval")).into(imgAval);
                            Glide.with(carrera_vista1.this).load(respuesta.optString("resultados")).into(imgResultados);
                            id_organizador = respuesta.optString("id_usuario");
                            monto = respuesta.optString("precio");
                            coordenadas = respuesta.optString("coordenadas");
                            nombreCompe = respuesta.optString("nom_comp");
                            txtNomCompe.setText(respuesta.optString("nom_comp"));
                            txtOrganizador.setText(respuesta.optString("nombre"));
                            txtFechaCompe.setText(respuesta.optString("fecha"));
                            txtLugarCompe.setText(respuesta.optString("ciudad")+", "+respuesta.optString("colonia")+", "+respuesta.optString("calle"));
                            txtPrecioCompe.setText("$" + respuesta.optString("precio"));
                            txtDescripcionCompe.setText(respuesta.optString("descripcion"));
                            createMark();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexión al servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void getLastViewData() {
        boolean flag;

        Bundle extra = carrera_vista1.this.getIntent().getExtras();
        id_competencia = extra.getString("id");
        flag = extra.getBoolean("registro");

        if (flag)
            btnInscripcion.setEnabled(flag);
        else
            btnInscripcion.setEnabled(flag);
    }

    private void comentarForo(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,//Una peticion para pasarle al web service el comentario
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {//Nos responde el web service
                        if (response.equals("Exito"))
                            txtComentario.setText("");//Se limpia la casilla del comentario
                        else if (response.equals("Error"))
                            Toast.makeText(carrera_vista1.this, "Vuelve a intentarlo en un poco de tiempo", Toast.LENGTH_SHORT).show();//Nos dice que ubo un error
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(carrera_vista1.this, "Hubo un error con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);//Con eso ponemos en cola la petición
    }

    private void cargarComentarios(String URL) {
        comentariosList.clear();//Limpia la lista del recicler view

        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,//Aqui hacemos la peticion
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //Hacer el string a json array object
                            JSONArray array = new JSONArray(response);//Creamos un objeto de tipo json ARRAY el cual contiene la respuetsa

                            //Recorremos con un for lo que tiene el array
                            for (int i = 0; i < array.length(); i++) {//El ciclo que va a guardar en tu liosta de comentarios cada comentario uno por uno
                                //Obtenemos los objetos tipo competencias del array
                                JSONObject comentario = array.getJSONObject(i);

                                //Añadir valores a los correspondientes textview
                                comentariosList.add(new Comentarios(
                                        comentario.getString("id_foro"),
                                        comentario.getString("mensaje"),
                                        comentario.getString("nombre"),
                                        comentario.getString("tipo_mensaje")
                                ));
                            }

                            //Creamos instancia del adapter
                            adaptador = new comentariosAdapter(carrera_vista1.this, comentariosList);

                            comentariosAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (id_organizador.equals(""+user.getId())) {
                                        eliminarComentario(dominio + "agregarComentario.php?id_comentario=" +
                                                comentariosList.get(ForoRecycler.getChildAdapterPosition(v)).getId_foro());
                                    }
                                }
                            });

                            ForoRecycler.setAdapter(adaptador);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error de conección con el servidor", Toast.LENGTH_SHORT).show();
            }
        });

        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void eliminarComentario(final String URL) {
        AlertDialog.Builder alertaComentario = new AlertDialog.Builder(this);
        alertaComentario.setTitle("Eliminar comentario");
        alertaComentario.setMessage("Esta acción es irreversible");
        alertaComentario.setPositiveButton("Eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                StringRequest request = new StringRequest(Request.Method.GET, URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                if (response.equals("Exito"))
                                    Toast.makeText(carrera_vista1.this, "Operación exitosa", Toast.LENGTH_SHORT).show();
                                else if (response.equals("Error"))
                                    Toast.makeText(carrera_vista1.this, "Vuelve a intentarlo en un poco de tiempo", Toast.LENGTH_SHORT).show();
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(carrera_vista1.this, "Hubo un error con el servidor", Toast.LENGTH_SHORT).show();
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
    private String PonerMes(int Mes){
        switch(Mes){
            case 1:
                return "Enero";
                break;
            case 2:
                return "Febrero";
            break;
            case 3:
                return "Marzo";
            break;
            case 4:
                return "Abril";
            break;
            case 5:
                return "Mayo";
            break;
            case 6:
                return "Junio";
            break;
            case 7:
                return "Julio";
            break;
            case 8:
                return "Agosto";
            break;
            case 9:
                return "Septiembre";
            break;
            case 10:
                return "Octubre";
            break;
            case 11:
                return "Nobiembre";
            break;
            case 12:
                return "Diciembre";
            break;

        }

    }

    //Google maps integration
    public boolean googleServicesAvailable() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int status = api.isGooglePlayServicesAvailable(this);

        if (status == ConnectionResult.SUCCESS) {
            return true;

        }else if (api.isUserResolvableError(status)){
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, (Activity)getApplicationContext(), 10);
            dialog.show();

        }else {
            Toast.makeText(this, "Error con los servicios de Google Play", Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
    }

    private void createMark() {//Metodo que se activa cuando se accede desde la vista de la competncia 1
        String lat, lng;
        String[] corde;
        corde = coordenadas.split(",");

        lat = corde[0];
        lng = corde[1];

        LatLng startCom = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));// Creamos el objeto para crear latitud y longitud
        mMap.addMarker(new MarkerOptions().position(startCom)
                .title("Inicio de "+nombreCompe));//Agregamos un marcador al mapa
        float zoom = 16;

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(startCom, zoom));//Movemos el mapa a la latitud y longitud que tiene la carrera
    }
}

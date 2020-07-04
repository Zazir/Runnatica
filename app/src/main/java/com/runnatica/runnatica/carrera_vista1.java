package com.runnatica.runnatica;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.paypal.android.sdk.payments.PayPalService;
import com.runnatica.runnatica.adapter.comentariosAdapter;
import com.runnatica.runnatica.poho.Comentarios;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class carrera_vista1 extends AppCompatActivity {
    private ImageView imgCompetencia;
    private TextView txtNomCompe, txtOrganizador, txtFechaCompe, txtHoraCompe,
            txtLugarCompe, txtPrecioCompe, txtDescripcionCompe, txtRegistrarse,
            txtComentario;
    private Button btnInscripcion, btnEnviarComentario;
    private Spinner spCategorias, spFiltro;
    private RecyclerView ForoRecycler;

    private List<Comentarios> comentariosList = new ArrayList<>();
    private Usuario user = Usuario.getUsuarioInstance();
    private String id_competencia;
    private String id_organizador;

    private comentariosAdapter adaptador;

    private String monto;
    private String categoria;
    private String dominio;

    @Override
    protected void onDestroy() {
        stopService(new Intent(this, PayPalService.class));
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_carrera_vista1);
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
        txtRegistrarse = (TextView)findViewById(R.id.tvRegistrarse);
        txtComentario = (TextView)findViewById(R.id.etRespuestaForo);
        btnEnviarComentario = (Button)findViewById(R.id.btnEnviarForo);
        ForoRecycler = (RecyclerView)findViewById(R.id.rvForo);
        ForoRecycler.setHasFixedSize(true);
        ForoRecycler.setLayoutManager(new LinearLayoutManager(this));

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

        txtRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                            id_organizador = respuesta.optString("id_usuario");
                            txtNomCompe.setText(respuesta.optString("nom_comp"));
                            txtOrganizador.setText(respuesta.optString("organizador"));
                            txtFechaCompe.setText(respuesta.optString("fecha"));
                            txtHoraCompe.setText(respuesta.optString("hora") + " horas");
                            txtLugarCompe.setText(respuesta.optString("coordenadas"));
                            txtPrecioCompe.setText("$" + respuesta.optString("precio"));
                            monto = respuesta.optString("precio");
                            txtDescripcionCompe.setText(respuesta.optString("descripcion"));
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
        Bundle extra = carrera_vista1.this.getIntent().getExtras();
        id_competencia = extra.getString("id");
    }

    private void comentarForo(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("Exito"))
                            txtComentario.setText("");
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
        Volley.newRequestQueue(this).add(stringRequest);
    }

    private void cargarComentarios(String URL) {
        comentariosList.clear();

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
}

package com.runnatica.runnatica;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.safetynet.SafetyNet;
import com.google.android.gms.safetynet.SafetyNetApi;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.runnatica.runnatica.poho.Inscripciones;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.runnatica.runnatica.carrera_vista1.IDS_FORANEOS;

public class InscripcionForaneo extends AppCompatActivity {

    private String monto = "";

    private Button btnCrearForaneos, btnInscribir;

    private List<Inscripciones> inscripcionesList;

    private String id_competencia, NombreCompetencia, Fecha, Lugar, Organizador;
    private ListView lvForaneo;


    private String ids_foraneos;

    private int contForaneosSeleccionados = 0;

    public String idsForaneos = "";

    private static final String TAG = "JORGE";
    private Usuario usuario = Usuario.getUsuarioInstance();

    public static final String SITE_KEY = "6LfiMKkZAAAAAIIpYcZWdXd7TaRoya7OBKhi8EeQ";
    public static final String SITE_SECRET_KEY = "6LfiMKkZAAAAADbV7pR01TxTXQcZSZMDqx_8SCCM";
    String userResponseToken;
    int Cupo, Totalyainscritos=0, GuardarInscritos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inscripcion_foraneo);
        btnInscribir = (Button)findViewById(R.id.btnPagarInscripciones);
        lvForaneo = (ListView)findViewById(R.id.lvForaneos);

        getLastViewData();
        String dominio = getString(R.string.ip);
        //cargarInscripciones(dominio + "obtenerInscripciones.php?id_compentencia=" + id_competencia);

        ConsultarForaneos("http://31.220.61.80/WebServiceRunnatica/obtenerForaneos.php?id_usuario=" + usuario.getId());

        lvForaneo.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String[] idTemp = parent.getItemAtPosition(position).toString().split(" ");
                String idTemporalPulsado = idTemp[0];

                consultarForaneosInscritos(idTemporalPulsado, idTemp);
            }
        });

        btnInscribir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ids_foraneos = inscripcionesAdapter.idsForaneos;
                guardarDatosInscripcion();
                CrearInscripcion();
            }

        });

    }


    private void guardarDatosInscripcion() {
        SharedPreferences preferences = getSharedPreferences("Autoguardado_Inscripcion", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putString(IDS_FORANEOS, idsForaneos);

        editor.commit();
    }

    private void getLastViewData() {
        Bundle extra = InscripcionForaneo.this.getIntent().getExtras();
        id_competencia = extra.getString("ID_COMPENTENCIA");
        monto = extra.getString("monto");
        NombreCompetencia = extra.getString("NOMBRE_COMPETENCIA");
        Fecha = extra.getString("FECHA");
        Lugar = extra.getString("LUGAR");
        Organizador = extra.getString("ORGANIZADOR");
        //Toast.makeText(getApplicationContext(), ""+NombreCompetencia, Toast.LENGTH_SHORT).show();
    }

    private void ConsultarForaneos(String URL) {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response.equals("[]"))
                            Toast.makeText(getApplicationContext(), "No Has añadido usuarios foráneos", Toast.LENGTH_SHORT).show();
                        else
                            llenarListView(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error en la conexión", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(stringRequest);
    }
    private void llenarListView(String response) {
        ArrayList<String> listaParaSp = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i=0 ; i < jsonArray.length() ; i++){

                JSONObject foraneo = jsonArray.getJSONObject(i);

                    listaParaSp.add(foraneo.optInt("id_foraneo") + " : " + foraneo.optString("nombre"));

            }
            ArrayAdapter<String> foraneoArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaParaSp);
            lvForaneo.setAdapter(foraneoArrayAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void moveNewActivity() {
        Intent intent = new Intent(InscripcionForaneo.this, AvisoPago.class);
        intent.putExtra("monto", monto);
        intent.putExtra("ID_COMPENTENCIA", id_competencia);
        intent.putExtra("CANT_FORANEOS", ids_foraneos);
        intent.putExtra("NOMBRE_COMPETENCIA", NombreCompetencia);
        intent.putExtra("FECHA", Fecha);
        intent.putExtra("LUGAR", Lugar);
        intent.putExtra("ORGANIZADOR", Organizador);
        startActivity(intent);
    }

    private void CrearInscripcion() {

        //Toast.makeText(this, ""+NombreCompetencia, Toast.LENGTH_SHORT).show();
        SafetyNet.getClient(this).verifyWithRecaptcha(SITE_KEY)
                .addOnSuccessListener(this,
                        new OnSuccessListener<SafetyNetApi.RecaptchaTokenResponse>()
                        {
                            @Override
                            public void onSuccess(SafetyNetApi.RecaptchaTokenResponse response)
                            {

                                userResponseToken = response.getTokenResult();

                                if (!userResponseToken.isEmpty())
                                {
                                    sendRequest();
                                }
                            }
                        })
                .addOnFailureListener(this, new OnFailureListener()
                {
                    @Override
                    public void onFailure(@NonNull Exception e)
                    {
                        if (e instanceof ApiException)
                        {
                            ApiException apiException = (ApiException) e;
                            int statusCode = apiException.getStatusCode();

                            Log.e(TAG, "ERROR 1: " + CommonStatusCodes.getStatusCodeString(statusCode));
                        }
                        else
                        {
                            Log.e(TAG, "ERROR 2: " + e.getMessage());
                        }
                    }
                });

    }

    private void consultarForaneosInscritos(String idTemporalPulsado, String []idTemp) {
        String URL = "http://31.220.61.80/WebServiceRunnatica/" + "obtenerDatosCompetencia.php?id_competencia="+id_competencia+"&consulta=2";

        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.i(response, "consultarForaneosInscritos");
                            //totalforaneos, foraneosyaregistrados;
                            Totalyainscritos = Integer.parseInt(response);
                            consultarTotalInscripciones(idTemporalPulsado, idTemp);
                        }catch (Exception e) {}
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }

    private void consultarTotalInscripciones(String idTemporalPulsado, String []idTemp) {
        String URL = "http://31.220.61.80/WebServiceRunnatica/" + "obtenerDatosCompetencia.php?id_competencia="+id_competencia+"&consulta=3";
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        try {
                            Log.i(response, "consultarTotalInscripciones");
                            JSONArray res = new JSONArray(response);
                            JSONObject totalInscripciones = res.getJSONObject(0);
                            Cupo = totalInscripciones.optInt("Total_foraneos");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        //txtTotalUsuarios.setText();

                        if (idsForaneos.equals("")) {
                            idsForaneos = " "+idTemporalPulsado+" ";
                            Toast.makeText(getApplicationContext(), "Has seleccionado a: " + idTemp[2], Toast.LENGTH_SHORT).show();
                            contForaneosSeleccionados++; //contForaneosSeleccionados siempre será 1 a inicio
                            Log.i("foraneos", idsForaneos);

                            GuardarInscritos = Totalyainscritos+1;

                            Log.i("GuardarInscritos", GuardarInscritos+"");

                            if(Totalyainscritos > Cupo){
                                Toast.makeText(getApplicationContext(), "Ya no se admiten mas Usuarios Foraneos ", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        }else if (contForaneosSeleccionados <= 4) {

                            Log.i("contador_foraneos", contForaneosSeleccionados+"");
                            Log.i("foraneos", idsForaneos);

                            if (idsForaneos.contains(" "+idTemporalPulsado+" ")) {
                                Toast.makeText(getApplicationContext(), "Ya elegiste a ese usuario", Toast.LENGTH_SHORT).show();
                            }else {

                                if(GuardarInscritos >= Cupo){
                                    Toast.makeText(getApplicationContext(), "Ya se han agotado las inscripciones de Usuarios Foraneos", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                                idsForaneos = idsForaneos.concat(idTemporalPulsado + " ");
                                contForaneosSeleccionados++;
                                Toast.makeText(getApplicationContext(), "Has seleccionado a: " + idTemp[2], Toast.LENGTH_SHORT).show();

                                GuardarInscritos++;
                            }
                        }else if(contForaneosSeleccionados >= 5){
                            Toast.makeText(getApplicationContext(), "No se pueden seleccionar mas de 5 usuarios foraneos", Toast.LENGTH_SHORT).show();
                        }
                        Log.i("Cupo", Cupo+"");
                        Log.i("Totalyainscritos", Totalyainscritos+"");

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(this).add(request);
    }
    public void sendRequest()
    {
        String url = "https://www.google.com/recaptcha/api/siteverify";

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response)
                    {
                        try
                        {
                            JSONObject obj = new JSONObject(response);

                            if (obj.getString("success").equals("true"))
                            {
                                Log.e(TAG, "EXITO");
                                moveNewActivity();
                            }
                        }
                        catch (JSONException e)
                        {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Log.e(TAG, "ERROR 3");
                        Toast.makeText(InscripcionForaneo.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> params = new HashMap<>();
                params.put("secret", SITE_SECRET_KEY);
                params.put("response", userResponseToken);
                return params;
            }
        };

        Controlador_Captcha.getInstance(this).addToRequestQueue(stringRequest);
    }
}

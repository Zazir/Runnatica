package com.runnatica.runnatica;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RegistrarForaneos extends AppCompatActivity {

    private EditText txtNombre, txtCorreo, txtEdad;
    private RadioButton rbHombre, rbMujer;
    private Button btnCrearForaneo;
    ListView lvForaneo;
    private String id_competencia;
    private Usuario usuario = Usuario.getUsuarioInstance();
    private int index = 0;
    private String sexo;


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
        lvForaneo = (ListView)findViewById(R.id.lvForaneos);

        final String ip = getString(R.string.ip);

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
                        llenarListView(response);
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
        } else if (EdadInt <0 || EdadInt >99){
            txtEdad.setError("La edad no es valida");
        }else if (rbHombre.isChecked() || rbMujer.isChecked()){
            siguiente = true;
        }else siguiente = false;

        return siguiente;
    }
    private void llenarListView(String response) {
        ArrayList<String> listaParaSp = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i=0 ; i < jsonArray.length() ; i++){

                JSONObject foraneo = jsonArray.getJSONObject(i);
                listaParaSp.add(foraneo.optString("nombre"));
            }
            ArrayAdapter<String> foraneoArrayAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaParaSp);
            lvForaneo.setAdapter(foraneoArrayAdapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
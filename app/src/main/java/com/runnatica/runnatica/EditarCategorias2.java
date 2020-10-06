package com.runnatica.runnatica;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import androidx.appcompat.app.AppCompatActivity;



public class EditarCategorias2 extends AppCompatActivity {

    private EditText NombreCategoria, CompetidoresCategorias, ForaneosCategorias, MinimoEdad, MaximoEdad;
    private Button Finalizar;

    private String dominio, id_categoria;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_categorias2);

        getString(R.string.ip);
        getLastViewData();

        NombreCategoria = (EditText)findViewById(R.id.etNombreInscripcion);
        CompetidoresCategorias = (EditText)findViewById(R.id.etCantidadNormal);
        ForaneosCategorias = (EditText)findViewById(R.id.etCantidadForaneos);
        MinimoEdad = (EditText)findViewById(R.id.etDesdeAnos);
        MaximoEdad = (EditText)findViewById(R.id.etHastaAnos);
        Finalizar = (Button) findViewById(R.id.btnFinalizarCompetencia);

        Finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validaciones()) {
                    peticion(dominio + "actualizarCategoria.php?edad_max="+MaximoEdad.getText().toString()+"&id_categoria="+id_categoria);
                    peticion(dominio + "actualizarCategoria.php?edad_min="+MinimoEdad.getText().toString()+"&id_categoria="+id_categoria);
                    peticion(dominio + "actualizarCategoria.php?cantidad_usuarios"+CompetidoresCategorias.getText().toString()+"&id_categoria="+id_categoria);
                    peticion(dominio + "actualizarCategoria.php?cantidad_foraneos="+ForaneosCategorias.getText().toString()+"&id_categoria="+id_categoria);
                    peticion(dominio + "actualizarCategoria.php?nombre_categoria="+NombreCategoria.getText().toString().replaceAll(" ", "%20")+"&id_categoria="+id_categoria);
                }
            }
        });
    }

    private void getLastViewData() {
        Bundle extra = this.getIntent().getExtras();
        id_categoria = extra.getString("ID_CAT");
    }

    private void peticion(String URL) {
        StringRequest request = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                            Toast.makeText(getApplicationContext(), "Error de conección con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });

        Volley.newRequestQueue(this).add(request);
    }

    private boolean validaciones() {

        return true;
    }
}
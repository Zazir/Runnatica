package com.runnatica.runnatica.Fragmentos;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link Inscripcion_foraneo#} factory method to
 * create an instance of this fragment.
 */
public class Inscripcion_foraneo extends Fragment {
    private int cantidadForaneos;
    private String id_competencia;

    private View vistaForaneo;
    private EditText txtNombre, txtCorreo, txtEdad;
    private RadioButton rbHombre, rbMujer;
    private Button btnCrearForaneo;

    public String[] arregloID;
    private int index = 0;
    private String sexo;

    private int CondicionalTamano;

    public Inscripcion_foraneo() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            cantidadForaneos = getArguments().getInt("CANTIDADFORANEOS", 0);
            id_competencia = getArguments().getString("ID_COMPENTENCIA");
            arregloID = new String[cantidadForaneos];
            CondicionalTamano = cantidadForaneos;
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        vistaForaneo = inflater.inflate(R.layout.fragment_inscripcion_foraneo, container, false);
        txtNombre = (EditText)vistaForaneo.findViewById(R.id.etNombreForaneo);
        txtCorreo = (EditText)vistaForaneo.findViewById(R.id.etCorreoForaneo);
        txtEdad = (EditText)vistaForaneo.findViewById(R.id.etEdadForaneo);
        rbHombre = (RadioButton) vistaForaneo.findViewById(R.id.rbtnHombre);
        rbMujer = (RadioButton) vistaForaneo.findViewById(R.id.rbtnMujer);
        btnCrearForaneo = (Button)vistaForaneo.findViewById(R.id.btnCreateForaneo);

        final String ip = getString(R.string.ip);

        btnCrearForaneo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CondicionalTamano > 0) {
                    setSexo();
                    crearUsuarioForaneo(ip + "agregarUsuarioForaneo.php?" +
                            "id_competencia=" + id_competencia +
                            "&nombre=" + txtNombre.getText().toString().replaceAll(" ", "%20") +
                            "&correo=" + txtCorreo.getText().toString() +
                            "&sexo=" + sexo +
                            "&edad=" + txtEdad.getText().toString());
                    CondicionalTamano--;
                }else {
                    btnCrearForaneo.setEnabled(false);
                    btnCrearForaneo.setText("No puedes agregar más");
                }
            }
        });

        return vistaForaneo;
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
                        arregloID[index++] = response;
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getContext(), "Error de conección con el servidor", Toast.LENGTH_SHORT).show();
                    }
                });
        Volley.newRequestQueue(getContext()).add(stringRequest);
    }
}

package com.runnatica.runnatica.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.runnatica.runnatica.R;
import com.runnatica.runnatica.poho.Inscripciones;
import com.runnatica.runnatica.poho.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class inscripcionesForaneoAdapter extends RecyclerView.Adapter<inscripcionesForaneoAdapter.ViewHolderInscripciones> implements AdapterView.OnItemSelectedListener {
    private Context ctx;
    private List<Inscripciones> inscripcionesList;
    private int contForaneos = 0;
    private int[] idForaneo = new int[5];

    public inscripcionesForaneoAdapter(Context ctx, List<Inscripciones> inscripcionesList) {
        this.ctx = ctx;
        this.inscripcionesList = inscripcionesList;
    }

    @NonNull
    @Override
    public inscripcionesForaneoAdapter.ViewHolderInscripciones onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recyclerview_inscripciones_foraneo, null);
        return new ViewHolderInscripciones(view);
    }

    @Override
    public void onBindViewHolder(@NonNull inscripcionesForaneoAdapter.ViewHolderInscripciones viewHolderInscripciones, int position) {
        viewHolderInscripciones.asignarDatos(inscripcionesList.get(position), position);
        if (contForaneos <= 5)
            viewHolderInscripciones.spForaneo.setOnItemSelectedListener(this);
        else
            viewHolderInscripciones.spForaneo.setEnabled(false);
    }

    @Override
    public int getItemCount() {
        return inscripcionesList.size();
    }

    public int[] getIdForaneo() {
        return idForaneo;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Toast.makeText(ctx, "Seleccionaste "+parent.getItemAtPosition(position).toString(), Toast.LENGTH_SHORT ).show();
        //idForaneo += ;
        contForaneos++;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class ViewHolderInscripciones extends RecyclerView.ViewHolder {
        TextView txtNombreInscripcion, txtMinEdad, txtMaxEdad;
        Spinner spForaneo;
        Usuario usuario = Usuario.getUsuarioInstance();

        public ViewHolderInscripciones(View vistaInscripcion) {
            super(vistaInscripcion);
            txtNombreInscripcion = (TextView)vistaInscripcion.findViewById(R.id.tvNombreInscripcionForaneo);
            txtMinEdad = (TextView)vistaInscripcion.findViewById(R.id.tvMinEdadInscripcionForaneo);
            txtMaxEdad = (TextView)vistaInscripcion.findViewById(R.id.tvMaxEdadInscripcionForaneo);
            spForaneo = (Spinner)vistaInscripcion.findViewById(R.id.spCantidadBoletosForaneo);
        }

        public void asignarDatos(Inscripciones inscripciones, final int posicion) {
            txtNombreInscripcion.setText(inscripciones.getNombreInscripcion());
            txtMinEdad.setText("Edad mínima "+inscripciones.getEdadMinina() + " años");
            txtMaxEdad.setText("Edad máxima "+inscripciones.getEdadMaxima() + " años");
            ConsultarDatosSpinner("https://runnatica.000webhostapp.com/WebServiceRunnatica/obtenerForaneos.php?id_usuario=" + usuario.getId(), inscripciones);
        }

        private void ConsultarDatosSpinner(String URL, final Inscripciones inscripciones) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("[]"))
                                Toast.makeText(ctx, "No Has añadido usuarios foráneos", Toast.LENGTH_SHORT).show();
                            else
                                llenarSpinner(response, inscripciones);
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Toast.makeText(ctx, "Error en la conexión", Toast.LENGTH_SHORT).show();
                        }
                    });
            Volley.newRequestQueue(ctx).add(stringRequest);
        }

        private void llenarSpinner(String response, Inscripciones inscripciones) {
            ArrayList<String> listaParaSp = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i=0 ; i < jsonArray.length() ; i++){

                    JSONObject foraneo = jsonArray.getJSONObject(i);

                    if (foraneo.optInt("edad") >= inscripciones.getEdadMinina() && foraneo.optInt("edad") <= inscripciones.getEdadMaxima()){
                        listaParaSp.add(foraneo.optString("nombre"));
                    }
                }
                ArrayAdapter<String> foraneoArrayAdapter = new ArrayAdapter<>(ctx, android.R.layout.simple_dropdown_item_1line, listaParaSp);
                spForaneo.setAdapter(foraneoArrayAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
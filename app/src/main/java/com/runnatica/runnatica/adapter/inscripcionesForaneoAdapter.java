package com.runnatica.runnatica.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

public class inscripcionesForaneoAdapter extends RecyclerView.Adapter<inscripcionesForaneoAdapter.ViewHolderInscripciones>
    implements AdapterView.OnItemSelectedListener{
    private Context ctx;
    private List<Inscripciones> inscripcionesList;
    private AdapterView.OnItemClickListener listener;
    private int contForaneosSeleccionados = 0;
    //public String[] idsForaneos = new String[5];
    public String idsForaneos = "";

    public inscripcionesForaneoAdapter(Context ctx, List<Inscripciones> inscripcionesList) {
        this.ctx = ctx;
        this.inscripcionesList = inscripcionesList;
    }

    @NonNull
    @Override
    public inscripcionesForaneoAdapter.ViewHolderInscripciones onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recyclerview_inscripciones_foraneo, null);
        //view.setOnItemClickListener(this);
        return new ViewHolderInscripciones(view);
    }

    @Override
    public void onBindViewHolder(@NonNull inscripcionesForaneoAdapter.ViewHolderInscripciones viewHolderInscripciones, int position) {
        viewHolderInscripciones.asignarDatos(inscripcionesList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return inscripcionesList.size();
    }

    public void setOnItemClick(AdapterView.OnItemClickListener itemSelectedListener) {
        listener = itemSelectedListener;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if (listener != null) {
            listener.onItemClick(parent, view, position, id);
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class ViewHolderInscripciones extends RecyclerView.ViewHolder implements AdapterView.OnItemClickListener {
        TextView txtNombreInscripcion, txtMinEdad, txtMaxEdad;
        ListView lvForaneo;

        Usuario usuario = Usuario.getUsuarioInstance();

        public ViewHolderInscripciones(View vistaInscripcion) {
            super(vistaInscripcion);
            txtNombreInscripcion = (TextView)vistaInscripcion.findViewById(R.id.tvNombreInscripcionForaneo);
            //txtMinEdad = (TextView)vistaInscripcion.findViewById(R.id.tvMinEdadInscripcionForaneo);
            //txtMaxEdad = (TextView)vistaInscripcion.findViewById(R.id.tvMaxEdadInscripcionForaneo);
            lvForaneo = (ListView) vistaInscripcion.findViewById(R.id.lvForaneos);

            lvForaneo.setOnItemClickListener(this);
        }

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String[] idTemp = parent.getItemAtPosition(position).toString().split(" ");
            String idTemporalPulsado = idTemp[0];

            if (idsForaneos.equals("")) {

                    idsForaneos = " "+idTemporalPulsado+" ";
                    Toast.makeText(ctx, "Has seleccionado a: " + idTemp[2], Toast.LENGTH_SHORT).show();
                    contForaneosSeleccionados++; //contForaneosSeleccionados siempre será 1 a inicio
                    Log.i("foraneos", idsForaneos);

            }else if (contForaneosSeleccionados <= 5) {
                Log.i("contador_foraneos", contForaneosSeleccionados+"");
                Log.i("foraneos", idsForaneos);

                if (idsForaneos.contains(" "+idTemporalPulsado+" ")) {
                    Toast.makeText(ctx, "Ya elegiste a ese usuario", Toast.LENGTH_SHORT).show();
                }else {
                    idsForaneos = idsForaneos.concat(idTemporalPulsado + " ");
                    contForaneosSeleccionados++;
                    Toast.makeText(ctx, "Has seleccionado a: " + idTemp[2], Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void asignarDatos(Inscripciones inscripciones, final int posicion) {
            txtNombreInscripcion.setText(inscripciones.getNombreInscripcion());
            /*txtMinEdad.setText("Edad mínima "+inscripciones.getEdadMinina() + " años");
            txtMaxEdad.setText("Edad máxima "+inscripciones.getEdadMaxima() + " años");*/

            ConsultarDatosSpinner("http://31.220.61.80/WebServiceRunnatica/obtenerForaneos.php?id_usuario=" + usuario.getId(), inscripciones);
        }

        private void ConsultarDatosSpinner(String URL, final Inscripciones inscripciones) {
            StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.equals("[]"))
                                Toast.makeText(ctx, "No Has añadido usuarios foráneos", Toast.LENGTH_SHORT).show();
                            else
                                llenarListView(response, inscripciones);
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

        private void llenarListView(String response, Inscripciones inscripciones) {
            ArrayList<String> listaParaSp = new ArrayList<>();
            try {
                JSONArray jsonArray = new JSONArray(response);
                for (int i=0 ; i < jsonArray.length() ; i++){

                    JSONObject foraneo = jsonArray.getJSONObject(i);

                    if (foraneo.optInt("edad") >= inscripciones.getEdadMinina() && foraneo.optInt("edad") <= inscripciones.getEdadMaxima()){
                        listaParaSp.add(foraneo.optInt("id_foraneo") + " : " + foraneo.optString("nombre"));
                    }
                }
                ArrayAdapter<String> foraneoArrayAdapter = new ArrayAdapter<>(ctx, android.R.layout.simple_list_item_1, listaParaSp);
                lvForaneo.setAdapter(foraneoArrayAdapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
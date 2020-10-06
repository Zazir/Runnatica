package com.runnatica.runnatica.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.runnatica.runnatica.R;
import com.runnatica.runnatica.poho.Inscripciones;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class inscripcionesAdapter extends RecyclerView.Adapter<inscripcionesAdapter.ViewHolderInscripciones> {
    private Context ctx;
    private List<Inscripciones> inscripcionesList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public inscripcionesAdapter(Context ctx, List<Inscripciones> inscripcionesList, OnItemClickListener listener) {
        this.ctx = ctx;
        this.inscripcionesList = inscripcionesList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public inscripcionesAdapter.ViewHolderInscripciones onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recyclerview_inscripciones, null);
        return new inscripcionesAdapter.ViewHolderInscripciones(view);
    }

    @Override
    public void onBindViewHolder(@NonNull inscripcionesAdapter.ViewHolderInscripciones viewHolderInscripciones, int position) {
        viewHolderInscripciones.asignarDatos(inscripcionesList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return inscripcionesList.size();
    }

    class ViewHolderInscripciones extends RecyclerView.ViewHolder {
        TextView txtNombreInscripcion, txtMinEdad, txtMaxEdad, txtLeyenda;

        public ViewHolderInscripciones(View vistaInscripcion) {
            super(vistaInscripcion);
            txtNombreInscripcion = (TextView)vistaInscripcion.findViewById(R.id.tvNombreInscripcion);
            txtMinEdad = (TextView)vistaInscripcion.findViewById(R.id.tvMinEdadInscripcion);
            txtMaxEdad = (TextView)vistaInscripcion.findViewById(R.id.tvMaxEdadInscripcion);
            txtLeyenda = (TextView)vistaInscripcion.findViewById(R.id.txtLeyenda);
        }

        public void asignarDatos(Inscripciones inscripciones, final int posicion) {
            txtNombreInscripcion.setText(inscripciones.getNombreInscripcion());
            txtMinEdad.setText("Edad mínima "+inscripciones.getEdadMinina() + " años");
            txtMaxEdad.setText("Edad máxima "+inscripciones.getEdadMaxima() + " años");
        }
    }
}

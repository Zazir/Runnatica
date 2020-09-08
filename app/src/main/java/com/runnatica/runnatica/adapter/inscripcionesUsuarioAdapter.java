package com.runnatica.runnatica.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.runnatica.runnatica.R;
import com.runnatica.runnatica.poho.Inscripciones;
import com.runnatica.runnatica.poho.Usuario;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class inscripcionesUsuarioAdapter extends RecyclerView.Adapter<inscripcionesUsuarioAdapter.ViewHolderInscripciones> {
    private Context ctx;
    private List<Inscripciones> inscripcionesList;
    public static boolean puedeAvanzar = false;

    public inscripcionesUsuarioAdapter(Context ctx, List<Inscripciones> inscripcionesList) {
        this.ctx = ctx;
        this.inscripcionesList = inscripcionesList;
    }

    @NonNull
    @Override
    public inscripcionesUsuarioAdapter.ViewHolderInscripciones onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recyclerview_inscripciones, null);
        return new ViewHolderInscripciones(view);
    }

    @Override
    public void onBindViewHolder(@NonNull inscripcionesUsuarioAdapter.ViewHolderInscripciones viewHolderInscripciones, int position) {
        viewHolderInscripciones.asignarDatos(inscripcionesList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return inscripcionesList.size();
    }

    class ViewHolderInscripciones extends RecyclerView.ViewHolder {
        TextView txtNombreInscripcion, txtMinEdad, txtMaxEdad, txtLeyenda;
        ImageView imgTicket;
        Usuario usuario = Usuario.getUsuarioInstance();

        public ViewHolderInscripciones(View vistaInscripcion) {
            super(vistaInscripcion);
            txtNombreInscripcion = (TextView)vistaInscripcion.findViewById(R.id.tvNombreInscripcion);
            txtMinEdad = (TextView)vistaInscripcion.findViewById(R.id.tvMinEdadInscripcion);
            txtMaxEdad = (TextView)vistaInscripcion.findViewById(R.id.tvMaxEdadInscripcion);
            txtLeyenda = (TextView)vistaInscripcion.findViewById(R.id.txtLeyenda);
            imgTicket = (ImageView) vistaInscripcion.findViewById(R.id.igvTicket);
        }

        public void asignarDatos(Inscripciones inscripciones, final int posicion) {
            txtNombreInscripcion.setText(inscripciones.getNombreInscripcion());
            txtMinEdad.setText("Edad mínima "+inscripciones.getEdadMinina() + " años");
            txtMaxEdad.setText("Edad máxima "+inscripciones.getEdadMaxima() + " años");
            if (usuario.getEdadUsuario() >= inscripciones.getEdadMinina() && usuario.getEdadUsuario() <= inscripciones.getEdadMaxima()) {
                //imgTicket.setImageResource(R.drawable.ticket);
                txtLeyenda.setText("Puedes adquirir 1 entrada");
                puedeAvanzar = true;
            }else imgTicket.setImageResource(R.drawable.dangerous);
        }
    }
}

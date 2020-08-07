package com.runnatica.runnatica.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.runnatica.runnatica.R;
import com.runnatica.runnatica.poho.Comentarios;

import java.util.List;

public class comentariosAdapter extends RecyclerView.Adapter<comentariosAdapter.ViewHolderComentario> implements View.OnClickListener{

    private Context ctx;
    private List<Comentarios> comentariosList;
    private static View.OnClickListener listenerClick;

    public comentariosAdapter(Context ctx, List<Comentarios> comentariosList) {
        this.ctx = ctx;
        this.comentariosList = comentariosList;
    }

    @NonNull
    @Override
    public comentariosAdapter.ViewHolderComentario onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext(), R.layout.recyclerview_comentarios, null);//Preparar la vista para recibir datos
        view.setOnClickListener(this);//Metodo para verificar si se presiono o no
        return new ViewHolderComentario(view);
    }

    @Override
    public void onBindViewHolder(@NonNull comentariosAdapter.ViewHolderComentario viewHolderComentario, int position) {//Guardamos los datod
        viewHolderComentario.asignarDatos(comentariosList.get(position), position);
    }

    @Override
    public int getItemCount() {
        return comentariosList.size();
    }//Cuenta el tamaño de la lista

    public static void setOnClickListener(View.OnClickListener listener) {
        listenerClick = listener;
    }

    @Override
    public void onClick(View v) {
        if (listenerClick != null) {
            listenerClick.onClick(v);
        }
    }

    class ViewHolderComentario extends RecyclerView.ViewHolder {
        TextView txtNombreUsuario, txtComentario;

        public ViewHolderComentario(View vistaComentario) {
            super(vistaComentario);
            txtNombreUsuario = (TextView)vistaComentario.findViewById(R.id.tvNombreComentario);
            txtComentario = (TextView)vistaComentario.findViewById(R.id.tvComentario);
        }

        public void asignarDatos(Comentarios comentarios, final int posicion){
            txtNombreUsuario.setText(comentarios.getnombre_usuario());
            txtComentario.setText(comentarios.getMensaje());
        }
    }
}

package com.runnatica.runnatica.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.runnatica.runnatica.R;
import com.runnatica.runnatica.poho.UsuariosInscritos;

import java.util.List;

public class inscritosAdapter extends RecyclerView.Adapter<inscritosAdapter.ViewHolderInscritos> {
    private Context mCtx;
    private List<UsuariosInscritos> inscritosList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public inscritosAdapter(Context mCtx, List<UsuariosInscritos> inscritosList, OnItemClickListener listener) {
        this.mCtx = mCtx;
        this.inscritosList = inscritosList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public inscritosAdapter.ViewHolderInscritos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext() , R.layout.recyclerview_usuarios_inscritos, null);
        return new ViewHolderInscritos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull inscritosAdapter.ViewHolderInscritos holder, int position) {
        holder.asignarDatos(inscritosList.get(position), position, listener);
    }

    @Override
    public int getItemCount() { return inscritosList.size(); }

    class ViewHolderInscritos extends RecyclerView.ViewHolder {
        //Hacemos referencias a la vista
        TextView txtNombreInscrito;

        public ViewHolderInscritos(View itemView) {
            super(itemView);
            //Enlace vista
            txtNombreInscrito = (TextView) itemView.findViewById(R.id.tvNombreInscrito);
        }

        public void asignarDatos(UsuariosInscritos pojoInscrito, final int posicion, final OnItemClickListener listener) {
            txtNombreInscrito.setText(pojoInscrito.getNombre());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(posicion);
                }
            });
        }
    }
}

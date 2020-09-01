package com.runnatica.runnatica.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.runnatica.runnatica.R;
import com.runnatica.runnatica.poho.UsuarioForaneo;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class editarForaneoAdapter extends RecyclerView.Adapter<editarForaneoAdapter.ViewHolderForaneo> {

    private Context mCtx;
    private List<UsuarioForaneo> foraneosList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public editarForaneoAdapter(Context mCtx, List<UsuarioForaneo> foraneosList, OnItemClickListener listener) {
        this.mCtx = mCtx;
        this.foraneosList = foraneosList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public editarForaneoAdapter.ViewHolderForaneo onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext() , R.layout.recyclerview_foraneos, null);
        return new ViewHolderForaneo(view);
    }

    @Override
    public void onBindViewHolder(@NonNull editarForaneoAdapter.ViewHolderForaneo holder, int position) {
        holder.asignarDatos(foraneosList.get(position), position, listener);
    }

    @Override
    public int getItemCount() { return foraneosList.size(); }

    class ViewHolderForaneo extends RecyclerView.ViewHolder {
        //Hacemos referencias a la vista
        TextView txtNombreForaneo, txtEdadForaneo, txtCorreoForaneo;

        public ViewHolderForaneo(View itemView) {
            super(itemView);
            //Enlace vista
            txtNombreForaneo = (TextView) itemView.findViewById(R.id.tvNombreForaneos);
            txtEdadForaneo = (TextView) itemView.findViewById(R.id.tvEdad);
            txtCorreoForaneo = (TextView) itemView.findViewById(R.id.tvCorreoForaneo);
        }

        public void asignarDatos(UsuarioForaneo pojoForaneo, final int posicion, final OnItemClickListener listener) {
            txtNombreForaneo.setText(pojoForaneo.getNombre());
            txtEdadForaneo.setText(pojoForaneo.getEdad()+" Años");
            txtCorreoForaneo.setText(pojoForaneo.getCorreon());

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(posicion);
                }
            });
        }
    }
}

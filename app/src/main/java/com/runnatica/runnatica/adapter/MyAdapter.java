package com.runnatica.runnatica.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.runnatica.runnatica.R;
import com.runnatica.runnatica.poho.Competencias;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolderCompetencia> {

    private Context mCtx;
    private List<Competencias> competenciasList;
    private OnItemClickListener listener;

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public MyAdapter(Context mCtx, List<Competencias> competenciasList, OnItemClickListener listener) {
        this.mCtx = mCtx;
        this.competenciasList = competenciasList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyAdapter.ViewHolderCompetencia onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(parent.getContext() ,R.layout.recyclerview_competencias, null);
        return new ViewHolderCompetencia(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.ViewHolderCompetencia holder, int position) {
        holder.asignarDatos(competenciasList.get(position), position, listener);
    }

    @Override
    public int getItemCount() { return competenciasList.size(); }

    class ViewHolderCompetencia extends RecyclerView.ViewHolder {
        //Hacemos referencias a la vista
        TextView txtNombreCompetencias, txtDescripcionCompetencia, txtPrecioCompetencia, txtid;
        ImageView imgCompetencia;

        public ViewHolderCompetencia(View itemView) {
            super(itemView);
            //Enlace vista
            txtid = (TextView) itemView.findViewById(R.id.tvid);
            txtNombreCompetencias = (TextView) itemView.findViewById(R.id.tvNombreCompe);
            txtDescripcionCompetencia = (TextView) itemView.findViewById(R.id.tvDescripcionCompe);
            txtPrecioCompetencia = (TextView) itemView.findViewById(R.id.tvPrecioCompe);
            imgCompetencia = (ImageView) itemView.findViewById(R.id.imgvCompe);
        }

        public void asignarDatos(Competencias pojoCompetencia, final int posicion, final OnItemClickListener listener) {
            txtNombreCompetencias.setText(pojoCompetencia.getNombreCompetencia());
            txtDescripcionCompetencia.setText(pojoCompetencia.getDescripcionCompetencia());
            txtPrecioCompetencia.setText(pojoCompetencia.getPrecioCompetencia());
            if (!pojoCompetencia.getImageCompetencia().equals("null")){
                //imgCompetencia.setBackground(null);
                //Glide.with(mCtx).load(pojoCompetencia.getImageCompetencia()).into(imgCompetencia);
            }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.OnItemClick(posicion);
                }
            });
        }
    }
}
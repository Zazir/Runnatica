package com.runnatica.runnatica.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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

    public MyAdapter(Context mCtx, List<Competencias> competenciasList) {
        Log.i("Adaptador", "Inicializa el adaptador, se crea constructor");
        this.mCtx = mCtx;
        this.competenciasList = competenciasList;
    }

    @Override
    public ViewHolderCompetencia onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.lista_layout, null);
        return new ViewHolderCompetencia(view);
    }

    @Override
    public void onBindViewHolder(ViewHolderCompetencia holder, int position) {
        Competencias competencia = competenciasList.get(position);

        //Cargar imagen
        //Glide.with(mCtx).load(competencia.getImageCompetencia()).into(holder.imgCompetencia);
        //Cargar datos de la competencia
        //holder.txtid.setText(competencia.getId());
        holder.txtNombreCompetencias.setText(competencia.getNombreCompetencia());
        holder.txtDescripcionCompetencia.setText(competencia.getDescripcionCompetencia());
        holder.txtPrecioCompetencia.setText(String.valueOf(competencia.getPrecioCompetencia()));
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
    }
}
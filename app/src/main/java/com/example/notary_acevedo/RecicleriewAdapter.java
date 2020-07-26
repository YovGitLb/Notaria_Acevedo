package com.example.notary_acevedo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecicleriewAdapter extends RecyclerView.Adapter<RecicleriewAdapter.ViewHolder>{


    public static class ViewHolder extends RecyclerView.ViewHolder{

        private TextView nombre, requisitos;
        ImageView imgservicios;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            nombre=itemView.findViewById(R.id.txtanticipo);
            requisitos=itemView.findViewById(R.id.txtrequisitos);
            imgservicios=itemView.findViewById(R.id.image_anticipo);

        }
    }

    public List<Servicios_Notariales> servicio_lista;

    public RecicleriewAdapter(List<Servicios_Notariales> servicio_lista) {
        this.servicio_lista = servicio_lista;
    }




    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_servicios_notariales,parent,false);
        ViewHolder viewHolder=new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.nombre.setText(servicio_lista.get(position).getNombre());
        holder.requisitos.setText(servicio_lista.get(position).getRequisitos());
        holder.imgservicios.setImageResource(servicio_lista.get(position).getImgservicios());
    }

    @Override
    public int getItemCount() {
        return servicio_lista.size();
    }



}

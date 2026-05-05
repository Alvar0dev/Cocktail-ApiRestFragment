package com.example.simulacroCocktail.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simulacroCocktail.R;
import com.example.simulacroCocktail.models.Cocktail;
import com.squareup.picasso.Picasso;
import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ViewHolder> {

    private List<Cocktail> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Cocktail cocktail);
    }

    public FavoritosAdapter(List<Cocktail> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_elemento, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cocktail e = lista.get(position);

        holder.tvNombre.setText(e.getAtriString1());

        // Aplicar tamaño de letra desde SharedPreferences
        SharedPreferences prefs = holder.itemView.getContext().getSharedPreferences("AjustesApp", Context.MODE_PRIVATE);
        float tamanoLetra = prefs.getFloat("TAMANO_LETRA", 18f); 
        holder.tvNombre.setTextSize(tamanoLetra);

        if (e.getAtriString2() != null && !e.getAtriString2().isEmpty()) {
            Picasso.get().load(e.getAtriString2()).into(holder.ivFoto);
        }

        // En la lista de favoritos, la estrella siempre está activa
        holder.btnEstrella.setImageResource(android.R.drawable.btn_star_big_on);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizar(List<Cocktail> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        TextView tvNombre;
        ImageButton btnEstrella;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoto = itemView.findViewById(R.id.ivCocktail);
            tvNombre = itemView.findViewById(R.id.tvNombreCocktail);
            btnEstrella = itemView.findViewById(R.id.btnFavorito);
        }
    }
}
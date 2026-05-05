package com.example.simulacroCocktail.adapters;

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

public class ApiAdapter extends RecyclerView.Adapter<ApiAdapter.MyViewHolder> {
    
    private List<Cocktail> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onFavoritoClick(Cocktail cocktail);
    }

    public ApiAdapter(List<Cocktail> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_elemento, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cocktail c = lista.get(position);
        holder.tvNombre.setText(c.getAtriString1());
        
        if (c.getAtriString2() != null && !c.getAtriString2().isEmpty()) {
            Picasso.get().load(c.getAtriString2()).into(holder.ivFoto);
        }

        holder.btnFav.setOnClickListener(v -> {
            holder.btnFav.setImageResource(android.R.drawable.btn_star_big_on);
            if (listener != null) {
                listener.onFavoritoClick(c);
            }
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public void actualizarLista(List<Cocktail> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        TextView tvNombre;
        ImageButton btnFav;

        public MyViewHolder(@NonNull View v) {
            super(v);
            ivFoto = v.findViewById(R.id.ivCocktail);
            tvNombre = v.findViewById(R.id.tvNombreCocktail);
            btnFav = v.findViewById(R.id.btnFavorito);
        }
    }
}
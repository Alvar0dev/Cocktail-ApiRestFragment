package com.example.simulacroCocktail.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simulacroCocktail.adapters.FavoritosAdapter;
import com.example.simulacroCocktail.R;
import com.example.simulacroCocktail.database.CocktailDAO;
import com.example.simulacroCocktail.models.Cocktail;

import java.util.List;

public class ListaFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritosAdapter adapter;
    private CocktailDAO dao;
    private List<Cocktail> listaFavoritos;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dao = new CocktailDAO(getContext());
        listaFavoritos = dao.obtenerTodos();

        adapter = new FavoritosAdapter(listaFavoritos, cocktail -> {
            Toast.makeText(getContext(), "Favorito: " + cocktail.getAtriString1(), Toast.LENGTH_SHORT).show();
        });

        recyclerView.setAdapter(adapter);

        return view;
    }

    public void cargarDatos() {
        if (dao != null && adapter != null) {
            listaFavoritos = dao.obtenerTodos();
            adapter.actualizar(listaFavoritos);
        }
    }
}
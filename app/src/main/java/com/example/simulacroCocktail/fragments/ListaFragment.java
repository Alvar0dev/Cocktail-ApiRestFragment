package com.example.simulacroCocktail.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simulacroCocktail.DetalleActivity;
import com.example.simulacroCocktail.MainActivity;
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

    private final ActivityResultLauncher<Intent> detalleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    cargarDatos();
                    // También avisamos a la API para que quite la estrella rellena
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).actualizarListaApi();
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dao = new CocktailDAO(getContext());
        listaFavoritos = dao.obtenerTodos();

        adapter = new FavoritosAdapter(listaFavoritos, cocktail -> {
            Intent intent = new Intent(getContext(), DetalleActivity.class);
            intent.putExtra("cocktail", cocktail);
            detalleLauncher.launch(intent);
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
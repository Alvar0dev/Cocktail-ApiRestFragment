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

import com.example.simulacroCocktail.api.CocktailResponse;
import com.example.simulacroCocktail.MainActivity;
import com.example.simulacroCocktail.R;
import com.example.simulacroCocktail.api.RetrofitClient;
import com.example.simulacroCocktail.adapters.ApiAdapter;
import com.example.simulacroCocktail.database.CocktailDAO;
import com.example.simulacroCocktail.models.Cocktail;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ApiFragment extends Fragment {

    private RecyclerView recyclerView;
    private ApiAdapter adaptador;
    private List<Cocktail> listaElementos = new ArrayList<>();
    private CocktailDAO dao;

    public ApiFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_api, container, false);

        dao = new CocktailDAO(getContext());

        recyclerView = view.findViewById(R.id.rvElementosApi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        adaptador = new ApiAdapter(listaElementos, new ApiAdapter.OnItemClickListener() {
            @Override
            public void onFavoritoClick(Cocktail elemento) {
                dao.insertar(elemento);
                Toast.makeText(getContext(), elemento.getAtriString1() + " añadido a favoritos", Toast.LENGTH_SHORT).show();

                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).actualizarListaFavoritos();
                }
            }
        });

        recyclerView.setAdapter(adaptador);
        cargarDatosDeInternet();

        return view;
    }

    public void notificarCambioAdapter() {
        if (adaptador != null) {
            adaptador.notifyDataSetChanged();
        }
    }

    private void cargarDatosDeInternet() {
        RetrofitClient.getApiService().getElementos().enqueue(new Callback<CocktailResponse>() {
            @Override
            public void onResponse(Call<CocktailResponse> call, Response<CocktailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Cocktail> descargados = response.body().getListaCocktail();
                    adaptador.actualizarLista(descargados);
                } else {
                    Toast.makeText(getContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CocktailResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

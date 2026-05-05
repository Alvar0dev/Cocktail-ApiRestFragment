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
import com.example.simulacroCocktail.database.DbHelper;
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

        // 1. Inicializar DAO
        dao = new CocktailDAO(getContext());

        // 2. Configurar RecyclerView
        recyclerView = view.findViewById(R.id.rvElementosApi);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // 3. Configurar Adaptador con el listener para la estrella
        adaptador = new ApiAdapter(listaElementos, new ApiAdapter.OnItemClickListener() {
            @Override
            public void onFavoritoClick(Cocktail elemento) {
                // GUARDAR EN SQLITE
                dao.insertar(elemento);
                Toast.makeText(getContext(), elemento.getAtriString1() + " añadido a favoritos", Toast.LENGTH_SHORT).show();

                // AVISAR AL PADRE (MainActivity) para que refresque el fragment de abajo
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).actualizarListaFavoritos();
                }
            }
        });

        recyclerView.setAdapter(adaptador);

        // 4. Lanzar la descarga de datos
        cargarDatosDeInternet();

        return view;
    }

    private void cargarDatosDeInternet() {
        // Llamada usando el RetrofitClient y la interfaz ApiService
        RetrofitClient.getApiService().getElementos().enqueue(new Callback<CocktailResponse>() {
            @Override
            public void onResponse(Call<CocktailResponse> call, Response<CocktailResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Actualizar la lista con los datos que vienen de la API ("drinks")
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

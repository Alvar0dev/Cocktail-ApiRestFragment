package com.example.simulacroCocktail.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    // Nombre corregido para que coincida con el Fragment
    @GET("filter.php?a=Alcoholic")
    Call<CocktailResponse> getElementos();
}
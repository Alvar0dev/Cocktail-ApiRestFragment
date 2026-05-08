package com.example.simulacroCocktail.api;

import java.util.List;
import com.example.simulacroCocktail.models.Cocktail;
import com.google.gson.annotations.SerializedName;

public class CocktailResponse {
    
    // @SerializedName("drinks") es OBLIGATORIO porque así se llama en el JSON de la API
    @SerializedName("drinks")
    private List<Cocktail> drinks;

    // Ver estructura aqui  = "https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic"

    // Cambiamos el nombre del getter para que coincida con lo que usas en el Fragment
    public List<Cocktail> getListaCocktail() {
        return drinks;
    }

    public void setListaElementos(List<Cocktail> drinks) {
        this.drinks = drinks;
    }
}
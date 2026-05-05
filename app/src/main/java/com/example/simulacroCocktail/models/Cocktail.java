package com.example.simulacroCocktail.models;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Cocktail implements Serializable {

    @SerializedName("idDrink")
    private int id;

    @SerializedName("strDrink")
    private String atriString1;

    @SerializedName("strDrinkThumb")
    private String atriString2;

    private boolean atriBoolean1;

    public Cocktail() {}

    public Cocktail(int id, String atriString1, String atriString2, boolean atriBoolean1) {
        this.id = id;
        this.atriString1 = atriString1;
        this.atriString2 = atriString2;
        this.atriBoolean1 = atriBoolean1;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getAtriString1() { return atriString1; }
    public void setAtriString1(String atriString1) { this.atriString1 = atriString1; }

    public String getAtriString2() { return atriString2; }
    public void setAtriString2(String atriString2) { this.atriString2 = atriString2; }

    public boolean isAtriBoolean1() { return atriBoolean1; }
    public void setAtriBoolean1(boolean atriBoolean1) { this.atriBoolean1 = atriBoolean1; }
}
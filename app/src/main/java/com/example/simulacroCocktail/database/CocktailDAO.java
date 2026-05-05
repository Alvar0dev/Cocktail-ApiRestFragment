package com.example.simulacroCocktail.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.simulacroCocktail.models.Cocktail;
import java.util.ArrayList;

public class CocktailDAO {
    private DbHelper helper;
    public CocktailDAO(Context c) { helper = new DbHelper(c); }

    public long insertar(Cocktail e) {
        if (existe(e.getId())) return -1;
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("id", e.getId());
        v.put("atriString1", e.getAtriString1());
        v.put("atriString2", e.getAtriString2());
        v.put("atriBoolean1", 1); // Lo marcamos como favorito al insertar
        long id = db.insert(DbHelper.TABLE, null, v);
        db.close();
        return id;
    }

    public boolean existe(int id) {
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DbHelper.TABLE + " WHERE id = ?", new String[]{String.valueOf(id)});
        boolean res = c.getCount() > 0;
        c.close();
        db.close();
        return res;
    }

    public void borrar(int id) {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DbHelper.TABLE, "id = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    public ArrayList<Cocktail> obtenerTodos() {
        ArrayList<Cocktail> lista = new ArrayList<>();
        SQLiteDatabase db = helper.getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM " + DbHelper.TABLE, null);
        if (c.moveToFirst()) {
            do {
                Cocktail e = new Cocktail();
                e.setId(c.getInt(0));
                e.setAtriString1(c.getString(1));
                e.setAtriString2(c.getString(2));
                e.setAtriBoolean1(c.getInt(3) == 1); 
                lista.add(e);
            } while (c.moveToNext());
        }
        c.close();
        db.close();
        return lista;
    }

    public void borrarTodos() {
        SQLiteDatabase db = helper.getWritableDatabase();
        db.delete(DbHelper.TABLE, null, null);
        db.close();
    }
}
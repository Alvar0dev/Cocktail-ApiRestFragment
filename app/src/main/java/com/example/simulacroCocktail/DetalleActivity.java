package com.example.simulacroCocktail;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import com.example.simulacroCocktail.database.CocktailDAO;
import com.example.simulacroCocktail.models.Cocktail;
import com.squareup.picasso.Picasso;

public class DetalleActivity extends AppCompatActivity {

    private Cocktail cocktail;
    private CocktailDAO dao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detalle);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        dao = new CocktailDAO(this);
        cocktail = (Cocktail) getIntent().getSerializableExtra("cocktail");

        TextView tvNombre = findViewById(R.id.tvNombreDetalle);
        ImageView ivFoto = findViewById(R.id.ivFotoDetalle);
        Button btnEliminar = findViewById(R.id.btnEliminarFav);

        if (cocktail != null) {
            tvNombre.setText(cocktail.getAtriString1());
            Picasso.get().load(cocktail.getAtriString2()).into(ivFoto);
        }

        btnEliminar.setOnClickListener(v -> {
            if (cocktail != null) {
                dao.borrar(cocktail.getId());
                setResult(RESULT_OK);
                finish();
            }
        });
    }
}
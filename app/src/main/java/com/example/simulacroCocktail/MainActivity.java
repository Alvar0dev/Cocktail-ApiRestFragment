package com.example.simulacroCocktail;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import com.example.simulacroCocktail.fragments.ApiFragment;
import com.example.simulacroCocktail.fragments.ListaFragment;

public class MainActivity extends AppCompatActivity {

    SharedPreferences prefs = getSharedPreferences("AjustesApp", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (savedInstanceState == null) {
            Fragment superior = new ApiFragment();
            Fragment inferior = new ListaFragment();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container_fragment_1, superior)
                    .replace(R.id.container_fragment_2, inferior, "TAG_LISTA_FRAGMENT")
                    .commit();
        }
    }

    // 1. Inflar el menú (hacer que aparezca en la barra)
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    // 2. Darle funcionalidad a los botones del menú
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_changetext) {


            // Acción al pulsar Añadir

            return true;
        } else if (id == R.id.action_back) {

            //  Acción al pulsar atras
            finish();


            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void actualizarListaFavoritos() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("TAG_LISTA_FRAGMENT");
        if (fragment instanceof ListaFragment) {
            ((ListaFragment) fragment).cargarDatos();
        }
    }
}
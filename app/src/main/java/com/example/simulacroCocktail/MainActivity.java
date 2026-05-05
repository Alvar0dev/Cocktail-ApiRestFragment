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
                    .replace(R.id.container_fragment_1, superior, "TAG_API_FRAGMENT")
                    .replace(R.id.container_fragment_2, inferior, "TAG_LISTA_FRAGMENT")
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_change_font_size) {
            SharedPreferences prefs = getSharedPreferences("AjustesApp", Context.MODE_PRIVATE);
            float currentSize = prefs.getFloat("TAMANO_LETRA", 18f);
            float newSize = currentSize >= 30f ? 14f : currentSize + 4f;
            
            prefs.edit().putFloat("TAMANO_LETRA", newSize).apply();
            
            // Refrescar fragments para aplicar el cambio
            actualizarListaFavoritos();
            actualizarListaApi();
            return true;
        } else if (id == R.id.action_reset) {
            getSharedPreferences("AjustesApp", Context.MODE_PRIVATE).edit().clear().apply();
            actualizarListaFavoritos();
            actualizarListaApi();
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

    public void actualizarListaApi() {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag("TAG_API_FRAGMENT");
        if (fragment instanceof ApiFragment) {
            ((ApiFragment) fragment).notificarCambioAdapter();
        }
    }
}
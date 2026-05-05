# Proyecto SimulacroCocktail

## Archivo: .\app\src\androidTest\java\com\example\simulacrococtail\ExampleInstrumentedTest.java

``java
package com.example.simulacroCocktail;

import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.simulacroCocktail", appContext.getPackageName());
    }
}
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\adapters\ApiAdapter.java

``java
package com.example.simulacroCocktail.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.simulacroCocktail.R;
import com.example.simulacroCocktail.models.Cocktail;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ApiAdapter extends RecyclerView.Adapter<ApiAdapter.MyViewHolder> {
    
    private List<Cocktail> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onFavoritoClick(Cocktail cocktail);
    }

    public ApiAdapter(List<Cocktail> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_elemento, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cocktail c = lista.get(position);
        holder.tvNombre.setText(c.getAtriString1());
        
        if (c.getAtriString2() != null && !c.getAtriString2().isEmpty()) {
            Picasso.get().load(c.getAtriString2()).into(holder.ivFoto);
        }

        holder.btnFav.setOnClickListener(v -> {
            holder.btnFav.setImageResource(android.R.drawable.btn_star_big_on);
            if (listener != null) {
                listener.onFavoritoClick(c);
            }
        });
    }

    @Override
    public int getItemCount() { return lista.size(); }

    public void actualizarLista(List<Cocktail> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        TextView tvNombre;
        ImageButton btnFav;

        public MyViewHolder(@NonNull View v) {
            super(v);
            ivFoto = v.findViewById(R.id.ivCocktail);
            tvNombre = v.findViewById(R.id.tvNombreCocktail);
            btnFav = v.findViewById(R.id.btnFavorito);
        }
    }
}
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\adapters\FavoritosAdapter.java

``java
package com.example.simulacroCocktail.adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simulacroCocktail.R;
import com.example.simulacroCocktail.models.Cocktail;
import com.squareup.picasso.Picasso;
import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ViewHolder> {

    private List<Cocktail> lista;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Cocktail cocktail);
    }

    public FavoritosAdapter(List<Cocktail> lista, OnItemClickListener listener) {
        this.lista = lista;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_elemento, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cocktail e = lista.get(position);

        holder.tvNombre.setText(e.getAtriString1());

        SharedPreferences prefs = holder.itemView.getContext().getSharedPreferences("AjustesApp", Context.MODE_PRIVATE);
        float tamanoLetra = prefs.getFloat("TAMANO_LETRA", 18f); 
        holder.tvNombre.setTextSize(tamanoLetra);

        if (e.getAtriString2() != null && !e.getAtriString2().isEmpty()) {
            Picasso.get().load(e.getAtriString2()).into(holder.ivFoto);
        }

        holder.btnEstrella.setImageResource(android.R.drawable.btn_star_big_on);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(e);
            }
        });
    }

    @Override
    public int getItemCount() {
        return lista.size();
    }

    public void actualizar(List<Cocktail> nuevaLista) {
        this.lista = nuevaLista;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivFoto;
        TextView tvNombre;
        ImageButton btnEstrella;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivFoto = itemView.findViewById(R.id.ivCocktail);
            tvNombre = itemView.findViewById(R.id.tvNombreCocktail);
            btnEstrella = itemView.findViewById(R.id.btnFavorito);
        }
    }
}
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\api\ApiService.java

``java
package com.example.simulacroCocktail.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    // Nombre corregido para que coincida con el Fragment
    @GET("filter.php?a=Alcoholic")
    Call<CocktailResponse> getElementos();
}
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\api\CocktailResponse.java

``java
package com.example.simulacroCocktail.api;

import java.util.List;
import com.example.simulacroCocktail.models.Cocktail;
import com.google.gson.annotations.SerializedName;

public class CocktailResponse {
    
    // @SerializedName("drinks") es OBLIGATORIO porque así se llama en el JSON de la API
    @SerializedName("drinks")
    private List<Cocktail> drinks;

    // Cambiamos el nombre del getter para que coincida con lo que usas en el Fragment
    public List<Cocktail> getListaCocktail() {
        return drinks;
    }

    public void setListaElementos(List<Cocktail> drinks) {
        this.drinks = drinks;
    }
}
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\api\RetrofitClient.java

``java
package com.example.simulacroCocktail.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static final String BASE_URL = "https://www.thecocktaildb.com/api/json/v1/1/";
    private static Retrofit retrofit = null;

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    // Método añadido para que RetrofitClient.getApiService() funcione en el Fragment
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\database\CocktailDAO.java

``java
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
        SQLiteDatabase db = helper.getWritableDatabase();
        ContentValues v = new ContentValues();
        v.put("atriString1", e.getAtriString1());
        v.put("atriString2", e.getAtriString2());
        v.put("atriBoolean1", e.isAtriBoolean1() ? 1 : 0);
        long id = db.insert(DbHelper.TABLE, null, v);
        db.close();
        return id;
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
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\database\DbHelper.java

``java
package com.example.simulacroCocktail.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "examen.db";
    public static final int DB_VERSION = 1;
    public static final String TABLE = "tabla_elementos";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + " (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "atriString1 TEXT, " +
            "atriString2 TEXT, " +
            "atriBoolean1 INTEGER)";

    public DbHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    @Override
    public void onCreate(SQLiteDatabase db) { db.execSQL(CREATE_TABLE); }

    @Override
    public void onUpgrade(SQLiteDatabase db, int old, int n) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\fragments\ApiFragment.java

``java
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
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\fragments\ListaFragment.java

``java
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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dao = new CocktailDAO(getContext());
        listaFavoritos = dao.obtenerTodos();

        adapter = new FavoritosAdapter(listaFavoritos, cocktail -> {
            Toast.makeText(getContext(), "Favorito: " + cocktail.getAtriString1(), Toast.LENGTH_SHORT).show();
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
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\models\Cocktail.java

``java
package com.example.simulacroCocktail.models;

import com.google.gson.annotations.SerializedName;

public class Cocktail {

    private int id;

    @SerializedName("strDrink")
    private String atriString1;

    @SerializedName("strDrinkThumb")
    private String atriString2;

    private boolean atriBoolean1;

    public Cocktail() {}

    public Cocktail(String atriString1, String atriString2, boolean atriBoolean1) {
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
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\DetalleActivity.java

``java
package com.example.simulacroCocktail;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class DetalleActivity extends AppCompatActivity {

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
    }
}
``

## Archivo: .\app\src\main\java\com\example\simulacroCocktail\MainActivity.java

``java
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
``

## Archivo: .\app\src\main\res\layout\activity_detalle.xml

``xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.cardview.widget.CardView
    xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:layout_margin="8dp"
    app:cardElevation="4dp"
    app:cardCornerRadius="8dp">

    <androidx.constraintlayout.widget.ConstraintLayout
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:padding="16dp">

        <TextView
            android:id="@+id/tvString1"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:text="Nombre Elemento"
            android:textSize="18sp"
            android:textStyle="bold"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toTopOf="parent" />

        <TextView
            android:id="@+id/tvString2"
            android:layout_width="0dp"
            android:layout_height="wrap_content"
            android:layout_marginTop="4dp"
            android:alpha="0.7"
            android:text="Descripción o subtítulo aquí"
            app:layout_constraintBottom_toBottomOf="parent"
            app:layout_constraintEnd_toEndOf="parent"
            app:layout_constraintHorizontal_bias="0.0"
            app:layout_constraintStart_toStartOf="parent"
            app:layout_constraintTop_toBottomOf="@+id/tvString1"
            app:layout_constraintVertical_bias="0.195" />

        <ImageView
            android:id="@+id/imageView"
            android:layout_width="78dp"
            android:layout_height="90dp"
            tools:layout_editor_absoluteX="286dp"
            tools:layout_editor_absoluteY="-5dp"
            tools:srcCompat="@tools:sample/avatars" />

        <Button
            android:id="@+id/button"
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Eliminar de Favoritos"
            tools:layout_editor_absoluteX="87dp"
            tools:layout_editor_absoluteY="71dp" />

    </androidx.constraintlayout.widget.ConstraintLayout>
</androidx.cardview.widget.CardView>
``

## Archivo: .\app\src\main\res\layout\activity_main.xml

``xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <!-- Contenedor para el primer Fragment -->
    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/container_fragment_1"
        android:name="com.example.simulacroCocktail.fragments.ApiFragment"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />

    <!-- Contenedor para el segundo Fragment -->
    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/container_fragment_2"
        android:name="com.example.simulacroCocktail.fragments.ListaFragment"
        android:layout_width="match_parent"
        android:layout_height="0dp"
        android:layout_weight="1" />

</LinearLayout>
``

## Archivo: .\app\src\main\res\layout\fragment_api.xml

``xml
<?xml version="1.0" encoding="utf-8"?>
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical">

    <TextView
        android:id="@+id/textView"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:gravity="center"
        android:padding="10dp"
        android:text="Elementos Disponibles"
        android:textSize="20sp"
        android:textStyle="bold"
        tools:layout_editor_absoluteX="0dp"
        tools:layout_editor_absoluteY="0dp"
        app:layout_constraintBottom_toTopOf="@+id/rvElementosApi"/>

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/rvElementosApi"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:layout_constraintTop_toBottomOf="@+id/textView"
        tools:layout_editor_absoluteX="0dp" />
</androidx.constraintlayout.widget.ConstraintLayout>
``

## Archivo: .\app\src\main\res\layout\fragment_lista.xml

``xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <androidx.recyclerview.widget.RecyclerView
        android:id="@+id/recyclerView"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        android:padding="8dp" />
</FrameLayout>
``

## Archivo: .\app\src\main\res\layout\item_elemento.xml

``xml
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    android:orientation="horizontal"
    android:padding="10dp"
    android:gravity="center_vertical">

    <ImageView
        android:id="@+id/ivCocktail"
        android:layout_width="80dp"
        android:layout_height="80dp"
        android:scaleType="centerCrop"
        android:src="@mipmap/ic_launcher" />

    <TextView
        android:id="@+id/tvNombreCocktail"
        android:layout_width="0dp"
        android:layout_height="wrap_content"
        android:layout_weight="1"
        android:layout_marginStart="15dp"
        android:text="Nombre del Cocktail"
        android:textSize="18sp"
        android:textStyle="bold"
        android:textColor="@android:color/black" />

    <ImageButton
        android:id="@+id/btnFavorito"
        android:layout_width="48dp"
        android:layout_height="48dp"
        android:background="?attr/selectableItemBackgroundBorderless"
        android:src="@android:drawable/btn_star_big_off"
        android:contentDescription="Favorito" />

</LinearLayout>
``

## Archivo: .\app\src\main\res\menu\menu_principal.xml

``xml
<?xml version="1.0" encoding="utf-8"?>
    XML
    <?xml version="1.0" encoding="utf-8"?>
<menu xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto">

    <item
        android:id="@+id/action_changetext"
        android:title="Añadir"
        android:icon="@android:drawable/ic_menu_add"
        app:showAsAction="ifRoom" />

    <item
        android:id="@+id/action_back"
        android:icon="@android:drawable/ic_menu_revert"
        android:title="Añadir"
        app:showAsAction="ifRoom" />
</menu>
``

## Archivo: .\app\src\main\res\values\colors.xml

``xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
</resources>
``

## Archivo: .\app\src\main\res\values\strings.xml

``xml
<resources>
    <string name="app_name">SimulacroCoctail</string>
</resources>
``

## Archivo: .\app\src\main\res\values\themes.xml

``xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.SimulacroCoctail" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your light theme here. -->
        <!-- <item name="colorPrimary">@color/my_light_primary</item> -->
    </style>

    <style name="Theme.SimulacroCoctail" parent="Base.Theme.SimulacroCoctail" />
</resources>
``

## Archivo: .\app\src\main\res\values-night\themes.xml

``xml
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.SimulacroCoctail" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your dark theme here. -->
        <!-- <item name="colorPrimary">@color/my_dark_primary</item> -->
    </style>
</resources>
``

## Archivo: .\app\src\main\AndroidManifest.xml

``xml
<?xml version="1.0" encoding="utf-8"?>
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <uses-permission android:name="android.permission.INTERNET" />

    <application
        android:allowBackup="true"
        android:dataExtractionRules="@xml/data_extraction_rules"
        android:fullBackupContent="@xml/backup_rules"
        android:icon="@mipmap/ic_launcher"
        android:label="@string/app_name"
        android:roundIcon="@mipmap/ic_launcher_round"
        android:supportsRtl="true"
        android:theme="@style/Theme.SimulacroCoctail">
        <activity
            android:name=".DetalleActivity"
            android:exported="false" />
        <activity
            android:name=".MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />

                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>
    </application>

</manifest>
``

## Archivo: .\app\src\test\java\com\example\simulacrococtail\ExampleUnitTest.java

``java
package com.example.simulacroCocktail;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }
}
``

## Archivo: .\app\build.gradle.kts

``kotlin
plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.simulacroCocktail"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.simulacroCocktail"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // 🌐 Retrofit + GSON
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    
    // 🖼️ Picasso (Para cargar imágenes desde URL)
    implementation("com.squareup.picasso:picasso:2.71828")

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.fragment)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}
``

## Archivo: .\gradle\libs.versions.toml

``properties
[versions]
agp = "8.13.2"
junit = "4.13.2"
junitVersion = "1.3.0"
espressoCore = "3.7.0"
appcompat = "1.7.1"
material = "1.13.0"
activity = "1.13.0"
constraintlayout = "2.2.1"
fragment = "1.8.9"

[libraries]
junit = { group = "junit", name = "junit", version.ref = "junit" }
ext-junit = { group = "androidx.test.ext", name = "junit", version.ref = "junitVersion" }
espresso-core = { group = "androidx.test.espresso", name = "espresso-core", version.ref = "espressoCore" }
appcompat = { group = "androidx.appcompat", name = "appcompat", version.ref = "appcompat" }
material = { group = "com.google.android.material", name = "material", version.ref = "material" }
activity = { group = "androidx.activity", name = "activity", version.ref = "activity" }
constraintlayout = { group = "androidx.constraintlayout", name = "constraintlayout", version.ref = "constraintlayout" }
fragment = { group = "androidx.fragment", name = "fragment", version.ref = "fragment" }

[plugins]
android-application = { id = "com.android.application", version.ref = "agp" }

``

## Archivo: .\build.gradle.kts

``kotlin
// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
}
``

## Archivo: .\gradle.properties

``properties
# Project-wide Gradle settings.
# IDE (e.g. Android Studio) users:
# Gradle settings configured through the IDE *will override*
# any settings specified in this file.
# For more details on how to configure your build environment visit
# http://www.gradle.org/docs/current/userguide/build_environment.html
# Specifies the JVM arguments used for the daemon process.
# The setting is particularly useful for tweaking memory settings.
org.gradle.jvmargs=-Xmx2048m -Dfile.encoding=UTF-8
# When configured, Gradle will run in incubating parallel mode.
# This option should only be used with decoupled projects. For more details, visit
# https://developer.android.com/r/tools/gradle-multi-project-decoupled-projects
# org.gradle.parallel=true
# AndroidX package structure to make it clearer which packages are bundled with the
# Android operating system, and which are packaged with your app's APK
# https://developer.android.com/topic/libraries/support-library/androidx-rn
android.useAndroidX=true
# Enables namespacing of each library's R class so that its R class includes only the
# resources declared in the library itself and none from the library's dependencies,
# thereby reducing the size of the R class for that library
android.nonTransitiveRClass=true
``

## Archivo: .\local.properties

``properties
## This file is automatically generated by Android Studio.
# Do not modify this file -- YOUR CHANGES WILL BE ERASED!
#
# This file should *NOT* be checked into Version Control Systems,
# as it contains information specific to your local configuration.
#
# Location of the SDK. This is only used by Gradle.
# For customization when using a Version Control System, please read the
# header note.
sdk.dir=C\:\\Users\\alvar\\AppData\\Local\\Android\\Sdk
``

## Archivo: .\settings.gradle.kts

``kotlin
pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "SimulacroCocktail"
include(":app")
``


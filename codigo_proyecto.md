### AndroidManifest.xml
`$lang
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
``n
### DetalleActivity.java
`$lang
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
``n
### MainActivity.java
`$lang
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
``n
### ApiAdapter.java
`$lang
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
import com.example.simulacroCocktail.database.CocktailDAO;
import com.example.simulacroCocktail.models.Cocktail;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ApiAdapter extends RecyclerView.Adapter<ApiAdapter.MyViewHolder> {
    
    private List<Cocktail> lista;
    private OnItemClickListener listener;
    private CocktailDAO dao;

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
        dao = new CocktailDAO(parent.getContext());
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Cocktail c = lista.get(position);
        holder.tvNombre.setText(c.getAtriString1());
        
        // Aplicar tamaÃ±o de letra desde SharedPreferences
        SharedPreferences prefs = holder.itemView.getContext().getSharedPreferences("AjustesApp", Context.MODE_PRIVATE);
        float tamanoLetra = prefs.getFloat("TAMANO_LETRA", 18f); 
        holder.tvNombre.setTextSize(tamanoLetra);

        if (c.getAtriString2() != null && !c.getAtriString2().isEmpty()) {
            Picasso.get().load(c.getAtriString2()).into(holder.ivFoto);
        }

        // Verificar si es favorito para poner la estrella rellena o vacÃ­a
        if (dao.existe(c.getId())) {
            holder.btnFav.setImageResource(android.R.drawable.btn_star_big_on);
        } else {
            holder.btnFav.setImageResource(android.R.drawable.btn_star_big_off);
        }

        holder.btnFav.setOnClickListener(v -> {
            if (!dao.existe(c.getId())) {
                holder.btnFav.setImageResource(android.R.drawable.btn_star_big_on);
                if (listener != null) {
                    listener.onFavoritoClick(c);
                }
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
``n
### FavoritosAdapter.java
`$lang
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

        // Aplicar tamaÃ±o de letra desde SharedPreferences
        SharedPreferences prefs = holder.itemView.getContext().getSharedPreferences("AjustesApp", Context.MODE_PRIVATE);
        float tamanoLetra = prefs.getFloat("TAMANO_LETRA", 18f); 
        holder.tvNombre.setTextSize(tamanoLetra);

        if (e.getAtriString2() != null && !e.getAtriString2().isEmpty()) {
            Picasso.get().load(e.getAtriString2()).into(holder.ivFoto);
        }

        // En la lista de favoritos, la estrella siempre estÃ¡ activa
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
``n
### ApiService.java
`$lang
package com.example.simulacroCocktail.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    // Nombre corregido para que coincida con el Fragment
    @GET("filter.php?a=Alcoholic")
    Call<CocktailResponse> getElementos();
}
``n
### CocktailResponse.java
`$lang
package com.example.simulacroCocktail.api;

import java.util.List;
import com.example.simulacroCocktail.models.Cocktail;
import com.google.gson.annotations.SerializedName;

public class CocktailResponse {
    
    // @SerializedName("drinks") es OBLIGATORIO porque asÃ­ se llama en el JSON de la API
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
``n
### RetrofitClient.java
`$lang
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

    // MÃ©todo aÃ±adido para que RetrofitClient.getApiService() funcione en el Fragment
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}
``n
### CocktailDAO.java
`$lang
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
``n
### DbHelper.java
`$lang
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
``n
### ApiFragment.java
`$lang
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
                Toast.makeText(getContext(), elemento.getAtriString1() + " aÃ±adido a favoritos", Toast.LENGTH_SHORT).show();

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

``n
### ListaFragment.java
`$lang
package com.example.simulacroCocktail.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.simulacroCocktail.DetalleActivity;
import com.example.simulacroCocktail.MainActivity;
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

    private final ActivityResultLauncher<Intent> detalleLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    cargarDatos();
                    // TambiÃ©n avisamos a la API para que quite la estrella rellena
                    if (getActivity() instanceof MainActivity) {
                        ((MainActivity) getActivity()).actualizarListaApi();
                    }
                }
            }
    );

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lista, container, false);

        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        dao = new CocktailDAO(getContext());
        listaFavoritos = dao.obtenerTodos();

        adapter = new FavoritosAdapter(listaFavoritos, cocktail -> {
            Intent intent = new Intent(getContext(), DetalleActivity.class);
            intent.putExtra("cocktail", cocktail);
            detalleLauncher.launch(intent);
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
``n
### Cocktail.java
`$lang
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
``n
### activity_detalle.xml
`$lang
<?xml version="1.0" encoding="utf-8"?>
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/main"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:gravity="center_horizontal"
    android:padding="20dp">

    <TextView
        android:id="@+id/tvNombreDetalle"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        android:text="Nombre Cocktail"
        android:textSize="24sp"
        android:textStyle="bold"
        android:layout_marginBottom="20dp"/>

    <ImageView
        android:id="@+id/ivFotoDetalle"
        android:layout_width="200dp"
        android:layout_height="200dp"
        android:scaleType="centerCrop"
        android:layout_marginBottom="30dp"/>

    <Button
        android:id="@+id/btnEliminarFav"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:text="Eliminar de favoritos"
        android:backgroundTint="#6750A4"
        android:textColor="@android:color/white"/>

</LinearLayout>
``n
### activity_main.xml
`$lang
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
``n
### fragment_api.xml
`$lang
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
``n
### fragment_lista.xml
`$lang
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
``n
### item_elemento.xml
`$lang
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

``n
### colors.xml
`$lang
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <color name="black">#FF000000</color>
    <color name="white">#FFFFFFFF</color>
</resources>
``n
### strings.xml
`$lang
<resources>
    <string name="app_name">SimulacroCoctail</string>
</resources>
``n
### themes.xml
`$lang
<resources xmlns:tools="http://schemas.android.com/tools">
    <!-- Base application theme. -->
    <style name="Base.Theme.SimulacroCoctail" parent="Theme.Material3.DayNight.NoActionBar">
        <!-- Customize your light theme here. -->
        <!-- <item name="colorPrimary">@color/my_light_primary</item> -->
    </style>

    <style name="Theme.SimulacroCoctail" parent="Base.Theme.SimulacroCoctail" />
</resources>
``n


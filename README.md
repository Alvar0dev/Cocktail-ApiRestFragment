# SimulacroCocktail - Práctica Android

Este proyecto es una aplicación de Android que gestiona una lista de cócteles obtenida de una API externa, permitiendo guardar favoritos en una base de datos local y personalizar la interfaz.

## 🚀 Tecnologías Utilizadas

*   **Retrofit + GSON**: Para el consumo de la API REST [TheCocktailDB](https://www.thecocktaildb.com/).
*   **SQLite (OpenHelper)**: Para la persistencia de los cócteles marcados como favoritos.
*   **Picasso**: Para la carga y caché de imágenes desde URL.
*   **SharedPreferences**: Para almacenar de forma persistente la configuración del tamaño de letra.
*   **RecyclerView + CardView**: Para la visualización eficiente de listas.
*   **Fragments**: Estructura de pantalla principal dividida en dos secciones dinámicas.
*   **Intents & Activity Result API**: Navegación entre la lista y el detalle con refresco de datos.

## 📱 Funcionalidades

### 1. Pantalla Principal (Main)
*   **Fragmento Superior (API)**: Descarga en tiempo real los cócteles disponibles. Permite marcarlos como favoritos pulsando el icono de la estrella.
*   **Fragmento Inferior (Favoritos)**: Muestra la lista de cócteles guardados en el dispositivo. Se sincroniza automáticamente cuando se añade uno arriba.

### 2. Gestión de Favoritos
*   Al pulsar la estrella en la lista de la API, el elemento se guarda en SQLite.
*   Si un elemento ya es favorito, la estrella aparecerá rellena y no permitirá duplicados.
*   Al pulsar un elemento en la lista de favoritos, se abre una pantalla de **Detalle**.

### 3. Pantalla de Detalle
*   Muestra la información del cóctel seleccionado.
*   Permite eliminar el cóctel de la lista de favoritos. Al volver atrás, la lista se actualiza y la estrella en la sección superior se vacía.

### 4. Configuración Persistente
*   Incluye un menú en la barra de herramientas (Toolbar).
*   **Botón Zoom**: Permite alternar entre diferentes tamaños de letra para los nombres de los cócteles. Esta elección se guarda en `SharedPreferences` y se mantiene incluso tras cerrar la aplicación.

## 🛠️ Estructura del Proyecto

*   **`adapters/`**: Controladores para las listas (`ApiAdapter`, `FavoritosAdapter`).
*   **`api/`**: Configuración de Retrofit y modelos de respuesta.
*   **`database/`**: Lógica de base de datos (`DbHelper`, `CocktailDAO`).
*   **`fragments/`**: Vistas de la pantalla principal.
*   **`models/`**: POJO de `Cocktail`.
*   **`MainActivity.java`**: Punto de entrada y gestión de comunicación entre fragmentos.
*   **`DetalleActivity.java`**: Gestión de edición/borrado de favoritos.

## 🌐 API Endpoint
`https://www.thecocktaildb.com/api/json/v1/1/filter.php?a=Alcoholic`

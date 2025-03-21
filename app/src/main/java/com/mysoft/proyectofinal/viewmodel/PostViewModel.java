package com.mysoft.proyectofinal.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.mysoft.proyectofinal.model.Post;
import com.mysoft.proyectofinal.providers.PostProvider;
import com.parse.ParseObject;

import java.util.List;

public class PostViewModel extends ViewModel {
    private final PostProvider postProvider;
    private final MutableLiveData<List<Post>> postsLiveData;
    private final MutableLiveData<String> postSuccess;
    private String currentCategoria = "Todas";
    private String currentOrden = "Más recientes";

    public PostViewModel() {
        postProvider = new PostProvider();
        postsLiveData = new MutableLiveData<>();
        postSuccess = new MutableLiveData<>();
        loadPosts();
    }

    public LiveData<List<Post>> getPosts() {
        return postsLiveData;
    }

    public LiveData<String> getPostSuccess() {
        return postSuccess;
    }

    public LiveData<String> publicar(Post post) {
        MutableLiveData<String> resultLiveData = new MutableLiveData<>();
        postProvider.addPost(post).observeForever(result -> {
            postSuccess.setValue(result);
            resultLiveData.setValue(result);
            if (result.equals("Post publicado")) {
                loadPosts();
            }
        });
        return resultLiveData;
    }

    public LiveData<List<Post>> getAllPosts() {
        return postProvider.getAllPosts();
    }

    public LiveData<List<Post>> getPostsByCurrentUser() {
        return postProvider.getPostsByCurrentUser();
    }

    public void aplicarFiltros(String categoria, String orden) {
        Log.d("PostViewModel", "Aplicando filtros: Categoría=" + categoria + ", Orden=" + orden);
        this.currentCategoria = categoria;
        this.currentOrden = orden;
        loadPosts();
    }

    // Método para resetear los filtros a sus valores predeterminados
    public void resetFilters() {
        Log.d("PostViewModel", "Reseteando filtros");
        this.currentCategoria = "Todas";
        this.currentOrden = "Más recientes";
    }

    // Método para cargar posts con los filtros actuales
    public void loadPosts() {
        Log.d("PostViewModel", "Cargando posts con filtros: Categoría=" + currentCategoria + ", Orden=" + currentOrden);
        postProvider.getPostsFiltrados(currentCategoria, currentOrden)
                .observeForever(posts -> {
                    Log.d("PostViewModel", "Posts cargados: " + (posts != null ? posts.size() : 0));
                    postsLiveData.setValue(posts);
                });
    }

    // Método para verificar si hay filtros activos
    public boolean isFiltered() {
        boolean filtered = !currentCategoria.equals("Todas") || !currentOrden.equals("Más recientes");
        Log.d("PostViewModel", "¿Hay filtros activos? " + filtered);
        return filtered;
    }
}
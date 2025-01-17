package com.mysoft.proyectofinal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysoft.proyectofinal.model.Post;
import com.mysoft.proyectofinal.providers.PostProvider;

import java.util.List;

public class PostViewModel extends ViewModel {
    private final MutableLiveData<Boolean> postSuccess = new MutableLiveData<>();
    private final MutableLiveData<Post> postDetail = new MutableLiveData<>();
    private final PostProvider postProvider;
    private LiveData<List<Post>> posts;

    public PostViewModel() {
        postProvider = new PostProvider();
        posts = new MutableLiveData<>();
    }

    // LiveData para el éxito de la publicación
    public LiveData<Boolean> getPostSuccess() {
        return postSuccess;
    }

    // LiveData para obtener la lista de posts
    public LiveData<List<Post>> getPosts(int page) {
        return postProvider.getAllPosts(page); // Pasa la página actual a PostProvider
    }

    // Método para publicar un post
    public void publicar(Post post) {
        postProvider.addPost(post).observeForever(result -> {
            if ("Post publicado".equals(result)) {
                postSuccess.setValue(true);
            } else {
                postSuccess.setValue(false);
            }
        });
    }

    // Método para obtener detalles de un post específico
    public LiveData<Post> getPostDetail(String postId) {
        // Aquí debes implementar la lógica para obtener los detalles del post con el ID proporcionado
        // Esto puede ser un llamado a tu backend para obtener los datos del post
        // Para este ejemplo, se está simulando con un post vacío
        MutableLiveData<Post> postLiveData = new MutableLiveData<>();
        postLiveData.setValue(new Post());  // Reemplaza esto con la lógica real
        return postLiveData;
    }
}

package com.mysoft.proyectofinal.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.mysoft.proyectofinal.model.Post;
import com.mysoft.proyectofinal.providers.PostProvider;

import java.util.List;

public class PostViewModel extends ViewModel {
    private final MutableLiveData<Boolean> postSuccess = new MutableLiveData<>();
    private final PostProvider postProvider;
    private LiveData<List<Post>> posts;

    public PostViewModel() {
        postProvider = new PostProvider();
        posts = new MutableLiveData<>();
    }

    public LiveData<Boolean> getPostSuccess() {
        return postSuccess;
    }

    public LiveData<List<Post>> getPosts(int page) {
        return postProvider.getAllPosts(page); // Pasa la página actual a PostProvider
    }


    public void publicar(Post post) {
        postProvider.addPost(post).observeForever(result -> {
            if ("Post publicado".equals(result)) {
                postSuccess.setValue(true);
            } else {
                postSuccess.setValue(false);
            }
        });
    }

}

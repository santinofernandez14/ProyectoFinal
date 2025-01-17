package com.mysoft.proyectofinal.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysoft.proyectofinal.model.Post;
import com.mysoft.proyectofinal.providers.PostProvider;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PostDetailViewModel extends ViewModel {

    private MutableLiveData<List<String>> images = new MutableLiveData<>();
    private MutableLiveData<Post> postDetail = new MutableLiveData<>();
    private MutableLiveData<String> profileImageUrl = new MutableLiveData<>();
    private PostProvider postProvider;

    public PostDetailViewModel() {
        postProvider = new PostProvider();
    }

    // Este método devuelve la URL de la imagen de perfil
    public LiveData<String> getProfileImageUrl() {
        return profileImageUrl;
    }

    // Método para obtener las imágenes de los posts
    public LiveData<List<String>> getImages() {
        return images;
    }

    // Método para obtener los detalles de un post
    public LiveData<Post> getPostDetail(String postId) {
        MutableLiveData<Post> postDetail = new MutableLiveData<>();

        if (postId == null || postId.isEmpty()) {
            Log.e("PostDetailViewModel", "El postId es nulo o vacío");
            postDetail.setValue(null);
            return postDetail;
        }

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.getInBackground(postId, new GetCallback<Post>() {
            @Override
            public void done(Post post, ParseException e) {
                if (e == null && post != null) {
                    postDetail.setValue(post);  // Si se encuentra el post, actualiza el LiveData
                } else {
                    if (e != null) {
                        Log.e("PostDetailViewModel", "Error obteniendo los detalles del post: " + e.getMessage());
                    }
                    postDetail.setValue(null);  // En caso de error, devuelve null
                }
            }
        });

        return postDetail;
    }

    // Obtener la imagen de perfil del usuario
    public void fetchProfileImage(String userId) {
        // Verificar que el userId no sea nulo o vacío antes de hacer la consulta
        if (userId == null || userId.isEmpty()) {
            Log.e("PostDetailViewModel", "El userId recibido es nulo o vacío");
            return;
        }

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", userId);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null && user != null) {
                    String fotoPerfilUrl = user.getString("foto_perfil");
                    profileImageUrl.setValue(fotoPerfilUrl); // Actualiza el LiveData con la URL
                } else {
                    if (e != null) {
                        Log.e("ParseError", "Error obteniendo el usuario: " + e.getMessage());
                    }
                    profileImageUrl.setValue(null); // En caso de error, se establece null
                }
            }
        });
    }
}

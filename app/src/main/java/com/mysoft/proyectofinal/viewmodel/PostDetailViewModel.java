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
    private MutableLiveData<String> profileImageUrl = new MutableLiveData<>();  // Aquí definimos profileImageUrl
    private PostProvider postProvider;
    private String postId;  // Agregamos postId como una variable de clase

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

    // Método para establecer el postId
    public void setPostId(String postId) {
        this.postId = postId;
    }

    // Método para obtener los detalles de un post
    public LiveData<Post> getPostDetail() {
        postProvider.getPostDetail(postId, new PostProvider.PostDetailCallback() {

            @Override
            public void onSuccess(Post post) {
                postDetail.setValue(post);  // Actualizar LiveData con los detalles del post
            }

            @Override
            public void onFailure(ParseException e) {
                Log.e("PostDetailViewModel", "Error al cargar los detalles del post", e);
                postDetail.setValue(null);  // En caso de error, se establece null
            }
        });

        return postDetail;
    }

    // Obtener todos los posts de un usuario
    public void getAllPosts(ParseUser user) {
        postProvider.getAllPostsByUser(user, new PostProvider.PostsCallback() {
            @Override
            public void onSuccess(List<Post> posts) {
                List<String> imageUrls = new ArrayList<>();
                for (Post post : posts) {
                    if (post.getObjectId().equals(postId)) { // Aseguramos que obtenemos solo las imágenes de ese post
                        imageUrls.addAll(post.getImagenes());
                        break; // Solo necesitamos las imágenes del post específico
                    }
                }
                images.setValue(imageUrls);  // Actualizar LiveData con las imágenes
            }

            @Override
            public void onFailure(ParseException e) {
                Log.e("PostDetailViewModel", "Error al cargar los posts", e);
                images.setValue(new ArrayList<>());  // Si hay error, se devuelve una lista vacía
            }
        });
    }

    // Obtener la imagen de perfil del usuario
    public void fetchProfileImage(String userId) {
        // Consulta Parse para obtener la URL de la imagen de perfil
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("objectId", userId);

        query.getFirstInBackground(new GetCallback<ParseUser>() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null && user != null) {
                    String fotoPerfilUrl = user.getString("foto_perfil");
                    profileImageUrl.setValue(fotoPerfilUrl); // Actualiza el LiveData con la URL
                } else {
                    Log.e("ParseError", "Error obteniendo el usuario: " + e.getMessage());
                }
            }
        });
    }
}

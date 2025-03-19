package com.mysoft.proyectofinal.viewmodel;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysoft.proyectofinal.providers.PostProvider;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class PostDetailViewModel extends ViewModel {

    private final MutableLiveData<List<ParseObject>> commentsLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> errorLiveData = new MutableLiveData<>();
    private final MutableLiveData<String> successLiveData = new MutableLiveData<>();
    private final PostProvider postProvider;

    public PostDetailViewModel() {
        postProvider = new PostProvider();
    }

    public LiveData<List<ParseObject>> getCommentsLiveData() {
        return commentsLiveData;
    }

    public LiveData<String> getErrorLiveData() {
        return errorLiveData;
    }

    public LiveData<String> getSuccessLiveData() {
        return successLiveData;
    }

    // Método para obtener los comentarios
    public void fetchCommentario(String postId) {
        postProvider.fetchComments(postId, new PostProvider.CommentsCallback() {
            @Override
            public void onSuccess(List<ParseObject> comments) {
                commentsLiveData.postValue(comments);
            }

            @Override
            public void onFailure(Exception e) {
                errorLiveData.postValue(e.getMessage());
            }
        });
    }

    // Método para eliminar el post
    public void eliminarPost(String postId) {
        postProvider.deletePost(postId).observeForever(mensaje -> {
            if (mensaje.equals("Post eliminado correctamente")) {
                successLiveData.postValue(mensaje);
            } else {
                errorLiveData.postValue(mensaje);
            }
        });
    }

    // Método para grabar un comentario
    public void grabaComentario(String postId, String commentText) {
        ParseUser currentUser = ParseUser.getCurrentUser();

        // Validación del usuario
        if (currentUser == null) {
            errorLiveData.postValue("Usuario no autenticado");
            return;
        }

        postProvider.saveComment(postId, commentText, currentUser, new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    fetchCommentario(postId); // Actualiza la lista de comentarios después de guardar el comentario
                } else {
                    errorLiveData.postValue(e.getMessage());
                }
            }
        });
    }
}

package com.mysoft.proyectofinal.providers;

import android.util.Log;

import com.mysoft.proyectofinal.model.Post;
import com.mysoft.proyectofinal.model.User;
import com.parse.GetCallback;
import com.parse.SaveCallback;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class PostProvider {
    private MutableLiveData<List<Post>> postsLiveData = new MutableLiveData<>();

    // Mét odo para agregar un nuevo post
    public LiveData<String> addPost(Post post) {
        MutableLiveData<String> result = new MutableLiveData<>();

        // Asignar el usuario actual al post
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            result.setValue("Error: Usuario no autenticado.");
            return result;
        }
        post.put("user", currentUser);

        // Guardar el post en segundo plano
        post.saveInBackground(e -> {
            if (e == null) {
                // Guardar las imágenes relacionadas
                guardarImagenes(post, result);
            } else {
                result.setValue("Error al guardar el post: " + e.getMessage());
            }
        });

        return result;
    }

    // Méto do para guardar las imágenes relacionadas con el post
    private void guardarImagenes(Post post, MutableLiveData<String> result) {
        ParseRelation<ParseObject> relation = post.getRelation("images");
        for (String url : post.getImagenes()) {
            ParseObject imageObject = new ParseObject("Image");
            imageObject.put("url", url);
            imageObject.saveInBackground(imgSaveError -> {
                if (imgSaveError == null) {
                    relation.add(imageObject);
                    post.saveInBackground(saveError -> {
                        if (saveError == null) {
                            result.setValue("Post publicado");
                        } else {
                            result.setValue("Error al guardar la relación con las imágenes: " + saveError.getMessage());
                        }
                    });
                } else {
                    result.setValue("Error al guardar la imagen: " + imgSaveError.getMessage());
                }
            });
        }
    }

    // Mét odo para obtener los posts del usuario actual
    public LiveData<List<Post>> getPostsByCurrentUser() {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            result.setValue(new ArrayList<>());
            return result;
        }

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.whereEqualTo("user", currentUser);
        query.include("user");
        query.orderByDescending("createdAt");
        ejecutarConsulta(query, result);

        return result;
    }

    // Mét odo para obtener todos los posts
    public LiveData<List<Post>> getAllPosts() {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include("user"); // Incluir el usuario relacionado
        ejecutarConsulta(query, result);

        return result;
    }

    // Mét odo para eliminar un post
    public LiveData<String> deletePost(String postId) {
        MutableLiveData<String> result = new MutableLiveData<>();

        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.getInBackground(postId, (post, e) -> {
            if (e == null) {
                post.deleteInBackground(e1 -> {
                    if (e1 == null) {
                        result.setValue("Post eliminado correctamente");
                    } else {
                        result.setValue("Error al eliminar el post: " + e1.getMessage());
                    }
                });
            } else {
                result.setValue("Error al encontrar el post: " + e.getMessage());
            }
        });

        return result;
    }

    // Mét odo para obtener los detalles de un post
    public LiveData<Post> getPostDetail(String postId) {
        MutableLiveData<Post> result = new MutableLiveData<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);
        query.include("user");
        query.include("images");
        query.getInBackground(postId, (post, e) -> {
            if (e == null) {
                cargarImagenesYUsuario(post, result);
            } else {
                result.setValue(null);
                Log.e("PostProvider", "Error al obtener el post: ", e);
            }
        });

        return result;
    }

    // Mét odo para cargar las imágenes y el usuario de un post
    private void cargarImagenesYUsuario(Post post, MutableLiveData<Post> result) {
        ParseRelation<ParseObject> relation = post.getRelation("images");
        try {
            List<ParseObject> images = relation.getQuery().find();
            List<String> imageUrls = new ArrayList<>();
            for (ParseObject imageObject : images) {
                imageUrls.add(imageObject.getString("url"));
            }
            post.setImagenes(imageUrls);
        } catch (ParseException parseException) {
            parseException.printStackTrace();
        }

        ParseObject userObject = post.getParseObject("user");
        if (userObject != null) {
            try {
                userObject.fetchIfNeeded();
                User user = new User();
                user.setUsername(userObject.getString("username"));
                user.setEmail(userObject.getString("email"));
                user.setFotoperfil(userObject.getString("foto_perfil"));
                post.setUser(user);
            } catch (ParseException userFetchException) {
                userFetchException.printStackTrace();
            }
        } else {
            Log.w("PostProvider", "El usuario asociado al post es nulo.");
        }

        result.setValue(post);
    }

    // Mét odo para obtener comentarios de un post
    public interface CommentsCallback {
        void onSuccess(List<ParseObject> comments);

        void onFailure(Exception e);
    }

    public void fetchComments(String postId, CommentsCallback callback) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Comentario");
        query.whereEqualTo("post", ParseObject.createWithoutData("Post", postId));
        query.include("user"); // Incluir los datos del usuario en la consulta
        query.findInBackground((comentarios, e) -> {
            if (e == null) {
                callback.onSuccess(comentarios);
            } else {
                callback.onFailure(e);
            }
        });
    }

    // Mét odo para guardar un comentario
    public void saveComment(String postId, String commentText, ParseUser currentUser, SaveCallback callback) {
        ParseObject post = ParseObject.createWithoutData("Post", postId);

        ParseObject comentario = ParseObject.create("Comentario");
        comentario.put("texto", commentText);
        comentario.put("post", post);
        comentario.put("user", currentUser);

        comentario.saveInBackground(callback);
    }

    // Mét odo para obtener posts filtrados
    public LiveData<List<Post>> getPostsFiltrados(String categoria, String orden) {
        MutableLiveData<List<Post>> result = new MutableLiveData<>();
        ParseQuery<Post> query = ParseQuery.getQuery(Post.class);

        // Apply category filter
        if (!categoria.equals("Todas")) {
            query.whereEqualTo("categoria", categoria);
        }

        // Apply order
        switch (orden) {
            case "Más recientes":
                query.orderByDescending("createdAt");
                break;
            case "Más antiguos":
                query.orderByAscending("createdAt");
                break;
            default:
                query.orderByDescending("createdAt"); // Por defecto, ordenar por más recientes
                break;
        }

        query.include("user");
        query.findInBackground((posts, e) -> {
            if (e == null) {
                // Siempre devolver la lista de posts, incluso si está vacía
                result.setValue(posts != null ? posts : new ArrayList<>());

                // Registrar en el log para depuración
                if (posts == null || posts.isEmpty()) {
                    if (!categoria.equals("Todas")) {
                        Log.d("PostProvider", "No hay posts para la categoría: " + categoria);
                    } else {
                        Log.d("PostProvider", "No hay posts disponibles");
                    }
                } else {
                    Log.d("PostProvider", "Se encontraron " + posts.size() + " posts");
                }
            } else {
                result.setValue(new ArrayList<>());
                Log.e("PostProvider", "Error al recuperar posts filtrados: ", e);
            }
        });

        return result;
    }

    // Mét odo genérico para ejecutar consultas de posts
    private void ejecutarConsulta(ParseQuery<Post> query, MutableLiveData<List<Post>> result) {
        query.findInBackground((posts, e) -> {
            if (e == null) {
                result.setValue(posts);
            } else {
                result.setValue(new ArrayList<>());
                Log.e("PostProvider", "Error al recuperar posts: ", e);
            }
        });
    }

}
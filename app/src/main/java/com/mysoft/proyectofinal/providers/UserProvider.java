package com.mysoft.proyectofinal.providers;


import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.mysoft.proyectofinal.model.User;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class UserProvider {
    private static final String TAG = "UserProvider";
    private static final String USER_CLASS = "User";

    // Crear usuario
    public LiveData<String> createUser(User user) {
        MutableLiveData<String> result = new MutableLiveData<>();

        // Validar datos del usuario
        if (user.getEmail() == null || user.getEmail().isEmpty() ||
                user.getPassword() == null || user.getPassword().isEmpty() ||
                user.getId() == null || user.getId().isEmpty()) {
            result.setValue("Error: Los datos del usuario son inválidos");
            return result;
        }

        ParseObject userObject = new ParseObject(USER_CLASS);
        userObject.put("user_id", user.getId());
        userObject.put("email", user.getEmail());
        userObject.put("password", user.getPassword());
        userObject.put("profilePicture", null); // Inicializar vacío

        userObject.saveInBackground(e -> {
            if (e == null) {
                result.setValue("Usuario creado correctamente");
            } else {
                result.setValue("Error al crear usuario: " + e.getMessage());
                Log.e(TAG, "Error al crear usuario", e);
            }
        });

        return result;
    }

    // Obtener usuario por email
    public LiveData<User> getUser(String email) {
        MutableLiveData<User> userData = new MutableLiveData<>();
        ParseQuery<ParseObject> query = ParseQuery.getQuery(USER_CLASS);
        query.whereEqualTo("email", email);

        query.findInBackground((users, e) -> {
            if (e == null && !users.isEmpty()) {
                ParseObject userObject = users.get(0);
                User user = parseUserObject(userObject);
                userData.setValue(user);
            } else {
                userData.setValue(null);
                Log.e(TAG, "Error al obtener usuario", e);
            }
        });

        return userData;
    }

    // Actualizar perfil del usuario
    public LiveData<String> updateUserProfile(User user, byte[] profilePicture) {
        MutableLiveData<String> result = new MutableLiveData<>();

        ParseQuery<ParseObject> query = ParseQuery.getQuery(USER_CLASS);
        query.whereEqualTo("user_id", user.getId());

        query.findInBackground((users, e) -> {
            if (e == null && !users.isEmpty()) {
                ParseObject userObject = users.get(0);
                if (user.getEmail() != null && !user.getEmail().isEmpty())
                    userObject.put("email", user.getEmail());
                if (user.getPassword() != null && !user.getPassword().isEmpty())
                    userObject.put("password", user.getPassword());
                if (profilePicture != null) {
                    ParseFile file = new ParseFile("profilePicture.jpg", profilePicture);
                    userObject.put("profilePicture", file);
                }

                userObject.saveInBackground(e1 -> {
                    if (e1 == null) {
                        result.setValue("Perfil actualizado correctamente");
                    } else {
                        result.setValue("Error al actualizar perfil: " + e1.getMessage());
                        Log.e(TAG, "Error al actualizar perfil", e1);
                    }
                });
            } else {
                result.setValue("Usuario no encontrado");
                Log.e(TAG, "Usuario no encontrado", e);
            }
        });

        return result;
    }

    // Obtener la foto de perfil del usuario
    public ParseFile getProfilePicture(ParseObject userObject) {
        return userObject.getParseFile("profilePicture");
    }

    // Método para parsear un ParseObject a un objeto User
    private User parseUserObject(ParseObject userObject) {
        User user = new User();
        user.setObjectId(userObject.getString("user_id"));
        user.setEmail(userObject.getString("email"));
        user.setPassword(userObject.getString("password"));
        ParseFile profilePictureFile = userObject.getParseFile("profilePicture");
        if (profilePictureFile != null) {
            user.setFotoperfil(profilePictureFile.getUrl());
        }
        return user;
    }
}

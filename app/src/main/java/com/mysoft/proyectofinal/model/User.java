package com.mysoft.proyectofinal.model;

import android.util.Log;
import com.parse.ParseClassName;
import com.parse.ParseObject;

@ParseClassName("User")
public class User extends ParseObject {

    // Constructor vacío necesario para Parse
    public User() {
        // Constructor vacío para el uso de Parse
    }

    // Getter y setter para "username"
    public String getUsername() {
        return getString("username");
    }

    public void setUsername(String username) {
        if (username != null && !username.trim().isEmpty()) {
            put("username", username);
        } else {
            Log.w("User", "El nombre de usuario es nulo o vacío.");
        }
    }

    // Getter y setter para "email"
    public String getEmail() {
        return getString("email");
    }

    public void setEmail(String email) {
        if (email != null && !email.trim().isEmpty()) {
            put("email", email);
        } else {
            Log.w("User", "El correo electrónico es nulo o vacío.");
        }
    }

    // Getter y setter para "password"
    public String getPassword() {
        return getString("password");
    }

    public void setPassword(String password) {
        if (password != null && !password.trim().isEmpty()) {
            put("password", password);
        } else {
            Log.w("User", "La contraseña es nula o vacía.");
        }
    }

    // Getter y setter para "redSocial"
    public String getRedSocial() {
        return getString("redSocial");
    }

    public void setRedSocial(String redSocial) {
        if (redSocial != null && !redSocial.trim().isEmpty()) {
            put("redSocial", redSocial);
        } else {
            Log.w("User", "La red social es nula o vacía.");
        }
    }

    // Getter y setter para "fotoperfil"
    public String getFotoperfil() {
        return getString("fotoperfil");
    }

    public void setFotoperfil(String fotoperfil) {
        if (fotoperfil != null && !fotoperfil.trim().isEmpty()) {
            put("foto_perfil", fotoperfil);
        } else {
            Log.w("User", "La foto de perfil es nula o vacía.");
        }
    }

    // Getter para "id" (no necesitas un setter para "id" porque Parse lo genera automáticamente)
    public String getId() {
        return getObjectId();
    }

    // Manejo del campo "postCount" a través de la base de datos
    public int getPostCount() {
        return getInt("postCount");
    }

    public void incrementPostCount() {
        int currentCount = getPostCount();
        put("postCount", currentCount + 1);
    }

    public void decrementPostCount() {
        int currentCount = getPostCount();
        if (currentCount > 0) {
            put("postCount", currentCount - 1);
        } else {
            Log.w("User", "El contador de publicaciones no puede ser menor que 0.");
        }
    }

}

package com.mysoft.proyectofinal.providers;

import static com.parse.Parse.getApplicationContext;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;


import com.mysoft.proyectofinal.R;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseUser;

import java.io.File;

public class AuthProvider {
    public AuthProvider(){}

    public AuthProvider(Context context) {

            Parse.initialize(new Parse.Configuration.Builder(context)
                    .applicationId(context.getString(R.string.back4app_app_id))
                    .clientKey(context.getString(R.string.back4app_client_key))
                    .server(context.getString(R.string.back4app_server_url))
                    .build()
            );

    }

    public LiveData<String> signIn(String email, String password) {
        MutableLiveData<String> authResult = new MutableLiveData<>();
        if (email.isEmpty() || password.isEmpty()) {
            authResult.setValue("Email o contraseña vacíos");
            return authResult;
        }
        ParseUser.logInInBackground(email, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    authResult.setValue(user.getObjectId());
                    Log.d("AuthProvider", "Usuario autenticado exitosamente: " + user.getObjectId());
                } else {
                    Log.e("AuthProvider", "Error en inicio de sesión: ", e);
                    authResult.setValue(e.getMessage());
                }
            }
        });
        return authResult;
    }

    public LiveData<String> signUp(String email, String password, String username) {
        MutableLiveData<String> authResult = new MutableLiveData<>();
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            authResult.setValue("Email o contraseña vacíos");
            return authResult;
        }
        ParseUser user = new ParseUser();
        user.setUsername(email);
        user.setPassword(password);
        user.signUpInBackground(e -> {
            if (e == null) {
                authResult.setValue(user.getObjectId());
                Log.d("AuthProvider", "Usuario registrado exitosamente: " + user.getObjectId());
            } else {
                Log.e("AuthProvider", "Error en registro: ", e);
                authResult.setValue(e.getMessage());
            }
        });
        return authResult;
    }

    public LiveData<String> getCurrentUserID() {
        MutableLiveData<String> currentUserId = new MutableLiveData<>();
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUserId.setValue(currentUser.getObjectId());
        }
        return currentUserId;
    }

    public LiveData<Boolean> logout() {
        MutableLiveData<Boolean> logoutResult = new MutableLiveData<>();
        ParseUser.logOutInBackground(e -> {
            if (e == null) {
                File cacheDir = getApplicationContext().getCacheDir();
                if (cacheDir.isDirectory()) {
                    for (File file : cacheDir.listFiles()) {
                        file.delete();
                    }
                }
                logoutResult.setValue(true);
                Log.d("AuthProvider", "Caché eliminada y usuario desconectado.");
            } else {
                logoutResult.setValue(false);
                Log.e("AuthProvider", "Error al desconectar al usuario: ", e);
            }
        });
        return logoutResult;
    }
}

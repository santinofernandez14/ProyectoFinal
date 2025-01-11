package com.mysoft.proyectofinal.viewmodel;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.mysoft.proyectofinal.providers.AuthProvider;

public class MainViewModel extends ViewModel {
    private final AuthProvider authProvider;

    public MainViewModel(Context context) {
        authProvider = new AuthProvider(context);
    }

    // Método de inicio de sesión
    public LiveData<String> login(String email, String password) {
        MutableLiveData<String> loginResult = new MutableLiveData<>();

        // Llamamos al AuthProvider para iniciar sesión
        authProvider.signIn(email, password).observeForever(userId -> {
            if (userId != null) {
                // Si el login es exitoso, devolver el userId
                loginResult.setValue(userId);
            } else {
                // Si el login falla, devolver null
                loginResult.setValue(null);
            }
        });

        return loginResult;
    }
}

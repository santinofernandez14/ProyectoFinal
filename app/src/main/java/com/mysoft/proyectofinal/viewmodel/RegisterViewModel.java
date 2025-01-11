package com.mysoft.proyectofinal.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;


import com.mysoft.proyectofinal.providers.AuthProvider;
import com.parse.ParseUser;




public class RegisterViewModel extends AndroidViewModel {
    private final AuthProvider authProvider;
    private final MutableLiveData<String> registerResult = new MutableLiveData<>();

    public RegisterViewModel(Application application) {
        super(application);
        authProvider = new AuthProvider(application);
    }

    public LiveData<String> getRegisterResult() {
        return registerResult;
    }

    public void register(String username, String email, String password) {
        authProvider.signUp(username, email, password).observeForever(result -> {
            if (result != null && !result.isEmpty()) {
                registerResult.setValue("Registro exitoso");
            } else {
                registerResult.setValue("Error en el registro: " + result);
            }
        });
    }
}

package com.mysoft.proyectofinal.viewmodel;

import android.app.Application;
import android.util.Log;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;


import com.mysoft.proyectofinal.model.User;
import com.mysoft.proyectofinal.providers.AuthProvider;
import com.parse.ParseUser;




public class RegisterViewModel extends ViewModel {
    private final MutableLiveData<String> registerResult = new MutableLiveData<>();
    private final AuthProvider authProvider;


    public RegisterViewModel() {
        this.authProvider = new AuthProvider();
    }


    public LiveData<String> getRegisterResult() {
        return registerResult;
    }


    public void register(User user) {

        LiveData<String> result = authProvider.signUp(user);


        result.observeForever(new Observer<String>() {
            @Override
            public void onChanged(String objectId) {
                if (objectId != null) {

                    registerResult.setValue(objectId);
                    Log.d("RegisterViewModel", "Usuario registrado con ID: " + objectId);
                } else {
                    // Registration failed
                    registerResult.setValue(null);
                    Log.e("RegisterViewModel", "Error durante el registro.");
                }

                result.removeObserver(this);
            }
        });
    }
}
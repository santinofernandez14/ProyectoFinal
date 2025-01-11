package com.mysoft.proyectofinal.view;


import android.os.Bundle;
import android.util.Log;

import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;



import com.mysoft.proyectofinal.databinding.ActivityRegisterBinding;
import com.mysoft.proyectofinal.util.Validaciones;
import com.mysoft.proyectofinal.viewmodel.RegisterViewModel;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {
    private ActivityRegisterBinding binding;
    private RegisterViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        viewModel = new ViewModelProvider(this).get(RegisterViewModel.class);
        viewModel.getRegisterResult().observe(this, this::showToast);
        manejarEventos();
    }

    private void manejarEventos() {
        binding.circleImageBack.setOnClickListener(v -> finish());
        binding.btRegistrar.setOnClickListener(v -> realizarRegistro());
    }

    private void realizarRegistro() {
        String usuario = Objects.requireNonNull(binding.itUsuario.getText()).toString().trim();
        String email = Objects.requireNonNull(binding.itEmail.getText()).toString().trim();
        String pass = Objects.requireNonNull(binding.itPassword.getText()).toString().trim();
        String pass1 = Objects.requireNonNull(binding.itPassword1.getText()).toString().trim();

        if (!Validaciones.validarTexto(usuario)) {
            showToast("Usuario incorrecto");
            return;
        }
        if (!Validaciones.validarMail(email)) {
            showToast("El correo no es válido");
            return;
        }
        String passError = Validaciones.validarPass(pass, pass1);
        if (passError != null) {
            showToast(passError);
            return;
        }

        // Llamar al método register con los parámetros individuales
        viewModel.register(usuario, email, pass);
    }

    private void showToast(String message) {
        Log.d("RegisterActivity", "Mensaje: " + message);
        Toast.makeText(RegisterActivity.this, message, Toast.LENGTH_LONG).show();
    }
}

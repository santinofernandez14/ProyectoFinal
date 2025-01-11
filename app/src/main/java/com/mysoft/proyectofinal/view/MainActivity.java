package com.mysoft.proyectofinal.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mysoft.proyectofinal.databinding.ActivityMainBinding;
import com.mysoft.proyectofinal.providers.AuthProvider;
import com.mysoft.proyectofinal.util.Validaciones;
import com.mysoft.proyectofinal.viewmodel.MainViewModel;
import com.mysoft.proyectofinal.viewmodel.MainViewModelFactory;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private MainViewModel viewModel;
    private AuthProvider authProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializamos el ViewModel y el AuthProvider
        viewModel = new ViewModelProvider(this, new MainViewModelFactory(this)).get(MainViewModel.class);
        authProvider = new AuthProvider(this);

        manejarEventos();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void manejarEventos() {
        // Evento para redirigir al registro
        binding.tvRegistro.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });

        // Evento para el inicio de sesión
        binding.btLogin.setOnClickListener(v -> {
            String email = binding.itUsuario.getText().toString().trim();
            String pass = binding.itPassword.getText().toString().trim();

            // Validamos los datos del usuario
            if (!Validaciones.validarMail(email)) {
                showToast("Email incorrecto");
                return;
            }
            if (!Validaciones.controlarPasword(pass)) {
                showToast("Password incorrecto");
                return;
            }

            // Llamamos al ViewModel para procesar el login
            viewModel.login(email, pass).observe(this, user_id -> {
                if (user_id != null) {
                    // Inicio de sesión exitoso, redirigir al HomeActivity
                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                    intent.putExtra("user_id", user_id); // Puedes enviar el ID del usuario si lo necesitas
                    startActivity(intent);
                    finish(); // Finalizar MainActivity para que no regrese aquí
                } else {
                    // Login fallido
                    showToast("Login fallido. Verifica tus credenciales.");
                }
            });
        });
    }

    private void showToast(String message) {
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        limpiarCampos();
    }

    private void limpiarCampos() {
        // Limpiar los campos de entrada
        binding.itUsuario.setText("");
        binding.itPassword.setText("");
    }
}

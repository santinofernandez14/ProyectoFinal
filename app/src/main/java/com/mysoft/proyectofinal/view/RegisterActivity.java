package com.mysoft.proyectofinal.view;

import android.content.Intent;
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

        // Observamos el resultado del registro
        viewModel.getRegisterResult().observe(this, result -> {
            showToast(result);  // Mostrar el mensaje
            if (result.equals("Registro exitoso")) {
                // Si el registro es exitoso, redirigir a la MainActivity
                redirectToMainActivity();
            }
        });

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

    private void redirectToMainActivity() {
        // Crear una nueva intención para la MainActivity
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

        // Limpiar la pila de actividades para que no se pueda volver a la actividad de registro
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        // Iniciar la MainActivity
        startActivity(intent);

        // Finalizar la actividad actual (registro)
        finish();
    }
}

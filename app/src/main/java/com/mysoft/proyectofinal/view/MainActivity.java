package com.mysoft.proyectofinal.view;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.mysoft.proyectofinal.R;
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
        viewModel = new ViewModelProvider(this, new MainViewModelFactory(this)).get(MainViewModel.class);
        authProvider=new AuthProvider(this);
        manejarEventos();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }


    private void manejarEventos() {
        binding.tvRegistro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        binding.btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.itUsuario.getText().toString().trim();
                String pass = binding.itPassword.getText().toString().trim();

                if (!Validaciones.validarMail(email)) {
                    showToast("Email incorrecto");
                    return;
                }
                if (!Validaciones.controlarPasword(pass)) {
                    showToast("Password incorrecto");
                    return;
                }

                viewModel.login(email, pass).observe(MainActivity.this, user_id -> {
                    if (user_id != null) {

                        Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                        //   intent.putExtra("user_id",user_id);
                        startActivity(intent);
                    } else {
                        showToast("Login fallido");
                    }
                });
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
    }
    @Override
    protected void onResume () {
        super.onResume();
        limpiarCampos();
    }
    private void limpiarCampos() {
        binding.itUsuario.setText("");
        binding.itPassword.setText("");
    }
}
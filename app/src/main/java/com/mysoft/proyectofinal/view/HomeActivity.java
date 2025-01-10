package com.mysoft.proyectofinal.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.navigation.NavigationBarView;
import com.mysoft.proyectofinal.R;
import com.mysoft.proyectofinal.databinding.ActivityHomeBinding;
import com.mysoft.proyectofinal.view.fragments.ChatsFragment;
import com.mysoft.proyectofinal.view.fragments.FiltroFragment;
import com.mysoft.proyectofinal.view.fragments.HomeFragment;
import com.mysoft.proyectofinal.view.fragments.PerfilFragment;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private LottieAnimationView loadingAnimation;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtener referencias del ProgressBar y la animación Lottie
        loadingAnimation = findViewById(R.id.loadingAnimation);
        progressBar = findViewById(R.id.progressBar);

        // Mostrar el ProgressBar y la animación Lottie al inicio
        progressBar.setVisibility(View.VISIBLE); // Mostrar el ProgressBar
        loadingAnimation.setVisibility(View.VISIBLE); // Mostrar la animación Lottie
        loadingAnimation.playAnimation(); // Iniciar la animación Lottie

        // Simulamos la carga de datos
        loadData();

        // Configurar el BottomNavigationView
        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.navItemHome) {
                    openFragment(HomeFragment.newInstance("", ""));
                } else if (item.getItemId() == R.id.navItemFiltros) {
                    openFragment(new FiltroFragment());
                } else if (item.getItemId() == R.id.navItemCharts) {
                    openFragment(new ChatsFragment());
                } else if (item.getItemId() == R.id.navItemPerfil) {
                    openFragment(new PerfilFragment());
                }
                return true;
            }
        });

        // Abrir el fragmento inicial
        openFragment(HomeFragment.newInstance("", ""));
    }

    private void loadData() {
        // Simulamos un retraso de 3 segundos para cargar datos (como un login)
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Después de la carga, ocultar el ProgressBar y la animación Lottie
                progressBar.setVisibility(View.GONE); // Ocultar el ProgressBar
                loadingAnimation.setVisibility(View.GONE); // Ocultar la animación Lottie
                loadingAnimation.cancelAnimation(); // Detener la animación

                // Mostrar la barra de navegación después de la carga
                binding.bottomNav.setVisibility(View.VISIBLE); // Hacer visible la barra de navegación
            }
        }, 3000); // Simulamos un retraso de 3 segundos
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragmentContainer, fragment);
        fragmentTransaction.commit();
    }

    public void hideProgressBar() {
        View progressBarLayout = findViewById(R.id.progress_layout);
        if (progressBarLayout != null) {
            progressBarLayout.setVisibility(View.GONE);
        }
    }
}

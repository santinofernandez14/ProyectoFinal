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

        // Referencias a la animación Lottie y al ProgressBar
        loadingAnimation = findViewById(R.id.loadingAnimation);
        progressBar = findViewById(R.id.progressBar);

        // Mostrar el ProgressBar y la animación al inicio
        showLoading();

        // Simular la carga de datos
        loadData();

        // Configuración del BottomNavigationView
        binding.bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.navItemHome) {
                    openFragment(HomeFragment.newInstance("", ""));
                } else if (itemId == R.id.navItemFiltros) {
                    openFragment(new FiltroFragment());
                } else if (itemId == R.id.navItemCharts) {
                    openFragment(new ChatsFragment());
                } else if (itemId == R.id.navItemPerfil) {
                    openFragment(new PerfilFragment());
                }
                return true;
            }
        });


        // Abrir el fragmento inicial
        openFragment(HomeFragment.newInstance("", ""));
    }

    private void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
        loadingAnimation.setVisibility(View.VISIBLE);
        loadingAnimation.playAnimation();
        binding.bottomNav.setVisibility(View.GONE); // Ocultar barra de navegación
    }

    private void loadData() {
        new Handler().postDelayed(() -> {
            // Ocultar el ProgressBar y la animación tras la carga
            progressBar.setVisibility(View.GONE);
            loadingAnimation.setVisibility(View.GONE);
            loadingAnimation.cancelAnimation();

            // Mostrar la barra de navegación
            binding.bottomNav.setVisibility(View.VISIBLE);
        }, 3000); // Simulación de un retraso de 3 segundos
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

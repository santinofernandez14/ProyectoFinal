package com.mysoft.proyectofinal.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.navigation.NavigationBarView;
import com.mysoft.proyectofinal.R;
import com.mysoft.proyectofinal.databinding.ActivityHomeBinding;
import com.mysoft.proyectofinal.view.fragments.ChatsFragment;
import com.mysoft.proyectofinal.view.fragments.FiltrosFragment;
import com.mysoft.proyectofinal.view.fragments.HomeFragment;
import com.mysoft.proyectofinal.view.fragments.PerfilFragment;
import com.mysoft.proyectofinal.viewmodel.PostViewModel;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private PostViewModel postViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inicializar ViewModel
        postViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Add progress bar
        LayoutInflater inflater = LayoutInflater.from(this);
        View progressBarLayout = inflater.inflate(R.layout.progress_layout, binding.mainCont, false);
        binding.mainCont.addView(progressBarLayout);

        // Set up bottom navigation
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.itemHome) {
                    // Resetear filtros solo cuando se selecciona explícitamente el Home
                    postViewModel.resetFilters();
                    postViewModel.loadPosts();
                    openFragment(HomeFragment.newInstance(), true, "HOME_FRAGMENT");

                } else if (item.getItemId() == R.id.itemPerfil) {
                    openFragment(new PerfilFragment(), false, "PERFIL_FRAGMENT");
                } else if (item.getItemId() == R.id.itemFiltros) {
                    openFragment(new FiltrosFragment(), false, "FILTROS_FRAGMENT");
                }
                return true;
            }
        });

        if (savedInstanceState == null) {
            openFragment(HomeFragment.newInstance(), true, "HOME_FRAGMENT");
        }
    }

    private void openFragment(Fragment fragment, boolean isHome, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Limpiar el back stack si estamos abriendo el HomeFragment
        if (isHome) {
            int backStackCount = fragmentManager.getBackStackEntryCount();
            for (int i = 0; i < backStackCount; i++) {
                fragmentManager.popBackStack();
            }
        }

        // Verificar si el fragmento ya existe
        Fragment existingFragment = fragmentManager.findFragmentByTag(tag);
        if (existingFragment == null) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment, tag);

            // Solo añadir al back stack si no es HomeFragment
            if (!isHome) {
                fragmentTransaction.addToBackStack(null);
            }

            fragmentTransaction.commit();
        } else {
            // Si el fragmento ya existe, simplemente mostrarlo
            fragmentManager.beginTransaction()
                    .show(existingFragment)
                    .commit();
        }
    }

    public void hideProgressBar() {
        View progressBarLayout = findViewById(R.id.progress_layout);
        if (progressBarLayout != null) {
            progressBarLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onBackPressed() {
        Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.container);
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else if (!(currentFragment instanceof HomeFragment)) {
            // Resetear filtros solo cuando volvemos explícitamente al Home
            postViewModel.resetFilters();
            postViewModel.loadPosts();
            openFragment(HomeFragment.newInstance(), true, "HOME_FRAGMENT");
        } else {
            super.onBackPressed();
        }
    }
}
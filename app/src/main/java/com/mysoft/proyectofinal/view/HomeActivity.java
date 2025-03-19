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
import com.google.android.material.navigation.NavigationBarView;
import com.mysoft.proyectofinal.R;
import com.mysoft.proyectofinal.databinding.ActivityHomeBinding;
import com.mysoft.proyectofinal.view.fragments.ChatsFragment;
import com.mysoft.proyectofinal.view.fragments.FiltrosFragment;
import com.mysoft.proyectofinal.view.fragments.HomeFragment;
import com.mysoft.proyectofinal.view.fragments.PerfilFragment;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private View progressBarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inflar el ProgressBar
        LayoutInflater inflater = LayoutInflater.from(this);
        progressBarLayout = inflater.inflate(R.layout.progress_layout, binding.mainCont, false);
        binding.mainCont.addView(progressBarLayout);
        showProgressBar();

        // Configurar el BottomNavigationView
        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                try {
                    Fragment selectedFragment = null;
                    if (item.getItemId() == R.id.itemHome) {
                        selectedFragment = HomeFragment.newInstance();
                    } else if (item.getItemId() == R.id.itemChats) {
                        selectedFragment = new ChatsFragment();
                    } else if (item.getItemId() == R.id.itemPerfil) {
                        selectedFragment = new PerfilFragment();
                    } else if (item.getItemId() == R.id.itemFiltros) {
                        selectedFragment = new FiltrosFragment();
                    }

                    if (selectedFragment != null) {
                        openFragment(selectedFragment);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;
            }
        });

        // Cargar HomeFragment por defecto
        if (savedInstanceState == null) {
            openFragment(HomeFragment.newInstance());
        }
    }

    private void openFragment(Fragment fragment) {
        try {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container, fragment);
            fragmentTransaction.commitAllowingStateLoss(); // Previene crashes por pérdida de estado
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showProgressBar() {
        if (progressBarLayout != null) {
            progressBarLayout.setVisibility(View.VISIBLE);
        }
    }

    public void hideProgressBar() {
        if (progressBarLayout != null) {
            progressBarLayout.setVisibility(View.GONE);
        }
    }
}

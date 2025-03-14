package com.mysoft.proyectofinal.view;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //agrego un progress bar carando datos
        LayoutInflater inflater = LayoutInflater.from(this);
        View progressBarLayout = inflater.inflate(R.layout.progress_layout, binding.mainCont, false);
        binding.mainCont.addView(progressBarLayout);


        binding.bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if (item.getItemId() == R.id.itemHome) {
                    openFragment(HomeFragment.newInstance()); // Pasamos userId aquí
                } else if (item.getItemId() == R.id.itemChats) {
                    openFragment(new ChatsFragment());
                } else if (item.getItemId() == R.id.itemPerfil) {
                    openFragment(new PerfilFragment());
                } else if (item.getItemId() == R.id.itemFiltros) {
                    openFragment(new FiltrosFragment());
                }
                return true;
            }
        });
        openFragment(HomeFragment.newInstance()); // Pasa userId aquí también
    }

    private void openFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container, fragment);
        fragmentTransaction.commit();
    }

    public void hideProgressBar() {
        View progressBarLayout = findViewById(R.id.progress_layout);
        if (progressBarLayout != null) {
            progressBarLayout.setVisibility(View.GONE);
        }
    }
}
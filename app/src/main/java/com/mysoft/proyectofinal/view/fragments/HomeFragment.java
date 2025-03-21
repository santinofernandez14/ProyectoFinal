package com.mysoft.proyectofinal.view.fragments;



import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;


import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mysoft.proyectofinal.R;
import com.mysoft.proyectofinal.adapters.PostAdapter;
import com.mysoft.proyectofinal.databinding.FragmentHomeBinding;
import com.mysoft.proyectofinal.model.Post;
import com.mysoft.proyectofinal.view.HomeActivity;
import com.mysoft.proyectofinal.view.MainActivity;
import com.mysoft.proyectofinal.view.PostActivity;
import com.mysoft.proyectofinal.view.PostDetailActivity;
import com.mysoft.proyectofinal.viewmodel.AuthViewModel;
import com.mysoft.proyectofinal.viewmodel.PostViewModel;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private FragmentHomeBinding binding;
    private PostViewModel postViewModel;
    private AuthViewModel authViewModel;
    private PostAdapter postAdapter;
    private boolean isFirstLoad = true;
    private LinearLayout emptyView;
    private FloatingActionButton fab;

    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize ViewModels
        postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);
        authViewModel = new ViewModelProvider(requireActivity()).get(AuthViewModel.class);

        // Configure Toolbar
        ((AppCompatActivity) requireActivity()).setSupportActionBar(binding.tools);

        // Configure RecyclerView
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        postAdapter = new PostAdapter(new ArrayList<>()); // Initialize with empty list
        binding.recyclerView.setAdapter(postAdapter);

        // Get reference to empty view
        emptyView = binding.emptyView;

        // Configure FAB
        fab = binding.fab;
        if (fab != null) {
            Log.d("HomeFragment", "FAB encontrado y configurado");
            fab.setVisibility(View.VISIBLE);
            fab.setOnClickListener(v -> {
                Log.d("HomeFragment", "FAB clicked");
                Intent intent = new Intent(getContext(), PostActivity.class);
                startActivity(intent);
            });
        } else {
            Log.e("HomeFragment", "FAB es null");
            // Intenta encontrar el FAB directamente desde la vista
            fab = view.findViewById(R.id.fab);
            if (fab != null) {
                Log.d("HomeFragment", "FAB encontrado directamente desde la vista");
                fab.setVisibility(View.VISIBLE);
                fab.setOnClickListener(v -> {
                    Log.d("HomeFragment", "FAB clicked");
                    Intent intent = new Intent(getContext(), PostActivity.class);
                    startActivity(intent);
                });
            } else {
                Log.e("HomeFragment", "FAB no encontrado en la vista");
            }
        }

        // Solo resetear filtros en la primera carga
        if (isFirstLoad) {
            postViewModel.resetFilters();
            isFirstLoad = false;
        }

        // Cargar posts
        cargarPosts();

        // Setup menu
        setupMenu();
    }

    @Override
    public void onResume() {
        super.onResume();

        // Limpiar el adaptador para evitar que se muestren imágenes antiguas
        if (postAdapter != null) {
            postAdapter.clearPosts();
        }

        // Solo resetear filtros si venimos de otra actividad, no de un fragmento
        if (requireActivity().getSupportFragmentManager().getBackStackEntryCount() == 0) {
            postViewModel.resetFilters();
        }

        // Recargar posts
        cargarPosts();

        // Asegurar que el FAB esté visible
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
        } else {
            Log.e("HomeFragment", "FAB es null en onResume");
            // Intenta encontrar el FAB nuevamente
            View view = getView();
            if (view != null) {
                fab = view.findViewById(R.id.fab);
                if (fab != null) {
                    fab.setVisibility(View.VISIBLE);
                }
            }
        }
    }

    private void cargarPosts() {
        // Mostrar el indicador de progreso
        if (getActivity() instanceof HomeActivity) {
            View progressBarLayout = getActivity().findViewById(R.id.progress_layout);
            if (progressBarLayout != null) {
                progressBarLayout.setVisibility(View.VISIBLE);
            }
        }

        postViewModel.getPosts().observe(getViewLifecycleOwner(), this::updateUI);
    }

    private void updateUI(List<Post> posts) {
        // Ocultar el indicador de progreso
        if (getActivity() instanceof HomeActivity) {
            ((HomeActivity) requireActivity()).hideProgressBar();
        }

        if (posts == null) {
            posts = new ArrayList<>();
        }

        Log.d("HomeFragment", "Número de posts: " + posts.size());

        // Actualizar el adaptador
        postAdapter.setPosts(posts);

        // Mostrar vista vacía si no hay posts y hay filtros activos
        if (posts.isEmpty() && postViewModel.isFiltered()) {
            binding.recyclerView.setVisibility(View.GONE);
            emptyView.setVisibility(View.VISIBLE);
        } else {
            binding.recyclerView.setVisibility(View.VISIBLE);
            emptyView.setVisibility(View.GONE);
        }

        // Asegurar que el FAB esté visible
        if (fab != null) {
            fab.setVisibility(View.VISIBLE);
        }
    }

    private void setupMenu() {
        requireActivity().addMenuProvider(new MenuProvider() {
            @Override
            public void onCreateMenu(@NonNull Menu menu, @NonNull MenuInflater menuInflater) {
                menuInflater.inflate(R.menu.main_menu, menu);
            }

            @Override
            public boolean onMenuItemSelected(@NonNull MenuItem menuItem) {
                if (menuItem.getItemId() == R.id.itemLogout) {
                    onLogout();
                    return true;
                }
                return false;
            }
        }, getViewLifecycleOwner(), Lifecycle.State.RESUMED);
    }

    private void onLogout() {
        authViewModel.logout().observe(getViewLifecycleOwner(), logoutResult -> {
            if (logoutResult != null && logoutResult) {
                Intent intent = new Intent(getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            } else {
                Toast.makeText(getContext(), "Error al cerrar sesión. Intenta nuevamente.", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Prevent memory leaks
    }
}

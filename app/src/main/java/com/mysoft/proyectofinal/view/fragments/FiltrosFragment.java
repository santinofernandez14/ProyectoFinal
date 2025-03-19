package com.mysoft.proyectofinal.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.mysoft.proyectofinal.R;
import com.parse.ParseUser;

public class FiltrosFragment extends Fragment {

    private Spinner spinnerCategorias;
    private Button btnAplicar;
    private String categoriaSeleccionada;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filtros, container, false);

        spinnerCategorias = view.findViewById(R.id.spinnerCategorias);
        btnAplicar = view.findViewById(R.id.btnAplicar);

        setupCategorySpinner();
        setupApplyFilterButton();

        return view;
    }

    private void setupCategorySpinner() {
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.categorias_array, android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategorias.setAdapter(adapter);

        spinnerCategorias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoriaSeleccionada = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                categoriaSeleccionada = null;
            }
        });
    }

    private void setupApplyFilterButton() {
        btnAplicar.setOnClickListener(v -> {
            if (categoriaSeleccionada == null || categoriaSeleccionada.isEmpty()) {
                Toast.makeText(getContext(), "Selecciona una categoría", Toast.LENGTH_SHORT).show();
                return;
            }

            // Verifica que el usuario sigue autenticado antes de aplicar filtros
            if (ParseUser.getCurrentUser() == null) {
                Toast.makeText(getContext(), "La sesión ha expirado. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
                return;
            }

            abrirPostDetailFragment(categoriaSeleccionada);
        });
    }

    private void abrirPostDetailFragment(String categoria) {
        PostDetailFragment postDetailFragment = new PostDetailFragment();
        Bundle args = new Bundle();
        args.putString("categoria", categoria);
        postDetailFragment.setArguments(args);

        FragmentTransaction transaction = requireActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.container, postDetailFragment, "POST_DETAIL");
        transaction.addToBackStack(null);
        transaction.commit();
    }
}

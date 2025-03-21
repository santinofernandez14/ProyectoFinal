package com.mysoft.proyectofinal.view.fragments;

import android.os.Bundle;
import android.util.Log;
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
import androidx.lifecycle.ViewModelProvider;

import com.mysoft.proyectofinal.R;
import com.mysoft.proyectofinal.viewmodel.PostViewModel;
import com.parse.ParseUser;

public class FiltrosFragment extends Fragment {
    private Spinner spinnerCategoria;
    private Spinner spinnerOrden;
    private Button btnAplicar;
    private PostViewModel postViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_filtros, container, false);

        spinnerCategoria = view.findViewById(R.id.spinnerCategoria);
        spinnerOrden = view.findViewById(R.id.spinnerOrden);
        btnAplicar = view.findViewById(R.id.btnAplicar);

        postViewModel = new ViewModelProvider(requireActivity()).get(PostViewModel.class);

        configurarSpinners();
        btnAplicar.setOnClickListener(v -> aplicarFiltros());

        return view;
    }

    private void configurarSpinners() {
        ArrayAdapter<CharSequence> categoriaAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.categorias_array, android.R.layout.simple_spinner_item);
        categoriaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(categoriaAdapter);

        ArrayAdapter<CharSequence> ordenAdapter = ArrayAdapter.createFromResource(
                requireContext(), R.array.orden_array, android.R.layout.simple_spinner_item);
        ordenAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOrden.setAdapter(ordenAdapter);
    }

    private void aplicarFiltros() {
        String categoria = spinnerCategoria.getSelectedItem().toString();
        String orden = spinnerOrden.getSelectedItem().toString();

        Log.d("FiltrosFragment", "Aplicando filtros: Categoría=" + categoria + ", Orden=" + orden);

        // Aplicar filtros y cargar posts filtrados
        postViewModel.aplicarFiltros(categoria, orden);

        requireActivity().getSupportFragmentManager().popBackStack();
    }
}
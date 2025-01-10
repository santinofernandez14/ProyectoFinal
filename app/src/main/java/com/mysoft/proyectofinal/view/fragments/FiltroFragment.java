package com.mysoft.proyectofinal.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mysoft.proyectofinal.R;
import com.mysoft.proyectofinal.databinding.FragmentFiltrosBinding;

public class FiltroFragment extends Fragment {

    private FragmentFiltrosBinding binding;
    private ActivityResultLauncher<Intent> someActivityResultLauncher;

    public FiltroFragment() {}

    public static FiltroFragment newInstance(String p1, String p2) {
        return new FiltroFragment();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_filtros, container, false);
    }

}
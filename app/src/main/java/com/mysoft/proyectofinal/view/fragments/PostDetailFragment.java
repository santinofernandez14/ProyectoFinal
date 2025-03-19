package com.mysoft.proyectofinal.view.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mysoft.proyectofinal.R;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

public class PostDetailFragment extends Fragment {

    private TextView txtLugar, txtCategoria, txtDescripcion, txtDuracion, txtPresupuesto;
    private ImageView imgPost;
    private String categoriaRecibida;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_post_detail, container, false);

        imgPost = view.findViewById(R.id.viewPager);
        txtLugar = view.findViewById(R.id.lugar);
        txtCategoria = view.findViewById(R.id.categoria);
        txtDescripcion = view.findViewById(R.id.description);
        txtDuracion = view.findViewById(R.id.duracion);
        txtPresupuesto = view.findViewById(R.id.presupuesto);

        if (getArguments() != null) {
            categoriaRecibida = getArguments().getString("categoria");
        }

        if (categoriaRecibida == null || categoriaRecibida.isEmpty()) {
            Toast.makeText(getContext(), "No se recibió una categoría válida.", Toast.LENGTH_SHORT).show();
            return view;
        }

        // Verificar si el usuario sigue autenticado antes de cargar los posts
        if (ParseUser.getCurrentUser() == null) {
            Toast.makeText(getContext(), "La sesión ha expirado. Inicia sesión nuevamente.", Toast.LENGTH_SHORT).show();
            requireActivity().finish();
            return view;
        }

        cargarPostPorCategoria(categoriaRecibida);

        return view;
    }

    private void cargarPostPorCategoria(String categoria) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Post");
        query.whereEqualTo("categoria", categoria);
        query.getFirstInBackground((post, e) -> {
            if (e == null && post != null) {
                txtLugar.setText(post.getString("lugar"));
                txtCategoria.setText(post.getString("categoria"));
                txtDescripcion.setText(post.getString("descripcion"));
                txtDuracion.setText(post.getString("duracion"));
                txtPresupuesto.setText(post.getString("presupuesto"));

                ParseFile imageFile = post.getParseFile("imagen");
                if (imageFile != null) {
                    Glide.with(requireContext()).load(imageFile.getUrl()).into(imgPost);
                }
            } else {
                Toast.makeText(getContext(), "No se encontraron posts para esta categoría.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

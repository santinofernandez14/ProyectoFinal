package com.mysoft.proyectofinal.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mysoft.proyectofinal.R;
import com.mysoft.proyectofinal.model.Post;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private List<Post> posts;
    private OnPostClickListener listener; // Listener para manejar el click

    // Constructor con el listener
    public PostAdapter(List<Post> posts, OnPostClickListener listener) {
        this.posts = posts;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_post, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post = posts.get(position);
        holder.tvTitulo.setText(post.getTitulo());
        holder.tvDescripcion.setText(post.getDescripcion());

        Log.d("PostAdapter", "Cargando imágenes: " + post.getImagenes());

        List<String> imagenes = post.getImagenes();

        // Verificar si las imágenes no son nulas ni vacías
        if (imagenes != null && !imagenes.isEmpty()) {
            // Recorrer las imágenes y cargarlas dinámicamente
            ImageView[] imageViews = {holder.ivImage1, holder.ivImage2, holder.ivImage3};

            // Ocultar todas las imágenes inicialmente
            for (ImageView imageView : imageViews) {
                imageView.setVisibility(View.GONE);
            }

            // Cargar las imágenes correspondientes
            for (int i = 0; i < imagenes.size(); i++) {
                Picasso.get()
                        .load(imagenes.get(i))
                        .fit()
                        .centerCrop()
                        .error(R.drawable.uploadimg)
                        .into(imageViews[i]);
                imageViews[i].setVisibility(View.VISIBLE);

                // Añadir un OnClickListener a cada imagen
                final int index = i; // Para capturar el índice de la imagen
                imageViews[i].setOnClickListener(v -> {
                    // Llamar al listener para pasar el post seleccionado
                    listener.onPostClick(post, index);
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }

    // ViewHolder para manejar las vistas
    public static class PostViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion;
        ImageView ivImage1, ivImage2, ivImage3;

        public PostViewHolder(View itemView) {
            super(itemView);
            tvTitulo = itemView.findViewById(R.id.tvTitulo);
            tvDescripcion = itemView.findViewById(R.id.tvDescripcion);
            ivImage1 = itemView.findViewById(R.id.ivImage1);
            ivImage2 = itemView.findViewById(R.id.ivImage2);
            ivImage3 = itemView.findViewById(R.id.ivImage3);
        }
    }

    // Interface para manejar el click en el post
    public interface OnPostClickListener {
        void onPostClick(Post post, int imageIndex);
    }
}

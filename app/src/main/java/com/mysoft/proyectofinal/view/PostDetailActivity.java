package com.mysoft.proyectofinal.view;

import android.os.Bundle;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.mysoft.proyectofinal.R;
import com.mysoft.proyectofinal.databinding.ActivityPostDetailBinding;
import com.mysoft.proyectofinal.viewmodel.PostDetailViewModel;
import com.mysoft.proyectofinal.viewmodel.PostViewModel;
import com.parse.ParseUser;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {
    private ActivityPostDetailBinding binding;
    private ImageCarousel imageCarousel;
    private PostViewModel postDetailViewModel;
    private String postId;  // Declaramos postId como una variable de clase
    private String userId;  // Declaramos userId
    private CircleImageView circleImageView; // Declaramos CircleImageView para la foto de perfil

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Obtener el userId y postId desde el Intent
        userId = getIntent().getStringExtra("USER_ID");
        postId = getIntent().getStringExtra("POST_ID");

        // Verificar que los valores no sean nulos o vacíos
        if (userId == null || userId.isEmpty()) {
            Log.e("PostDetailActivity", "El userId recibido es nulo o vacío");
            return;  // Termina aquí si el userId es inválido
        }
        if (postId == null || postId.isEmpty()) {
            Log.e("PostDetailActivity", "El postId recibido es nulo o vacío");
            return;  // Termina aquí si el postId es inválido
        }

        Log.d("PostDetailActivity", "UserId recibido: " + userId);
        Log.d("PostDetailActivity", "PostId recibido: " + postId);

        // Inicializar componentes
        imageCarousel = findViewById(R.id.carousel);
        circleImageView = findViewById(R.id.circleImageView);
        postDetailViewModel = new ViewModelProvider(this).get(PostViewModel.class);

        // Llamar al método para cargar la foto de perfil del usuario

        // Llamar al método para cargar los detalles del post
        cargarDetallesPost(postId);
    }

    // Método para cargar los detalles del post
    private void cargarDetallesPost(String postId) {
        TextView lugar = findViewById(R.id.lugar);
        TextView categoria = findViewById(R.id.categoria);
        TextView description = findViewById(R.id.description);
        TextView duracion = findViewById(R.id.duracion);
        TextView presupuesto = findViewById(R.id.presupuesto);

        // Obtener los detalles del post desde el ViewModel
        postDetailViewModel.getPostDetail(postId).observe(this, postDetails -> {
            if (postDetails != null) {
                lugar.setText(postDetails.getTitulo());
                categoria.setText(postDetails.getCategoria());
                description.setText(postDetails.getDescripcion());
                duracion.setText(postDetails.getDuracion());
                presupuesto.setText(String.valueOf(postDetails.getPresupuesto())); // Asegúrate de convertir el valor en String
            } else {
                Log.e("PostDetailActivity", "No se encontraron detalles para el post con ID: " + postId);
            }
        });
    }


}

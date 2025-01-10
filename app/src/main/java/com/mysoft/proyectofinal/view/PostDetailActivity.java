package com.mysoft.proyectofinal.view;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.mysoft.proyectofinal.R;
import com.mysoft.proyectofinal.databinding.ActivityPostDetailBinding;
import com.mysoft.proyectofinal.viewmodel.PostDetailViewModel;
import com.parse.ParseUser;

import org.imaginativeworld.whynotimagecarousel.ImageCarousel;
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class PostDetailActivity extends AppCompatActivity {
    private ActivityPostDetailBinding binding;
    private ImageCarousel imageCarousel;
    private PostDetailViewModel postDetailViewModel;
    private String postId;  // Declaramos postId como una variable de clase
    private CircleImageView circleImageView; // Declaramos CircleImageView para la foto de perfil
    private CircleImageView circleImageBackPostDetail;
    private MutableLiveData<Boolean> navigateToHome = new MutableLiveData<>();

    public LiveData<Boolean> getNavigateToHome() {
        return navigateToHome;
    }

    public void navigateToHome() {
        navigateToHome.setValue(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_detail);

        // Obtener el ImageCarousel del layout
        imageCarousel = findViewById(R.id.carousel);

        // Inicializar CircleImageView para la foto de perfil
        circleImageView = findViewById(R.id.circleImageView);

        // Inicializar ViewModel
        postDetailViewModel = new ViewModelProvider(this).get(PostDetailViewModel.class);

        // Obtener el usuario (supongamos que ya tienes un ParseUser actual)
        ParseUser user = ParseUser.getCurrentUser();  // Obtener el usuario actual (por ejemplo)

        // Obtener los posts de este usuario
        postDetailViewModel.getAllPosts(user);

        // Observar las imágenes de los posts
        postDetailViewModel.getImages().observe(this, posts -> {
            if (posts != null && !posts.isEmpty()) {
                List<CarouselItem> carouselItems = new ArrayList<>();
                for (String imageUrl : posts) {
                    // Crear un CarouselItem para cada imagen
                    carouselItems.add(new CarouselItem(imageUrl));
                }

                // Configurar las imágenes del carrusel
                imageCarousel.setData(carouselItems);
            }
        });

        // Obtener el postId (puedes obtenerlo de un extra de Intent, o asignarlo de alguna manera)
        postId = "some_post_id";  // Reemplaza con el ID del post real

        // Llamar al método para cargar los detalles del post
        cargarDetallesPost();

        // Llamar al método para cargar la foto de perfil del usuario
        cargarFotoPerfil(user.getObjectId());



    }



    private void cargarDetallesPost() {
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
            }
        });
    }


    // Método para cargar la foto de perfil
    private void cargarFotoPerfil(String userId) {
        // Llamar al método del ViewModel para obtener la URL de la foto de perfil
        postDetailViewModel.fetchProfileImage(userId);

        // Observar la URL de la foto de perfil
        postDetailViewModel.getProfileImageUrl().observe(this, fotoPerfilUrl -> {
            if (fotoPerfilUrl != null && !fotoPerfilUrl.isEmpty()) {
                // Si la URL no está vacía, cargamos la imagen con Glide
                Glide.with(this)
                        .load(fotoPerfilUrl)
                        .into(circleImageView);
            } else {
                // Si no hay URL, se asigna una imagen predeterminada
                circleImageView.setImageResource(R.drawable.ic_person);
            }
        });
    }



}

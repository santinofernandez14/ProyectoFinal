package com.mysoft.proyectofinal.model;



import com.parse.ParseClassName;
import com.parse.ParseObject;

import java.util.List;

@ParseClassName("Post")
public class Post extends ParseObject {


    // Definimos los campos de la clase Post
    private String titulo;
    private String descripcion;
    private int duracion;
    private String categoria;
    private double presupuesto;
    private List<String> imagenes;

    // Constructor
    public Post() {
        // Esto es necesario para ParseObject
    }

    public String getId() {
        return getObjectId(); // Obtiene el ID único de Parse
    }

    // Getters y setters para cada campo
    public String getTitulo() {
        return getString("titulo");
    }

    public void setTitulo(String titulo) {
        put("titulo", titulo);
    }

    public String getDescripcion() {
        return getString("descripcion");
    }

    public void setDescripcion(String descripcion) {
        put("descripcion", descripcion);
    }

    public int getDuracion() {
        return getInt("duracion");
    }

    public void setDuracion(int duracion) {
        put("duracion", duracion);
    }

    public String getCategoria() {
        return getString("categoria");
    }

    public void setCategoria(String categoria) {
        put("categoria", categoria);
    }

    public double getPresupuesto() {
        return getDouble("presupuesto");
    }

    public void setPresupuesto(double presupuesto) {
        put("presupuesto", presupuesto);
    }

    public List<String> getImagenes() {
        return getList("imagenes");
    }

    public void setImagenes(List<String> imagenes) {
        put("imagenes", imagenes);
    }
}
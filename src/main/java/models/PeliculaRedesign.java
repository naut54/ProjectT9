package models;

import utils.Validate;

import java.util.Objects;

public class PeliculaRedesign {
    private int id;
    private String titulo;
    private String director;
    private int duracion;
    private double precio;
    private enum categoria{
        COMEDIA,
        TERROR,
        ACCION,
        ROMANCE,
        AVENTURA
    };

    public PeliculaRedesign(int id, String titulo, String director, int duracion, double precio) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.duracion = duracion;
        this.precio = precio;
    }

    public int getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDirector() {
        return director;
    }

    public double getDuracion() {
        return duracion;
    }

    public double getPrecio() {
        return precio;
    }

    @Override
    public String toString() {
        return "PeliculaRedesign{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", director='" + director + '\'' +
                ", duracion=" + duracion +
                ", precio=" + precio +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PeliculaRedesign pelicula = (PeliculaRedesign) o;
        return (id == pelicula.id);
    }

    @Override
    public int hashCode() {
        int result;
        result = id;
        return result;
    }
}

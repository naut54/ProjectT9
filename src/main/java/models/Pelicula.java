package models;

import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import data.DataAccessObject;

public class Pelicula {
    private int id;
    private String titulo;
    private String director;
    private int duracion;
    private double precio;
    private String categoria;
    private final DataAccessObject dao = new DataAccessObject();

    public Pelicula(int id, String titulo, String director, int duracion, double precio, String categoria) {
        this.id = id;
        this.titulo = titulo;
        this.director = director;
        this.duracion = duracion;
        this.precio = precio;
        this.categoria = categoria;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    @Override
    public String toString() {
        return "Pelicula{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", director='" + director + '\'' +
                ", duracion=" + duracion +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' +
                '}';
    }

    public boolean storeMovie(Pelicula movie) {
        return dao.storeObject(movie);
    }

    public boolean updateMovie(Pelicula movie) {
        return dao.updateObject(movie);
    }

    //public boolean deleteMovie(Pelicula movie) {
    //    return dao.deleteObject(movie);
    //}

    public Pelicula getMovieById(int id) {
        ObjectSet<Pelicula> result = dao.retrieveWithCondition(Pelicula.class, new Predicate<Pelicula>() {
            @Override
            public boolean match(Pelicula pelicula) {
                return pelicula.getId() == id;
            }
        });

        return result.hasNext() ? result.next() : null;
    }

//    public ObjectSet<Pelicula> getAllMovies() {
//        return dao.retrieveAll(new Pelicula(0, null, null, 0, 0, null));
//    }
}

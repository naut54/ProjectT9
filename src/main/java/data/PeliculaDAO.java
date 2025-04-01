package data;

import com.db4o.ObjectSet;
import models.Pelicula;

import java.util.List;

public class PeliculaDAO {
    private final DataAccessObject dao;

    public PeliculaDAO() {
        dao = new DataAccessObject();
    }

    public boolean storeMovie(Pelicula movie) {
        return dao.storeObject(movie);
    }

    public boolean updateMovie(Pelicula movie) {
        return dao.updateObject(movie);
    }

    public boolean deleteMovie(Pelicula movie) {
        return dao.deleteObject(movie);
    }

    public Pelicula searchById(int id) {
        ObjectSet<Pelicula> result = dao.retrieveWithCondition(Pelicula.class,
                pelicula -> pelicula.getId() == id);

        return result.hasNext() ? result.next() : null;
    }

    public List<Pelicula> getAllMovies() {

    }
}

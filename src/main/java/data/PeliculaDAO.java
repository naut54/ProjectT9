package data;

import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import models.PeliculaRedesign;

import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO {
    private static DataAccessObject dao = new DataAccessObject();

    public PeliculaDAO() {
        dao = new DataAccessObject();
    }

    public static void storeMovie(PeliculaRedesign movie) {
        dao.storeObject(movie);
    }

    public boolean updateMovie(PeliculaRedesign movie) {
        return dao.updateObject(movie);
    }

    public boolean deleteMovie(PeliculaRedesign movie) {
        return dao.deleteObject(movie);
    }

    public PeliculaRedesign searchById(final int id) {
        ObjectSet<PeliculaRedesign> result = dao.retrieveWithCondition(PeliculaRedesign.class,
                new Predicate<PeliculaRedesign>() {
                    public boolean match(PeliculaRedesign pelicula) {
                        return pelicula.getId() == id;
                    }
                });

        return result != null && result.hasNext() ? result.next() : null;
    }

    /**
     * Recupera todas las películas almacenadas en la base de datos.
     *
     * @return Lista con todas las películas, o una lista vacía si no hay películas
     * o ocurre un error.
     */
    public List<PeliculaRedesign> getAllMovies() {
        ObjectSet<PeliculaRedesign> result = dao.retrieveWithCondition(PeliculaRedesign.class,
                new Predicate<PeliculaRedesign>() {
                    public boolean match(PeliculaRedesign pelicula) {
                        return true; // Devuelve todas las películas
                    }
                });

        List<PeliculaRedesign> movies = new ArrayList<>();
        if (result != null) {
            while (result.hasNext()) {
                movies.add(result.next());
            }
        }

        return movies;
    }

    /**
     * Busca películas que contengan la cadena especificada en su título o director.
     * La búsqueda no distingue entre mayúsculas y minúsculas.
     *
     * @param searchTerm Término de búsqueda para comparar con título o director
     * @return Lista de películas que coinciden con el criterio de búsqueda
     */
    public List<PeliculaRedesign> searchByString(final String searchTerm) {
        if (searchTerm == null || searchTerm.trim().isEmpty()) {
            return new ArrayList<>();
        }

        final String term = searchTerm.toLowerCase();

        ObjectSet<PeliculaRedesign> result = dao.retrieveWithCondition(PeliculaRedesign.class,
                new Predicate<PeliculaRedesign>() {
                    public boolean match(PeliculaRedesign pelicula) {
                        return pelicula.getTitulo().toLowerCase().contains(term) ||
                                pelicula.getDirector().toLowerCase().contains(term);
                    }
                });

        List<PeliculaRedesign> movies = new ArrayList<>();
        if (result != null) {
            while (result.hasNext()) {
                movies.add(result.next());
            }
        }

        return movies;
    }
}
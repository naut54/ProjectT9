package data;

import models.PeliculaRedesign;

import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO {
    private static DataAccessObject dao = new DataAccessObject();

    public PeliculaDAO() {
        dao = new DataAccessObject();
    }

    /**
     * Stores the specified movie in the database.
     *
     * @param movie the {@code PeliculaRedesign} object to be stored; must not be null
     * @return true if the movie is successfully stored in the database, false otherwise
     */
    public static boolean storeMovie(PeliculaRedesign movie) {
        return dao.storeObject(movie);
    }

    /**
     * Updates the details of a given movie in the database.
     *
     * @param movie the {@code PeliculaRedesign} object containing the updated movie details; must not be null
     * @return true if the movie is successfully updated in the database, false otherwise
     */
    public boolean updateMovie(PeliculaRedesign movie) {
        return dao.updateObject(movie);
    }

    /**
     * Deletes the specified movie from the database.
     *
     * @param movie the {@code PeliculaRedesign} object to be deleted; must not be null
     * @return true if the movie is successfully deleted, false otherwise
     */
    public boolean deleteMovie(PeliculaRedesign movie) {
        return dao.deleteObject(movie);
    }

    /**
     * Searches for a PeliculaRedesign object by its unique identifier.
     *
     * @param id the unique identifier of the PeliculaRedesign to search for
     * @return the PeliculaRedesign object with the specified id if found,
     *         or null if no such object exists
     */
    public PeliculaRedesign searchById(final int id) {
        List<PeliculaRedesign> results = dao.retrieveByFieldValue(PeliculaRedesign.class, "id", id);
        return !results.isEmpty() ? results.getFirst() : null;
    }

    /**
     * Recupera todas las películas almacenadas en la base de datos.
     *
     * @return Lista con todas las películas, o una lista vacía si no hay películas
     * o ocurre un error.
     */
    public List<PeliculaRedesign> getAllMovies() {
        return dao.retrieveAllObjects(PeliculaRedesign.class);
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

        // Recuperamos todas las películas y filtramos manualmente
        List<PeliculaRedesign> allMovies = getAllMovies();
        List<PeliculaRedesign> results = new ArrayList<>();

        for (PeliculaRedesign pelicula : allMovies) {
            if (pelicula.getTitulo().toLowerCase().contains(term) ||
                    pelicula.getDirector().toLowerCase().contains(term)) {
                results.add(pelicula);
            }
        }

        return results;
    }
}
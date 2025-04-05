package data;

import models.PeliculaRedesign;

import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO {
    private static DataAccessObject dao = new DataAccessObject();

    public PeliculaDAO() {
        dao = new DataAccessObject();
    }

    public static boolean storeMovie(PeliculaRedesign movie) {
        return dao.storeObject(movie);
    }

    public boolean updateMovie(PeliculaRedesign movie) {
        return dao.updateObject(movie);
    }

    public boolean deleteMovie(PeliculaRedesign movie) {
        return dao.deleteObject(movie);
    }

    public PeliculaRedesign searchById(final int id) {
        // Usamos consulta SODA en lugar de Predicate
        List<PeliculaRedesign> results = dao.retrieveByFieldValue(PeliculaRedesign.class, "id", id);
        return !results.isEmpty() ? results.get(0) : null;
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
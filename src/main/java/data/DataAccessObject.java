package data;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import models.PeliculaRedesign;

import java.util.ArrayList;
import java.util.List;

public class DataAccessObject {
    private static ObjectContainer db = DBConnection.connect();
    /**
     * Attempts to store the given object into the database. If the database connection
     * is not established, it attempts to reconnect before proceeding. Commits the transaction
     * on success or rolls back the transaction in case of failure.
     *
     * @param object the object to be stored in the database
     * @return true if the object is successfully stored, false otherwise
     */
    public boolean storeObject(Object object) {
        if (db == null) {
            if (!DBConnection.reConnect()) {
                return false;
            }
        }

        try {
            db.store(object);
            db.commit();
            return true;
        } catch (Exception e) {
            try {
                db.rollback();
                System.err.println("Failed to store object: " + e.getMessage());
            } catch (Exception ex) {
                System.err.println("Failed to rollback transaction: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Retrieves all database records that match the example object passed as a parameter.
     * If the database connection is not established, it attempts to reconnect before proceeding.
     * Ensures that the transaction is committed on success or rolled back in case of failure.
     *
     * @param object the example object used as a template for querying matching records
     * @return true if the records are successfully retrieved, false otherwise
     */
    public boolean retrieveAll(Object object) {
        if (db == null) {
            if (!DBConnection.reConnect()) {
                return false;
            }
        }
        try {
            db.queryByExample(object);
            db.commit();
            return true;
        } catch (Exception e) {
            try {
                db.rollback();
                System.err.println("Failed to retrieve object: " + e.getMessage());
            } catch (Exception ex) {
                System.err.println("Failed to rollback transaction: " + e.getMessage());
            }
            return false;
        }
    }

    /**
     * Retrieves records from the database that match the given condition. If the database connection
     * is not established, it attempts to reconnect before proceeding. This method queries the database
     * using the specified predicate to filter results, prints each result to the console, and returns
     * the retrieved records if the operation is successful.
     *
     * @param <T>       the type of objects to be retrieved
     * @param clazz     the class object representing the type of the records to be retrieved
     * @param condition a predicate used to filter the database records
     * @return an ObjectSet containing the retrieved records if successful, or null otherwise
     */
    public <T> ObjectSet<T> retrieveWithCondition(Class<T> clazz, Predicate<T> condition) {
        if (db == null) {
            if (!DBConnection.reConnect()) {
                return null;
            }
        }
        try {
            ObjectSet<T> results = db.query(condition);
            for (T result : results) {
                System.out.println(result);
            }
            return results;
        } catch (Exception e) {
            System.err.println("Failed to retrieve records: " + e.getMessage());
            return null;
        }
    }

    /**
     * Updates the specified object within the database. If the database connection
     * is not established or has been closed, it attempts to reconnect before proceeding.
     * Commits the transaction if the update is successful, or rolls back the transaction
     * in case of failure.
     *
     * @param object the object to be updated in the database
     * @return true if the object is successfully updated in the database, false otherwise
     */
    public boolean updateObject(Object object) {
        if (object == null) {
            System.err.println("Cannot update null object");
            return false;
        }

        if (db == null || db.ext().isClosed()) {
            if (!DBConnection.reConnect()) {
                System.err.println("Failed to connect to database");
                return false;
            }
            db = DBConnection.connect();
        }

        try {
            db.ext().set(object);
            db.commit();
            return true;
        } catch (Exception e) {
            try {
                db.rollback();
                System.err.println("Failed to update object: " + e.getMessage());
            } catch (Exception ex) {
                System.err.println("Failed to rollback transaction: " + ex.getMessage());
            }
            return false;
        }
    }

    /**
     * Deletes the specified PeliculaRedesign object from the database based on its ID.
     * If the database connection is not established or has been closed, attempts to reconnect before proceeding.
     *
     * @param pelicula the PeliculaRedesign object to be deleted from the database; must not be null
     * @return true if the object is successfully deleted, false otherwise
     */
    public boolean deleteObject(PeliculaRedesign pelicula) {
        if (pelicula == null) {
            System.err.println("No se puede eliminar una película nula");
            return false;
        }

        int idBuscado = pelicula.getId();

        if (db == null || db.ext().isClosed()) {
            if (!DBConnection.reConnect()) {
                System.err.println("Error al conectar con la base de datos");
                return false;
            }
            db = DBConnection.connect();
        }

        try {
            ObjectSet<PeliculaRedesign> result = db.query(new Predicate<PeliculaRedesign>() {
                public boolean match(PeliculaRedesign p) {
                    return p.getId() == idBuscado;
                }
            });

            if (result.hasNext()) {
                PeliculaRedesign peliculaToDelete = result.next();
                db.delete(peliculaToDelete);
                db.commit();
                return true;
            } else {
                System.err.println("No se encontró película con ID: " + idBuscado);
                return false;
            }
        } catch (Exception e) {
            try {
                db.rollback();
                System.err.println("Error al eliminar objeto: " + e.getMessage());
            } catch (Exception ex) {
                System.err.println("Error al hacer rollback: " + ex.getMessage());
            }
            return false;
        }
    }

    /**
     * Recupera todos los objetos de un tipo específico.
     */
    public <T> List<T> retrieveAllObjects(Class<T> clazz) {
        if (db == null) {
            if (!DBConnection.reConnect()) {
                return new ArrayList<>();
            }
        }

        try {
            List<T> results = new ArrayList<>();
            ObjectSet<T> queryResults = db.query(clazz);

            while (queryResults.hasNext()) {
                results.add(queryResults.next());
            }

            return results;
        } catch (Exception e) {
            System.err.println("Error al recuperar objetos: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Recupera objetos por el valor de un campo específico
     */
    public <T> List<T> retrieveByFieldValue(Class<T> clazz, String fieldName, Object value) {
        if (db == null) {
            if (!DBConnection.reConnect()) {
                return new ArrayList<>();
            }
        }

        try {
            com.db4o.query.Query query = db.query();
            query.constrain(clazz);
            query.descend(fieldName).constrain(value);

            List<T> results = new ArrayList<>();
            ObjectSet<T> queryResults = query.execute();

            while (queryResults.hasNext()) {
                results.add(queryResults.next());
            }

            return results;
        } catch (Exception e) {
            System.err.println("Error al recuperar objetos por campo: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}

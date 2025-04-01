package data;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

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
     * Deletes the specified object from the database. If the database connection is not
     * established or has been closed, attempts to reconnect before proceeding. Commits
     * the transaction upon successful deletion or rolls back the transaction in case of
     * failure.
     *
     * @param object the object to be deleted from the database; must not be null
     * @return true if the object is successfully deleted, false otherwise
     */
    public boolean deleteObject(Object object) {
        if (object == null) {
            System.err.println("Cannot delete null object");
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
            db.ext().delete(object);
            db.commit();
            return true;
        } catch (Exception e) {
            try {
                db.rollback();
                System.err.println("Failed to delete object: " + e.getMessage());
            } catch (Exception ex) {
                System.err.println("Failed to rollback transaction: " + ex.getMessage());
            }
        }
        return false;
    }
}

package edumdev;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

public class DataAccessObject {
    private static final ObjectContainer db = DBConnection.connect();
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
     * true if the operation is successful.
     *
     * @param <T> the type of objects to be retrieved
     * @param clazz the class object representing the type of the records to be retrieved
     * @param condition a predicate used to filter the database records
     * @return true if the records are successfully retrieved, false otherwise
     */
    public <T> boolean retrieveWithCondition(Class<T> clazz, Predicate<T> condition) {
        if (db == null) {
            if (!DBConnection.reConnect()) {
                return false;
            }
        }
        ObjectSet<T> results = db.query(condition);
        for (T result : results) {
            System.out.println(result);
        }
        return true;
    }
}

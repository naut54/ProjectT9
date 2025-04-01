package data;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;

public class DBConnection {
    private static final String DB_FILE = "database.db4o";
    private static ObjectContainer db;
    private static final int maxRetries = 5;;

    /**
     * Establishes a connection to the database file. If the database connection
     * is null or has been closed, it opens a new connection to the specified
     * database file. Once connected successfully, a confirmation message is
     * displayed on the console.
     * This method does not attempt to reconnect multiple times, but ensures that
     * a valid connection is established before proceeding with any database operations.
     * The database file path is determined by the `DB_FILE` constant, and the
     * configuration used is the default provided by the Db4oEmbedded API.
     */
    public static ObjectContainer connect() {
        if (db == null || db.ext().isClosed()) {
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FILE);
            System.out.println("Connected to database");
        }

        return db;
    }

    /**
     * Checks if a connection to the database is currently established.
     *
     * @return true if the database connection is not null, otherwise false.
     */
    public boolean isConnected() {
        return db != null;
    }

    /**
     * Attempts to reconnect to the database if the connection is currently null.
     * This method will try up to a predefined number of retries (`maxRetries`) to establish a connection.
     * If a connection is successfully established within the retry limit, it returns true.
     *
     * @return true if the database connection is successfully established or already connected,
     *         false if the connection attempt fails after all retries.
     */
    public static boolean reConnect(){
        if (db == null) {
            try {
                for (int attempts = 0; attempts < maxRetries; attempts++) {
                    connect();
                    if (db != null) {
                        break;
                    }
                    Thread.sleep(1000);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return db != null;
        }
        return true;
    }

    public void close() {
        if (db != null) {
            reConnect();
            db = null;
            System.out.println("Disconnected from database");
        }
    }
}

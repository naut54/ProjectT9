package edumdev;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;

public class DBConnection {
    private static final String DB_FILE = "database.db4o";
    private ObjectContainer db;
    private static final int maxRetries = 5;;

    /**
     * Establece una conexión con la base de datos si no está ya conectada
     * o si la conexión actual está cerrada.
     * Utiliza un archivo de base de datos especificado.
     * <p>
     * Nota: La base de datos se almacena en el archivo definido por la constante DB_FILE.
     */
    public void connect() {
        if (db == null || db.ext().isClosed()) {
            db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), DB_FILE);
            System.out.println("Connected to database");
        }
    }

    /**
     * Verifica si la conexión con la base de datos está activa.
     *
     * @return true si la conexión con la base de datos está establecida, de lo contrario false.
     */
    public boolean isConnected() {
        return db != null;
    }

    public boolean reConnect(){
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

    /**
     * Cierra la conexión con la base de datos si está abierta.
     * Establece la referencia de la base de datos a null después de cerrarla.
     * Muestra un mensaje en la consola confirmando la desconexión.
     */
    public void close() {
        if (db != null) {
            db.close();
            db = null;
            System.out.println("Disconnected from database");
        }
    }

    /**
     * Almacena un objeto en la base de datos, estableciendo una conexión si no está activa.
     *
     * <p>Este método verifica si la conexión con la base de datos está establecida.
     * Si no lo está, intenta conectarse con un número máximo de reintentos.
     * Si la conexión no puede ser establecida, el método devuelve false.
     *
     * @param object El objeto que se desea almacenar en la base de datos.
     * @return true si el objeto fue almacenado correctamente, false si ocurrió un error.
     */
    public boolean storeObject(Object object) {
        if (db == null) {
            for (int attempts = 0; attempts < maxRetries; attempts++) {
                connect();
                if (db != null) {
                    break;
                }
            }

            if (db == null) {
                return false;
            }
        }

        try {
            db.store(object);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera todos los objetos de la base de datos que coincidan con el ejemplo especificado.
     * <p>
     * Este método verifica si la conexión con la base de datos está activa.
     * Si no lo está, establece la conexión antes de intentar recuperar los objetos.
     * Si ocurre un error durante la recuperación, este se imprime en la consola.
     *
     * @param object El objeto de ejemplo utilizado para buscar coincidencias en la base de datos.
     * @return true si los objetos fueron recuperados correctamente, false si ocurrió un error.
     */
    public boolean retrieveAll(Object object) {
        if (db == null) {
            connect();
            return false;
        }
        try {
            db.queryByExample(object);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Recupera todos los objetos de la base de datos que cumplen con una condición específica.
     * <p>
     * Este método verifica si la conexión con la base de datos está activa.
     * Si no lo está, establece la conexión antes de intentar recuperar los objetos.
     * Si ocurre un error durante la recuperación, este se imprime en la consola.
     *
     * @param clazz     La clase de los objetos que se quiere buscar.
     * @param condition La condición que deben cumplir los objetos para ser recuperados.
     * @param <T>       El tipo de los objetos que se quieren recuperar.
     * @return true si los objetos fueron recuperados correctamente, false si ocurrió un error.
     */
    public <T> boolean retrieveWithCondition(Class<T> clazz, Predicate<T> condition) {
        if (db == null) {
            connect();
            return false;
        }
        ObjectSet<T> results = db.query(condition);
        for (T result : results) {
            System.out.println(result);
        }
        return true;
    }
}

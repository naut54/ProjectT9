package edumdev;

public class DataAccessObject {
    public static void executeQueryNoValues(String query) {
        if (!query.trim().startsWith("SELECT")) {
            throw new IllegalArgumentException("Query must start with SELECT");
        }

        StringBuilder result = new StringBuilder();

    }
}

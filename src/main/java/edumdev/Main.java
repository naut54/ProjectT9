package edumdev;

public class Main {
    public static void main(String[] args) {
        DBConnection db = new DBConnection();
        db.connect();
        db.close();
    }
}
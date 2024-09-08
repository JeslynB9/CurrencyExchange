package CurrencyExchange.Users;

import CurrencyExchange.FileHandlers.Database;
import CurrencyExchange.FileHandlers.Json;
import processing.data.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CurrencyConverterApp {
    public static void main(String[] args) {
        // Load the SQLite JDBC driver
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("SQLite JDBC driver not found. Make sure it's in your classpath.");
            e.printStackTrace();
            return;
        }

        String dbPath = System.getProperty("user.dir") + "/app/src/main/java/resources/main/database.db";
        Database database = new Database(dbPath);
        String jsonFilePath = "app/src/main/java/resources/main/config.json";
        JSONObject jsonObject = null;

        try {
            String content = new String(Files.readAllBytes(Paths.get(jsonFilePath)));
            jsonObject = JSONObject.parse(content);
        } catch (IOException e) {
            System.out.println("Error reading JSON file: " + e.getMessage());
            jsonObject = new JSONObject();
        }

        Json jsonHandler = new Json(jsonObject, jsonFilePath);

        CurrencyManager manager = new CurrencyManager(database, jsonHandler);
        UserInterface ui = new UserInterface(manager);
        ui.run();
    }
}
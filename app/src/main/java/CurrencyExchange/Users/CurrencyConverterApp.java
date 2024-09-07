package CurrencyExchange.Users;

import CurrencyExchange.FileHandlers.Database;
import CurrencyExchange.FileHandlers.Json;
import processing.data.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class CurrencyConverterApp {
    public static void main(String[] args) {
        Database database = new Database("java/CurrencyExchange/FileHandlers/Database.java");
        String jsonFilePath = "java/CurrencyExchange/FileHandlers/Json.java";
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
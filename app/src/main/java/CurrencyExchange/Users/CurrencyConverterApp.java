package CurrencyExchange.Users;

import CurrencyExchange.FileHandlers.Database;
import CurrencyExchange.FileHandlers.Json;

public class CurrencyConverterApp {
    public static void main(String[] args) {
        Database database = new Database("CurrencyExchange.db");
        Json jsonHandler = new Json("CurrencyExchange.json");
        CurrencyManager manager = new CurrencyManager(database, jsonHandler);
        UserInterface ui = new UserInterface(manager);
        ui.run();
    }
}
package CurrencyExchange.Users;

import CurrencyExchange.FileHandlers.*;

import java.util.*;

public class PopularCurrency {
    private Database database;
    private HashMap<String, Double> popularCurrencies;

    public PopularCurrency(Database database){
        this.database = database;
    }

    public void setPopularCurrency(String c1, String c2, String c3, String c4) {
        popularCurrencies = new HashMap<>();
        popularCurrencies.put(c1, database.getLastExchangeRate(c1));
        popularCurrencies.put(c2, database.getLastExchangeRate(c2));
        popularCurrencies.put(c3, database.getLastExchangeRate(c3));
        popularCurrencies.put(c4, database.getLastExchangeRate(c4));
    }

    public HashMap<String, Double> getPopularCurrency() {
        return popularCurrencies;
    }
}
package CurrencyExchange.Users;


import CurrencyExchange.FileHandlers.Database;
import CurrencyExchange.FileHandlers.Json;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;

// Currency manager class
class CurrencyManager {
    private Database database;
    private Json jsonHandler;
    private List<String> popularCurrencies;

    public CurrencyManager(Database database, Json jsonHandler) {
        this.database = database;
        this.jsonHandler = jsonHandler;
        this.popularCurrencies = new ArrayList<>(Arrays.asList("AU", "US", "UK", "JP"));
        database.initialiseDatabase();
    }

    public void addExchangeRate(String currency, double rate, LocalDate date) {
        database.updateRate(currency, rate);
    }

    public void addNewCurrency(String currency, double initialRate) {
        database.addCountry(currency, initialRate);
        List<String> allCurrencies = database.getAllCurrencies();
        setPopularCurrencies(allCurrencies);  // Update the list of popular currencies
    }

    public void setPopularCurrencies(List<String> currencies) {
        this.popularCurrencies = currencies;
    }

    public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double fromRate = database.getLastExchangeRate(fromCurrency);
        double toRate = database.getLastExchangeRate(toCurrency);
        return Math.abs(amount * (toRate / fromRate));
    }

    public void displayPopularCurrencies() {
        List<String> allCurrencies = database.getAllCurrencies();

        System.out.println("\nAll Currencies Exchange Rates:");
        System.out.printf("%-6s", "From/To");
        for (String currency : allCurrencies) {
            System.out.printf("%-10s", currency);
        }
        System.out.println();

        for (String fromCurrency : allCurrencies) {
            System.out.printf("%-6s", fromCurrency);
            for (String toCurrency : allCurrencies) {
                if (fromCurrency.equals(toCurrency)) {
                    System.out.printf("%-10s", "-");
                } else {
                    double rate = convertCurrency(1, fromCurrency, toCurrency);
                    String symbol = jsonHandler.getSymbol(toCurrency);
                    System.out.printf("%-10s", String.format("%.2f %s", rate, symbol));
                }
            }
            System.out.println();
        }
    }

    public void displayRateSummary(String currency1, String currency2, LocalDate startDate, LocalDate endDate) {
        // This method needs to be implemented using database queries
        // You'll need to add a method in the Database class to fetch historical data
        System.out.println("Rate summary feature not yet implemented.");
    }

    public void addCountryData(String country, String flagFilePath, String symbol) {
        jsonHandler.addCountry(country, flagFilePath, symbol);
    }

    public void updateCountryFlag(String country, String flagFilePath) {
        jsonHandler.updateFlag(country, flagFilePath);
    }

    public void updateCountrySymbol(String country, String symbol) {
        jsonHandler.updateSymbol(country, symbol);
    }

    public String getCountryFlag(String country) {
        return jsonHandler.getFlag(country);
    }

    public String getCountrySymbol(String country) {
        return jsonHandler.getSymbol(country);
    }

    public void printAllRecords() {
        database.printAllRecords();
    }
}
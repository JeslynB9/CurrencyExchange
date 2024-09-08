package CurrencyExchange.Users;


import CurrencyExchange.FileHandlers.Database;
import CurrencyExchange.FileHandlers.Json;

import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import CurrencyExchange.FileHandlers.Database.ExchangeRateEntry;
import java.time.LocalDate;
import java.util.stream.Collectors;
//import java.util.stream.Collectors;

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

//    public void addExchangeRate(String currency, double rate, LocalDate date) {
//        database.updateRate(currency, rate);
//    }

    public void addExchangeRates(Map<String, Double> currencyRates) {
        database.updateRates(currencyRates);
        System.out.println("Exchange rates updated successfully.");
        displayPopularCurrencies();  // Show the updated rates
    }

    public void addNewCurrency(String currency, double initialRate) {
        database.addCountry(currency, initialRate);
        List<String> allCurrencies = database.getAllCurrencies();
        setPopularCurrencies(allCurrencies);  // Update the list of popular currencies
    }

    public void setPopularCurrencies(List<String> currencies) {
        if (currencies.size() != 4) {
            throw new IllegalArgumentException("Exactly 4 popular currencies must be set.");
        }
        this.popularCurrencies = new ArrayList<>(currencies);
    }

    public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double fromRate = database.getLastExchangeRate(fromCurrency);
        double toRate = database.getLastExchangeRate(toCurrency);
        return Math.abs(amount * (toRate / fromRate));
    }

    public void displayPopularCurrencies() {
        System.out.println("\nPopular Currencies Exchange Rates:");
        System.out.printf("%-6s", "From/To");
        for (String currency : popularCurrencies) {
            System.out.printf("%-10s", currency);
        }
        System.out.println();

        for (String fromCurrency : popularCurrencies) {
            System.out.printf("%-6s", fromCurrency);
            for (String toCurrency : popularCurrencies) {
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


    public ExchangeRateSummary getExchangeRateSummary(String currency1, String currency2, LocalDate startDate, LocalDate endDate) {
        List<Database.ExchangeRateEntry> historicalRates = database.getHistoricalRates(currency1, currency2, startDate, endDate);

        if (historicalRates.isEmpty()) {
            return null; // or throw an exception
        }

        List<Double> rates = historicalRates.stream().mapToDouble(entry -> entry.rate).sorted().boxed().collect(Collectors.toList());
        int size = rates.size();

        double min = rates.get(0);
        double max = rates.get(size - 1);
        double sum = rates.stream().mapToDouble(Double::doubleValue).sum();
        double average = sum / size;

        double median;
        if (size % 2 == 0) {
            median = (rates.get(size / 2 - 1) + rates.get(size / 2)) / 2.0;
        } else {
            median = rates.get(size / 2);
        }

        double variance = rates.stream().mapToDouble(rate -> Math.pow(rate - average, 2)).sum() / size;
        double standardDeviation = Math.sqrt(variance);

        return new ExchangeRateSummary(
                historicalRates,
                min,
                max,
                average,
                median,
                standardDeviation
        );
    }

    public static class ExchangeRateSummary {
        public List<Database.ExchangeRateEntry> allRates;
        public double minimum;
        public double maximum;
        public double average;
        public double median;
        public double standardDeviation;

        public ExchangeRateSummary(List<ExchangeRateEntry> allRates, double minimum, double maximum,
                                   double average, double median, double standardDeviation) {
            this.allRates = allRates;
            this.minimum = minimum;
            this.maximum = maximum;
            this.average = average;
            this.median = median;
            this.standardDeviation = standardDeviation;
        }
    }

}
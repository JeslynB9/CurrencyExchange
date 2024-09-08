package CurrencyExchange.Users;

import CurrencyExchange.FileHandlers.Database;
import CurrencyExchange.FileHandlers.Json;
import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;



// User interface class
class UserInterface {
    private Scanner scanner;
    private CurrencyManager manager;
    private User currentUser;

    public UserInterface(CurrencyManager manager) {
        this.scanner = new Scanner(System.in);
        this.manager = manager;
    }

    public void run() {
        while (true) {
            if (currentUser == null) {
                login();
            } else if (currentUser.isAdmin()) {
                adminMenu();
            } else {
                userMenu();
            }
        }
    }

    private void login() {
        System.out.print("Enter username (admin/user): ");
        String username = scanner.nextLine();
        currentUser = "admin".equals(username) ? new AdminUser() : new NormalUser();
    }

    private void adminMenu() {
        System.out.println("\n--- Admin Menu ---");
        System.out.println("1. Add/Update Exchange Rates");
        System.out.println("2. Add New Currency");
        System.out.println("3. Set Popular Currencies");
        System.out.println("4. Convert Currency");
        System.out.println("5. Display Popular Currencies");
        System.out.println("6. Logout");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                addUpdateExchangeRates();
                break;
            case 2:
                addNewCurrency();
                break;
            case 3:
                setPopularCurrencies();
                break;
            case 4:
                convertCurrency();
                break;
            case 5:
                displayPopularCurrencies();
                break;
            case 6:
                currentUser = null;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

    private void userMenu() {
        System.out.println("\n--- User Menu ---");
        System.out.println("1. Convert Currency");
        System.out.println("2. Display Popular Currencies");
        System.out.println("3. View Currency Rate Summary");
        System.out.println("4. Logout");
        System.out.print("Enter your choice: ");

        int choice = scanner.nextInt();
        scanner.nextLine(); // Consume newline

        switch (choice) {
            case 1:
                convertCurrency();
                break;
            case 2:
                displayPopularCurrencies();
                break;
            case 3:
                viewCurrencyRateSummary();
                break;
            case 4:
                currentUser = null;
                break;
            default:
                System.out.println("Invalid choice. Please try again.");
        }
    }

//    private void addUpdateExchangeRate() {
//        System.out.print("Enter currency code: ");
//        String currency = scanner.nextLine().toUpperCase();
//        System.out.print("Enter new rate (relative to USD): ");
//        double rate = scanner.nextDouble();
//        scanner.nextLine(); // Consume newline
//        System.out.print("Enter date (YYYY-MM-DD): ");
//        String dateStr = scanner.nextLine();
//        LocalDate date = LocalDate.parse(dateStr);
//
//        manager.addExchangeRate(currency, rate, date);
//        System.out.println("Exchange rate updated successfully.");
//    }


    private void addUpdateExchangeRates() {
        Map<String, Double> currencyRates = new HashMap<>();

        while (true) {
            System.out.print("Enter currency code (or 'done' to finish): ");
            String currency = scanner.nextLine().toUpperCase();

            if (currency.equals("DONE")) {
                break;
            }

            System.out.print("Enter new rate for " + currency + ": ");
            double rate = scanner.nextDouble();
            scanner.nextLine(); // Consume newline

            currencyRates.put(currency, rate);
        }

        if (!currencyRates.isEmpty()) {
            manager.addExchangeRates(currencyRates);
            System.out.println("Exchange rates updated successfully.");
        } else {
            System.out.println("No rates were entered.");
        }
    }

    private void addNewCurrency() {
        System.out.print("Enter new currency code: ");
        String currency = scanner.nextLine().toUpperCase();
        System.out.print("Enter initial rate (relative to USD): ");
        double rate = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        manager.addNewCurrency(currency, rate);
        System.out.println("New currency added successfully.");
        displayPopularCurrencies();  // Show the updated list of currencies
    }

    private void setPopularCurrencies() {
        List<String> currencies = new ArrayList<>();
        System.out.println("Enter 4 popular currency codes:");
        for (int i = 0; i < 4; i++) {
            System.out.print("Currency " + (i + 1) + ": ");
            currencies.add(scanner.nextLine().toUpperCase());
        }
        try {
            manager.setPopularCurrencies(currencies);
            System.out.println("Popular currencies updated successfully.");
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void convertCurrency() {
        System.out.print("Enter amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        System.out.print("Enter source currency code: ");
        String sourceCurrency = scanner.nextLine().toUpperCase();

        System.out.print("Enter target currency code: ");
        String targetCurrency = scanner.nextLine().toUpperCase();

        double result = manager.convertCurrency(amount, sourceCurrency, targetCurrency);
        System.out.printf("%.2f %s = %.2f %s%n", amount, sourceCurrency, result, targetCurrency);
    }

    private void displayPopularCurrencies() {
        manager.displayPopularCurrencies();
    }

    private void viewCurrencyRateSummary() {
        System.out.print("Enter first currency code: ");
        String currency1 = scanner.nextLine().toUpperCase();
        System.out.print("Enter second currency code: ");
        String currency2 = scanner.nextLine().toUpperCase();

        LocalDate startDate = null;
        LocalDate endDate = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        while (startDate == null) {
            System.out.print("Enter start date (YYYY-MM-DD): ");
            try {
                startDate = LocalDate.parse(scanner.nextLine(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        while (endDate == null) {
            System.out.print("Enter end date (YYYY-MM-DD): ");
            try {
                endDate = LocalDate.parse(scanner.nextLine(), formatter);
            } catch (DateTimeParseException e) {
                System.out.println("Invalid date format. Please use YYYY-MM-DD.");
            }
        }

        CurrencyManager.ExchangeRateSummary summary = manager.getExchangeRateSummary(currency1, currency2, startDate, endDate);

        if (summary == null) {
            System.out.println("No data available for the specified period and currencies.");
            return;
        }

        System.out.printf("\nExchange Rate Summary (%s to %s):\n", currency1, currency2);
        System.out.printf("Period: %s to %s\n", startDate, endDate);
        System.out.printf("Minimum: %.4f\n", summary.minimum);
        System.out.printf("Maximum: %.4f\n", summary.maximum);
        System.out.printf("Average: %.4f\n", summary.average);
        System.out.printf("Median: %.4f\n", summary.median);
        System.out.printf("Standard Deviation: %.4f\n\n", summary.standardDeviation);

        System.out.println("All rates:");
        for (Database.ExchangeRateEntry entry : summary.allRates) {
            System.out.printf("%s: %.4f\n", entry.date, entry.rate);
        }
    }
}

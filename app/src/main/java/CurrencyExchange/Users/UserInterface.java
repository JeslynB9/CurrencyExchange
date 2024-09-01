package CurrencyExchange.Users;

import java.util.*;
import java.io.*;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;



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
        System.out.println("1. Add/Update Exchange Rate");
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
                addUpdateExchangeRate();
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

    private void addUpdateExchangeRate() {
        System.out.print("Enter currency code: ");
        String currency = scanner.nextLine().toUpperCase();
        System.out.print("Enter new rate (relative to USD): ");
        double rate = scanner.nextDouble();
        scanner.nextLine(); // Consume newline
        System.out.print("Enter date (YYYY-MM-DD): ");
        String dateStr = scanner.nextLine();
        LocalDate date = LocalDate.parse(dateStr);

        manager.addExchangeRate(currency, rate, date);
        System.out.println("Exchange rate updated successfully.");
    }

    private void addNewCurrency() {
        System.out.print("Enter new currency code: ");
        String currency = scanner.nextLine().toUpperCase();
        System.out.print("Enter initial rate (relative to USD): ");
        double rate = scanner.nextDouble();
        scanner.nextLine(); // Consume newline

        manager.addNewCurrency(currency, rate);
        System.out.println("New currency added successfully.");
    }

    private void setPopularCurrencies() {
        List<String> currencies = new ArrayList<>();
        System.out.println("Enter 4 popular currency codes:");
        for (int i = 0; i < 4; i++) {
            System.out.print("Currency " + (i + 1) + ": ");
            currencies.add(scanner.nextLine().toUpperCase());
        }
        manager.setPopularCurrencies(currencies);
        System.out.println("Popular currencies updated successfully.");
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
        System.out.print("Enter start date (YYYY-MM-DD): ");
        LocalDate startDate = LocalDate.parse(scanner.nextLine());
        System.out.print("Enter end date (YYYY-MM-DD): ");
        LocalDate endDate = LocalDate.parse(scanner.nextLine());

        manager.displayRateSummary(currency1, currency2, startDate, endDate);
    }
}

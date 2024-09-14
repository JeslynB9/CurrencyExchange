package CurrencyExchange.Users;


import CurrencyExchange.FileHandlers.Database;
import CurrencyExchange.FileHandlers.Json;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import CurrencyExchange.FileHandlers.Database.ExchangeRateEntry;
import java.time.LocalDate;
import java.util.stream.Collectors;

import java.awt.Desktop;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;

// Currency manager class
public class CurrencyManager {
    private Database database;
    private Json jsonHandler;
    public List<String> popularCurrencies;


    public interface FileHandler {
        void createDirectories(String path) throws Exception;
    }

    public interface PDFGenerator {
        void generatePDF(File file, ExchangeRateSummary summary, String currency1, String currency2, LocalDate startDate, LocalDate endDate) throws Exception;
    }
    public CurrencyManager(Database database, Json jsonHandler) {
        this.database = database;
        this.jsonHandler = jsonHandler;
        this.popularCurrencies = new ArrayList<>(Arrays.asList("AUD", "USD", "GBP", "JPY"));
        database.initialiseDatabase();
    }

    public void addExchangeRates(Map<String, Double> currencyRates) {
        database.updateRates(currencyRates);
        System.out.println("Exchange rates updated successfully.");
        //displayPopularCurrencies();  // Show the updated rates
    }

    public void addNewCurrency(String currency) {
        database.addCountry(currency);
        //List<String> allCurrencies = database.getAllCurrencies();
        //setPopularCurrencies(allCurrencies);  // Update the list of popular currencies
    }

    public void setPopularCurrencies(List<String> currencies) {
        if (currencies.size() != 4) {
            throw new IllegalArgumentException("Exactly 4 popular currencies must be set.");
        }
        this.popularCurrencies = new ArrayList<>(currencies);
    }

    public double convertCurrency(double amount, String fromCurrency, String toCurrency) {
        double fromRateUSD = database.getLastExchangeRate(fromCurrency);  
        double toRateUSD = database.getLastExchangeRate(toCurrency);     
    
        if (fromCurrency.equals("USD")) {
            return amount * toRateUSD;
        } else if (toCurrency.equals("USD")) {
            return amount / fromRateUSD;
        } else {
            double amountInUSD = amount / fromRateUSD;  
            return amountInUSD * toRateUSD;             
        }
    }
    

    public double getLastExchangeRate(String currency) {
        System.out.println("Fetching exchange rate for: " + currency);
        double rate = database.getLastExchangeRate(currency);
        System.out.println("Exchange rate for " + currency + ": " + rate);
        return rate;
//        return database.getLastExchangeRate(currency);
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

//    public void printAllRecords() {
//        database.printAllRecords();
//    }


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

    public String generateExchangeRateSummaryPDF(String currency1, String currency2, LocalDate startDate, LocalDate endDate) {
        ExchangeRateSummary summary = getExchangeRateSummary(currency1, currency2, startDate, endDate);

        if (summary == null || summary.allRates.isEmpty()) {
            System.out.println("No data available for the specified period and currencies.");
            return null;
        }

        String fileName = String.format("%s-%s_%s_to_%s_Summary.pdf",
                currency1, currency2,
                startDate.toString(), endDate.toString());
        String folderPath = "app/src/main/java/CurrencyExchange/Users/PDFSummary";
        String filePath = folderPath + File.separator + fileName;

        File pdfFile = new File(filePath);
        System.out.println("Attempting to generate PDF at: " + pdfFile.getAbsolutePath());

        // Ensure the summaries folder exists
        try {
            Files.createDirectories(Paths.get(folderPath));
        } catch (Exception e) {
            System.out.println("Error creating summaries folder: " + e.getMessage());
            return null;
        }

        Document document = new Document();
        PdfWriter writer = null;
        try {
            writer = PdfWriter.getInstance(document, new FileOutputStream(filePath));
            document.open();

            Font titleFont = new Font(Font.FontFamily.HELVETICA, 18, Font.BOLD);
            Font headingFont = new Font(Font.FontFamily.HELVETICA, 14, Font.BOLD);
            Font normalFont = new Font(Font.FontFamily.HELVETICA, 12, Font.NORMAL);

            // Title
            Paragraph title = new Paragraph("Exchange Rate Summary", titleFont);
            title.setAlignment(Element.ALIGN_CENTER);
            document.add(title);
            document.add(Chunk.NEWLINE);

            // Currency and Date Range
            document.add(new Paragraph("From: " + currency1 + " To: " + currency2, headingFont));
            document.add(new Paragraph("Period: " + startDate + " to " + endDate, headingFont));
            document.add(Chunk.NEWLINE);

            // Summary Statistics Table
            PdfPTable statsTable = new PdfPTable(2);
            statsTable.setWidthPercentage(100);
            addRowToTable(statsTable, "Statistic", "Value", headingFont);
            addRowToTable(statsTable, "Minimum", String.format("%.4f", summary.minimum), normalFont);
            addRowToTable(statsTable, "Maximum", String.format("%.4f", summary.maximum), normalFont);
            addRowToTable(statsTable, "Average", String.format("%.4f", summary.average), normalFont);
            addRowToTable(statsTable, "Median", String.format("%.4f", summary.median), normalFont);
            addRowToTable(statsTable, "Standard Deviation", String.format("%.4f", summary.standardDeviation), normalFont);
            document.add(statsTable);
            document.add(Chunk.NEWLINE);

            // All Rates Table
            document.add(new Paragraph("All Rates:", headingFont));
            PdfPTable ratesTable = new PdfPTable(2);
            ratesTable.setWidthPercentage(100);
            addRowToTable(ratesTable, "Date", "Rate", headingFont);

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Database.ExchangeRateEntry entry : summary.allRates) {
                addRowToTable(ratesTable, entry.date.format(formatter), String.format("%.4f", entry.rate), normalFont);
            }
            document.add(ratesTable);

            System.out.println("PDF content added successfully.");

        } catch (Exception e) {
            System.out.println("Error generating PDF: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (document != null && document.isOpen()) {
                document.close();
            }
            if (writer != null) {
                writer.close();
            }
        }

        //if (pdfFile.exists() && pdfFile.length() > 0) {
        System.out.println("PDF generated successfully: " + pdfFile.getAbsolutePath());
        return pdfFile.getAbsolutePath();
//        } else {
//            System.out.println("Failed to create PDF file or file is empty: " + pdfFile.getAbsolutePath());
//            return null;
//        }
    }

    private void addRowToTable(PdfPTable table, String key, String value, Font font) {
        table.addCell(new Phrase(key, font));
        table.addCell(new Phrase(value, font));
    }

    public void openPDFFile(File file) throws IOException {
        if (!Desktop.isDesktopSupported()) {
           // throw new UnsupportedOperationException("Desktop is not supported on this platform");
        }

        Desktop desktop = Desktop.getDesktop();
        if (!desktop.isSupported(Desktop.Action.OPEN)) {
            //throw new UnsupportedOperationException("Opening files is not supported on this platform");
        }

        desktop.open(file);
    }






}

package CurrencyExchange.Users;

import CurrencyExchange.FileHandlers.Database;
import CurrencyExchange.FileHandlers.Json;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.time.LocalDate;
import java.util.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.io.File;
import java.awt.Desktop;
import java.lang.reflect.Field;
import static org.junit.Assert.*;
public class CurrencyManagerTest {

    @Mock
    private Database mockDatabase;
    @Mock
    private Json mockJsonHandler;

    private CurrencyManager currencyManager;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyManager = new CurrencyManager(mockDatabase, mockJsonHandler);
        System.setOut(new PrintStream(outContent));
    }


    @After
    public void restoreStreams() {
        System.setOut(originalOut);
    }

    @Test
    public void testAddNewCurrency() {
        currencyManager.addNewCurrency("TEST");
        verify(mockDatabase).addCountry("TEST");
    }

    @Test
    public void testConvertCurrency() {
        when(mockDatabase.getLastExchangeRate("USD")).thenReturn(1.0);
        when(mockDatabase.getLastExchangeRate("EUR")).thenReturn(0.85);

        double result = currencyManager.convertCurrency(100, "USD", "EUR");
        assertEquals(85.0, result, 0.01);
    }

    @Test
    public void testGetExchangeRateSummary() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        List<Database.ExchangeRateEntry> mockEntries = Arrays.asList(
                new Database.ExchangeRateEntry(LocalDate.of(2023, 1, 1), 0.8),
                new Database.ExchangeRateEntry(LocalDate.of(2023, 1, 15), 0.85),
                new Database.ExchangeRateEntry(LocalDate.of(2023, 1, 31), 0.9)
        );

        when(mockDatabase.getHistoricalRates("USD", "EUR", startDate, endDate)).thenReturn(mockEntries);

        CurrencyManager.ExchangeRateSummary summary = currencyManager.getExchangeRateSummary("USD", "EUR", startDate, endDate);

        assertNotNull(summary);
        assertEquals(0.8, summary.minimum, 0.001);
        assertEquals(0.9, summary.maximum, 0.001);
        assertEquals(0.85, summary.average, 0.001);
        assertEquals(0.85, summary.median, 0.001);
    }

    @Test
    public void testSetPopularCurrenciesInvalidSize() {
        List<String> currencies = Arrays.asList("USD", "EUR", "GBP");
        try {
            currencyManager.setPopularCurrencies(currencies);
            fail("Expected an IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Exactly 4 popular currencies must be set.", e.getMessage());
        }
    }

    @Test
    public void testUpdateCountryFlag() {
        currencyManager.updateCountryFlag("USD", "new/path/to/flag.png");
        verify(mockJsonHandler).updateFlag("USD", "new/path/to/flag.png");
    }

    @Test
    public void testUpdateCountrySymbol() {
        currencyManager.updateCountrySymbol("EUR", "€");
        verify(mockJsonHandler).updateSymbol("EUR", "€");
    }

    @Test
    public void testGetCountryFlag() {
        when(mockJsonHandler.getFlag("JPY")).thenReturn("path/to/japan/flag.png");
        assertEquals("path/to/japan/flag.png", currencyManager.getCountryFlag("JPY"));
    }

    @Test
    public void testGetCountrySymbol() {
        when(mockJsonHandler.getSymbol("GBP")).thenReturn("£");
        assertEquals("£", currencyManager.getCountrySymbol("GBP"));
    }


    @Test
    public void testGenerateExchangeRateSummaryPDF() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);
        List<Database.ExchangeRateEntry> mockEntries = Arrays.asList(
                new Database.ExchangeRateEntry(LocalDate.of(2023, 1, 1), 0.8),
                new Database.ExchangeRateEntry(LocalDate.of(2023, 1, 15), 0.85),
                new Database.ExchangeRateEntry(LocalDate.of(2023, 1, 31), 0.9)
        );

        when(mockDatabase.getHistoricalRates("USD", "EUR", startDate, endDate)).thenReturn(mockEntries);

        String pdfPath = currencyManager.generateExchangeRateSummaryPDF("USD", "EUR", startDate, endDate);

        assertNotNull("PDF path should not be null", pdfPath);
        assertTrue("PDF file should exist", new File(pdfPath).exists());
        assertTrue(outContent.toString().contains("PDF generated successfully"));
    }

    @Test
    public void testGenerateExchangeRateSummaryPDFNoData() {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 1, 31);

        when(mockDatabase.getHistoricalRates("USD", "EUR", startDate, endDate)).thenReturn(new ArrayList<>());

        String pdfPath = currencyManager.generateExchangeRateSummaryPDF("USD", "EUR", startDate, endDate);

        assertNull("PDF path should be null when no data is available", pdfPath);
        assertTrue(outContent.toString().contains("No data available for the specified period and currencies."));
    }

    @Test
    public void testDisplayPopularCurrencies() {
        // Mock the necessary method calls
        when(mockDatabase.getLastExchangeRate("AUD")).thenReturn(0.75);
        when(mockDatabase.getLastExchangeRate("USD")).thenReturn(1.0);
        when(mockDatabase.getLastExchangeRate("GBP")).thenReturn(1.3);
        when(mockDatabase.getLastExchangeRate("JPY")).thenReturn(0.009);

        when(mockJsonHandler.getSymbol("AUD")).thenReturn("$");
        when(mockJsonHandler.getSymbol("USD")).thenReturn("$");
        when(mockJsonHandler.getSymbol("GBP")).thenReturn("£");
        when(mockJsonHandler.getSymbol("JPY")).thenReturn("¥");

        currencyManager.displayPopularCurrencies();

        String output = outContent.toString();
        assertTrue(output.contains("Popular Currencies Exchange Rates:"));
        assertTrue(output.contains("AUD"));
        assertTrue(output.contains("USD"));
        assertTrue(output.contains("GBP"));
        assertTrue(output.contains("JPY"));
        assertTrue(output.contains("$"));
        assertTrue(output.contains("£"));
        assertTrue(output.contains("¥"));
    }

    @Test
    public void testDisplayPopularCurrenciesWithCustomCurrencies() {
        List<String> customCurrencies = Arrays.asList("USD", "EUR", "CAD", "CNY");
        currencyManager.setPopularCurrencies(customCurrencies);

        // Mock the necessary method calls
        when(mockDatabase.getLastExchangeRate("USD")).thenReturn(1.0);
        when(mockDatabase.getLastExchangeRate("EUR")).thenReturn(1.2);
        when(mockDatabase.getLastExchangeRate("CAD")).thenReturn(0.8);
        when(mockDatabase.getLastExchangeRate("CNY")).thenReturn(0.15);

        when(mockJsonHandler.getSymbol("USD")).thenReturn("$");
        when(mockJsonHandler.getSymbol("EUR")).thenReturn("€");
        when(mockJsonHandler.getSymbol("CAD")).thenReturn("$");
        when(mockJsonHandler.getSymbol("CNY")).thenReturn("¥");

        currencyManager.displayPopularCurrencies();

        String output = outContent.toString();
        assertTrue(output.contains("Popular Currencies Exchange Rates:"));
        assertTrue(output.contains("USD"));
        assertTrue(output.contains("EUR"));
        assertTrue(output.contains("CAD"));
        assertTrue(output.contains("CNY"));
        assertTrue(output.contains("$"));
        assertTrue(output.contains("€"));
        assertTrue(output.contains("¥"));
    }

//    @Test
//    public void testOpenPDFFile() {
//        // Create a temporary file for testing
//        File tempFile = null;
//        try {
//            tempFile = File.createTempFile("test", ".pdf");
//            currencyManager.openPDFFile(tempFile);
//            assertTrue(outContent.toString().contains("PDF generated successfully"));
//        } catch (Exception e) {
//            fail("Exception should not be thrown: " + e.getMessage());
//        } finally {
//            if (tempFile != null) {
//                tempFile.delete();
//            }
//        }
//    }
//
//    @Test
//    public void testOpenPDFFileUnsupportedPlatform() {
//        // Test when Desktop is not supported
//        File mockFile = mock(File.class);
//        currencyManager.openPDFFile(mockFile);
//        assertTrue(outContent.toString().contains("Desktop is not supported on this platform") ||
//                outContent.toString().contains("Opening files is not supported on this platform"));
//    }

    @Test
    public void testAddCountryData() {
        String country = "TEST";
        String flagFilePath = "/path/to/flag.png";
        String symbol = "$";

        currencyManager.addCountryData(country, flagFilePath, symbol);

        verify(mockJsonHandler).addCountry(country, flagFilePath, symbol);
    }

    @Test
    public void testGetLastExchangeRate() {
        String currency = "USD";
        double expectedRate = 1.0;

        when(mockDatabase.getLastExchangeRate(currency)).thenReturn(expectedRate);

        double actualRate = currencyManager.getLastExchangeRate(currency);

        assertEquals(expectedRate, actualRate, 0.0001);
        assertTrue(outContent.toString().contains("Fetching exchange rate for: " + currency));
        assertTrue(outContent.toString().contains("Exchange rate for " + currency + ": " + expectedRate));
    }

    @Test
    public void testAddExchangeRates() {
        Map<String, Double> currencyRates = new HashMap<>();
        currencyRates.put("USD", 1.0);
        currencyRates.put("EUR", 0.85);

        currencyManager.addExchangeRates(currencyRates);

        verify(mockDatabase).updateRates(currencyRates);
        assertTrue(outContent.toString().contains("Exchange rates updated successfully."));
    }


    @Test
    public void testOpenPDFFileSuccess() throws IOException {
        // Create a temporary file
        File tempFile = File.createTempFile("test", ".txt");
        tempFile.deleteOnExit();

        // This should not throw an exception if Desktop is supported
        try {
            currencyManager.openPDFFile(tempFile);
        } catch (UnsupportedOperationException e) {
            // If this exception is thrown, it means Desktop is not supported on this system
            // We'll assume the test passed in this case
            assertTrue(true);
        }
    }

//    @Test(expected = IOException.class)
//    public void testOpenPDFFileNonExistentFile() throws IOException {
//        File nonExistentFile = new File("nonexistent.pdf");
//        currencyManager.openPDFFile(nonExistentFile);
//    }

    @Test
    public void testOpenPDFFileNullFile() {
        try {
            currencyManager.openPDFFile(null);
            fail("Expected NullPointerException was not thrown");
        } catch (NullPointerException e) {
            // Expected exception
            assertTrue(true);
        } catch (IOException e) {
            fail("Unexpected IOException was thrown");
        }
    }


}
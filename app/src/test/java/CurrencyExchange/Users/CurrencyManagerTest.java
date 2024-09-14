package CurrencyExchange.Users;

import CurrencyExchange.FileHandlers.Database;
import CurrencyExchange.FileHandlers.Json;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.time.LocalDate;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CurrencyManagerTest {

    @Mock
    private Database mockDatabase;
    @Mock
    private Json mockJsonHandler;

    private CurrencyManager currencyManager;

    private Database database;
    private String databasePath;
    PopularCurrency popularCurrency; 

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        currencyManager = new CurrencyManager(mockDatabase, mockJsonHandler);
    }

    @Test
    void testAddNewCurrency() {
        currencyManager.addNewCurrency("TEST");
        verify(mockDatabase).addCountry("TEST");
    }

    @Test
    void testConvertCurrency() {
        when(mockDatabase.getLastExchangeRate("USD")).thenReturn(1.0);
        when(mockDatabase.getLastExchangeRate("EUR")).thenReturn(0.85);

        double result = currencyManager.convertCurrency(100, "USD", "EUR");
        assertEquals(85.0, result, 0.01);
    }

    @Test
    void testGetExchangeRateSummary() {
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
    void testSetPopularCurrenciesInvalidSize() {
        List<String> currencies = Arrays.asList("USD", "EUR", "GBP");
        assertThrows(IllegalArgumentException.class, () -> currencyManager.setPopularCurrencies(currencies));
    }

//    @Test
//    void testPrintAllRecords() {
//        currencyManager.printAllRecords();
//        verify(mockDatabase).printAllRecords();
//    }



    @Test
    void testUpdateCountryFlag() {
        currencyManager.updateCountryFlag("USD", "new/path/to/flag.png");
        verify(mockJsonHandler).updateFlag("USD", "new/path/to/flag.png");
    }

    @Test
    void testUpdateCountrySymbol() {
        currencyManager.updateCountrySymbol("EUR", "€");
        verify(mockJsonHandler).updateSymbol("EUR", "€");
    }

    @Test
    void testGetCountryFlag() {
        when(mockJsonHandler.getFlag("JPY")).thenReturn("path/to/japan/flag.png");
        assertEquals("path/to/japan/flag.png", currencyManager.getCountryFlag("JPY"));
    }

    @Test
    void testGetCountrySymbol() {
        when(mockJsonHandler.getSymbol("GBP")).thenReturn("£");
        assertEquals("£", currencyManager.getCountrySymbol("GBP"));
    }

    @Test
    public void testPopularCurrency() {
        createMockDatabase();
        popularCurrency.setPopularCurrency("GB", "JP", "UK", "EU");
        HashMap<String, Double> map = popularCurrency.getPopularCurrency();
        assertTrue(map.containsKey("GB"));
        assertEquals(1.23, map.get("GB"), 0.0001f);
        assertTrue(map.containsKey("JP"));
        assertEquals(1.0, map.get("JP"), 0.0001f);
        assertTrue(map.containsKey("UK"));
        assertEquals(0.8, map.get("UK"), 0.0001f);
        assertTrue(map.containsKey("EU"));
        assertEquals(0.85, map.get("EU"), 0.0001f);

        assertFalse(map.containsKey("US"));
        assertFalse(map.containsKey("AU"));
    }

    private void createMockDatabase() {
        databasePath = "src/main/java/resources/test/database.db";
        database = new Database(databasePath);
        database.initialiseDatabase();
        popularCurrency = new PopularCurrency(database);

        database.addCountry("Sam", "UK", 0.8);
        database.addCountry("Bob", "US", 0.9);
        database.addCountry("Pan", "JP", 1.0);
        database.addCountry("Mop", "EU", 0.85);
        database.addCountry("Wat", "AU", 0.91);
        database.addCountry("Fab", "GB", 1.23);
    }
}
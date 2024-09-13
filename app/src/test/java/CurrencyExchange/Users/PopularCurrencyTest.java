package CurrencyExchange.Users;

import org.junit.*;

import CurrencyExchange.FileHandlers.Database;
import processing.data.JSONObject;
import static org.junit.Assert.*;
import java.util.*;


public class PopularCurrencyTest {
    private static Database database;
    private String databasePath;
    PopularCurrency popularCurrency; 

    @Before
    public void setUp() {
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

    @Test
    public void testPopularCurrency() {
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

}

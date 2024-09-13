package CurrencyExchange.FileHandlers;

import org.junit.*;
import processing.data.JSONObject;
import static org.junit.Assert.*;


public class JsonTest {
    private static Json json;

    @Before
    public void setup() {
        String jsonFilepath = "src/main/java/resources/test/config.json";

        JSONObject initialJson = new JSONObject();
        
        JSONObject symbol = new JSONObject();
        symbol.setString("AU", "$");
        symbol.setString("UK", "£");
        symbol.setString("US", "$");
        symbol.setString("FR", "€");
        symbol.setString("AB", "");

        JSONObject flag = new JSONObject();
        flag.setString("AU", "auFlag.png");
        flag.setString("UK", "ukFlag.png");
        flag.setString("US", "");

        initialJson.setJSONObject("symbol", symbol);
        initialJson.setJSONObject("flag", flag);

        json = new Json(initialJson, jsonFilepath);
    }

    @Test 
    public void testLoadJson() {
        json = new Json(null, "src/main/java/resources/test/config.json");
        json = new Json(null, "eeeoooeeeooo");

    }
    

    @Test
    public void testAddCountry1() { //valid general case
        json.addCountry("NZ", "nzFlag.png", "$");
        assertEquals("Expected: $, Actual: " + json.getSymbol("NZ"), "$", json.getSymbol("NZ"));
        assertEquals("Expected: nzFlag.png, Actual: " + json.getFlag("NZ"), "nzFlag.png", json.getFlag("NZ"));
    }

    @Test
    public void testAddCountry2() { //adding existing country 
        json.addCountry("NZ", "nzFlag.png", "$");
        assertEquals("Expected: nzFlag.png, Actual: " + json.getFlag("NZ"), "nzFlag.png", json.getFlag("NZ"));
        assertEquals("Expected: $, Actual: " + json.getSymbol("NZ"), "$", json.getSymbol("NZ"));

        json.addCountry("NZ", "nzFlag.png", "$");
        assertEquals("Expected: nzFlag.png, Actual: " + json.getFlag("NZ"), "nzFlag.png", json.getFlag("NZ"));
        assertEquals("Expected: $, Actual: " + json.getSymbol("NZ"), "$", json.getSymbol("NZ"));
    }

    @Test
    public void testAddCountry3() { //invalid country
        json.addCountry("234", "234Flag.png", "R");
        assertNull(json.getFlag("234"));
        assertNull(json.getSymbol("234"));
    }

    @Test 
    public void testUpdateCountryFlag1() { //updating existing country 
        json.updateFlag("AU", "newAuFlag.png");
        assertEquals("Expected: newAuFlag.png, Actual: " + json.getFlag("AU"), "newAuFlag.png", json.getFlag("AU"));
    }

    @Test 
    public void testUpdateCountryFlag2() { //updating nonexisting country 
        json.updateFlag("MU", "newMuFlag.png");
        assertEquals("Expected: newMuFlag.png, Actual: " + json.getFlag("MU"), "newMuFlag.png", json.getFlag("MU"));
    }

    @Test 
    public void testUpdateCountryFlag3() { //updating nonexisting invalid country 
        json.updateFlag("234", "234.png");
        assertNull(json.getFlag("234"));
    }

    @Test
    public void testUpdateCountrySymbol1() { //update an existing country currency symbol 
        json.updateSymbol("AU", "$");
        assertEquals("Expected: $, Actual: " + json.getSymbol("AU"), "$", json.getSymbol("AU"));
    }

    @Test
    public void testUpdateCountrySymbol2() { //update nonexisting country symbol
        json.updateSymbol("MU", "$");
        assertEquals("Expected: $, Actual: " + json.getSymbol("MU"), "$", json.getSymbol("MU"));
    }

    @Test
    public void testUpdateCountrySymbol3() { //update invalid country symbol 
        json.updateSymbol("234", "$");
        assertNull(json.getSymbol("234"));
    }

} 
package CurrencyExchange.FileHandlers;

import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import java.sql.*;
import java.time.*;
import java.time.format.DateTimeFormatter;

public class DatabaseTest {
    private static Database Database;
    private String databasePath;
    private Connection connection;

    @Before
    public void setUp() {
        databasePath = "src/main/java/resources/test/database.db";
        Database = new Database(databasePath);
        Database.initialiseDatabase();
    }

    @Test
    public void testInitialiseDatabase() { //determine if file exists
        assertTrue(new File(databasePath).exists());
    } 
    

    @Test
    public void testAddCountry1() { //general case
        Database.addCountry("Bob", "NY", 1.5);
        assertTrue(columnExists("NY"));
        verifyColumnData("NY", 1.5);
    }

    @Test
    public void testAddCountry2() { //adding an existing country
        Database.addCountry("Bob", "NY", 2.5);
        assertTrue(columnExists("NY"));
        verifyColumnData("NY", 2.5);
    }

    @Test
    public void testAddCountry3() { //adding invalid country 
        Database.addCountry("Bob", "", 1.5);
        assertFalse(columnExists(""));
        Database.addCountry("Bob", "123", 2.5);
        assertFalse(columnExists("123"));
    }

    @Test 
    public void testUpdateRate1() { //general case 
        Database.addCountry("Amber", "TU", 2.5);
        assertTrue(columnExists("TU"));
        verifyColumnData("TU", 2.5);
        Database.updateRate("Guoba", "NY", 1.1);
        verifyColumnData("NY", 1.1);
    }

    @Test
    public void testUpdateRate2() { //updating a non-existent country 
        Database.updateRate("Bebo", "AU", 1.1);
        assertTrue(columnExists("AU"));
    }

    @Test 
    public void testUpdateRate3() { //updating with negative value 
        Database.addCountry("Pablo", "JS", 1.5);
        Database.updateRate("Pablo", "JS", -1);
        verifyColumnData("JS", 1.5);
    }

    @Test
    public void testGetLastExchangeRate1() { //general case 
        Database.addCountry("Muhummad", "DB", 1.0);
        Database.updateRate("Muhood", "DB", 2.0); 
        Database.updateRate("Muhaed", "DB", 2.5); 

        assertEquals((float) 2.5, Database.getLastExchangeRate("DB"), 0.1);
    }

    @Test
    public void testGetLastExchangeRate2() { //country doesnt exist 
        assertEquals((float) -1, Database.getLastExchangeRate("QQ"), 0.1);
    }

    @Test
    public void testGetLastExchangeRate3() { //updating with negative values 
        Database.addCountry("Kiri", "OW", 1.0);
        Database.updateRate("Bastion", "OW", -2.0);
        assertEquals((float) 1.0, Database.getLastExchangeRate("OW"), 0.1);
    }

    @Test
    public void testGetLastTwoExchangeRates1() { //general case 
        Database.addCountry("Ashe", "WO", 1.0);
        Database.updateRate("Cassidy", "WO", 2.0);
        Database.updateRate("Tracer", "WO", 2.5);
        float[] expectedRates = {2.5f, 2.0f};

        assertArrayEquals(expectedRates, Database.getLastTwoExchangeRates("WO"), 0.1f);
    }

    @Test
    public void testGetLastTwoExchangeRates2() { //adding negative number
        Database.addCountry("McLovin", "WO", 1.0);
        Database.updateRate("BillyBobJohnson", "WO", 2.0);
        Database.updateRate("Yoyo", "WO", 3.0);
        Database.updateRate("Bap", "WO", -2.5);
        float[] expectedRates = {3.0f, 2.0f}; 

        assertArrayEquals(expectedRates, Database.getLastTwoExchangeRates("WO"), 0.1f);
    }

    @Test
    public void testGetLastTwoExchangeRates3() { //country doesnt exist
        assertArrayEquals(new float[] {-1.0f, -1.0f}, Database.getLastTwoExchangeRates("ME"), 0.1f);
    }

    @Test
    public void testGetLastUser1() { //general case
        Database.addCountry("Phillip", "CA", 1.0);
        Database.updateRate("Raphael", "CA", 2.0);  
        assertEquals("Raphael", Database.getLastUser("CA"));
    }

    @Test
    public void testGetLastUser2() { //updated with negative value 
        Database.addCountry("Navia", "FN", 1.0);
        Database.updateRate("Furina", "FN", -2.0);  
        assertEquals("Navia", Database.getLastUser("FN"));
    }

    @Test
    public void testGetLastUser3() { //country doesnt exist 
        assertNull(Database.getLastDate("RA"));
    }

    @Test
    public void testGetLastDate1() { //general case 
        Database.addCountry("goru", "LY", 2.4);
        assertNotNull(Database.getLastDate("LY"));
    }

    @Test
    public void testGetLastDate2() { //country doesnt exist 
        assertNull(Database.getLastDate("PO"));
    }    

    private boolean columnExists(String country) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
             Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("PRAGMA table_info(ExchangeRates);");
            while (rs.next()) {
                if (country.equals(rs.getString("name"))) {
                    return true;
                }
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private void verifyColumnData(String country, double expectedRate) {
        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:" + databasePath);
             Statement stmt = connection.createStatement()) {

            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            PreparedStatement pstmt = connection.prepareStatement("SELECT " + country + " FROM ExchangeRates WHERE datetime = ?");
            pstmt.setString(1, currentDateTime);
            ResultSet rs = pstmt.executeQuery();

            assertTrue(rs.next());
            assertEquals(expectedRate, rs.getDouble(country), 0.00001);

        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

}

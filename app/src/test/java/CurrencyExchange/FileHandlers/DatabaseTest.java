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

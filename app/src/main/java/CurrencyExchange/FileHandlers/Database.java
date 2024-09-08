package CurrencyExchange.FileHandlers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

//https://www.marcobehler.com/guides/java-databases
//https://docs.oracle.com/javase/8/docs/api/java/sql/Connection.html
//https://stackoverflow.com/questions/40277054/error-with-createstatement-using-sql-jdbc
//https://www.geeksforgeeks.org/introduction-to-jdbc/


public class Database {
    private final String url;

    public Database(String filePath) {
        this.url = "jdbc:sqlite:" + filePath;  // SQLite URL
    }

    /*
     * Create and return new connection to SQLite database using URL
     */
    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url);
    }

    /*
     * Initialise a database table if it doesnt exist
     */
    public void initialiseDatabase() {
        String createTableSQL = "CREATE TABLE IF NOT EXISTS ExchangeRates ("
                + "datetime TEXT PRIMARY KEY, "
                + "User VARCHAR(30), "
                + "AU DECIMAL(10, 5), "
                + "US DECIMAL(10, 5), "
                + "UK DECIMAL(10, 5)"
                + ");";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute(createTableSQL);
            System.out.println("Database initialized successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /*
     * Add a country (column) to the database
     * @params:
     *      country: String
     *      rate: double
     */
    public void addCountry(String country, double rate) {
        // Check if country is a valid string
        if (country == null || !country.matches("[a-zA-Z]+")) {
            System.out.println("Invalid country code. Please use only letters.");
            return;
        }

        // Define new column
        String addColumnSQL = "ALTER TABLE ExchangeRates ADD COLUMN " + country + " DECIMAL(10, 5)";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            // Check if column exists
            if (!columnExists(connection, country)) {
                stmt.execute(addColumnSQL);
                System.out.println("Added new column: " + country);
            } else {
                System.out.println("Column " + country + " already exists.");
            }

            // Insert data
            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String insertSQL = "INSERT INTO ExchangeRates (datetime, " + country + ") VALUES (?, ?) "
                    + "ON CONFLICT(datetime) DO UPDATE SET " + country + " = excluded." + country;

            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                pstmt.setString(1, currentDateTime);
                pstmt.setDouble(2, rate);
                int rowsAffected = pstmt.executeUpdate();
                System.out.println("Rows affected: " + rowsAffected);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        checkDatabaseContents();
    }

    /*
     * Checks if a column with columnName exists
     * @params:
     *      connection: Connection
     *      columnName: String
     */
    private boolean columnExists(Connection connection, String columnName) throws SQLException {
        String querySQL = "PRAGMA table_info(ExchangeRates)";

        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            while (rs.next()) {
                String existingColumnName = rs.getString("name");
                if (existingColumnName.equals(columnName)) {
                    return true;
                }
            }
        }
        return false;
    }

    /*
     * Updates an existing column with a rate
     * @params:
     *      country: String
     *      rate: double
     */
    public void updateRate(String country, double rate) {
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String query = "INSERT INTO ExchangeRates (datetime, " + country + ") VALUES (?, ?) "
                + "ON CONFLICT(datetime) DO UPDATE SET " + country + " = excluded." + country;

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, currentDateTime);
            pstmt.setDouble(2, rate);

            int rowsAffected = pstmt.executeUpdate();

            System.out.println("Rows affected: " + rowsAffected);
            System.out.println("Updated " + country + " rate to " + rate + " at " + currentDateTime);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // After updating, let's verify the change
        checkDatabaseContents();
    }


    /*
     * Get the last exchange rate value for a given country
     * @params:
     *      country: String
     */
    public float getLastExchangeRate(String country) {
        String querySQL = "SELECT " + country + " FROM ExchangeRates "
                + "WHERE " + country + " IS NOT NULL "
                + "ORDER BY datetime DESC LIMIT 1";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            if (rs.next()) {
                return rs.getFloat(country);
            } else {
                System.out.println("No exchange rate found for " + country);
                return -1; // or some other default value
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // or some other default value
        }
    }

    /*
     * Function to print everything in database
     */
    public void printAllRecords() {
        String querySQL = "SELECT * FROM " + "ExchangeRates";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            int columnCount = rs.getMetaData().getColumnCount();

            for (int i = 1; i <= columnCount; i++) {
                System.out.printf("%-20s", rs.getMetaData().getColumnName(i));
            }
            System.out.println();

            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.printf("%-20s", rs.getString(i));
                }
                System.out.println();
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<String> getAllCurrencies() {
        List<String> currencies = new ArrayList<>();
        String query = "PRAGMA table_info(ExchangeRates)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                String columnName = rs.getString("name");
                if (!columnName.equals("datetime") && !columnName.equals("User")) {
                    currencies.add(columnName);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencies;
    }


    public void checkDatabaseContents() {
        String query = "PRAGMA table_info(ExchangeRates)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            System.out.println("Current columns in ExchangeRates table:");
            while (rs.next()) {
                System.out.println(rs.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}


/*
 * Access database:
 *      sqlite3 database.db
 *      .tables
 *
 */
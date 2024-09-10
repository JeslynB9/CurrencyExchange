package CurrencyExchange.FileHandlers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;
import java.time.LocalDate;
import java.util.*;

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



    public void addCountry(String country) {
        if (country == null || !country.matches("[a-zA-Z]+")) {
            System.out.println("Invalid country code. Please use only letters.");
            return;
        }

        String addColumnSQL = "ALTER TABLE ExchangeRates ADD COLUMN " + country + " DECIMAL(10, 5)";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            if (!columnExists(connection, country)) {
                stmt.execute(addColumnSQL);
                System.out.println("Added new column: " + country);
            } else {
                System.out.println("Column " + country + " already exists.");
            }

        } catch (SQLException e) {
            System.out.println("Error adding new country: " + e.getMessage());
            e.printStackTrace();
        }

        //checkDatabaseContents();
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


    // unnecessary function
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


    public List<ExchangeRateEntry> getHistoricalRates(String currency1, String currency2, LocalDate startDate, LocalDate endDate) {
        List<ExchangeRateEntry> rates = new ArrayList<>();
        String query = "SELECT datetime, " + currency1 + ", " + currency2 +
                " FROM ExchangeRates WHERE datetime BETWEEN ? AND ? " +
                "AND " + currency1 + " IS NOT NULL AND " + currency2 + " IS NOT NULL " +
                "ORDER BY datetime";

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, startDate.toString());
            pstmt.setString(2, endDate.toString());

            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    LocalDate date = LocalDate.parse(rs.getString("datetime").split(" ")[0]);
                    double rate1 = rs.getDouble(currency1);
                    double rate2 = rs.getDouble(currency2);
                    double conversionRate = rate2 / rate1;
                    rates.add(new ExchangeRateEntry(date, conversionRate));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rates;
    }

    public static class ExchangeRateEntry {
        public LocalDate date;
        public double rate;

        public ExchangeRateEntry(LocalDate date, double rate) {
            this.date = date;
            this.rate = rate;
        }
    }


    public void updateRates(Map<String, Double> currencyRates) {
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        StringBuilder queryBuilder = new StringBuilder("INSERT INTO ExchangeRates (datetime");
        StringBuilder valuesBuilder = new StringBuilder(") VALUES (?");

        for (String currency : currencyRates.keySet()) {
            queryBuilder.append(", ").append(currency);
            valuesBuilder.append(", ?");
        }

        queryBuilder.append(valuesBuilder).append(") ON CONFLICT(datetime) DO UPDATE SET ");

        boolean first = true;
        for (String currency : currencyRates.keySet()) {
            if (!first) {
                queryBuilder.append(", ");
            }
            queryBuilder.append(currency).append(" = excluded.").append(currency);
            first = false;
        }

        String query = queryBuilder.toString();
        System.out.println("Debug: Query = " + query);

        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setString(1, currentDateTime);
            int paramIndex = 2;
            for (Double rate : currencyRates.values()) {
                pstmt.setDouble(paramIndex++, rate);
            }

            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Rows affected: " + rowsAffected);

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
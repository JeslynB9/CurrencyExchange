package CurrencyExchange.FileHandlers;

import java.time.LocalDate;
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
                + "AUD DECIMAL(10, 5), "
                + "USD DECIMAL(10, 5), "
                + "GBP DECIMAL(10, 5)"
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
     *      user: String 
     *      country: String 
     *      rate: double 
     */
    public void addCountry(String user, String country, double rate) {
        //check if country is valid string 
        if ((country != null && !country.matches("[a-zA-Z]+")) || rate < 0) {
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
    }

    /*
     * Checks if a column with columnName exists
     * @params:
     *      connection: Connection
     *      columnName: String
     */
    private boolean columnExists(Connection connection, String columnName) throws SQLException {
        if (columnName == null) {
            return false;
        }
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
     *      user: String 
     *      country: String 
     *      rate: double 
     */
    public void updateRate(String user, String country, double rate) {
        if (rate < 0) {
            return; 
        }
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String query = "UPDATE ExchangeRates SET " + country + " = ?, User = ? WHERE datetime = ?";
        
        //try to open connection to the SQLite database
        try (Connection connection = getConnection()) {
        
            //check if column exists
            if (!columnExists(connection, country)) {
                return;
            }
    
            try (PreparedStatement pstmt = connection.prepareStatement(query)) {
                pstmt.setDouble(1, rate);
                pstmt.setString(2, user);
                pstmt.setString(3, currentDateTime);
                pstmt.executeUpdate();
            }
    
        }
        catch (SQLException e) {
            e.printStackTrace();
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
     * Get the last user to modify rate value for a given country
     * @params: 
     *      country: String 
     * @ret: 
     *      user: String else null 
     */
    // public String getLastUser(String country) {
    //     String querySQL = "SELECT User FROM ExchangeRates "
    //                   + "WHERE " + country + " IS NOT NULL "
    //                   + "ORDER BY datetime DESC LIMIT 1";

    //     try (Connection connection = getConnection();
    //          Statement stmt = connection.createStatement();
    //          ResultSet rs = stmt.executeQuery(querySQL)) {

    //         //move the cursor to the next row
    //         if (rs.next()) {
    //             return rs.getString("User"); 
    //         }

    //     }
    //     catch (SQLException e) {
    //         e.printStackTrace();
    //     }

    //     return null;
    // }

    /*
     * Get the last date to modify rate value for a given country
     * @params: 
     *      country: String 
     * @ret: 
     *      datetime: String else null
     */
    // public String getLastDate(String country) {
    //     String querySQL = "SELECT datetime FROM ExchangeRates "
    //                     + "WHERE " + country + " IS NOT NULL "
    //                     + "ORDER BY datetime DESC LIMIT 1";


    //     try (Connection connection = getConnection();
    //          Statement stmt = connection.createStatement();
    //          ResultSet rs = stmt.executeQuery(querySQL)) {

    //         //move the cursor to the next row
    //         if (rs.next()) {
    //             return rs.getString("datetime"); 
    //         }

    //     }
    //     catch (SQLException e) {
    //         e.printStackTrace();
    //     }

    //     return null;
    // }

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


}


/*
 * Access database:
 *      sqlite3 database.db
 *      .tables
 *
 */
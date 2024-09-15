package CurrencyExchange.FileHandlers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;
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
    Connection getConnection() throws SQLException {
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
                + "GBP DECIMAL(10, 5), "
                + "JPY DECIMAL(10, 5), "
                + "EUR DECIMAL(10, 5), "
                + "PHP DECIMAL(10, 5) "
                + ");";

        //try to open connection to the SQLite database
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute(createTableSQL); //create table if it no exists

            String countRowsSQL = "SELECT COUNT(*) FROM ExchangeRates"; //to check if table is empty 
            try (ResultSet rs = stmt.executeQuery(countRowsSQL)) {
                if (rs.next() && rs.getInt(1) == 0) { //check if table empty 
                    String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    String insertSQL = "INSERT INTO ExchangeRates (datetime, User, AUD, USD, GBP, JPY, EUR, PHP) "
                            + "VALUES (?, 'system', 1.49, 1, 0.76, 140.94, 0.90, 56.02)";
                    try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                        pstmt.setString(1, currentDateTime);
                        pstmt.executeUpdate();
                    }
                }
            }
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

        //define new column
        String addColumnSQL = "ALTER TABLE ExchangeRates ADD COLUMN " + country + " DECIMAL(10, 5)";

        //try to open connection to the SQLite database
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            //check column exist
            if (!columnExists(connection, country)) {
                stmt.execute(addColumnSQL);
            }

            //insert data
            String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            String insertSQL = "INSERT INTO ExchangeRates (datetime, User, " + country + ") VALUES (?, ?, ?) "
                    + "ON CONFLICT(datetime) DO UPDATE SET User = excluded.User, " + country + " = excluded." + country;

            //create a PreparedStatement to execute SQL query
            //automatically closes after try block
            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                //insert a new record
                pstmt.setString(1, currentDateTime);
                pstmt.setString(2, user);
                pstmt.setDouble(3, rate);
                pstmt.executeUpdate();
            }
            catch (SQLException e) {
                e.printStackTrace();
            }

        }
        catch (SQLException e) {
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
                pstmt.setDouble(3, rate);
                pstmt.setString(2, user);
                pstmt.setString(1, currentDateTime);
                pstmt.executeUpdate();
                System.out.println("updated rate onto database");
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateRates(Map<String, Double> currencyRates) {
        if (currencyRates == null || currencyRates.isEmpty()) {
            return;
        }
        //check if entered values are invalid 
        for (Map.Entry<String, Double> entry : currencyRates.entrySet()) {
            if (entry.getKey() == null || entry.getKey().isEmpty() || entry.getValue() == null || entry.getValue() < 0) {
                return;
            }
        }

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
    public double getLastExchangeRate(String country) {
        String querySQL = "SELECT " + country + " FROM ExchangeRates "
                + "WHERE " + country + " IS NOT NULL "
                + "ORDER BY datetime DESC LIMIT 1";


        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            //move the cursor to the next row
            if (rs.next()) {
                //System.out.println(rs.getDouble(country));
                return rs.getDouble(country);
            }

        }
        catch (SQLException e) {
            System.out.println("Column " + country + " doesnt exist");
            e.printStackTrace();
        }

        return -1;
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

    public Map<String, Double> getAllCurrencies() {
        Map<String, Double> currencies = new HashMap<>();
        String query = "PRAGMA table_info(ExchangeRates)";
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String columnName = rs.getString("name");

                if (!columnName.equals("datetime") && !columnName.equals("User")) {
                    double rate = getLastExchangeRate(columnName);
                    currencies.put(columnName, rate);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return currencies;
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
    public void printAllExchangeRates() {
        String query = "SELECT * FROM ExchangeRates";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            // Get column names and print them
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                System.out.print(rsmd.getColumnName(i) + "\t");
            }
            System.out.println();

            // Iterate through the result set and print each row
            while (rs.next()) {
                for (int i = 1; i <= columnCount; i++) {
                    System.out.print(rs.getString(i) + "\t");
                }
                System.out.println();
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
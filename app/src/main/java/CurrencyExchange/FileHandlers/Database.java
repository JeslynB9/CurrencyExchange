package CurrencyExchange.FileHandlers;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.sql.*;

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

        //try to open connection to the SQLite database
        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement()) {

            stmt.execute(createTableSQL);

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void addCountry(String country, double rate) {
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
            String insertSQL = "INSERT INTO ExchangeRates (datetime, " + country + ") VALUES (?, ?) "
                             + "ON CONFLICT(datetime) DO UPDATE SET " + country + " = excluded." + country;

            //create a PreparedStatement to execute SQL query
            //automatically closes after try block
            try (PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {
                //insert a new record
                pstmt.setString(1, currentDateTime);
                pstmt.setDouble(2, rate);
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

    /*
     * Checks if a column with columnName exists
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
     */
    public void updateRate(String country, double rate) {
        String currentDateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));

        String query = "UPDATE ExchangeRates SET " + country + " = ? WHERE datetime = ?";

        //try to open connection to the SQLite database
        try (Connection connection = getConnection();
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            //insert a new record
            pstmt.setDouble(1, rate);
            pstmt.setString(2, currentDateTime);
            pstmt.executeUpdate();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
    }


    /*
     * Get the last exchange rate value for a given country
     */
    public float getLastExchangeRate(String country) {
        String querySQL = "SELECT " + country + " FROM ExchangeRates "
                        + "WHERE " + country + " IS NOT NULL "
                        + "ORDER BY datetime DESC LIMIT 1";


        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            //move the cursor to the next row
            if (rs.next()) {
                return rs.getFloat(country);
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return -1;
    }

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
}


/*
 * Access database:
 *      sqlite3 database.db
 *      .tables
 *
 */

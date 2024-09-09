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

    /*
    * Gets the last two exchange rates for a given country
    * @params: 
    *      country: String 
    * @ret: 
    *       rates: float[] where float[0] is the most recent rate 
    */
    public float[] getLastTwoExchangeRates(String country) {
        float[] rates = new float[2];

        rates[0] = -1.0f;
        rates[1] = -1.0f;
        
        String querySQL = "SELECT " + country + " FROM ExchangeRates "
                    + "WHERE " + country + " IS NOT NULL "
                    + "ORDER BY datetime DESC LIMIT 2";
    
        try (Connection connection = getConnection();
            Statement stmt = connection.createStatement();
            ResultSet rs = stmt.executeQuery(querySQL)) {

            int i = 0;
            while (rs.next() && i < 2) {
                rates[i] = rs.getFloat(1);
                i++;
            }
        } 
        catch (SQLException e) {
            e.printStackTrace();
        }
            
        return rates;
    }

    /*
     * Get the last user to modify rate value for a given country
     * @params: 
     *      country: String 
     * @ret: 
     *      user: String else null 
     */
    public String getLastUser(String country) {
        String querySQL = "SELECT User FROM ExchangeRates "
                      + "WHERE " + country + " IS NOT NULL "
                      + "ORDER BY datetime DESC LIMIT 1";

        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            //move the cursor to the next row
            if (rs.next()) {
                return rs.getString("User"); 
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    /*
     * Get the last date to modify rate value for a given country
     * @params: 
     *      country: String 
     * @ret: 
     *      datetime: String else null
     */
    public String getLastDate(String country) {
        String querySQL = "SELECT datetime FROM ExchangeRates "
                        + "WHERE " + country + " IS NOT NULL "
                        + "ORDER BY datetime DESC LIMIT 1";


        try (Connection connection = getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(querySQL)) {

            //move the cursor to the next row
            if (rs.next()) {
                return rs.getString("datetime"); 
            }

        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}


/*
 * Access database:
 *      sqlite3 database.db
 *      .tables
 *
 */

package database;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/**
 * This class holds all the related methods and attributes to connect to ElephantSQL.
 */
public class Database {
    private String URL = "jdbc:postgresql://rogue.db.elephantsql.com:5432/ltjiipos";
    private String USER = "ltjiipos";
    private String PASS = "NI39rKBxYqrYd9KKkQHu_NQo_n2LRCjf";
    private static Database database = new Database();
    private Connection connection = null;
    /**
     * Gets the object Database.
     * @return The object Database that is able to establish connection with ElephantSQL.
     */
    public static Database getInstance()
    {
        return database;
    }
    /**
     * Unique constructor for the object Database.
     */
    private Database()
    {}
    /**
     * Tries to connect to the database, prints an error if an exception toke place.
     */
    public void connect()
    {
        try
        {
            System.out.println("ElephantSQL: connecting to the database...");
            connection = DriverManager.getConnection(URL, USER, PASS);
            System.out.println("ElephantSQL: connection successful");
        } catch ( SQLException exception )
        {
            System.err.println("Error initializing connection: " + exception);
        }
    }
    /**
     * Tries to disconnect from the database, prints an error if an exception toke place.
     */
    public void close()
    {
        try
        {
            System.out.println("ElephantSQL: disconnecting from the database...");
            connection.close();
            System.out.println("ElephantSQL: connection terminated");
        } catch ( SQLException exception )
        {
            System.err.println("Error terminating connection: " + exception);
            connection = null;
        }
    }
    /**
     * Gets the attribute connection.
     * @return The attribute connection from the Database object to process queries into it.
     */
    public Connection getConnection()
    {
        return connection;
    }
}
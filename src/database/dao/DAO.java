package database.dao;
import database.Database;
import misc.Exceptions;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * This class holds the common methods between all data access objects.
 */
public class DAO {
    /**
     * Gets an object ResultSet from a query to the database.
     * @param query The query from which the ResultSet will be returned.
     * @return ResultSet object with the content of the query from the parameter.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    protected ResultSet initializeResultSet(String query) throws SQLException
    {
        return Database.getInstance().getConnection().createStatement().executeQuery(query);
    }
    /**
     * Prepares a query that doesn't return values.
     * @param query The query to be done to the database.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    protected void prepareStatement(String query) throws SQLException
    {
        Database.getInstance().getConnection().prepareStatement(query).execute();
    }
    /**
     * Throws an exception related to database queries.
     * @param exception The SQLException from the query.
     * @param query The query that prompts an exception.
     */
    protected void throwException(Exception exception, String query)
    {
        Exceptions.printError(String.format("Error: query \"%s\" was unsuccessful%n", query));
        Exceptions.printError(String.format("Description: %s%n", exception.getMessage()));
        Exceptions.printError(String.format("Class: %s%n%n", exception.toString().split(" ")[0].replace(':', ' ')));
    }
    /**
     * Gets the last available id from a table.
     * @return Last available id.
     * @throws SQLException An exception that provides information on a database access error or other errors.
     */
    protected int getLastId(String column, String table) throws SQLException
    {
        ResultSet result = Database.getInstance().getConnection().createStatement().executeQuery(String.format("select %s from %s;", column, table));
        List<Integer> ids = new ArrayList<>();
        while ( result.next() )
        {
            ids.add(result.getInt(column));
        }
        if ( ids.size() < 1 ) return 1;
        int i = 0, max = ids.get(0);
        do
        {
            if ( ids.get(i) > max ) max = ids.get(i);
        } while ( ++ i < ids.size() );
        return max + 1;
    }
}
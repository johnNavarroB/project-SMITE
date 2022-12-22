package database.dao;
import database.Database;
import misc.Exceptions;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
/**
 * This class holds the related methods to obtain,
 * modify and delete items from the database.
 */
public class ItemsDAO extends DAO {
    private static ItemsDAO itemsDAO = new ItemsDAO();
    /**
     * Gets the object itemsDAO to retrieve data from the table items.
     * @return Object itemsDAO.
     */
    public static ItemsDAO getInstance()
    {
        return itemsDAO;
    }
    /**
     * Unique constructor for ItemsDAO.
     */
    private ItemsDAO()
    {}
    /**
     * Gets all the items from the table items.
     * @return A list of lists of object with all the items of the table items.
     */
    public List<List<Object>> selectItems()
    {
        List<List<Object>> itemsList = new ArrayList<>();
        String selectQuery = "select * from items;";
        try
        {
            System.out.println("ItemsDAO: initializing process of accessing data through query...");
            ResultSet result = super.initializeResultSet(selectQuery);
            System.out.println("ItemsDAO: query data was accessed successfully");
            while ( result.next() )
            {
                List<Object> statistics = new ArrayList<>();
                statistics.add(result.getInt("itemsid"));
                statistics.add(result.getInt("statistics"));
                statistics.add(result.getString("first_item"));
                statistics.add(result.getString("second_item"));
                statistics.add(result.getString("third_item"));
                statistics.add(result.getString("fourth_item"));
                statistics.add(result.getString("fifth_item"));
                statistics.add(result.getString("sixth_item"));
                itemsList.add(statistics);
            }
            if ( itemsList.size() < 1 ) Exceptions.printError("Error: the items table is empty\n\n");
            return itemsList;
        } catch ( SQLException exception )
        {
            super.throwException(exception, selectQuery);
        }
        return itemsList;
    }
    /**
     * Inserts items to the table items.
     * @param items The items to be inserted to the table items.
     */
    public void insertItems(Object[] items, int statisticsid)
    {
        String insertQuery = "insert into items (statistics, first_item, second_item, third_item, fourth_item, fifth_item, sixth_item) values (?, ?, ?, ?, ?, ?, ?);";
        try
        {
            System.out.println("ItemsDAO: inserting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(insertQuery);
            statement.setInt(1, statisticsid);
            statement.setString(2, (String) items[0]);
            statement.setString(3, (String) items[1]);
            statement.setString(4, (String) items[2]);
            statement.setString(5, (String) items[3]);
            statement.setString(6, (String) items[4]);
            statement.setString(7, (String) items[5]);
            statement.execute();
            System.out.println("ItemsDAO: the inserts were successfully committed");
        } catch ( SQLException exception )
        {
            super.throwException(exception, insertQuery);
        } finally
        {
            try
            {
                super.prepareStatement("commit;");
            } catch ( SQLException exception )
            {
                super.throwException(exception, insertQuery);
            }
        }
    }
    /**
     * Updates the items of a player in a team in a given match id.
     * @param ordinal The prefix of each item to be selected at the database query.
     * @param item The item to be written in place of the updated item.
     * @param lane The lane at which pertains the teammate.
     * @param team The side of the team, order or chaos.
     * @param matchId The id of the match in which the items of the teammates are updated.
     */
    public void updateItems(String ordinal, String item, String lane, String team, int matchId)
    {
        String updateQuery = String.format("update items set %s_item = '%s' where %s_item in (" +
                "select i.%s_item from items i inner join statistics s on i.statistics = s.statisticsid " +
                "inner join team t on s.player like t.%s " +
                "inner join match m on t.teamname like m.%s " +
                "where m.matchid = '%d');", ordinal, item, ordinal, ordinal, lane, team, matchId);
        try
        {
            System.out.println("ItemsDAO: updating registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(updateQuery);
            statement.execute();
            System.out.println("ItemsDAO: the updates were successfully committed");
        } catch ( SQLException exception )
        {
            super.throwException(exception, updateQuery);
        } finally
        {
            try
            {
                super.prepareStatement("commit;");
            } catch ( SQLException exception )
            {
                super.throwException(exception, updateQuery);
            }
        }
    }
    /**
     * Deletes all the items from a match id.
     * @param matchId The match id from the match which all the items will be removed.
     */
    public void deleteItems(int matchId)
    {
        String updateQuery = String.format("delete from items where statistics in (" +
                "select i.statistics from items i " +
                "inner join statistics s on i.statistics = s.statisticsid " +
                "where match = '%d');", matchId);
        try
        {
            System.out.println("ItemsDAO: deleting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(updateQuery);
            statement.execute();
            System.out.println("ItemsDAO: the registers have been deleted");
        } catch ( SQLException exception )
        {
            super.throwException(exception, updateQuery);
        } finally
        {
            try
            {
                super.prepareStatement("commit;");
            } catch ( SQLException exception )
            {
                super.throwException(exception, updateQuery);
            }
        }
    }
}
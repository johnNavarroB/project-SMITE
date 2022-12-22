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
 * modify and delete statistics from the database.
 */
public class StatisticsDAO extends DAO {
    private static StatisticsDAO statisticsDAO = new StatisticsDAO();
    /**
     * Gets the object statisticsDAO to retrieve data from the table team.
     * @return Object statisticsDAO.
     */
    public static StatisticsDAO getInstance()
    {
        return statisticsDAO;
    }
    /**
     * Unique constructor for StatisticsDAO.
     */
    private StatisticsDAO()
    {}
    /**
     * Given the side of a match, the lane and the match id, obtains all the statistics from the teammate.
     * @param lane The lane of the teammate which its stats will be selected from the database.
     * @param team The side of the team, order or chaos.
     * @param matchId The id of the match which the statistics of the player in question are related to.
     * @return All the statistics and items from the player in a list of objects.
     */
    public List<Object> selectAllStatistics(String lane, String team, int matchId)
    {
        List<Object> statistics = new ArrayList<>();
        String selectQuery = String.format("select s.player, s.gold, s.kills, s.deaths, s.assists, s.structuredmg, s.miniondmg, s.playerdmg, " +
                "i.first_item, i.second_item, i.third_item, i.fourth_item, i.fifth_item, i.sixth_item " +
                "from statistics s " +
                "inner join items i on s.statisticsid = i.statistics " +
                "inner join team t on s.player like t.%s " +
                "inner join match m on t.teamname like m.%s " +
                "where s.match = '%d';", lane, team, matchId);
        try
        {
            ResultSet result = super.initializeResultSet(selectQuery);
            while ( result.next() )
            {
                statistics.add(result.getString("player"));
                statistics.add(result.getInt("gold"));
                statistics.add(result.getInt("kills"));
                statistics.add(result.getInt("deaths"));
                statistics.add(result.getInt("assists"));
                statistics.add(result.getInt("structuredmg"));
                statistics.add(result.getInt("miniondmg"));
                statistics.add(result.getInt("playerdmg"));
                statistics.add(result.getString("first_item"));
                statistics.add(result.getString("second_item"));
                statistics.add(result.getString("third_item"));
                statistics.add(result.getString("fourth_item"));
                statistics.add(result.getString("fifth_item"));
                statistics.add(result.getString("sixth_item"));
            }
            if ( statistics.size() < 1 )
            {
                Exceptions.printError(String.format("Error: the statistics from the \"%s\" of \"%s\" were not found%n", lane, team));
                Exceptions.printError("Query: " + selectQuery + "\n\n");
            }
            return statistics;
        } catch ( SQLException exception )
        {
            super.throwException(exception, selectQuery);
        }
        return statistics;
    }
    /**
     * Gets the statistics of a teammate in a given lane, match id and team name.
     * @return A list containing the name of the teammate, its kills, deaths and assists.
     */
    public List<Object> selectStatistics(String lane, String teamName, int matchId)
    {
        List<Object> statistics = new ArrayList<>();
        String selectQuery = String.format("select s.player, s.kills, s.deaths, s.assists from statistics s " +
                             "inner join team t on s.player like t.%s " +
                             "where t.teamname like '%s' and s.match = '%d';", lane, teamName, matchId);
        try
        {
            ResultSet result = super.initializeResultSet(selectQuery);
            while ( result.next() )
            {
                statistics.add(result.getString("player"));
                statistics.add(result.getInt("kills"));
                statistics.add(result.getInt("deaths"));
                statistics.add(result.getInt("assists"));
            }
            if ( statistics.size() < 1 )
            {
                Exceptions.printError(String.format("Error: the statistics from the \"%s\" of \"%s\" were not found%n", lane, teamName));
                Exceptions.printError("Query: " + selectQuery + "\n\n");
            }
            return statistics;
        } catch ( SQLException exception )
        {
            super.throwException(exception, selectQuery);
        }
        return statistics;
    }
    /**
     * Inserts statistics to the table statistics.
     * @param statistics The statistics to be inserted to the table statistics.
     */
    public int insertStatistics(Object[] statistics)
    {
        String insertQuery = "insert into statistics (statisticsid, player, match, gold, kills, deaths, assists, structuredmg, miniondmg, playerdmg) values (?, ?, ?, ?, ?, ?, ?, ?, ?, ?);";
        int id = 0;
        try
        {
            id = super.getLastId("statisticsid", "statistics");
            System.out.println("StatisticsDAO: inserting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(insertQuery);
            statement.setInt(1, id);
            statement.setString(2, (String) statistics[0]);
            statement.setInt(3, (Integer) statistics[1]);
            statement.setInt(4, (Integer) statistics[2]);
            statement.setInt(5, (Integer) statistics[3]);
            statement.setInt(6, (Integer) statistics[4]);
            statement.setInt(7, (Integer) statistics[5]);
            statement.setInt(8, (Integer) statistics[6]);
            statement.setInt(9, (Integer) statistics[7]);
            statement.setInt(10, (Integer) statistics[8]);
            statement.execute();
            System.out.println("StatisticsDAO: the inserts were successfully committed");
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
        return id;
    }
    /**
     * Updates a single statistic on the table statistics on a given match id, lane and team.
     * @param column The column of the statistic to be updated.
     * @param value The new value of the statistic to be updated.
     * @param lane The lane at which pertains the teammate of the statistic to be updated.
     * @param team The side of the team, order or chaos.
     * @param matchId The id of the match in which the nickname is updated.
     */
    public void updateStatistics(String column, int value, String lane, String team, int matchId)
    {
        String updateQuery = String.format("update statistics set %s = '%d' where player in (" +
                "select s.player from statistics s " +
                "inner join team t on s.player like t.%s " +
                "inner join match m on t.teamname like m.%s " +
                "where m.matchid = '%d');", column, value, lane, team, matchId);
        try
        {
            System.out.println("StatisticsDAO: updating registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(updateQuery);
            statement.execute();
            System.out.println("StatisticsDAO: the updates were successfully committed");
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
     * Deletes all the statistics from a match id.
     * @param matchId The match id from the match which its statistics will be deleted.
     */
    public void deleteStatistics(int matchId)
    {
        String updateQuery = String.format("delete from statistics where match = '%d';", matchId);
        try
        {
            System.out.println("StatisticsDAO: deleting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(updateQuery);
            statement.execute();
            System.out.println("StatisticsDAO: the registers have been deleted");
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
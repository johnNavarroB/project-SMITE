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
 * modify and delete matches from the database.
 */
public class MatchDAO extends DAO {
    private static MatchDAO matchDAO = new MatchDAO();
    /**
     * Gets the object matchDAO to retrieve data from the table match.
     * @return Object matchDAO.
     */
    public static MatchDAO getInstance()
    {
        return matchDAO;
    }
    /**
     * Unique constructor for MatchDAO.
     */
    private MatchDAO()
    {}
    /**
     * Gets all the matches from the table match.
     * @return A list of lists of object with all the matches of the table match.
     */
    public List<List<Object>> selectMatches()
    {
        List<List<Object>> matches = new ArrayList<>();
        String selectQuery = "select * from match;";
        try
        {
            ResultSet result = super.initializeResultSet(selectQuery);
            while ( result.next() )
            {
                List<Object> team = new ArrayList<>();
                team.add(result.getInt("matchid"));
                team.add(result.getString("orderteam"));
                team.add(result.getString("chaosteam"));
                team.add(result.getInt("minutes"));
                matches.add(team);
            }
            if ( matches.size() < 1 ) Exceptions.printError("Error: the match table is empty\n\n");
            return matches;
        } catch ( SQLException exception )
        {
            super.throwException(exception, selectQuery);
        }
        return matches;
    }
    /**
     * Selects both order and chaos team names from a match id.
     * @param matchId The match id from the match from which the team names will be selected.
     * @return The names of the two teams from a match id of the match in which have played.
     */
    public List<String> selectTeams(int matchId)
    {
        List<String> teams = new ArrayList<>();
        String selectQuery = String.format("select orderteam, chaosteam from match where matchid = '%d';", matchId);
        try
        {
            System.out.println("MatchDAO: initializing process of accessing data through query...");
            ResultSet result = super.initializeResultSet(selectQuery);
            System.out.println("MatchDAO: query data was accessed successfully");
            while ( result.next() )
            {
                teams.add(result.getString("orderteam"));
                teams.add(result.getString("chaosteam"));
            }
            if ( teams.size() < 1 ) Exceptions.printError(String.format("Error: the match '%d' does not exist\n\n", matchId));
            return teams;
        } catch ( SQLException exception )
        {
            super.throwException(exception, selectQuery);
        }
        return teams;
    }
    /**
     * Selects the duration from a match.
     * @param matchId The match id from the match from which the time will be selected.
     * @return The time that a match lasted in minutes.
     */
    public int selectTime(int matchId)
    {
        int time = 0;
        String selectQuery = String.format("select minutes from match where matchid = '%d';", matchId);
        try
        {
            System.out.println("MatchDAO: initializing process of accessing data through query...");
            ResultSet result = super.initializeResultSet(selectQuery);
            System.out.println("MatchDAO: query data was accessed successfully");
            while ( result.next() )
            {
                time = result.getInt("minutes");
            }
            return time;
        } catch ( SQLException exception )
        {
            super.throwException(exception, selectQuery);
        }
        return time;
    }
    /**
     * Inserts a match to the table match.
     * @param match The match to be inserted to the table match.
     */
    public int insertMatch(Object[] match)
    {
        String insertQuery = "insert into match (matchid, orderteam, chaosteam, minutes) values (?, ?, ?, ?);";
        int id = 0;
        try
        {
            id = super.getLastId("matchid", "match");
            System.out.println("MatchDAO: inserting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(insertQuery);
            statement.setInt(1, id);
            statement.setString(2, (String) match[0]);
            statement.setString(3, (String) match[1]);
            statement.setInt(4, (Integer) match[2]);
            statement.execute();
            System.out.println("MatchDAO: the inserts were successfully committed");
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
     * Updates the value of the duration of a match of a given match id.
     * @param minutes The new value of minutes to be written in place of the value to be updated.
     * @param matchId The id of the match in which the nickname is updated.
     */
    public void updateTime(int minutes, int matchId)
    {
        String updateQuery = String.format("update match set minutes = '%d' where matchid = '%d';", minutes, matchId);
        try
        {
            System.out.println("MatchDAO: updating registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(updateQuery);
            statement.execute();
            System.out.println("MatchDAO: the updates were successfully committed");
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
     * Given a match id, deletes the match from the database.
     * @param matchId The match id from the match which will be deleted from the database.
     */
    public void deleteMatch(int matchId)
    {
        String updateQuery = String.format("delete from match where matchid = '%d';", matchId);
        try
        {
            System.out.println("MatchDAO: deleting a match...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(updateQuery);
            statement.execute();
            System.out.println("MatchDAO: the match has been deleted");
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
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
 * modify and delete teammates from the database.
 */
public class TeammateDAO extends DAO{

    private static TeammateDAO teammateDAO = new TeammateDAO();
    /**
     * Gets the object teammateDAO to retrieve data from the table teammate.
     * @return Object teammateDAO.
     */
    public static TeammateDAO getInstance()
    {
        return teammateDAO;
    }
    /**
     * Unique constructor for TeammateDAO.
     */
    private TeammateDAO()
    {}
    /**
     * Gets all the nicknames from the table teammate.
     * @return A list of string with all the nicknames of the table teammate.
     */
    public List<String> selectNicknames()
    {
        List<String> nicknames = new ArrayList<>();
        String selectQuery = "select nickname from teammate;";
        try
        {
            System.out.println("TeammateDAO: initializing process of accessing data through query...");
            ResultSet result = super.initializeResultSet(selectQuery);
            System.out.println("TeammateDAO: query data was accessed successfully");
            while ( result.next() )
            {
                String nickname = result.getString("nickname");
                nicknames.add(nickname);
            }
            if ( nicknames.size() < 1 ) Exceptions.printError("Error: the teammate table is empty\n\n");
            return nicknames;
        } catch ( SQLException exception )
        {
            super.throwException(exception, selectQuery);
        }
        return nicknames;
    }
    /**
     * Given a team name, selects all the teammates from the team.
     * @param teamName The team from which the teammates will be returned.
     * @return A list of the teammates from a team.
     */
    public List<String> selectNicknames(String teamName)
    {
        List<String> nicknames = new ArrayList<>();
        String selectQuery = String.format("select tm.nickname from teammate tm " +
                "inner join team t on tm.nickname like t.support " +
                "or tm.nickname like t.adc " +
                "or tm.nickname like t.mid " +
                "or tm.nickname like t.jungle " +
                "or tm.nickname like t.solo " +
                "where t.teamname like '%s'; ", teamName);
        try
        {
            System.out.println("TeammateDAO: initializing process of accessing data through query...");
            ResultSet result = super.initializeResultSet(selectQuery);
            System.out.println("TeammateDAO: query data was accessed successfully");
            while ( result.next() )
            {
                String nickname = result.getString("nickname");
                nicknames.add(nickname);
            }
            if ( nicknames.size() < 1 ) Exceptions.printError(String.format("Error: teammates were not found in the team \"%s\"\n\n", teamName));
            return nicknames;
        } catch ( SQLException exception )
        {
            super.throwException(exception, selectQuery);
        }
        return nicknames;
    }
    /**
     * Inserts a nickname to the table teammate.
     * @param nickname The nickname to be inserted to the table teammate.
     */
    public void insertNickname(String nickname)
    {
        String insertQuery = "insert into teammate (nickname) values (?);";
        try
        {
            System.out.println("TeammateDAO: inserting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(insertQuery);
            statement.setString(1, nickname);
            statement.execute();
            System.out.println("TeammateDAO: the inserts were successfully committed");
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
     * Updates a nickname of the given lane, team and match id.
     * @param nickname The nickname to be written in place of the updated nickname.
     * @param lane The lane at which pertains the teammate.
     * @param team The side of the team, order or chaos.
     * @param matchId The id of the match in which the nickname is updated.
     */
    public void updateNickname(String nickname, String lane, String team, int matchId)
    {
        String updateQuery = String.format("update teammate set nickname = '%s' where nickname in (" +
                "select tm.nickname from teammate tm " +
                "inner join team t on tm.nickname like t.%s " +
                "inner join match m on t.teamname like m.%s " +
                "where m.matchid = '%d');", nickname, lane, team, matchId);
        try
        {
            System.out.println("TeammateDAO: updating registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(updateQuery);
            statement.execute();
            System.out.println("TeammateDAO: the updates were successfully committed");
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
     * Deletes a teammate from it's nickname.
     * @param teammate The nickname from the teammate that will be deleted.
     */
    public void deleteTeammate(String teammate)
    {
        String updateQuery = String.format("delete from teammate where nickname like '%s';", teammate);
        try
        {
            System.out.println("TeammateDAO: deleting a teammate...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(updateQuery);
            statement.execute();
            System.out.println("TeammateDAO: the teammate has been deleted");
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
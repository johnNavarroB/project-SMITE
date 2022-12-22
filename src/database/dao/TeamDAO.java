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
 * modify and delete teams from the database.
 */
public class TeamDAO extends DAO{
    private static TeamDAO teamDAO = new TeamDAO();
    /**
     * Gets the object teamDAO to retrieve data from the table team.
     * @return Object teamDAO.
     */
    public static TeamDAO getInstance()
    {
        return teamDAO;
    }
    /**
     * Unique constructor for TeamDAO.
     */
    private TeamDAO()
    {}
    /**
     * Gets all the teams from the table team.
     * @return A list of lists of string with all the teams of the table team.
     */
    public List<List<String>> selectTeams()
    {
        List<List<String>> teams = new ArrayList<>();
        String selectQuery = "select * from team;";
        try
        {
            System.out.println("TeamDAO: initializing process of accessing data through query...");
            ResultSet result = super.initializeResultSet(selectQuery);
            System.out.println("TeamDAO: query data was accessed successfully");
            while ( result.next() )
            {
                List<String> team = new ArrayList<>();
                team.add(result.getString("teamname"));
                team.add(result.getString("support"));
                team.add(result.getString("adc"));
                team.add(result.getString("mid"));
                team.add(result.getString("jungle"));
                team.add(result.getString("solo"));
                teams.add(team);
            }
            if ( teams.size() < 1 ) Exceptions.printError("Error: the team table is empty\n\n");
            return teams;
        } catch ( SQLException exception )
        {
            super.throwException(exception, selectQuery);
        }
        return teams;
    }
    /**
     * Selects a team from a match id and a side.
     * @param matchId The id from the match from which the team will be selected.
     * @param side The side on the match that the team is playing.
     * @return The team from the match which the match id is and of the side it is.
     */
    public List<String> selectTeam(int matchId, String side)
    {
        List<String> team = new ArrayList<>();
        String query = String.format("select t.teamname, t.support, t.adc, t.mid, t.jungle, t.solo from team t " +
                       "inner join match m on t.teamname like m.%s " +
                       "where m.matchid = '%d';", side, matchId);
        try
        {
            System.out.println("TeamDAO: initializing process of accessing data through query...");
            ResultSet result = super.initializeResultSet(query);
            System.out.println("TeamDAO: query data was accessed successfully");
            while ( result.next() )
            {
                team.add(result.getString("teamname"));
                team.add(result.getString("support"));
                team.add(result.getString("adc"));
                team.add(result.getString("mid"));
                team.add(result.getString("jungle"));
                team.add(result.getString("solo"));
            }
            if ( team.size() < 1 ) Exceptions.printError("Error: the team was not found\n\n");
            return team;
        } catch ( SQLException exception )
        {
            super.throwException(exception, query);
        }
        return team;
    }
    /**
     * Inserts a team to the table team.
     * @param team The team to be inserted to the table team.
     */
    public void insertTeam(String[] team)
    {
        String insertQuery = "insert into team (teamname, support, adc, mid, jungle, solo) values (?, ?, ?, ?, ?, ?);";
        try
        {
            System.out.println("TeamDAO: inserting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(insertQuery);
            statement.setString(1, team[0]);
            statement.setString(2, team[1]);
            statement.setString(3, team[2]);
            statement.setString(4, team[3]);
            statement.setString(5, team[4]);
            statement.setString(6, team[5]);
            statement.execute();
            System.out.println("TeamDAO: the inserts were successfully committed");
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
     * Updates a team name in the database.
     * @param teamName The new team name to be written in place of the team name to be updated.
     * @param team The side of the team, order or chaos.
     * @param matchId The id of the match in which the nickname is updated.
     */
    public void updateTeamName(String teamName, String team, int matchId)
    {
        String updateQuery = String.format("update team set teamname = '%s' where teamname in (" +
                "select t.teamname from team t " +
                "inner join match m on t.teamname like m.%s " +
                "where m.matchid = '%d');", teamName, team, matchId);
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
     * Deletes a team from it's name.
     * @param teamName The team name from which the team will be removed.
     */
    public void deleteTeam(String teamName)
    {
        String updateQuery = String.format("delete from team where teamname like '%s';", teamName);
        try
        {
            System.out.println("TeamDAO: deleting a team...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(updateQuery);
            statement.execute();
            System.out.println("TeamDAO: the team has been deleted");
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
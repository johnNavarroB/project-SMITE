package misc;
import java.util.List;
import java.util.Arrays;
import data.Statistics;
import data.Teammate;
import data.Team;
import data.Match;
import database.dao.MatchDAO;
import database.dao.StatisticsDAO;
import ui.DatabaseMenu;
import static misc.Utils.KDACalculation;
/**
 * This class holds all the new methods for calculating scores in the database.
 */
public class ShowScore {
    /**
     * Selects all the statistics, items and nicknames from all the teammates from a match id, in a matrix of objects.
     * @param matchId The match id from the match from which the statistics will be selected.
     * @return A matrix of objects containing Statistics and the nickname as a String for each of the 10 players in a team.
     */
    private static Object[][] selectStatisticsFromDB(int matchId)
    {
        String[] lanes = {"support", "adc", "mid", "jungle", "solo"};
        String[] sides = {"orderteam", "chaosteam"};
        Object[][] statistics = new Object[10][2];
        int i = 0;
        for ( String side : sides )
        {
            for ( String lane : lanes )
            {
                List<Object> fullStats = StatisticsDAO.getInstance().selectAllStatistics(lane, side, matchId);
                String[] items = { (String) fullStats.get(8), (String) fullStats.get(9), (String) fullStats.get(10), (String) fullStats.get(11), (String) fullStats.get(12), (String) fullStats.get(13), };
                statistics[i ++] = new Object[]{ fullStats.get(0), new Statistics(items, (Integer) fullStats.get(1), (Integer) fullStats.get(2), (Integer) fullStats.get(3), (Integer) fullStats.get(4), (Integer) fullStats.get(5), (Integer) fullStats.get(6), (Integer) fullStats.get(7)) };
            }
        }
        return statistics;
    }
    /**
     * Selects all the matches on the database, creates an array of matches, and creates a new match from every match id registered on the database,
     * with the size of the number of results given by the query.
     * For each match, gets a match id, the names of both teams and the time the match lasted. Then gets the statistics, nicknames and items from the 10 players of the match.
     * Creates a matrix of teammates with two lists of 5 teammates corresponding to the teammates of each team.
     * Then creates a new match, with the name of each team, the list of teammates and the time the match lasted, adding it to the list of matches that is returned by the method.
     * @return A list of matches containing all the matches of the database.
     */
    private static Match[] selectMatchesFromDB()
    {
        List<List<Object>> matchesOnDB = MatchDAO.getInstance().selectMatches();
        Match[] matches = new Match[matchesOnDB.size()];
        int matchCount = 0;
        for ( List<Object> match : matchesOnDB )
        {
            int matchId = (Integer) match.get(0);
            String orderTeam = (String) match.get(1);
            String chaosTeam = (String) match.get(2);
            int minutes = (Integer) match.get(3);
            Object[][] statistics = selectStatisticsFromDB(matchId);
            Teammate[][] teammates = new Teammate[2][5];
            Teammate[] orderTeammates = new Teammate[5];
            Teammate[] chaosTeammates = new Teammate[5];
            for ( int i = 0, j = 0 ; i < 5 ; i ++ )
            {
                orderTeammates[i] = new Teammate( (String) statistics[j][0], (Statistics) statistics[j ++][1] );
            }
            teammates[0] = orderTeammates;
            for ( int i = 0, j = 5 ; i < 5 ; i ++ )
            {
                chaosTeammates[i] = new Teammate( (String) statistics[j][0], (Statistics) statistics[j ++][1] );
            }
            teammates[1] = chaosTeammates;
            matches[matchCount ++] = new Match(new Team(orderTeam, teammates[0]), new Team(chaosTeam, teammates[1]), minutes, matchId);
        }
        return matches;
    }
    /**
     * Selects a match given a match id from the database.
     * @param matchId The match id from the match which will be selected.
     * @return A match from the database.
     */
    private static Match selectMatchFromDBID(int matchId)
    {
        return Arrays.stream(selectMatchesFromDB()).filter(x -> x.getId() == matchId).findFirst().orElse(null);
    }
    /**
     * Shows the team and player with higher KDA registered.
     */
    public static void showTopTeam()
    {
        Match[] matches = selectMatchesFromDB();
        Team higherTeam = matches[0].getOrder();
        double higherKDA = playerRankingGetHigherKDA(higherTeam);
        for (Match match : matches)
        {
            Team currentTeam = playerRankingGetHigherKDATeam(match);
            double currentKDA = playerRankingGetHigherKDA(currentTeam);
            if ( currentKDA > higherKDA ) higherTeam = currentTeam;
        }
        System.out.format("\n\"%s\" is the team with the player with higher KDA%n", higherTeam.getTeamName());
        Teammate higherTeammate = playerRankingGetHigherKDAPlayer(higherTeam);
        System.out.format("as \"%s\" with a KDA of %.1f, %d kills, %d deaths and %d assists.%n%n", higherTeammate.getId(), higherKDA, higherTeammate.getKills(), higherTeammate.getDeaths(), higherTeammate.getAssists());
    }
    /**
     * Searches for the team with higher KDA player in a match, between the two teams.
     * @param match The match where is gonna search for the higher KDA player.
     * @return The team with the higher KDA player.
     */
    private static Team playerRankingGetHigherKDATeam(Match match)
    {
        int kills = match.getOrder().getTeammates()[0].getKills();
        int deaths = match.getOrder().getTeammates()[0].getDeaths();
        int assists = match.getOrder().getTeammates()[0].getAssists();
        double higherKDA = KDACalculation(kills, deaths, assists);
        for (int i = 1 ; i < match.getOrder().getTeammates().length ; i ++)
        {
            kills = match.getOrder().getTeammates()[i].getKills();
            deaths = match.getOrder().getTeammates()[i].getDeaths();
            assists = match.getOrder().getTeammates()[i].getAssists();
            double KDA = KDACalculation(kills, deaths, assists);
            if ( KDA > higherKDA ) higherKDA = KDA;
        }
        for (int i = 0 ; i < match.getChaos().getTeammates().length ; i ++)
        {
            kills = match.getChaos().getTeammates()[i].getKills();
            deaths = match.getChaos().getTeammates()[i].getDeaths();
            assists = match.getChaos().getTeammates()[i].getAssists();
            double KDA = KDACalculation(kills, deaths, assists);
            if ( KDA > higherKDA )
            {
                return match.getChaos();
            }
        }
        return match.getOrder();
    }
    /**
     * Searches for the higher KDA player in a team and returns this value.
     * @param team Team where is going to search between the five players.
     * @return Average of the addition of kills between the number of deaths of the higher KDA player.
     */
    private static double playerRankingGetHigherKDA(Team team)
    {
        int kills = team.getTeammates()[0].getKills();
        int deaths = team.getTeammates()[0].getDeaths();
        int assists = team.getTeammates()[0].getAssists();
        double higherKDA = KDACalculation(kills, deaths, assists);
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            kills = team.getTeammates()[i].getKills();
            deaths = team.getTeammates()[i].getDeaths();
            assists = team.getTeammates()[i].getAssists();
            double KDA = KDACalculation(kills, deaths, assists);
            if ( KDA > higherKDA ) higherKDA = KDA;
        }
        return higherKDA;
    }
    /**
     * Searches for the higher KDA player in a team and returns it.
     * @param team Team where is going to search between the five players.
     * @return The teammate with higher KDA of the team.
     */
    private static Teammate playerRankingGetHigherKDAPlayer(Team team)
    {
        int kills = team.getTeammates()[0].getKills();
        int deaths = team.getTeammates()[0].getDeaths();
        int assists = team.getTeammates()[0].getAssists();
        double higherKDA = KDACalculation(kills, deaths, assists);
        Teammate higherTeammate = team.getTeammates()[0];
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            kills = team.getTeammates()[i].getKills();
            deaths = team.getTeammates()[i].getDeaths();
            assists = team.getTeammates()[i].getAssists();
            double KDA = KDACalculation(kills, deaths, assists);
            if ( KDA > higherKDA )
            {
                higherKDA = KDA;
                higherTeammate = team.getTeammates()[i];
            }
        }
        return higherTeammate;
    }
    /**
     * Adds all the kills on a match.
     * @param match A match to count all kills.
     * @return An integer with the addition of all kills on the match introduced.
     */
    private static int addAllKills(Match match)
    {
        int kills = 0;
        for (int i = 0 ; i < match.getOrder().getTeammates().length ; i ++)
        {
            kills += match.getOrder().getTeammates()[i].getKills();
        }
        for (int i = 0 ; i < match.getChaos().getTeammates().length ; i ++)
        {
            kills += match.getChaos().getTeammates()[i].getKills();
        }
        return kills;
    }
    /**
     * Calculates the average of kills on all matches registered.
     * @return A double with the average of kills calculated.
     */
    private static double calculateAverageKills(Match[] matches)
    {
        int kills = 0;
        for (Match match : matches)
        {
            kills += addAllKills(match);
        }
        return (double) kills / matches.length;
    }
    /**
     * Adds all the deaths on a match.
     * @param match A match to count all deaths.
     * @return An integer with the addition of all deaths on the match introduced.
     */
    private static int addAllDeaths(Match match)
    {
        int deaths = 0;
        for (int i = 0 ; i < match.getOrder().getTeammates().length ; i ++)
        {
            deaths += match.getOrder().getTeammates()[i].getDeaths();
        }
        for (int i = 0 ; i < match.getChaos().getTeammates().length ; i ++)
        {
            deaths += match.getChaos().getTeammates()[i].getDeaths();
        }
        return deaths;
    }
    /**
     * Calculates the average of deaths on all matches registered.
     * @return A double with the average of deaths calculated.
     */
    private static double calculateAverageDeaths(Match[] matches)
    {
        int deaths = 0;
        for (Match match : matches)
        {
            deaths += addAllDeaths(match);
        }
        return (double) deaths / matches.length;
    }
    /**
     * Adds all the assists on a match.
     * @param match A match to count all assists.
     * @return An integer with the addition of all assists on the match introduced.
     */
    private static int addAllAssists(Match match)
    {
        int assists = 0;
        for (int i = 0 ; i < match.getOrder().getTeammates().length ; i ++)
        {
            assists += match.getOrder().getTeammates()[i].getAssists();
        }
        for (int i = 0 ; i < match.getChaos().getTeammates().length ; i ++)
        {
            assists += match.getChaos().getTeammates()[i].getAssists();
        }
        return assists;
    }
    /**
     * Calculates the average of assists on all matches registered.
     * @return A double with the average of assists calculated.
     */
    private static double calculateAverageAssists(Match[] matches)
    {
        int assists = 0;
        for (Match match : matches)
        {
            assists += addAllAssists(match);
        }
        return (double) assists / matches.length;
    }
    /**
     * Prints at the screen an average of kills, deaths, assists on all registered teams.
     */
    public static void showAverageStatistics()
    {
        Match[] matches = selectMatchesFromDB();
        double averageKills = calculateAverageKills(matches);
        double averageDeaths = calculateAverageDeaths(matches);
        double averageAssists = calculateAverageAssists(matches);
        System.out.format("%nRegisters add an average of ");
        System.out.format("%.1f kills, %.1f deaths and %.1f assists in %d matches.%n%n", averageKills, averageDeaths, averageAssists, matches.length);
    }
    /**
     * Searches for the match with the player that
     * has got more kills and prints it at the screen.
     */
    public static void showTopKillsMatch()
    {
        Match[] matches = selectMatchesFromDB();
        Match higherMatch = matches[0];
        Team higherTeam = matches[0].getOrder();
        int higherKills = playerRankingGetHigherKills(higherTeam);
        for (int i = 0 ; i < matches.length ; i ++)
        {
            Team currentTeam = playerRankingGetHigherKillsTeam(matches[i]);
            int currentKills = playerRankingGetHigherKills(currentTeam);
            if ( currentKills > higherKills )
            {
                higherMatch = matches[i];
                higherTeam = currentTeam;
            }
        }
        System.out.format("%n\"%s vs %s\" is the match%n", higherMatch.getOrder().getTeamName(), higherMatch.getChaos().getTeamName());
        Teammate higherTeammate = playerRankingGetHigherKillsPlayer(higherTeam);
        System.out.format("with the player with higher kills as \"%s\" with %d kills.%n%n", higherTeammate.getId(), higherTeammate.getKills());
    }
    /**
     * Compares the kills of the ten players that are registered
     * in a match and finds the team with the player with more kills.
     * @param match The match where is going to search for the player with more kills.
     * @return The team of the match with the player with more kills.
     */
    private static Team playerRankingGetHigherKillsTeam(Match match)
    {
        int higherKills = match.getOrder().getTeammates()[0].getKills();
        for (int i = 1 ; i < match.getOrder().getTeammates().length ; i ++)
        {
            int currentKills = match.getOrder().getTeammates()[i].getKills();
            if ( currentKills > higherKills ) higherKills = currentKills;
        }
        for (int i = 0 ; i < match.getChaos().getTeammates().length ; i ++)
        {
            int currentKills = match.getChaos().getTeammates()[i].getKills();
            if ( currentKills > higherKills )
            {
                return match.getChaos();
            }
        }
        return match.getOrder();
    }
    /**
     * Searches for the higher value of kills in a team.
     * @param team The team where is going to search for the higher value of kills.
     * @return The higher value of kills found in a team.
     */
    private static int playerRankingGetHigherKills(Team team)
    {
        int higherKills = team.getTeammates()[0].getKills();
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            int currentKills = team.getTeammates()[i].getKills();
            if ( currentKills > higherKills ) higherKills = currentKills;
        }
        return higherKills;
    }
    /**
     * Searches for the player with more kills in a team.
     * @param team The team where is going to search for the player with more kills.
     * @return The player with more kills of the team.
     */
    private static Teammate playerRankingGetHigherKillsPlayer(Team team)
    {
        int higherKills = team.getTeammates()[0].getKills();
        Teammate higherTeammate = team.getTeammates()[0];
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            int currentKills = team.getTeammates()[i].getKills();
            if ( currentKills > higherKills )
            {
                higherKills = currentKills;
                higherTeammate = team.getTeammates()[i];
            }
        }
        return higherTeammate;
    }
    /**
     * Searches for the match with the player that
     * has got more assists and prints it at the screen.
     */
    public static void showTopAssistsMatch()
    {
        Match[] matches = selectMatchesFromDB();
        Match higherMatch = matches[0];
        Team higherTeam = matches[0].getOrder();
        int higherAssists = playerRankingGetHigherAssists(higherTeam);
        for (int i = 0 ; i < matches.length ; i ++)
        {
            Team currentTeam = playerRankingGetHigherAssistsTeam(matches[i]);
            int currentAssists = playerRankingGetHigherAssists(currentTeam);
            if ( currentAssists > higherAssists )
            {
                higherMatch = matches[i];
                higherTeam = currentTeam;
            }
        }
        System.out.format("%n\"%s vs %s\" is the match%n", higherMatch.getOrder().getTeamName(), higherMatch.getChaos().getTeamName());
        Teammate higherTeammate = playerRankingGetHigherAssistsPlayer(higherTeam);
        System.out.format("with the player with higher assists as \"%s\" with %d assists.%n%n", higherTeammate.getId(), higherTeammate.getAssists());
    }
    /**
     * Compares the assists of the ten players that are registered
     * in a match and finds the team with the player with more assists.
     * @param match The match where is going to search for the player with more assists.
     * @return The team of the match with the player with more assists.
     */
    private static Team playerRankingGetHigherAssistsTeam(Match match)
    {
        int higherAssists = match.getOrder().getTeammates()[0].getAssists();
        for (int i = 1 ; i < match.getOrder().getTeammates().length ; i ++)
        {
            int currentAssists = match.getOrder().getTeammates()[i].getAssists();
            if ( currentAssists > higherAssists ) higherAssists = currentAssists;
        }
        for (int i = 0 ; i < match.getChaos().getTeammates().length ; i ++)
        {
            int currentAssists = match.getChaos().getTeammates()[i].getAssists();
            if ( currentAssists > higherAssists )
            {
                return match.getChaos();
            }
        }
        return match.getOrder();
    }
    /**
     * Searches for the higher value of assists in a team.
     * @param team The team where is going to search for the higher value of assists.
     * @return The higher value of assists found in a team.
     */
    private static int playerRankingGetHigherAssists(Team team)
    {
        int higherAssists = team.getTeammates()[0].getAssists();
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            int currentAssists = team.getTeammates()[i].getAssists();
            if ( currentAssists > higherAssists ) higherAssists = currentAssists;
        }
        return higherAssists;
    }
    /**
     * Searches for the player with more assists in a team.
     * @param team The team where is going to search for the player with more assists.
     * @return The player with more assists of the team.
     */
    private static Teammate playerRankingGetHigherAssistsPlayer(Team team)
    {
        int higherAssists = team.getTeammates()[0].getAssists();
        Teammate higherTeammate = team.getTeammates()[0];
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            int currentAssists = team.getTeammates()[i].getAssists();
            if ( currentAssists > higherAssists )
            {
                higherAssists = currentAssists;
                higherTeammate = team.getTeammates()[i];
            }
        }
        return higherTeammate;
    }
    /**
     * Searches for the match with the player that
     * has got more gold and prints it at the screen.
     */
    public static void showTopGoldMatch()
    {
        Match[] matches = selectMatchesFromDB();
        Match higherMatch = matches[0];
        Team higherTeam = matches[0].getOrder();
        int higherGold = playerRankingGetHigherGold(higherTeam);
        for (int i = 0 ; i < matches.length ; i ++)
        {
            Team currentTeam = playerRankingGetHigherGoldTeam(matches[i]);
            int currentGold = playerRankingGetHigherGold(currentTeam);
            if ( currentGold > higherGold )
            {
                higherMatch = matches[i];
                higherTeam = currentTeam;
            }
        }
        System.out.format("%n\"%s vs %s\" is the match%n", higherMatch.getOrder().getTeamName(), higherMatch.getChaos().getTeamName());
        Teammate higherTeammate = playerRankingGetHigherGoldPlayer(higherTeam);
        System.out.format("with the player with higher gold as \"%s\" with %d gold.%n%n", higherTeammate.getId(), higherTeammate.getGold());
    }
    /**
     * Compares the gold of the ten players that are registered
     * in a match and finds the team with the player with more gold.
     * @param match The match where is going to search for the player with more gold.
     * @return The team of the match with the player with more gold.
     */
    private static Team playerRankingGetHigherGoldTeam(Match match)
    {
        int higherGold = match.getOrder().getTeammates()[0].getGold();
        for (int i = 1 ; i < match.getOrder().getTeammates().length ; i ++)
        {
            int currentGold = match.getOrder().getTeammates()[i].getGold();
            if ( currentGold > higherGold ) higherGold = currentGold;
        }
        for (int i = 0 ; i < match.getChaos().getTeammates().length ; i ++)
        {
            int currentGold = match.getChaos().getTeammates()[i].getGold();
            if ( currentGold > higherGold )
            {
                return match.getChaos();
            }
        }
        return match.getOrder();
    }
    /**
     * Searches for the higher value of gold in a team.
     * @param team The team where is going to search for the higher value of gold.
     * @return The higher value of gold found in a team.
     */
    private static int playerRankingGetHigherGold(Team team)
    {
        int higherGold = team.getTeammates()[0].getGold();
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            int currentGold = team.getTeammates()[i].getGold();
            if ( currentGold > higherGold ) higherGold = currentGold;
        }
        return higherGold;
    }
    /**
     * Searches for the player with more gold in a team.
     * @param team The team where is going to search for the player with more gold.
     * @return The player with more gold of the team.
     */
    private static Teammate playerRankingGetHigherGoldPlayer(Team team)
    {
        int higherGold = team.getTeammates()[0].getGold();
        Teammate higherTeammate = team.getTeammates()[0];
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            int currentGold = team.getTeammates()[i].getGold();
            if ( currentGold > higherGold )
            {
                higherGold = currentGold;
                higherTeammate = team.getTeammates()[i];
            }
        }
        return higherTeammate;
    }
    /**
     * Asks the user to choose a match to show all kills from.
     */
    public static void showTotalKills()
    {
        if ( MatchDAO.getInstance().selectMatches().size() > 0 ) chooseToShowTotalKills();
    }
    /**
     * Asks the user to choose a match to show all kills from.
     * If the user input is not the expected, throws an exception.
     * If the user inputs "return", returns to the specific data menu of the score menu.
     */
    private static void chooseToShowTotalKills()
    {
        int matches = MatchDAO.getInstance().selectMatches().size();
        DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
        System.out.format("Choose a match to show all kills from%n");
        System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
        String userValue;
        do {
            userValue = misc.Input.strictInput();
            if ( !userValue.equals("return") && !misc.Input.checkString(userValue) ) misc.Exceptions.stadisticsFormatException();
            else if ( !userValue.equals("return") )
            {
                matches = MatchDAO.getInstance().selectMatches().size();
                showTotalKillsFromMatch(selectMatchFromDBID(Integer.parseInt(userValue)));
                DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
                System.out.format("Choose a match to show all kills from%n");
                System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
            }
        } while ( !userValue.equals("return") && matches > 0 );
    }
    /**
     * Shows all kills performed by the two teams of a match.
     * @param match The match where is going to show the kills from.
     */
    private static void showTotalKillsFromMatch(Match match)
    {
        String orderName = match.getOrder().getTeamName();
        int orderKills = getTeamKills(match.getOrder());
        String chaosName = match.getChaos().getTeamName();
        int chaosKills = getTeamKills(match.getChaos());
        System.out.format("%n\"%s\" has %d kills and \"%s\" has %s kills%n", orderName, orderKills, chaosName, chaosKills);
        System.out.format("with a total of %d kills in the match ID [%d].%n%n", orderKills + chaosKills, match.getId());
    }
    /**
     * Counts all kills performed by each player of a team.
     * @param team The team where is going to count the kills from.
     * @return The amount of kills from all the players of the team.
     */
    private static int getTeamKills(Team team)
    {
        int kills = 0;
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            kills += team.getTeammates()[i].getKills();
        }
        return kills;
    }
    /**
     * Asks the user to choose a match to show all assists from.
     */
    public static void showTotalAssists()
    {
        if ( MatchDAO.getInstance().selectMatches().size() > 0 ) chooseToShowTotalAssists();
    }
    /**
     * Asks the user to choose a match to show all assists from.
     * If the user input is not the expected, throws an exception.
     * If the user inputs "return", returns to the specific data menu of the score menu.
     */
    private static void chooseToShowTotalAssists()
    {
        int matches = MatchDAO.getInstance().selectMatches().size();
        DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
        System.out.format("Choose a match to show all assists from%n");
        System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
        String userValue;
        do {
            userValue = misc.Input.strictInput();
            if ( !userValue.equals("return") && !misc.Input.checkString(userValue) ) misc.Exceptions.stadisticsFormatException();
            else if ( !userValue.equals("return") )
            {
                matches = MatchDAO.getInstance().selectMatches().size();
                showTotalAssistsFromMatch(selectMatchFromDBID(Integer.parseInt(userValue)));
                DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
                System.out.format("Choose a match to show all assists from%n");
                System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
            }
        } while ( !userValue.equals("return") && matches > 0 );
    }
    /**
     * Shows all assists performed by the two teams of a match.
     * @param match The match where is going to show the assists from.
     */
    private static void showTotalAssistsFromMatch(Match match)
    {
        String orderName = match.getOrder().getTeamName();
        int orderAssists = getTeamAssists(match.getOrder());
        String chaosName = match.getChaos().getTeamName();
        int chaosAssists = getTeamAssists(match.getChaos());
        System.out.format("%n\"%s\" has %d assists and \"%s\" has %s assists%n", orderName, orderAssists, chaosName, chaosAssists);
        System.out.format("with a total of %d assists in the match ID [%d].%n%n", orderAssists + chaosAssists, match.getId());
    }
    /**
     * Counts all assists performed by each player of a team.
     * @param team The team where is going to count the assists from.
     * @return The amount of assists from all the players of the team.
     */
    private static int getTeamAssists(Team team)
    {
        int assists = 0;
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            assists += team.getTeammates()[i].getAssists();
        }
        return assists;
    }
    /**
     * Checks that there are matches registered,
     * prints an exception if there are not or
     * asks the user to choose a match to show average kills from.
     */
    public static void showAverageKills()
    {
        if ( MatchDAO.getInstance().selectMatches().size() > 0 ) chooseToShowAverageKills();
    }
    /**
     * Asks the user to choose a match to show average kills from.
     * If the user input is not the expected, throws an exception.
     * If the user inputs "return", returns to the specific data menu of the score menu.
     */
    private static void chooseToShowAverageKills()
    {
        int matches = MatchDAO.getInstance().selectMatches().size();
        DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
        System.out.format("Choose a match to show average player kills from%n");
        System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
        String userValue;
        do {
            userValue = misc.Input.strictInput();
            if ( !userValue.equals("return") && !misc.Input.checkString(userValue) ) misc.Exceptions.stadisticsFormatException();
            else if ( !userValue.equals("return") )
            {
                matches = MatchDAO.getInstance().selectMatches().size();
                showAverageKillsFromMatch(selectMatchFromDBID(Integer.parseInt(userValue)));
                DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
                System.out.format("Choose a match to show average player kills from%n");
                System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
            }
        } while ( !userValue.equals("return") && matches > 0 );
    }
    /**
     * Shows average assists of all players in a match.
     * @param match The match where is going to show the average assists from.
     */
    private static void showAverageKillsFromMatch(Match match)
    {
        int averageMatchKills = addAllKills(match) / (match.getOrder().getTeammates().length + match.getChaos().getTeammates().length);
        System.out.format("%nThere is an average of %d kills in the match ID [%d].%n%n", averageMatchKills, match.getId());
    }
    /**
     * Checks that there are matches registered,
     * prints an exception if there are not or
     * asks the user to choose a match to show average assists from.
     */
    public static void showAverageAssists()
    {
        if ( MatchDAO.getInstance().selectMatches().size() > 0 ) chooseToShowAverageAssists();
    }
    /**
     * Asks the user to choose a match to show average assists from.
     * If the user input is not the expected, throws an exception.
     * If the user inputs "return", returns to the specific data menu of the score menu.
     */
    private static void chooseToShowAverageAssists()
    {
        int matches = MatchDAO.getInstance().selectMatches().size();
        DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
        System.out.format("Choose a match to show average player assists from%n");
        System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
        String userValue;
        do {
            userValue = misc.Input.strictInput();
            if ( !userValue.equals("return") && !misc.Input.checkString(userValue) ) misc.Exceptions.stadisticsFormatException();
            else if ( !userValue.equals("return") )
            {
                matches = MatchDAO.getInstance().selectMatches().size();
                showAverageAssistsFromMatch(selectMatchFromDBID(Integer.parseInt(userValue)));
                DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
                System.out.format("Choose a match to show average player assists from%n");
                System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
            }
        } while ( !userValue.equals("return") && matches > 0 );
    }
    /**
     * Shows average assists of all players in a match.
     * @param match The match where is going to show the average assists from.
     */
    private static void showAverageAssistsFromMatch(Match match)
    {
        int averageMatchAssists = addAllAssists(match) / (match.getOrder().getTeammates().length + match.getChaos().getTeammates().length);
        System.out.format("%nThere is an average of %d assists in the match ID [%d].%n%n", averageMatchAssists, match.getId());
    }
    /**
     * Asks the user to choose a match to show all the structure damage from.
     */
    public static void showTotalStructureDamage()
    {
        if ( MatchDAO.getInstance().selectMatches().size() > 0 ) chooseToShowTotalStructureDamage();
    }
    /**
     * Asks the user to choose a match to show all the structure damage from.
     * If the user input is not the expected, throws an exception.
     * If the user inputs "return", returns to the specific data menu of the score menu.
     */
    private static void chooseToShowTotalStructureDamage()
    {
        int matches = MatchDAO.getInstance().selectMatches().size();
        DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
        System.out.format("Choose a match to show all the structure damage from%n");
        System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
        String userValue;
        do {
            userValue = misc.Input.strictInput();
            if ( !userValue.equals("return") && !misc.Input.checkString(userValue) ) misc.Exceptions.stadisticsFormatException();
            else if ( !userValue.equals("return") )
            {
                matches = MatchDAO.getInstance().selectMatches().size();
                showTotalStructureDamageFromMatch(selectMatchFromDBID(Integer.parseInt(userValue)));
                DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
                System.out.format("Choose a match to show all the structure damage from%n");
                System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
            }
        } while ( !userValue.equals("return") && matches > 0 );
    }
    /**
     * Shows all the structure damage performed by the two teams of a match.
     * @param match The match where is going to show the structure damage from.
     */
    private static void showTotalStructureDamageFromMatch(Match match)
    {
        String orderName = match.getOrder().getTeamName();
        int orderStructureDamage = getTeamStructureDamage(match.getOrder());
        String chaosName = match.getChaos().getTeamName();
        int chaosStructureDamage = getTeamStructureDamage(match.getChaos());
        System.out.format("%n\"%s\" has %d structure damage and \"%s\" has %s structure damage%n", orderName, orderStructureDamage, chaosName, chaosStructureDamage);
        System.out.format("with a total of %d structure damage in the match ID [%d].%n%n", orderStructureDamage + chaosStructureDamage, match.getId());
    }
    /**
     * Counts all the structure damage performed by each player of a team.
     * @param team The team where is going to count the structure damage from.
     * @return The amount of structure damage from all the players of the team.
     */
    private static int getTeamStructureDamage(Team team)
    {
        int structureDamage = 0;
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            structureDamage += team.getTeammates()[i].getStructureDamage();
        }
        return structureDamage;
    }
    /**
     * Asks the user to choose a match to show all the minion damage from.
     */
    public static void showTotalMinionDamage()
    {
        if ( MatchDAO.getInstance().selectMatches().size() > 0 ) chooseToShowTotalMinionDamage();
    }
    /**
     * Asks the user to choose a match to show all the minion damage from.
     * If the user input is not the expected, throws an exception.
     * If the user inputs "return", returns to the specific data menu of the score menu.
     */
    private static void chooseToShowTotalMinionDamage()
    {
        int matches = MatchDAO.getInstance().selectMatches().size();
        DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
        System.out.format("Choose a match to show all the minion damage from%n");
        System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
        String userValue;
        do {
            userValue = misc.Input.strictInput();
            if ( !userValue.equals("return") && !misc.Input.checkString(userValue) ) misc.Exceptions.stadisticsFormatException();
            else if ( !userValue.equals("return") )
            {
                matches = MatchDAO.getInstance().selectMatches().size();
                showTotalMinionDamageFromMatch(selectMatchFromDBID(Integer.parseInt(userValue)));
                DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
                System.out.format("Choose a match to show all the minion damage from%n");
                System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
            }
        } while ( !userValue.equals("return") && matches > 0 );
    }
    /**
     * Shows all the minion damage performed by the two teams of a match.
     * @param match The match where is going to show the minion damage from.
     */
    private static void showTotalMinionDamageFromMatch(Match match)
    {
        String orderName = match.getOrder().getTeamName();
        int orderMinionDamage = getTeamMinionDamage(match.getOrder());
        String chaosName = match.getChaos().getTeamName();
        int chaosMinionDamage = getTeamMinionDamage(match.getChaos());
        System.out.format("%n\"%s\" has %d minion damage and \"%s\" has %s minion damage%n", orderName, orderMinionDamage, chaosName, chaosMinionDamage);
        System.out.format("with a total of %d minion damage in the match ID [%d].%n%n", orderMinionDamage + chaosMinionDamage, match.getId());
    }
    /**
     * Counts all the minion damage performed by each player of a team.
     * @param team The team where is going to count the minion damage from.
     * @return The amount of minion damage from all the players of the team.
     */
    private static int getTeamMinionDamage(Team team)
    {
        int minionDamage = 0;
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            minionDamage += team.getTeammates()[i].getMinionDamage();
        }
        return minionDamage;
    }
    /**
     * Asks the user to choose a match to show all the player damage from.
     */
    public static void showTotalPlayerDamage()
    {
        if ( MatchDAO.getInstance().selectMatches().size() > 0 ) chooseToShowTotalPlayerDamage();
    }
    /**
     * Asks the user to choose a match to show all the player damage from.
     * If the user input is not the expected, throws an exception.
     * If the user inputs "return", returns to the specific data menu of the score menu.
     */
    private static void chooseToShowTotalPlayerDamage()
    {
        int matches = MatchDAO.getInstance().selectMatches().size();
        DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
        System.out.format("Choose a match to show all the player damage from%n");
        System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
        String userValue;
        do {
            userValue = misc.Input.strictInput();
            if ( !userValue.equals("return") && !misc.Input.checkString(userValue) ) misc.Exceptions.stadisticsFormatException();
            else if ( !userValue.equals("return") )
            {
                matches = MatchDAO.getInstance().selectMatches().size();
                showTotalPlayerDamageFromMatch(selectMatchFromDBID(Integer.parseInt(userValue)));
                DatabaseMenu.getInstance().printMatches(misc.Input.getMatchesString());
                System.out.format("Choose a match to show all the player damage from%n");
                System.out.format("Select a match by entering a number or enter \"return\" to go back.%n");
            }
        } while ( !userValue.equals("return") && matches > 0 );
    }
    /**
     * Shows all the player damage performed by the two teams of a match.
     * @param match The match where is going to show the player damage from.
     */
    private static void showTotalPlayerDamageFromMatch(Match match)
    {
        String orderName = match.getOrder().getTeamName();
        int orderPlayerDamage = getTeamPlayerDamage(match.getOrder());
        String chaosName = match.getChaos().getTeamName();
        int chaosPlayerDamage = getTeamPlayerDamage(match.getChaos());
        System.out.format("%n\"%s\" has %d player damage and \"%s\" has %s player damage%n", orderName, orderPlayerDamage, chaosName, chaosPlayerDamage);
        System.out.format("with a total of %d player damage in the match ID [%d].%n%n", orderPlayerDamage + chaosPlayerDamage, match.getId());
    }
    /**
     * Counts all the player damage performed by each player of a team.
     * @param team The team where is going to count the player damage from.
     * @return The amount of player damage from all the players of the team.
     */
    private static int getTeamPlayerDamage(Team team)
    {
        int playerDamage = 0;
        for (int i = 0 ; i < team.getTeammates().length ; i ++)
        {
            playerDamage += team.getTeammates()[i].getPlayerDamage();
        }
        return playerDamage;
    }
}
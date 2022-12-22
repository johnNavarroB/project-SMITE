package misc;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;
import data.Data;
import data.Match;
import data.Team;
import data.Teammate;
import data.Statistics;
import database.dao.TeammateDAO;
import database.dao.TeamDAO;
import database.dao.MatchDAO;
import database.dao.StatisticsDAO;
import database.dao.ItemsDAO;
import ui.DatabaseMenu;
import static data.Data.importMatches;
import static ui.Main.scanner;
import static misc.Utils.myToCharArray;
import static misc.Exceptions.teammateIDOutOfBoundsException;
import static misc.Exceptions.teammateIDWhiteSpacesException;
import static misc.Exceptions.teamNameOutOfBoundsException;
import static misc.Exceptions.teamNameWhiteSpacesException;
import static misc.Exceptions.itemOutOfBoundsException;
import static misc.Exceptions.stadisticsFormatException;
import static misc.Exceptions.stadisticsLimitReachedException;
import static misc.Exceptions.minutesLimitReachedException;
import static misc.Exceptions.importMatchesLimitReachedException;
import static misc.Exceptions.booleanInsensitiveException;
/**
 * This class holds all methods related to the modification
 * of user input in order to produce a specific product.
 */
public class Input {
    /**
     * Checks if the first character of the parameter userValue is between 0 and 4.
     * @param userValue A string without whitespaces the user previously introduced.
     * @return Returns true if the first character is not between 0 and 4, returns false otherwise.
     * Also returns true if the parameter userValue is longer than 1.
     */
    public static boolean notAllowed(String userValue)
    {
        if ( userValue.length() > 1 ) return true;
        char option = userValue.charAt(0);
        return option != '0' && option != '1' && option != '2' && option != '3' && option != '4';
    }
    /**
     * Checks if the first character of the parameter userValue is between 0 and 5.
     * @param userValue A string without whitespaces the user previously introduced.
     * @return Returns true if the first character is not between 0 and 5, returns false otherwise.
     * Also returns true if the parameter userValue is longer than 1.
     */
    public static boolean mainMenuNotAllowed(String userValue)
    {
        if ( userValue.length() > 1 ) return true;
        char option = userValue.charAt(0);
        return option != '0' && option != '1' && option != '2' && option != '3' && option != '4' && option != '5';
    }
    /**
     * Searches for a space in the parameter userValue,
     * if any, rewrites the parameter until the first space not including the last one.
     * @param userValue A string the user previously introduced.
     * @return Returns a string without whitespaces,
     * if there were no whitespaces, returns the unchanged parameter.
     */
    private static String userString(String userValue)
    {
        String option = "";
        for (int i = 0 ; i < userValue.length() ; i += 1)
        {
            if ( userValue.charAt(i) == ' ' )
            {
                for (int j = 0 ; j < i ; j += 1)
                {
                    option += userValue.charAt(j);
                }
                return option;
            }
        }
        return userValue;
    }
    /**
     * Checks if parameter userValue is composed by integers only.
     * @param userValue A string without whitespaces the user previously introduced.
     * @return Returns false if the method finds something else
     * than integers in the parameter userValue, returns true otherwise.
     */
    public static boolean checkString(String userValue)
    {
        for (char character : userValue.toCharArray())
        {
            if ( character != '0' && character != '1' && character != '2' && character != '3' && character != '4' && character != '5' &&
                    character != '6' && character != '7' && character != '8' && character != '9') return false;
        }
        return true;
    }
    /**
     * Requests for user input.
     * Also checks if the user input is the expected, it changes its behaviour if is not.
     * If there is no input, or the input is a whitespace, waits for the next input.
     * @return Returns a string without whitespaces.
     */
    public static String strictInput()
    {
        String userValue;
        do{
            userValue = scanner.nextLine();
        } while ( userValue.length() == 0 || userValue.charAt(0) == ' ' );
        userValue = userString(userValue);
        return userValue;
    }
    /**
     * Checks if the first character of the parameter userValue is between 0 and 7.
     * @param userValue A string without whitespaces the user previously introduced.
     * @return Returns true if the first character is not between 0 and 7, returns false otherwise.
     * Also returns true if the parameter userValue is longer than 1.
     */
    public static boolean specificDataNotAllowed(String userValue)
    {
        if ( userValue.length() > 1 ) return true;
        char option = userValue.charAt(0);
        return option != '0' && option != '1' && option != '2' && option != '3' && option != '4' && option != '5' && option != '6' && option != '7';
    }
    /**
     * Checks if the userValue parameter is the expected value
     * regardless of whether it is uppercase or not.
     * @param userValue A string without whitespaces the user previously introduced.
     * @return Returns true if the first character of the parameter userValue
     * is not 'n', 'N', 'y' or 'Y'. Returns false otherwise.
     */
    private static boolean capInsensitive(String userValue)
    {
        if ( userValue.length() > 1 ) return true;
        char option = userValue.charAt(0);
        return option != 'n' && option != 'N' && option != 'y' && option != 'Y';
    }
    /**
     * Checks if the first character of the parameter userValue is between 0 and 2.
     * @param userValue A string without whitespaces the user previously introduced.
     * @return Returns true if the first character is not between 0 and 2, returns false otherwise.
     * Also returns true if the parameter userValue is longer than 1.
     */
    public static boolean matchesNotAllowed(String userValue)
    {
        if ( userValue.length() > 1 ) return true;
        char option = userValue.charAt(0);
        return option != '0' && option != '1' && option != '2';
    }
    /**
     * Requests for user input.
     * Also checks if the user input is the expected, it changes its behaviour if is not.
     * If there is no input, or the input is a whitespace, returns "none" per default.
     * @return Returns a string which can be the defined by the user,
     * or returns a default parameter in case the teammate had blank item slots defined as "none".
     */
    private static String teammateStrictInput()
    {
        String userValue = scanner.nextLine();
        if ( userValue.length() == 0 || checkBlankInput(userValue) )
        {
            return "none";
        }
        if ( userValue.charAt(0) == ' ' ) userValue = removeWhiteSpaces(userValue);
        return userValue;
    }
    /**
     * Keeps checking that the attribute "id" of Teammate has no white spaces and
     * its length is between one and sixteen. It asks for the "id" again if any
     * of the conditions mentioned didn't meet. Also outputs the corresponding error.
     * @param id Attribute of Teammate that requires the compliance of specific characteristics.
     * @return An String "id" without white spaces and between one and sixteen.
     */
    private static String verifyTeammateID(String id)
    {
        while ( teammateIDCheckSize(id) || checkSpaces(id) )
        {
            if ( teammateIDCheckSize(id) ) teammateIDOutOfBoundsException(id);
            else if ( checkSpaces(id) ) teammateIDWhiteSpacesException(id);
            id = scanner.nextLine();
        }
        return id;
    }
    /**
     * Checks if the parameter "id" is not between one and sixteen.
     * @param id Attribute of Teammate that requires the compliance of specific characteristics.
     * @return Returns true if the parameter "id" is not between one and sixteen, returns false otherwise.
     */
    private static boolean teammateIDCheckSize(String id)
    {
        return id.length() < 1 || id.length() > 16;
    }
    /**
     * Checks if the parameter "item" is between three and thirty-two.
     * @param item A String, part of a vector of Teammate, that requires the compliance of specific characteristics.
     * @return Returns true if the parameter "item" is not between three and thirty-two, returns false otherwise.
     */
    private static boolean itemCheckSize(String item)
    {
        return item.length() < 3 || item.length() > 32;
    }
    /**
     * Checks if the parameter "id" has white spaces.
     * @param id Attribute of Teammate that requires the compliance of specific characteristics.
     * @return Returns true if the parameter "id" has any white space, returns false otherwise.
     */
    private static boolean checkSpaces(String id)
    {
        for (char character : myToCharArray(id))
        {
            if ( character == ' ' ) return true;
        }
        return false;
    }
    /**
     * Keeps checking that the parameter "teamName" is not starting with a white space and
     * that its length is between one and sixteen. It asks for the "teamName" again if any
     * of the conditions mentioned didn't meet. Outputs an error if that happens.
     * @param teamName Attribute of Team that requires the compliance of specific characteristics.
     * @return An String with a length of one to sixteen.
     */
    private static String verifyTeamName(String teamName)
    {
        while ( teammateIDCheckSize(teamName) || teamName.charAt(0) == ' ' )
        {
            if ( teammateIDCheckSize(teamName) ) teamNameOutOfBoundsException(teamName);
            else if ( teamName.charAt(0) == ' ' ) teamNameWhiteSpacesException();
            teamName = scanner.nextLine();
        }
        return teamName;
    }
    /**
     * Checks if a string has anything else than white spaces.
     * @param string A String to check if contains anything else than white spaces.
     * @return Returns true if the parameter String its full of spaces, returns false otherwise.
     */
    private static boolean checkBlankInput(String string)
    {
        for (char character : myToCharArray(string))
        {
            if ( character != ' ' ) return false;
        }
        return true;
    }
    /**
     * Finds the first non white space character on a String and
     * creates a new String from the first non white space character.
     * @param string A String starting with a white space.
     * @return The parameter String introduced started with a non white space character.
     */
    private static String removeWhiteSpaces(String string)
    {
        int index = 0;
        for (int i = 0 ; i < string.length() ; i ++)
        {
            if ( string.charAt(i) != ' ' )
            {
                index = i;
                break;
            }
        }
        String clearedString = "";
        for (int i = index ; i < string.length() ; i ++)
        {
            clearedString += string.charAt(i);
        }
        return clearedString;
    }
    /**
     * Asks the user to identify a player and its related information.
     * @return An identified object Teammate with all the restrictions applied.
     */
    private static Teammate createTeammate()
    {
        String id = setTeammateID();
        return new Teammate(id, setStadistics());
    }
    /**
     * Asks for the main identification of a Teammate,
     * also checks all the restrictions specified.
     * @return A main identifier for a player with all the restrictions applied.
     */
    private static String setTeammateID()
    {
        System.out.format("Introduce username id please. Length 1-16 and can not contain spaces.%n");
        String id = scanner.nextLine();
        if ( teammateIDCheckSize(id) || checkSpaces(id) ) id = verifyTeammateID(id);
        return id;
    }
    /**
     * Prepares a vector of String to identify all objects a player has when the game ends.
     * Awares the user of what input is the expected, outputs errors when the input does not meet the requirements.
     * If there are any empty slots for the player items, it will fill them with "none".
     * @return A vector of String with all items identified and all the restrictions applied.
     */
    private static String[] setItems()
    {
        String[] items = new String[6];
        String ordinal = "st";
        System.out.format("Introduce the six items in order from first to sixth, please.%n");
        System.out.format("Items can contain spaces and have a length of 3-32.%n");
        System.out.format("If the slot is empty, press enter to skip.%n");
        for (int i = 0 ; i < items.length ; i += 1)
        {
            if ( i == 1 ) ordinal = "nd";
            else if ( i == 2 ) ordinal = "rd";
            else if ( i > 2 ) ordinal = "th";
            System.out.format("Introduce the %d%s item%n", i + 1, ordinal);
            do{
                items[i] = teammateStrictInput();
                if ( itemCheckSize(items[i]) ) itemOutOfBoundsException(items[i]);
            } while ( itemCheckSize(items[i]) );
        }
        return items;
    }
    /**
     * Creates a fixed vector of five teammates introducing an identifier
     * for each teammate and associating it to a role in the game.
     * @return A vector of five teammates identified.
     */
    private static Teammate[] createTeammates()
    {
        Teammate[] teammates = new Teammate[5];
        for (int i = 0 ; i < 5 ; i += 1)
        {
            String role = createTeammatesShowRole(i);
            System.out.format("Introduce the %s of the team please%n", role);
            teammates[i] = createTeammate();
        }
        return teammates;
    }
    /**
     * Uses an index to determine the role of a teammate to help the user know it when its introducing data.
     * @param i An integer index provided by a loop to choose a return String.
     * @return A String that will be printed at the screen to choose the role of each teammate introduced in the team.
     */
    private static String createTeammatesShowRole(int i)
    {
        switch(i)
        {
            case 0:
                return "support";
            case 1:
                return "adc";
            case 2:
                return "mid";
            case 3:
                return "jungle";
            case 4:
                return "solo";
        }
        return "none";
    }
    /**
     * Asks the user to identify a team and its teammates.
     * @return An identified object Team with all the restrictions applied.
     */
    private static Team createTeam()
    {
        String teamName = setTeamName();
        Teammate[] teammates = createTeammates();
        return new Team(teamName, teammates);
    }
    /**
     * Asks for the team name and checks that its length is between one and sixteen
     * and that does not start with a white space.
     * @return A team name with all the restrictions applied.
     */
    private static String setTeamName()
    {
        System.out.format("Length 1-16 and can not start with a white space.%n");
        String teamName = scanner.nextLine();
        if ( teammateIDCheckSize(teamName) || teamName.charAt(0) == ' ' ) teamName = verifyTeamName(teamName);
        return teamName;
    }
    /**
     * Asks for a teammate's items, gold, kills, deaths and assists,
     * then creates a new object Stadistics to introduce as parameter
     * at the object Teammate's constructor.
     * @return Object Stadistics that refers to all the related information
     * of a teammate in a match.
     */
    private static Statistics setStadistics()
    {
        String[] items = setItems();
        int gold = setStadisticsStrictInput("gold", false);
        int kills = setStadisticsStrictInput("kills", false);
        int deaths = setStadisticsStrictInput("deaths", false);
        int assists = setStadisticsStrictInput("assists", false);
        int structureDamage = setStadisticsStrictInput("structure damage", false);
        int minionDamage = setStadisticsStrictInput("minion damage", false);
        int playerDamage = setStadisticsStrictInput("player damage", false);
        return new Statistics(items, gold, kills, deaths, assists, structureDamage, minionDamage, playerDamage);
    }
    /**
     * Asks for an input that needs to be a whole number.
     * Shows an exception when is not a whole number or is above the limit.
     * @param stat Will clarify to the user what is introducing.
     * @param repeat Set true if is a recursive call in order to not repeat the output.
     * @return A whole number below the limit.
     */
    private static int setStadisticsStrictInput(String stat, boolean repeat)
    {
        if ( !repeat ) System.out.format("Introduce the current player's %s.%n", stat);
        String userValue;
        do{
            userValue = strictInput();
            if ( !checkString(userValue) ) stadisticsFormatException();
        } while ( !checkString(userValue) );
        if ( Integer.parseInt(userValue) > 9999999 )
        {
            stadisticsLimitReachedException();
            userValue = String.valueOf(setStadisticsStrictInput(stat, true));
        }
        return Integer.parseInt(userValue);
    }
    /**
     * Asks the user to identify two teams and the duration of a match in minutes.
     * @return A correctly identified object Match.
     */
    private static Match createMatch()
    {
        System.out.format("Introduce the order team name, please.%n");
        Team order = createTeam();
        System.out.format("Introduce the chaos team name, please.%n");
        Team chaos = createTeam();
        int minutes = minutesStrictInput(false);
        return new Match(order, chaos, minutes);
    }
    /**
     * Inserts the teammates from a match in the database.
     * @param match The match from which the teammates will be inserted to the database.
     */
    private static void saveTeammateDB(Match match)
    {
        for ( Teammate teammate : match.getChaos().getTeammates() )
        {
            TeammateDAO.getInstance().insertNickname(teammate.getId());
        }
        for ( Teammate teammate : match.getOrder().getTeammates() )
        {
            TeammateDAO.getInstance().insertNickname(teammate.getId());
        }
    }
    /**
     * Inserts the teams from a match in the database.
     * @param match The match from which the teams will be inserted to the database.
     */
    private static void saveTeamDB(Match match)
    {
        Team order = match.getOrder();
        Teammate[] orderTeammates = order.getTeammates();
        String[] orderTeam = new String[6];
        orderTeam[0] = order.getTeamName();
        for ( int i = 1 ; i < 6 ; i ++ )
        {
            orderTeam[i] = orderTeammates[i - 1].getId();
        }
        TeamDAO.getInstance().insertTeam(orderTeam);
        Team chaos = match.getChaos();
        Teammate[] chaosTeammates = chaos.getTeammates();
        String[] chaosTeam = new String[6];
        chaosTeam[0] = chaos.getTeamName();
        for ( int i = 1 ; i < 6 ; i ++ )
        {
            chaosTeam[i] = chaosTeammates[i - 1].getId();
        }
        TeamDAO.getInstance().insertTeam(chaosTeam);
    }
    /**
     * Inserts a match in the database.
     * @param match The match to be inserted to the database.
     */
    private static int saveMatchDB(Match match)
    {
        Object[] matchToBeInserted =
        {
                match.getOrder().getTeamName(),
                match.getChaos().getTeamName(),
                match.getMinutes()
        };
        return MatchDAO.getInstance().insertMatch(matchToBeInserted);
    }
    /**
     * Inserts statistics in the database.
     * @param match The statistics to be inserted to the database.
     */
    private static void saveStatistics(Match match, int matchId)
    {
        Teammate[] orderTeammates = match.getOrder().getTeammates();
        Teammate[] chaosTeammates = match.getChaos().getTeammates();
        Object[][] statistics = new Object[10][9];
        {
            int i = 0;
            do
            {
                Object[] stats =
                {
                    orderTeammates[i].getId(),
                    matchId,
                    orderTeammates[i].getGold(),
                    orderTeammates[i].getKills(),
                    orderTeammates[i].getDeaths(),
                    orderTeammates[i].getAssists(),
                    orderTeammates[i].getStructureDamage(),
                    orderTeammates[i].getMinionDamage(),
                    orderTeammates[i].getPlayerDamage(),
                    orderTeammates[i].getStatistics().getItems()
                };
                statistics[i] = stats;
            } while ( ++ i < 5 );
        }
        {
            int i = 0, j = 4;
            do
            {
                Object[] stats =
                {
                    chaosTeammates[i].getId(),
                    matchId,
                    chaosTeammates[i].getGold(),
                    chaosTeammates[i].getKills(),
                    chaosTeammates[i].getDeaths(),
                    chaosTeammates[i].getAssists(),
                    chaosTeammates[i].getStructureDamage(),
                    chaosTeammates[i].getMinionDamage(),
                    chaosTeammates[i].getPlayerDamage(),
                    chaosTeammates[i].getStatistics().getItems()
                };
                statistics[++ j] = stats;
            } while ( ++ i < 5 );
        }
        for (Object[] stats : statistics)
        {
            int statisticsId = StatisticsDAO.getInstance().insertStatistics(stats);
            ItemsDAO.getInstance().insertItems((Object[]) stats[9], statisticsId);
        }
    }
    /**
     * Initializes a new object match and calls a method to
     * add the new match to the vector of matches of the object Season.
     */
    public static void saveNewMatch()
    {
        //Match match = createMatch();
        saveTeammateDB(Data.match);
        saveTeamDB(Data.match);
        int matchId = saveMatchDB(Data.match);
        saveStatistics(Data.match, matchId);
    }
    /**
     * Saves a given match in the database.
     * @param match The match to be saved in the database.
     */
    public static void saveNewMatch(Match match)
    {
        saveTeammateDB(match);
        saveTeamDB(match);
        saveStatistics(match, saveMatchDB(match));
    }
    /**
     * Checks that there are matches registered,
     * prints an exception if there are not or
     * asks the user to choose a match to rewrite.
     */
    public static void alterExistentMatch()
    {
        if ( MatchDAO.getInstance().selectMatches().size() > 0 ) chooseToAlterMatch();
    }
    /**
     * Checks that there are matches registered,
     * prints an exception if there are not or
     * asks the user to choose a match to delete.
     */
    public static void deleteExistentMatch()
    {
        if ( MatchDAO.getInstance().selectMatches().size() > 0 ) chooseToDeleteMatch();
    }
    /**
     * Asks for an input that needs to be a whole number, with a limit of 1440.
     * Shows an exception when is not a whole number or is above the limit.
     * @param repeat Set true if is a recursive call in order to not repeat the output.
     * @return A whole number below the limit.
     */
    private static int minutesStrictInput(boolean repeat)
    {
        if ( !repeat ) System.out.format("Introduce the match duration in minutes, please.%n");
        String userValue;
        do{
            userValue = strictInput();
            if ( !checkString(userValue) ) stadisticsFormatException();
        } while ( !checkString(userValue) );
        if ( Integer.parseInt(userValue) > 1440 )
        {
            minutesLimitReachedException();
            userValue = String.valueOf(minutesStrictInput(true));
        }
        return Integer.parseInt(userValue);
    }
    /**
     * Converts minutes into hours and keeps the rest of minutes.
     * @param minute Total of minutes to convert.
     * @return A String with HH:MM format where minutes have been converted to hours.
     */
    public static String showMinutes(int minute)
    {
        int hour = minute / 60;
        minute -= 60 * hour;
        return String.format("%02d:%02d", hour, minute);
    }
    /**
     * Asks the user to choose how many matches wants to import.
     * If the input is not the expected, throws an exception.
     * @return An integer between one and ten that represents how many matches will be imported.
     */
    private static int importMatchesStrictInput()
    {
        System.out.format("Introduce the value of matches to import. [1-10]%n");
        String userValue;
        do{
            userValue = strictInput();
            if ( !checkString(userValue) )
            {
                stadisticsFormatException();
                return 0;
            }
        } while ( !checkString(userValue) );
        if ( userValue.length() > 2 || Integer.parseInt(userValue) > 10 )
        {
            importMatchesLimitReachedException();
            return 0;
        }
        return Integer.parseInt(userValue);
    }
    /**
     * Gets a list with all the matches with each team name and time.
     * @return A list with all the matches to be printed at the screen.
     */
    public static List<String> getMatchesString()
    {
        List<String> matches = new ArrayList<>();
        for ( List<Object> objects : MatchDAO.getInstance().selectMatches() )
        {
            matches.add( objects.get(0) + " - " + objects.get(1) + " vs " + objects.get(2) + " " + objects.get(3) + "'");
        }
        return matches;
    }
    /**
     * Updates the items of a teammate in the database.
     * @param ordinal The prefix of each item to be selected at the database query.
     * @param lanes The lane of each teammate to be selected at the database query.
     * @param teammates The teammates of a team.
     * @param team The side of the team, order or chaos.
     * @param matchId The id of the match in which the items of the teammates are updated.
     * @param i The iteration of the loop that chooses a teammate and its corresponding lane.
     */
    private static void updateItems(String[] ordinal, String[] lanes, Teammate[] teammates, String team, int matchId, int i)
    {
        int j = 0;
        do
        {
            String item = teammates[i].getStatistics().getItems()[j];
            ItemsDAO.getInstance().updateItems(ordinal[j], item, lanes[i], team, matchId);
        } while ( ++ j < 6 );
    }
    /**
     * Updates the statistics and calls another method to update the items from the teammates of a team.
     * @param columns Columns of the table statistics to be selected at the database query.
     * @param lanes The lane of each teammate to be selected at the database query.
     * @param teammates The teammates of a team.
     * @param team The side of the team, order or chaos.
     * @param matchId The id of the match in which the items of the teammates are updated.
     * @param i The iteration of the loop that chooses a teammate and its corresponding lane.
     */
    private static void updateStatisticsValues(String[] columns, String[] lanes, Teammate[] teammates, String team, int matchId, int i)
    {
        int[] values =
        {
            teammates[i].getGold(),
            teammates[i].getKills(),
            teammates[i].getDeaths(),
            teammates[i].getAssists(),
            teammates[i].getStructureDamage(),
            teammates[i].getMinionDamage(),
            teammates[i].getPlayerDamage()
        };
        int j = 1;
        do
        {
            StatisticsDAO.getInstance().updateStatistics(columns[j - 1], values[j - 1], lanes[i], team, matchId);
        } while ( ++ j < 8 );
    }
    /**
     * Updates the statistics and items of the teammates of a team.
     * @param teammates The teammates of a team.
     * @param team The side of the team, order or chaos.
     * @param matchId The id of the match in which the items of the teammates are updated.
     */
    private static void updateStatistics(Teammate[] teammates, String team, int matchId)
    {
        String[] lanes = {"support", "adc", "mid", "jungle", "solo"};
        String[] ordinal = {"first", "second", "third", "fourth", "fifth", "sixth"};
        String[] columns = {"gold", "kills", "deaths", "assists", "structuredmg", "miniondmg", "playerdmg"};
        int i = 0;
        do
        {
            TeammateDAO.getInstance().updateNickname(teammates[i].getId(), lanes[i], team, matchId);
            updateItems(ordinal, lanes, teammates, team, matchId, i);
            updateStatisticsValues(columns, lanes, teammates, team, matchId, i);
        } while ( ++ i < 5 );
    }
    /**
     * Updates a whole match in the database.
     * @param matchId The match id from the match which will be updated.
     * @param newMatch The match that will be written in place of the updated match.
     */
    private static void updateMatch(int matchId, Match newMatch)
    {
        updateStatistics(newMatch.getOrder().getTeammates(), "orderteam", matchId);
        updateStatistics(newMatch.getChaos().getTeammates(), "chaosteam", matchId);
        TeamDAO.getInstance().updateTeamName(newMatch.getOrder().getTeamName(), "orderteam", matchId);
        TeamDAO.getInstance().updateTeamName(newMatch.getChaos().getTeamName(), "chaosteam", matchId);
        MatchDAO.getInstance().updateTime(newMatch.getMinutes(), matchId);
    }
    /**
     * Asks the user to choose a match to be modified and then prints all the registered matches.
     * If the user input is not the expected or if there are not matches registered, throws an exception.
     * If the user inputs "return", returns to the data menu.
     */
    private static void chooseToAlterMatch()
    {
        DatabaseMenu.getInstance().printMatches(getMatchesString());
        System.out.format("Which match is desired to be modified?%n");
        System.out.format("Select a match by entering a number or enter \"return\" to go to the data menu.%n");
        String userValue;
        do {
            userValue = strictInput();
            if ( !userValue.equals("return") && !checkString(userValue) ) stadisticsFormatException();
            else if ( !userValue.equals("return") )
            {
                updateMatch(Integer.parseInt(userValue), Data.match);
                DatabaseMenu.getInstance().printMatches(getMatchesString());
                System.out.format("Which match is desired to be modified?%n");
                System.out.format("Select a match by entering a number or enter \"return\" to go to the data menu.%n");
            }
        } while ( !userValue.equals("return") );
    }
    /**
     * Given a match id, deletes all the statistics from it in the database.
     * @param matchId The match id from the match from which the statistics will be deleted.
     */
    private static void deleteStatisticsDB(int matchId)
    {
        ItemsDAO.getInstance().deleteItems(matchId);
        StatisticsDAO.getInstance().deleteStatistics(matchId);
    }
    /**
     * Given a match id, deletes it by removing the items, statistics, teams and teammates.
     * @param matchId The match id from the match that will be removed from the database.
     */
    private static void deleteMatchDB(int matchId)
    {
        deleteStatisticsDB(matchId);
        List<String> teams = MatchDAO.getInstance().selectTeams(matchId);
        List<String> teammates = new ArrayList<>();
        for ( String team : teams )
        {
            for ( String teammate : TeammateDAO.getInstance().selectNicknames(team) )
            {
                teammates.add(teammate);
            }
        }
        MatchDAO.getInstance().deleteMatch(matchId);
        for ( String team : teams )
        {
            TeamDAO.getInstance().deleteTeam(team);
        }
        for ( String teammate : teammates )
        {
            TeammateDAO.getInstance().deleteTeammate(teammate);
        }
    }
    /**
     * Asks the user to choose a match to be deleted and then prints all the registered matches.
     * If the user input is not the expected or if there are not matches registered, throws an exception.
     * If the user inputs "return", returns to the data menu.
     */
    private static void chooseToDeleteMatch()
    {
        int matches = MatchDAO.getInstance().selectMatches().size();
        DatabaseMenu.getInstance().printMatches(getMatchesString());
        System.out.format("Which match is desired to be removed?%n");
        System.out.format("Select a match by entering a number or enter \"return\" to go to the data menu.%n");
        String userValue;
        do {
            userValue = strictInput();
            if ( !userValue.equals("return") && !checkString(userValue) ) stadisticsFormatException();
            else if ( !userValue.equals("return") )
            {
                deleteMatchDB(Integer.parseInt(userValue));
                matches = MatchDAO.getInstance().selectMatches().size();
                if ( MatchDAO.getInstance().selectMatches().size() > 0 )
                {
                    DatabaseMenu.getInstance().printMatches(getMatchesString());
                    System.out.format("Which match is desired to be removed?%n");
                    System.out.format("Select a match by entering a number or enter \"return\" to go to the data menu.%n");
                }
            }
        } while ( !userValue.equals("return") && matches > 0 );
    }
    /**
     * Asks the user if wishes to create default matches,
     * then asks for how many matches is desired to register,
     * if the input is not what is expected, or if the user introduces
     * the negation input, it returns to the data menu.
     */
    public static void choseToImportMatches()
    {
        System.out.format("Are you sure you want to import matches? [y|n]%n");
        String userValue = strictInput();
        char option = userValue.charAt(0);
        if ( capInsensitive(userValue) ) booleanInsensitiveException(userValue);
        else if ( option == 'y' || option == 'Y' ) importMatches(importMatchesStrictInput());
        System.out.format("Returning to the data menu...%n");
    }
    /**
     * Prints two teams from a list in the screen.
     * @param teams The teams to be printed.
     * @param time The time that the match lasted.
     */
    private static void printMatch(List<List<String>> teams, int time)
    {
        String[] roles = {"Support", "ADC", "Mid", "Jungle", "Solo"};
        System.out.println("\nOrder team:");
        System.out.format("Team name: %s%n", teams.get(0).get(0));
        int i = 1;
        do
        {
            System.out.format("%s: %s%n", roles[i - 1], teams.get(0).get(i));
        } while ( ++ i < 6 );
        System.out.println();
        System.out.println("Chaos team:");
        System.out.format("Team name: %s%n", teams.get(1).get(0));
        i = 1;
        do
        {
            System.out.format("%s: %s%n", roles[i - 1], teams.get(1).get(i));
        } while ( ++ i < 6 );
        System.out.println();
        System.out.format("Match duration (hh:mm): %s%n%n", showMinutes(time));
    }
    /**
     * Asks the user to choose a match to be shown and shows all the registered matches.
     * If there is an unexpected input or if a match was shown, returns to the matches menu.
     */
    public static void selectMatch()
    {
        DatabaseMenu.getInstance().printMatches(getMatchesString());
        System.out.format("Which match is desired to be shown?%n");
        System.out.format("Select a match by entering a number or enter \"return\" to go to the matches menu.%n");
        String userValue = strictInput();
        if ( !userValue.equals("return") && !checkString(userValue) ) stadisticsFormatException();
        else if ( !userValue.equals("return") )
        {
            List<List<String>> teams = Arrays.asList(
                    TeamDAO.getInstance().selectTeam(Integer.parseInt(userValue), "orderteam"),
                    TeamDAO.getInstance().selectTeam(Integer.parseInt(userValue), "chaosteam")
            );
            printMatch(teams, MatchDAO.getInstance().selectTime(Integer.parseInt(userValue)));
        }
    }
    /**
     * Shows the statistics from the players in a given team name and match id.
     * @param lanes The lanes of the players which their statistics will be shown.
     * @param teamName The team name from the players which their statistics will be shown.
     * @param matchId The match id from the players which their statistics will be shown.
     */
    private static void showPlayerStatistics(String[] lanes, String teamName, int matchId)
    {
        System.out.println("Role  |  Name  |  Kills  |  Deaths  |  Assists");
        for (int i = 0 ; i < 5 ; i ++)
        {
            List<Object> statistics = StatisticsDAO.getInstance().selectStatistics(lanes[i], teamName, matchId);
            if ( statistics.size() == 4 ) System.out.format("%s:    %s    %s    %s    %s%n", lanes[i], statistics.get(0), statistics.get(1), statistics.get(2), statistics.get(3));
        }
    }
    /**
     * Shows five players of each of the two teams of a match
     * with their names, kills, deaths and assists.
     * @param matchId The match id form the match from which the players will be printed.
     */
    private static void showTeamPlayers(int matchId)
    {
        String[] lanes = {"support", "adc", "mid", "jungle", "solo"};
        List<String> teams = MatchDAO.getInstance().selectTeams(matchId);
        System.out.format("%nTeam order: %s%n", teams.get(0));
        showPlayerStatistics(lanes, teams.get(0), matchId);
        System.out.format("%nTeam chaos: %s%n", teams.get(1));
        showPlayerStatistics(lanes, teams.get(1), matchId);
        System.out.println();
    }
    /**
     * Asks the user to choose a match in order to show all players
     * of each team with their names, kills, deaths and assists.
     * If there is an unexpected input or if the players were shown, returns to the matches menu.
     */
    public static void selectPlayer()
    {
        DatabaseMenu.getInstance().printMatches(getMatchesString());
        System.out.format("Select a match to show both teams and its players.%n");
        System.out.format("Select a match by entering a number or enter \"return\" to go to the matches menu.%n");
        String userValue = strictInput();
        if ( !userValue.equals("return") && !checkString(userValue) ) stadisticsFormatException();
        else if ( !userValue.equals("return") )
        {
            showTeamPlayers(Integer.parseInt(userValue));
        }
    }
    /**
     * Checks if the first character of the parameter userValue is between 0 and 6.
     * @param userValue A string without whitespaces the user previously introduced.
     * @return Returns true if the first character is not between 0 and 6, returns false otherwise.
     */
    public static boolean scoreNotAllowed(String userValue)
    {
        if ( userValue.length() > 1 ) return true;
        char option = userValue.charAt(0);
        return option != '0' && option != '1' && option != '2' && option != '3' && option != '4' && option != '5' && option != '6';
    }
}
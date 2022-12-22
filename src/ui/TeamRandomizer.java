package ui;
import static misc.Exceptions.stadisticsFormatException;
import static misc.Exceptions.importMatchesLimitReachedException;
import static misc.Input.strictInput;
import static misc.Input.checkString;
/**
 * This class has a tool to create random teams.
 */
public class TeamRandomizer {
    private static TeamRandomizer teamRandomizer = new TeamRandomizer();
    /**
     * Gets the object teamRandomizer.
     * @return The object teamRandomizer to show contents in the team randomizer menu.
     */
    public static TeamRandomizer getInstance()
    {
        return teamRandomizer;
    }
    /**
     * Unique constructor for the object TeamRandomizer.
     */
    private TeamRandomizer()
    {}
    /**
     * Asks the user to select how many players are playing.
     */
    public void showTeamRandomizerMenu()
    {
        System.out.format("Introduce the total of players.%n");
        String userValue;
        userValue = strictInput();
        if ( !checkString(userValue) ) stadisticsFormatException();
        else if ( userValue.length() > 2 || Integer.parseInt(userValue) > 10 ) importMatchesLimitReachedException();
        else
        {
            introduceTeamValue(Integer.parseInt(userValue));
        }
    }
    /**
     * Asks the user to select how many players are in each team.
     * @param players Previous value chosen by the user referencing to the total players that are playing.
     */
    private void introduceTeamValue(int players)
    {
        System.out.format("Introduce how many players in a team.%n");
        int[] chances = teamChancesCalculation(players);
        String userValue;
        do{
            userValue = strictInput();
            if ( !checkString(userValue) ) stadisticsFormatException();
        } while ( !checkString(userValue) );
        if ( userValue.length() > 2 || !isATeam(Integer.parseInt(userValue), chances) ) teamRandomizer.userValueChancesException(chances, Integer.parseInt(userValue), players);
        else
        {
            introducePlayerNames(players / Integer.parseInt(userValue), players);
        }
    }
    /**
     * Asks the user to introduce an amount of player names
     * depending on the amount of player that are playing.
     * Then creates a vector of String with all names, and
     * another vector of integer filled with "-1" to hold random
     * values those from the players will be randomly shown in the end.
     * @param teams How many players are in each team.
     * @param players How many players are playing in total.
     */
    private void introducePlayerNames(int teams, int players)
    {
        System.out.format("Introduce %d player names please.%n", players);
        String[] playerNames = new String[players];
        String[] randomPlayerNames = new String[players];
        for (int i = 0 ; i < players ; i ++)
        {
            playerNames[i] = strictInput();
        }
        int[] randomValues = new int[players];
        for (int i = 0 ; i < randomValues.length ; i ++)
        {
            randomValues[i] = -1;
        }
        for (int i = 0 ; i < players ; i ++)
        {
            boolean loop = true;
            int random;
            do{
                random = (int) (Math.random() * players);
                if ( !randomExists(randomValues, random) )
                {
                    randomValues[i] = random;
                    loop = false;
                }
            } while ( loop );
            randomPlayerNames[i] = playerNames[random];
        }
        for (int i = 0, k = 0 ; i < teams ; i ++)
        {
            String ordinalTeam = "st";
            if ( i == 1 ) ordinalTeam = "nd";
            else if ( i == 2 ) ordinalTeam = "rd";
            else if ( i > 2 ) ordinalTeam = "th";
            for (int j = 0 ; j < (players / teams) ; j ++, k ++)
            {
                String ordinalPlayer = "st";
                if ( j == 1 ) ordinalPlayer = "nd";
                else if ( j == 2 ) ordinalPlayer = "rd";
                else if ( j > 2 ) ordinalPlayer = "th";
                System.out.format("The %d%s player in the %d%s team is %s%n", (j + 1), ordinalPlayer, (i + 1), ordinalTeam, randomPlayerNames[k]);
            }
        }
    }
    /**
     * Checks if a random value has appeared previously to not choose repeated values.
     * @param randomValues A vector of integer that holds previously random values.
     * @param random A current random value to be checked if it already exists.
     * @return Returns true if the value already exists, returns false otherwise.
     */
    private boolean randomExists(int[] randomValues, int random)
    {
        for ( int value : randomValues )
        {
            if ( value == random ) return true;
        }
        return false;
    }
    /**
     * Calculates how many equal teams can be created dividing each possible entry.
     * First creates the vector size, then fills it with the possible values.
     * @param players Total value of players that are playing.
     * @return A vector of integer holding all possible combinations of equal teams.
     */
    private int[] teamChancesCalculation(int players)
    {
        int chances = 0;
        for (int i = 2 ; i < players ; i ++)
        {
            if ( players % i == 0 ) chances ++;
        }
        int[] calculatedChances = new int[chances];
        for (int i = 2, index = 0 ; i < players ; i ++)
        {
            if ( players % i == 0 )
            {
                calculatedChances[index] = i;
                index ++;
            }
        }
        return calculatedChances;
    }
    /**
     * Checks if a team size allows for equal team sizes for every team or not.
     * @param players Value selected by the user referring to a team size.
     * @param chances A vector of integer with the possible team sizes.
     * @return Returns true if the user value matches any possible value, returns false otherwise.
     */
    private boolean isATeam(int players, int[] chances)
    {
        for (int value : chances)
        {
            if ( players == value ) return true;
        }
        return false;
    }
    /**
     * Awares the user of introducing a team size that is not allowed.
     * @param chances A vector of integer with the possible team sizes.
     * @param teamSize Team size selected by the user.
     * @param players The maximum players that are playing.
     */
    private void userValueChancesException(int[] chances, int teamSize, int players)
    {
        System.out.format("\u001B[31mError: it is not possible to create teams with a size of %d with a total of %d players\u001B[0m%n", teamSize, players);
        if ( chances.length == 0 ) System.out.format("\u001B[31mThere are no possible sizes%n\u001B[0m");
        else
        {
            System.out.format("\u001B[31mPossibles sizes of teams with %d players are \u001B[0m", players);
            for (int i = 0 ; i < (chances.length - 1) ; i ++)
            {
                System.out.format("\u001B[31m%d, \u001B[0m", chances[i]);
            }
            System.out.format("\u001B[31m%d \u001B[0m%n", chances[(chances.length - 1)]);
        }
    }
}
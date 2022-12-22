package ui;
import static misc.Exceptions.scoreMenuInputException;
import static misc.Exceptions.specificDataInputException;
import static misc.Input.scoreNotAllowed;
import static misc.Input.specificDataNotAllowed;
import static misc.Input.strictInput;
public class ScoreMenu {
    private static ScoreMenu scoreMenu = new ScoreMenu();
    /**
     * Gets the object scoreMenu.
     * @return The object scoreMenu to show contents in the score menu.
     */
    public static ScoreMenu getInstance()
    {
        return scoreMenu;
    }
    /**
     * Unique constructor for the object ScoreMenu.
     */
    private ScoreMenu()
    {}
    /**
     * Shows the available options the user has on the score menu.
     */
    private void scoreMenu()
    {
        System.out.format("You are currently on score menu.%n");
        System.out.format("Select an option by entering a number.%n");
        System.out.format("\t[1]\tShow team with the player with higher KDA%n");
        System.out.format("\t[2]\tShow average statistics%n");
        System.out.format("\t[3]\tShow match with the player with higher kills%n");
        System.out.format("\t[4]\tShow match with the player with higher assists%n");
        System.out.format("\t[5]\tShow match with the player with higher gold%n");
        System.out.format("\t[6]\tShow data of a specific match%n");
        System.out.format("\t[0]\tReturn%n");
    }
    /**
     * Allows the user to choose to show
     * general information about matches or
     * more specific information about a match.
     * If the input is 0, returns to the main menu.
     */
    public void showScoreMenu()
    {
        String userValue;
        char option;
        do {
            this.scoreMenu();
            userValue = strictInput();
            option = userValue.charAt(0);
            if ( scoreNotAllowed(userValue) ) scoreMenuInputException(userValue);
            else if ( option == '1' ) misc.ShowScore.showTopTeam();
            else if ( option == '2' ) misc.ShowScore.showAverageStatistics();
            else if ( option == '3' ) misc.ShowScore.showTopKillsMatch();
            else if ( option == '4' ) misc.ShowScore.showTopAssistsMatch();
            else if ( option == '5' ) misc.ShowScore.showTopGoldMatch();
            else if ( option == '6' ) this.specificMatchChoices();
        } while ( !(option == '0' && userValue.length() == 1) );
    }
    /**
     * Shows the available options the user has
     * when it comes to show specific data of a match.
     */
    private void specificDataMenu()
    {
        System.out.format("Choose the data to be shown.%n");
        System.out.format("Select an option by entering a number.%n");
        System.out.format("\t[1]\tTotal kills of a match%n");
        System.out.format("\t[2]\tTotal assists of a match%n");
        System.out.format("\t[3]\tAverage kills of a match%n");
        System.out.format("\t[4]\tAverage assists of a match%n");
        System.out.format("\t[5]\tTotal structure damage of a match%n");
        System.out.format("\t[6]\tTotal minion damage of a match%n");
        System.out.format("\t[7]\tTotal player damage of a match%n");
        System.out.format("\t[0]\tReturn%n");
    }
    /**
     * Allows the user to choose to show
     * any specific information about a match.
     */
    public void specificMatchChoices()
    {
        String userValue;
        char option;
        do {
            this.specificDataMenu();
            userValue = strictInput();
            option = userValue.charAt(0);
            if ( specificDataNotAllowed(userValue) ) specificDataInputException(userValue);
            else if ( option == '1' ) misc.ShowScore.showTotalKills();
            else if ( option == '2' ) misc.ShowScore.showTotalAssists();
            else if ( option == '3' ) misc.ShowScore.showAverageKills();
            else if ( option == '4' ) misc.ShowScore.showAverageAssists();
            else if ( option == '5' ) misc.ShowScore.showTotalStructureDamage();
            else if ( option == '6' ) misc.ShowScore.showTotalMinionDamage();
            else if ( option == '7' ) misc.ShowScore.showTotalPlayerDamage();
        } while ( !(option == '0' && userValue.length() == 1) );
    }
}
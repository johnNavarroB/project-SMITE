package ui;
import static misc.Exceptions.matchesMenuInputException;
import static misc.Input.matchesNotAllowed;
import static misc.Input.strictInput;
import static misc.Input.selectMatch;
import static misc.Input.selectPlayer;
/**
 * This class shows the menu that enables
 * screen printing of the existent matches,
 * teams and teammates in an human readable way.
 * If the user inputs zero, returns to the main menu.
 */
public class MatchesMenu {
    private static MatchesMenu matchesMenu = new MatchesMenu();
    /**
     * Gets the object matchesMenu.
     * @return The object matchesMenu to show contents in the matches menu.
     */
    public static MatchesMenu getInstance()
    {
        return matchesMenu;
    }
    /**
     * Unique constructor for the object MatchesMenu.
     */
    private MatchesMenu()
    {}
    /**
     * Shows the available options the user has on the matches menu.
     */
    private void matchesMenu()
    {
        System.out.format("You are currently on matches menu.%n");
        System.out.format("Select an option by entering a number.%n");
        System.out.format("\t[1]\tShow matches%n");
        System.out.format("\t[2]\tShow players%n");
        System.out.format("\t[0]\tReturn%n");
    }
    /**
     * Allows the user to choose to see information
     * related to matches, teams and players in detail.
     * If the input is 0, returns to the main menu.
     */
    public void showMatchesMenu()
    {
        String userValue;
        char option;
        do {
            this.matchesMenu();
            userValue = strictInput();
            option = userValue.charAt(0);
            if ( matchesNotAllowed(userValue) ) matchesMenuInputException(userValue);
            else if ( option == '1' ) selectMatch();
            else if ( option == '2' ) selectPlayer();
        } while ( !(option == '0' && userValue.length() == 1) );
    }
}
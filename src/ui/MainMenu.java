package ui;
import static ui.Main.mainMenu;
import static misc.Exceptions.mainMenuIntegerException;
import static misc.Input.mainMenuNotAllowed;
import static misc.Input.strictInput;
/**
 * This set of procedures eases link between all
 * the possible menus that the application has.
 * It shows the main menu and there is not any menu above it,
 * when user inputs zero the program ends.
 */
public class MainMenu {
    /**
     * Shows the available options the user has on the main menu.
     */
    private void mainMenu()
    {
        System.out.format("Welcome to \033[33mSMITE\u001B[0m eSports simulator.%n");
        System.out.format("Select an option by entering a number.%n");
        System.out.format("\t[1]\tAccess to ElephantSQL%n");
        System.out.format("\t[2]\tData%n");
        System.out.format("\t[3]\tMatches%n");
        System.out.format("\t[4]\tScore%n");
        System.out.format("\t[5]\tTeam randomizer%n");
        System.out.format("\t[0]\tExit%n");
    }
    /**
     * Shows the main menu in loop.
     * If the input is not between 0 and 4, throws an exception.
     * Also if the input is 0, finishes its execution.
     */
    public void showMainMenu()
    {
        String userValue;
        char option;
        do {
            mainMenu.mainMenu();
            userValue = strictInput();
            option = userValue.charAt(0);
            if ( mainMenuNotAllowed(userValue) ) mainMenuIntegerException(userValue);
            else if ( option == '1' ) DatabaseMenu.getInstance().accessToDatabase();
            else if ( option == '2' ) DataMenu.getInstance().showDataMenu();
            else if ( option == '3' ) MatchesMenu.getInstance().showMatchesMenu();
            else if ( option == '4' ) ScoreMenu.getInstance().showScoreMenu();
            else if ( option == '5' ) TeamRandomizer.getInstance().showTeamRandomizerMenu();
        } while ( !(option == '0' && userValue.length() == 1) );
    }
}
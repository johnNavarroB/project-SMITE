package ui;
import static misc.Exceptions.menuIntegerException;
import static misc.Input.notAllowed;
import static misc.Input.strictInput;
import static misc.Input.saveNewMatch;
import static misc.Input.alterExistentMatch;
import static misc.Input.deleteExistentMatch;
import static misc.Input.choseToImportMatches;
/**
 * This class shows a menu with the objective of
 * write and delete matches in a human readable way.
 * If the user inputs zero, returns to the main menu.
 */
public class DataMenu {
    private static DataMenu dataMenu = new DataMenu();
    /**
     * Gets the object dataMenu.
     * @return The object dataMenu to show contents in the data menu.
     */
    public static DataMenu getInstance()
    {
        return dataMenu;
    }
    /**
     * Unique constructor for the object DataMenu.
     */
    private DataMenu()
    {}
    /**
     * Shows the available options the user has on the data menu.
     */
    private void dataMenu()
    {
        System.out.format("You are currently on data menu.%n");
        System.out.format("Select an option by entering a number.%n");
        System.out.format("\t[1]\tRegister a new match%n");
        System.out.format("\t[2]\tModify an existent match%n");
        System.out.format("\t[3]\tDelete an existent match%n");
        System.out.format("\t[4]\tImport test matches%n");
        System.out.format("\t[0]\tReturn%n");
    }
    /**
     * Allows the user to choose between register a new match,
     * modify and delete an existent match if exists any
     * If the input is 0, returns to the main menu.
     */
    public void showDataMenu()
    {
        String userValue;
        char option;
        do {
            this.dataMenu();
            userValue = strictInput();
            option = userValue.charAt(0);
            if ( notAllowed(userValue) ) menuIntegerException(userValue);
            else if ( option == '1' ) saveNewMatch();
            else if ( option == '2' ) alterExistentMatch();
            else if ( option == '3' ) deleteExistentMatch();
            else if ( option == '4' ) choseToImportMatches();
        } while ( !(option == '0' && userValue.length() == 1) );
    }
}
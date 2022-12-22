package misc;
import database.dao.MatchDAO;
import static misc.Input.checkString;
/**
 * This class holds all methods related about let the user
 * know of undesirable use of the user input methods.
 */
public class Exceptions {
    /**
     * Awares the user of introducing a wrong input, either integer or string.
     * @param userValue A string without whitespaces the user previously introduced.
     */
    public static void menuIntegerException(String userValue)
    {
        if ( checkString(userValue) ) System.out.format("\u001B[31mNumber \"%s\" is not an available option [0-4]\u001B[0m%n", userValue);
        else System.out.format("\u001B[31mString \"%s\" is not an available option [0-4]\u001B[0m%n", userValue);
    }
    /**
     * Awares the user of introducing a wrong input, either integer or string.
     * @param userValue A string without whitespaces the user previously introduced.
     */
    public static void mainMenuIntegerException(String userValue)
    {
        if ( checkString(userValue) ) System.out.format("\u001B[31mNumber \"%s\" is not an available option [0-5]\u001B[0m%n", userValue);
        else System.out.format("\u001B[31mString \"%s\" is not an available option [0-5]\u001B[0m%n", userValue);
    }
    /**
     * Awares the user of introducing a wrong input.
     * @param userValue A string without whitespaces the user previously introduced.
     */
    public static void scoreMenuInputException(String userValue)
    {
        if ( checkString(userValue) ) System.out.format("\u001B[31mNumber \"%s\" is not an available option [0-6]\u001B[0m%n", userValue);
        else System.out.format("\u001B[31mString \"%s\" is not an available option [0-6]\u001B[0m%n", userValue);
    }
    /**
     * Awares the user of introducing a wrong input.
     * @param userValue A string without whitespaces the user previously introduced.
     */
    public static void matchesMenuInputException(String userValue)
    {
        if ( checkString(userValue) ) System.out.format("\u001B[31mNumber \"%s\" is not an available option [0-2]\u001B[0m%n", userValue);
        else System.out.format("\u001B[31mString \"%s\" is not an available option [0-2]\u001B[0m%n", userValue);
    }
    /**
     * Awares the user of introducing an input that its length is under or above the permitted.
     * @param id The user input related to the possible non-compliance of the designated properties.
     */
    public static void teammateIDOutOfBoundsException(String id)
    {
        if ( id.length() < 1 ) System.out.format("\u001B[31mUsername id \"%s\" is too short [1-16]\u001B[0m%n", id);
        else if ( id.length() > 16 ) System.out.format("\u001B[31mUsername id \"%s\" is too long [1-16]\u001B[0m%n", id);
        System.out.format("\u001B[31mTry again please\u001B[0m%n");
    }
    /**
     * Awares the user of introducing an input with white spaces.
     * @param id The user input related to the possible non-compliance of the designated properties.
     */
    public static void teammateIDWhiteSpacesException(String id)
    {
        System.out.format("\u001B[31mUsername id \"%s\" contains white spaces\u001B[0m%n", id);
        System.out.format("\u001B[31mTry again please\u001B[0m%n");
    }
    /**
     * Awares the user of introducing an input that its length is under or above the permitted.
     * @param item The user input related to the possible non-compliance of the designated properties.
     */
    public static void itemOutOfBoundsException(String item)
    {
        if ( item.length() < 3 ) System.out.format("\u001B[31mItem name \"%s\" is too short [1-32]\u001B[0m%n", item);
        else if ( item.length() > 32 ) System.out.format("\u001B[31mItem name \"%s\" is too long [1-32]\u001B[0m%n", item);
        System.out.format("\u001B[31mTry again please\u001B[0m%n");
    }
    /**
     * Awares the user of introducing an input that its length is under or above the permitted, when introducing a team name.
     * @param id The user input related to the possible non-compliance of the designated properties, its a future team name.
     */
    public static void teamNameOutOfBoundsException(String id)
    {
        if ( id.length() < 1 ) System.out.format("\u001B[31mThe team name \"%s\" is too short [1-16]\u001B[0m%n", id);
        else if ( id.length() > 16 ) System.out.format("\u001B[31mThe team name \"%s\" is too long [1-16]\u001B[0m%n", id);
        System.out.format("\u001B[31mTry again please\u001B[0m%n");
    }
    /**
     * Awares the user of introducing a team name starting with a white space.
     */
    public static void teamNameWhiteSpacesException()
    {
        System.out.format("\u001B[31mThe team name can not start with a white space\u001B[0m%n");
        System.out.format("\u001B[31mTry again please\u001B[0m%n");
    }
    /**
     * Awares the user of introducing a wrong input when a whole number is expected.
     */
    public static void stadisticsFormatException()
    {
        System.out.format("\u001B[31mError: current input method only accepts whole numbers\u001B[0m%n");
    }
    /**
     * Awares the user of introducing a value above the permitted.
     */
    public static void stadisticsLimitReachedException()
    {
        System.out.format("\u001B[31mError: input value limit reached, it can not exceed the limit of 9.999.999\u001B[0m%n");
    }
    /**
     * Awares the user of introducing a value above the permitted.
     */
    public static void minutesLimitReachedException()
    {
        System.out.format("\u001B[31mError: input value limit reached, it can not exceed the limit of 1.440\u001B[0m%n");
    }
    /**
     * Awares the user that there are not matches registered.
     */
    public static void seasonMatchesOutOfBoundsException()
    {
        System.out.format("\u001B[31mError: there are no matches registered\u001B[0m%n");
        System.out.format("\u001B[31mRegister a match at the data menu [1]\u001B[0m%n");
    }
    /**
     * Awares the user of introducing a value above the limit range.
     */
    public static void alterMatchInputOutOfBoundsException()
    {
        System.out.format("\u001B[31mError: the value selected is above the limit (%d)\u001B[0m%n", MatchDAO.getInstance().selectMatches().size());
        System.out.format("\u001B[31mChoose a registered match inside the range of index\u001B[0m%n");
    }
    /**
     * Awares the user of introducing a non-permitted value when there are two options.
     * @param userValue A String introduced by the user without any white spaces.
     */
    public static void booleanInsensitiveException(String userValue)
    {
        if ( checkString(userValue) ) System.out.format("\u001B[31mNumber \"%s\" is not an available option [y|n]|[Y|N]\u001B[0m%n", userValue);
        else System.out.format("\u001B[31mString \"%s\" is not an available option [y|n]|[Y|N]\u001B[0m%n", userValue);
    }
    /**
     * Awares the user of introducing a value above the permitted.
     */
    public static void importMatchesLimitReachedException()
    {
        System.out.format("\u001B[31mError: input value limit reached, it can not exceed the limit of 10\u001B[0m%n");
    }
    /**
     * Awares the user of introducing a wrong input.
     * @param userValue A string without whitespaces the user previously introduced.
     */
    public static void specificDataInputException(String userValue)
    {
        if ( checkString(userValue) ) System.out.format("\u001B[31mNumber \"%s\" is not an available option [0-7]\u001B[0m%n", userValue);
        else System.out.format("\u001B[31mString \"%s\" is not an available option [0-7]\u001B[0m%n", userValue);
    }
    /**
     * Prints an error message.
     * @param error Message to be printed.
     */
    public static void printError(String error)
    {
        System.out.format("\u001B[31m%s\u001B[0m", error);
    }
}
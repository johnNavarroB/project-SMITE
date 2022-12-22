package ui;
import java.util.Scanner;
import java.util.Locale;
import database.Database;
/**
 * This is the core of the execution,
 * it runs the main menu and executes
 * it again if the input is not zero.
 */
public class Main {
    public static Scanner scanner = new Scanner(System.in).useLocale(Locale.US);
    public static MainMenu mainMenu = new MainMenu();
    public static void main(String[] args)
    {
        Database.getInstance().connect();
        mainMenu.showMainMenu();
        Database.getInstance().close();
    }
}
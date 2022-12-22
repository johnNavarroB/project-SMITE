package ui;
import static misc.Exceptions.matchesMenuInputException;
import static misc.Exceptions.menuIntegerException;
import static misc.Input.matchesNotAllowed;
import static misc.Input.notAllowed;
import static misc.Input.strictInput;
import misc.Exceptions;
import java.util.List;
import database.dao.DataDictDAO;
/**
 * This class holds the methods related to
 * allowing to access to ElephantSQL database
 * to insert and retrieve data from it.
 */
public class DatabaseMenu {
    private static DatabaseMenu databaseMenu = new DatabaseMenu();
    /**
     * Gets the databaseMenu object.
     * @return The menu databaseMenu.
     */
    public static DatabaseMenu getInstance()
    {
        return databaseMenu;
    }
    /**
     * Unique constructor for DatabaseMenu.
     */
    private DatabaseMenu()
    {}
    /**
     * Shows the available options to access, retrieve and update the database.
     */
    private void databaseMenu()
    {
        System.out.format("You are currently on ElephantSQL.%n");
        System.out.format("Select an option by entering a number.%n");
        System.out.format("\t[1]\tManage tables%n");
        System.out.format("\t[2]\tSelect from a table%n");
        System.out.format("\t[0]\tReturn%n");
    }
    /**
     * Allows the user to go to the manage tables menu and to select from a table.
     */
    public void accessToDatabase()
    {
        String userValue;
        char option;
        do {
            this.databaseMenu();
            userValue = strictInput();
            option = userValue.charAt(0);
            if ( matchesNotAllowed(userValue) ) matchesMenuInputException(userValue);
            else if ( option == '1' ) this.manageTables();
            else if ( option == '2' ) this.selectFromATable();
        } while ( !(option == '0' && userValue.length() == 1) );
    }
    /**
     * Prints a list of all the matches of the database.
     * @param matches The list of matches to be printed.
     */
    public void printMatches(List<String> matches)
    {
        if ( matches.size() >= 1 )
        {
            System.out.println("\nThe current registered matches are:\n");
            int i = 0;
            while ( ++ i <= matches.size() )
            {
                System.out.println(matches.get( i - 1 ));
            }
            System.out.println();
        }
    }
    /**
     * Prints a list of all the tables of the database.
     * @param tables The list of tables to be printed.
     */
    private void printTables(List<String> tables)
    {
        if ( tables.size() >= 1 )
        {
            System.out.println("\nThe current registered tables are:\n");
            int i = 0;
            while ( ++ i <= tables.size() )
            {
                System.out.println(i + ". " + tables.get( i - 1 ));
            }
            System.out.println();
        }
    }
    /**
     * Shows the registers returned from a query.
     * @param registers The registers to be printed.
     */
    private void printRegisters(List<String> registers)
    {
        if ( registers.size() >= 1 )
        {
            if ( registers.size() > 1 ) System.out.println("\nThe registers returned from the query are:\n");
            else System.out.println("\nThe register returned from the query is:\n");
            int i = 0;
            while ( ++ i <= registers.size() )
            {
                System.out.println(i + ". " + registers.get( i - 1 ));
            }
            System.out.println();
        }
    }
    /**
     * Shows the available columns to choose from a table.
     * @param columns The columns to be printed.
     * @param table The table from which the columns will be printed.
     */
    private void printColumns(List<String> columns, String table)
    {
        if ( columns.size() >= 1 )
        {
            if ( columns.size() > 1 ) System.out.format("%nThe columns available for the table \"%s\" are:%n%n", table);
            else System.out.format("%nThe column available for the table \"%s\" is:%n%n", table);
            int i = 0;
            while ( ++ i <= columns.size() )
            {
                System.out.println(i + ". " + columns.get( i - 1 ));
            }
            System.out.println();
        }
    }
    /**
     * Allows the user to select a register from a table printing the available options in each case and allowing a where clause.
     */
    private void selectFromATable()
    {
        List<String> registers = DataDictDAO.getInstance().describeTables();
        if ( registers.size() > 0 )
        {
            this.printTables(registers);
            System.out.println("Introduce the table from which is desired to select the registers");
            String table = Main.scanner.next();
            List<String> columns = DataDictDAO.getInstance().selectColumnNameFromATable(table);
            if ( columns.size() > 0 )
            {
                this.printColumns(columns, table);
                System.out.println("Introduce the column from which is desired to select the registers");
                String column = Main.scanner.next();
                System.out.println("Do you want to introduce a where clause? [y|n]");
                String where = Main.scanner.next();
                if ( where.equals("y") || where.equals("Y") )
                {
                    System.out.println("Introduce the where clause which is desired to be applied");
                    Main.scanner.nextLine();
                    where = Main.scanner.nextLine();
                    this.printRegisters(DataDictDAO.getInstance().selectAllFromATable(table, column, where));
                } else if ( !where.equals("n") && !where.equals("N") ) Exceptions.printError("\nInvalid option, regex ([yY]|[nN])\n\n");
                else this.printRegisters(DataDictDAO.getInstance().selectAllFromATable(table, column, null));
            } else Exceptions.printError(String.format("%nThe table \"%s\" was not found%n%n", table));
        }
    }
    /**
     * Shows the available options to show all the tables, create or drop all the tables and insert test data.
     */
    private void manageTablesMenu()
    {
        System.out.format("You are currently on ElephantSQL manage tables submenu.%n");
        System.out.format("Select an option by entering a number.%n");
        System.out.format("\t[1]\tShow all tables%n");
        System.out.format("\t[2]\tCreate all tables%n");
        System.out.format("\t[3]\tDrop all tables%n");
        System.out.format("\t[4]\tInsert test data%n");
        System.out.format("\t[0]\tReturn%n");
    }
    /**
     * Allows the user to see the tables, create or drop all the tables and insert test data.
     */
    private void manageTables()
    {
        String userValue;
        char option;
        do {
            this.manageTablesMenu();
            userValue = strictInput();
            option = userValue.charAt(0);
            if ( notAllowed(userValue) ) menuIntegerException(userValue);
            else if ( option == '1' ) this.printTables(DataDictDAO.getInstance().describeTables());
            else if ( option == '2' ) DataDictDAO.getInstance().createTables();
            else if ( option == '3' ) DataDictDAO.getInstance().dropTables();
            else if ( option == '4' ) DataDictDAO.getInstance().insertIntoAllTables();
        } while ( !(option == '0' && userValue.length() == 1) );
    }
}
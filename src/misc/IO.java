package misc;
import data.Match;
import data.Team;
import data.Teammate;
import data.Statistics;
import database.dao.MatchDAO;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Scanner;
import java.util.Locale;
import static ui.Main.scanner;
import static misc.Exceptions.booleanInsensitiveException;
/**
 * This class holds all the methods related to the load and save of the data of the season.
 */
public class IO {
    public static IO io = new IO(Path.of("./data/save.txt"));
    private final Path dataFile;
    /**
     * IO can not be initialized.
     */
    private IO(Path data)
    {
        this.dataFile = data;
    }
    /**
     * Obtains the object Path where the save file is located.
     * @return Object Path with the information about the logical path of the save file.
     */
    private Path getData()
    {
        return this.dataFile;
    }
    /**
     * Is the main method to start the procedure of writing data of the current execution to a file.
     * Always asks the user to decide if the data will be saved.
     */
    public void saveData()
    {
        if ( !Files.exists(io.getData()) ) createSaveDirectory();
        if ( MatchDAO.getInstance().selectMatches().size() == 0 ) System.out.format("\u001B[31mIO: there are no matches in the database, no save has been made.\u001B[0m%n");
        else if ( askIfSaveData() )
        {
            saveSeason();
            System.out.format("IO: saving successful%n");
        }
    }
    /**
     * Calls a method to check if the ./data/save.txt file and parent directory exist, creates them if not,
     * then gets the data from the file to create the written matches in the season.
     */
    public void loadData()
    {
        if ( !Files.exists(io.getData()) ) createSaveDirectory();
        System.out.format("IO: loading matches data...%n");
        Match[] matches = null;
        try
        {
            Scanner data = new Scanner(io.getData()).useLocale(Locale.US);
            while (data.hasNext())
            {
                Team order = readTeam(data);
                Team chaos = readTeam(data);
                int minutes = data.nextInt();
                data.nextLine();
                Match match = new Match(order, chaos, minutes);
                matches = addMatch(matches, match);
            }
        } catch ( IOException exception )
        {
            System.out.format("\u001B[31mI/O object Scanner initialization operation interrupted. Error: %s\u001B[0m%n", exception);
        }
        for ( Match match : matches )
        {
            Input.saveNewMatch(match);
        }
    }
    /**
     * Gets the name of a team from a file and the information of five teammates.
     * @param data Object Scanner with the path to the file where the information is read.
     * @return An object Team created from the information recovered from a data file.
     */
    private Team readTeam(Scanner data)
    {
        String teamName = data.nextLine();
        Teammate[] teammates = readTeammates(data);
        data.nextLine();
        return new Team(teamName, teammates);
    }
    /**
     * Gets the nickname and statistics from five teammates from a file and returns a vector of object Teammate.
     * @param data Object Scanner with the path to the file where the information is read.
     * @return A vector of object Teammate created from the information recovered from a data file.
     */
    private Teammate[] readTeammates(Scanner data)
    {
        Teammate[] teammates = new Teammate[5];
        for (int i = 0 ; i < 5 ; i ++)
        {
            String nickname = data.next();
            data.nextLine();
            String[] items = new String[6];
            for (int j = 0 ; j < items.length ; j ++)
            {
                String item = data.nextLine();
                item = removeSpacesFromItem(item);
                items[j] = item;
            }
            int gold = data.nextInt();
            int kills = data.nextInt();
            int deaths = data.nextInt();
            int assists = data.nextInt();
            int structureDamage = data.nextInt();
            int minionDamage = data.nextInt();
            int playerDamage = data.nextInt();
            Statistics statistics = new Statistics(items, gold, kills, deaths, assists, structureDamage, minionDamage, playerDamage);
            Teammate teammate = new Teammate(nickname, statistics);
            teammates[i] = teammate;
        }
        return teammates;
    }
    /**
     * Removes exactly eight characters at the beginning of an object String
     * to prevent the accumulation of spaces generated at the beginning of items when loading data
     * @param item The String from which the spaces are to be cleared.
     * @return A String minus eight characters in the beginning.
     */
    private String removeSpacesFromItem(String item)
    {
        String itemWithoutSpaces = "";
        for (int i = 0 ; i < item.length() - 8 ; i ++)
        {
            itemWithoutSpaces += item.charAt( i + 8 );
        }
        return itemWithoutSpaces;
    }
    /**
     * Creates a new vector of object Match with the same values as
     * the one in the parameter matches with additional length,
     * then adds the parameter match in the end of the vector.
     * It changes its behaviour if the vector of object Match
     * from the parameter matches is null, and initializes it with length one.
     * @param matches Vector of object Match which is desired to add a new match to.
     * @param match Object Match that is desired to be added to the vector of object Match in the parameter matches.
     * @return A vector of object Match with the same values of the one in the parameter matches with a new object Match added at the end.
     */
    private static Match[] addMatch(Match[] matches, Match match)
    {
        Match[] newMatches = new Match[1];
        if ( matches == null )
        {
            matches = new Match[1];
        } else
        {
            newMatches = new Match[matches.length + 1];
            for (int i = 0 ; i < matches.length ; i ++)
            {
                newMatches[i] = matches[i];
            }
        }
        newMatches[newMatches.length - 1] = match;
        return newMatches;
    }
    /**
     * Asks the user if is willingly to save data, removing the current existing data file to create a new one.
     * @return returns true if the user input is [y|Y], returns false if the user input is [n|N].
     */
    private boolean askIfSaveData()
    {
        System.out.format("Do you want to save data? [y|n]%n");
        while (true)
        {
            String answer = scanner.next();
            switch(answer)
            {
                case"y":
                case"Y":
                    return true;
                case"n":
                case"N":
                    return false;
                default:
                    booleanInsensitiveException(answer);
            }
        }
    }
    /**
     * Awares the user about the beginning of the saving of data of the current session to the ./data/save.txt file.
     * Deletes the current data file and creates a new one, then begins writing each object match from the object season in a file.
     */
    private void saveSeason()
    {
        System.out.format("IO: saving matches data...%n");
        updateSaveFile();
        Match[] matches = new Match[1];
        for (Match match : matches)
        {
            saveTeam(match.getOrder());
            saveTeam(match.getChaos());
            try
            {
                Files.writeString(io.getData(), match.getMinutes()+"\n", StandardOpenOption.APPEND);
            } catch ( IOException exception )
            {
                System.out.format("\u001B[31mI/O season writting operation interrupted. Error: %s\u001B[0m%n", exception);
            }
        }
    }
    /**
     * Writes the team name and begins writing each object Teammate in a team, also controls the exception.
     * @param team object Team to be written into a file.
     */
    private void saveTeam(Team team)
    {
        try
        {
            Files.writeString(io.getData(), team.getTeamName()+"\n", StandardOpenOption.APPEND);
            for (Teammate teammate : team.getTeammates())
            {
                saveTeammate(teammate);
            }
        } catch ( IOException exception )
        {
            System.out.format("\u001B[31mI/O \"%s\" team writting operation interrupted. Error: %s\u001B[0m%n", team.getTeamName(), exception);
        }
    }
    /**
     * Writes the object Teammate's id, then begins writing the object Statistics, also controls the exception.
     * @param teammate object Teammate to be written into a file.
     */
    private void saveTeammate(Teammate teammate)
    {
        try
        {
            Files.writeString(io.getData(), "    "+teammate.getId()+"\n", StandardOpenOption.APPEND);
            saveStatistics(teammate.getStatistics());
        } catch ( IOException exception )
        {
            System.out.format("\u001B[31mI/O \"%s\" teammate writting operation interrupted. Error: %s\u001B[0m%n", teammate.getId(), exception);
        }
    }
    /**
     * Writes each String from the teammate's objects as a full line, then writes all the statistics, also controls the exception.
     * @param statistics object statistics to be written into a file.
     */
    private void saveStatistics(Statistics statistics)
    {
        for (String item : statistics.getItems())
        {
            try
            {
                Files.writeString(io.getData(), "        "+item+"\n", StandardOpenOption.APPEND);
            } catch ( IOException exception )
            {
                System.out.format("\u001B[31mI/O \"%s\" item writting operation interrupted. Error: %s\u001B[0m%n", item, exception);
            }
        }
        try
        {
            Files.writeString(io.getData(), "        "+statistics.getGold()+" ", StandardOpenOption.APPEND);
            Files.writeString(io.getData(), statistics.getKills()+" ", StandardOpenOption.APPEND);
            Files.writeString(io.getData(), statistics.getDeaths()+" ", StandardOpenOption.APPEND);
            Files.writeString(io.getData(), statistics.getAssists()+" ", StandardOpenOption.APPEND);
            Files.writeString(io.getData(), statistics.getStructureDamage()+" ", StandardOpenOption.APPEND);
            Files.writeString(io.getData(), statistics.getMinionDamage()+" ", StandardOpenOption.APPEND);
            Files.writeString(io.getData(), statistics.getPlayerDamage()+"\n", StandardOpenOption.APPEND);
        } catch ( IOException exception )
        {
            System.out.format("\u001B[31mI/O statistics writting operation interrupted. Error: %s\u001B[0m%n", exception);
        }
    }
    /**
     * Checks if the folder ./data exists, creates a new one if not.
     * Also creates a new ./data/save.txt file if does not exist.
     */
    private void createSaveDirectory()
    {
        if ( !Files.exists(io.getData().getParent()) )
        {
            try
            {
                Files.createDirectory(io.getData().getParent());
                System.out.format("IO: data folder does not exist, creating a new data folder...%n");
            } catch ( IOException exception )
            {
                System.out.format("\u001B[31mI/O data folder creation operation interrupted. Error: %s\u001B[0m%n", exception);
            }
        }
        if ( !Files.exists(io.getData()) )
        {
            try
            {
                Files.createFile(io.getData());
                System.out.format("IO: data file does not exist, creating a new file folder...%n");
            } catch ( IOException exception )
            {
                System.out.format("\u001B[31mI/O data file creation operation interrupted. Error: %s\u001B[0m%n", exception);
            }
        }
    }
    /**
     * Deletes the ./data/save.txt file if exists and creates a new one, creates a new one if not.
     * This method exists to prevent duplication of data at the save file when its being saved to.
     */
    private void updateSaveFile()
    {
        if ( Files.exists(io.getData()) )
        {
            try
            {
                Files.delete(io.getData());
                System.out.format("IO: updating data file...%n");
            } catch ( IOException exception )
            {
                System.out.format("\u001B[31mI/O data file removing operation interrupted. Error: %s\u001B[0m%n", exception);
            }
        }
        if ( !Files.exists(io.getData()) )
        {
            try
            {
                Files.createFile(io.getData());
            } catch ( IOException exception )
            {
                System.out.format("\u001B[31mI/O data file creation operation interrupted. Error: %s\u001B[0m%n", exception);
            }
        }
    }
}
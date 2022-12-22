package database.dao;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import database.Database;
import misc.Exceptions;
/**
 * This class holds all the related methods to create the tables needed
 * and to drop them both in the correct order, to show all the tables and
 * insert test registers.
 */
public class DataDictDAO extends DAO{
    private static DataDictDAO dataDictDAO = new DataDictDAO();
    /**
     * Gets the object dataDictDAO.
     * @return The object dataDictDAO to modify the database.
     */
    public static DataDictDAO getInstance()
    {
        return dataDictDAO;
    }
    /**
     * Unique constructor for the object DataDictDAO, it only instantiates the object.
     */
    public DataDictDAO()
    {}
    /**
     * Selects all the tables from the schema "public" from the database ignoring the table "pg_stat_statements".
     * @return A List containing all the tables registered on the database.
     */
    public List<String> describeTables()
    {
        List<String> tables = new ArrayList<>();
        String dtQuery = "select table_name from information_schema.tables where table_schema like 'public';";
        try
        {
            System.out.println("DataDictDAO: initializing process of accessing data through query...");
            ResultSet result = super.initializeResultSet(dtQuery);
            System.out.println("DataDictDAO: query data was accessed successfully");
            while ( result.next() )
            {
                String table = result.getString("table_name");
                if ( !table.equals("pg_stat_statements") ) tables.add(table);
            }
            if ( tables.size() < 1 ) Exceptions.printError("Error: there are not tables registered\n\n");
            return tables;
        } catch ( SQLException exception )
        {
            super.throwException(exception, dtQuery);
        }
        return tables;
    }
    /**
     * Selects the columns from the parameter table, returns them as a list and prints an exception if any.
     * @param table The table from which is desired to obtain its columns.
     * @return A list of String with all the columns of the table in it.
     */
    public List<String> selectColumnNameFromATable(String table)
    {
        List<String> columns = new ArrayList<>();
        String columnsQuery = String.format("select column_name from information_schema.columns where table_schema like 'public' and table_name like '%s';", table);
        try
        {
            System.out.println("DataDictDAO: initializing process of accessing data through query...");
            ResultSet result = super.initializeResultSet(columnsQuery);
            System.out.println("DataDictDAO: query data was accessed successfully");
            while ( result.next() )
            {
                String column = result.getString("column_name");
                if ( !table.equals("pg_stat_statements") ) columns.add(column);
            }
            return columns;
        } catch ( SQLException exception )
        {
            super.throwException(exception, columnsQuery);
        }
        return columns;
    }
    /**
     * Creates a table and prompts an exception if an error occurred.
     * @param query The query that creates a table on the database.
     */
    private void createTable(String query)
    {
        try
        {
            System.out.println("DataDictDAO: creating a table...");
            super.prepareStatement(query);
            System.out.format("DataDictDAO: the table \"%s\" was created successfully%n", query.split(" ")[2]);
        } catch ( SQLException exception )
        {
            super.throwException(exception, query);
        }
    }
    /**
     * Creates the table teammate.
     */
    private void createTableTeammate()
    {
        String createTable = "create table teammate ( ";
        String column = "nickname varchar(32), ";
        String constraint = "constraint pktmnn primary key (nickname) );";
        this.createTable( createTable + column + constraint );
    }
    /**
     * Creates the table team.
     */
    private void createTableTeam()
    {
        String createTable = "create table team ( ";
        String column1 = "teamname varchar(32), ";
        String column2 = "support varchar(32), ";
        String column3 = "adc varchar(32), ";
        String column4 = "mid varchar(32), ";
        String column5 = "jungle varchar(32), ";
        String column6 = "solo varchar(32), ";
        String constraint1 = "constraint pkttn primary key (teamname), ";
        String constraint2 = "constraint fktsp foreign key (support) references teammate (nickname) on delete restrict on update cascade, ";
        String constraint3 = "constraint fkta foreign key (adc) references teammate (nickname) on delete restrict on update cascade, ";
        String constraint4 = "constraint fktm foreign key (mid) references teammate (nickname) on delete restrict on update cascade, ";
        String constraint5 = "constraint fktj foreign key (jungle) references teammate (nickname) on delete restrict on update cascade, ";
        String constraint6 = "constraint fkts foreign key (solo) references teammate (nickname) on delete restrict on update cascade );";
        this.createTable( createTable + column1 + column2 + column3 + column4 + column5 + column6
                 + constraint1 + constraint2 + constraint3 + constraint4 + constraint5 + constraint6 );
    }
    /**
     * Creates the table match.
     */
    private void createTableMatch()
    {
        String createTable = "create table match ( ";
        String column1 = "matchid serial, ";
        String column2 = "orderteam varchar(32), ";
        String column3 = "chaosteam varchar(32), ";
        String column4 = "minutes numeric(16, 0), ";
        String constraint1 = "constraint pkmmi primary key (matchid), ";
        String constraint2 = "constraint fkmot foreign key (orderteam) references team (teamname) on delete restrict on update cascade, ";
        String constraint3 = "constraint fkmct foreign key (chaosteam) references team (teamname) on delete restrict on update cascade );";
        this.createTable( createTable + column1 + column2 + column3 + column4
                                       + constraint1 + constraint2 + constraint3 );
    }
    /**
     * Creates the table statistics.
     */
    private void createTableStatistics()
    {
        String createTable = "create table statistics ( ";
        String column1 = "statisticsid serial, ";
        String column2 = "player varchar(32), ";
        String column3 = "match integer, ";
        String column4 = "gold numeric(16, 0), ";
        String column5 = "kills numeric(16, 0), ";
        String column6 = "deaths numeric(16, 0), ";
        String column7 = "assists numeric(16, 0), ";
        String column8 = "structuredmg numeric(16, 0), ";
        String column9 = "miniondmg numeric(16, 0), ";
        String column10 = "playerdmg numeric(16, 0), ";
        String constraint1 = "constraint pkssi primary key (statisticsid), ";
        String constraint2 = "constraint fksp foreign key (player) references teammate (nickname) on delete restrict on update cascade, ";
        String constraint3 = "constraint fksm foreign key (match) references match (matchid) on delete restrict on update cascade );";
        this.createTable( createTable + column1 + column2 + column3 + column4 + column5 + column6
                  + column7 + column8 + column9 + column10 + constraint1 + constraint2 + constraint3 );
    }
    /**
     * Creates the table items.
     */
    private void createTableItems()
    {
        String createTable = "create table items ( ";
        String column1 = "itemsid serial, ";
        String column2 = "statistics integer, ";
        String column3 = "first_item varchar(32), ";
        String column4 = "second_item varchar(32), ";
        String column5 = "third_item varchar(32), ";
        String column6 = "fourth_item varchar(32), ";
        String column7 = "fifth_item varchar(32), ";
        String column8 = "sixth_item varchar(32), ";
        String constraint1 = "constraint pkii primary key (itemsid),  ";
        String constraint2 = "constraint fkis foreign key (statistics) references statistics (statisticsid) on delete restrict on update cascade );";
        this.createTable( createTable + column1 + column2 + column3 + column4 + column5 + column6
                                                     + column7 + column8 + constraint1 + constraint2 );
    }
    /**
     * Creates all the tables in the correct order to maintain the data consistency.
     */
    public void createTables()
    {
        this.createTableTeammate();
        this.createTableTeam();
        this.createTableMatch();
        this.createTableStatistics();
        this.createTableItems();
    }
    /**
     * Drops a table and prompts an exception if an error occurred.
     * @param table The name of the table to be dropped from the database.
     */
    private void dropTable(String table)
    {
        String query = String.format("drop table %s;", table);
        try
        {
            System.out.println("DataDictDAO: dropping a table...");
            super.prepareStatement(query);
            System.out.format("DataDictDAO: the table \"%s\" was dropped successfully%n", table);
        } catch ( SQLException exception )
        {
            super.throwException(exception, query);
        }
    }
    /**
     * Drops all the tables in the correct order to maintain the data consistency.
     */
    public void dropTables()
    {
        this.dropTable("items");
        this.dropTable("statistics");
        this.dropTable("match");
        this.dropTable("team");
        this.dropTable("teammate");
    }
    /**
     * Inserts the test registers on the table teammate.
     */
    private void insertIntoTeammate()
    {
        String query = "insert into teammate (nickname) values (?);";
        String[] values = { "TBone", "Jackhammer", "Aqua", "Grim", "Cannonball", "Speed", "Wiggles", "Captain", "Flame", "Lucky" };
        try
        {
            System.out.println("DataDictDAO: inserting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(query);
            for ( String value : values )
            {
                statement.setString(1, value);
                statement.execute();
            }
            System.out.println("DataDictDAO: the inserts were successfully committed to the table teammate");
        } catch ( SQLException exception )
        {
            super.throwException(exception, query);
        } finally
        {
            try
            {
                super.prepareStatement("commit;");
            } catch ( SQLException exception )
            {
                super.throwException(exception, query);
            }
        }
    }
    /**
     * Inserts the test registers on the table team.
     */
    private void insertIntoTeam()
    {
        String query = "insert into team (teamname, support, adc, mid, jungle, solo) values (?, ?, ?, ?, ?, ?);";
        String[][] values = {
        new String[]{"Controlling Pidgeons", "TBone", "Jackhammer", "Aqua", "Grim", "Cannonball" },
        new String[]{"Forgetful Hotspurs", "Speed", "Wiggles", "Captain", "Flame", "Lucky" }};
        try
        {
            System.out.println("DataDictDAO: inserting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(query);
            for ( String[] team : values )
            {
                statement.setString(1, team[0]);
                statement.setString(2, team[1]);
                statement.setString(3, team[2]);
                statement.setString(4, team[3]);
                statement.setString(5, team[4]);
                statement.setString(6, team[5]);
                statement.execute();
            }
            System.out.println("DataDictDAO: the inserts were successfully committed to the table team");
        } catch ( SQLException exception )
        {
            super.throwException(exception, query);
        } finally
        {
            try
            {
                super.prepareStatement("commit;");
            } catch ( SQLException exception )
            {
                super.throwException(exception, query);
            }
        }
    }
    /**
     * Inserts the test registers on the table match.
     */
    private void insertIntoMatch()
    {
        String query = "insert into match (orderteam, chaosteam, minutes) values (?, ?, ?);";
        try
        {
            System.out.println("DataDictDAO: inserting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(query);
            statement.setString(1, "Controlling Pidgeons");
            statement.setString(2, "Forgetful Hotspurs");
            statement.setInt(3, 61);
            statement.execute();
            System.out.println("DataDictDAO: the inserts were successfully committed to the table match");
        } catch ( SQLException exception )
        {
            super.throwException(exception, query);
        } finally
        {
            try
            {
                super.prepareStatement("commit;");
            } catch ( SQLException exception )
            {
                super.throwException(exception, query);
            }
        }
    }
    /**
     * Inserts the test registers on the table statistics.
     */
    private void insertIntoStatistics()
    {
        String query = "insert into statistics (player, match, gold, kills, deaths, assists, structuredmg, miniondmg, playerdmg) values (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        Object[][] data =
        {
                {"TBone", 1, 20935, 10, 5, 20, 271, 74751, 32049},
                {"Jackhammer", 1, 17491, 2, 8, 14, 1975, 96444, 24784},
                {"Aqua", 1, 20448, 10, 7, 11, 2016, 153943, 40171},
                {"Grim", 1, 23779, 17, 4, 15, 3358, 196313, 56929},
                {"Cannonball", 1, 18379, 9, 21, 7, 3559, 73739, 45480},
                {"Speed", 1, 18637, 2, 5, 18, 291, 53165, 23449},
                {"Wiggles", 1, 20666, 13, 14, 4, 2051, 148625, 32737},
                {"Captain", 1, 19243, 5, 13, 4, 2674, 145441, 20429},
                {"Flame", 1, 19597, 6, 13, 9, 1314, 121259, 14958},
                {"Lucky", 1, 24178, 17, 3, 9, 2406, 137411, 41788}
        };
        try
        {
            System.out.println("DataDictDAO: inserting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(query);
            for ( Object[] arguments : data )
            {
                statement.setString(1, (String) arguments[0]);
                statement.setInt(2, (Integer) arguments[1]);
                statement.setInt(3, (Integer) arguments[2]);
                statement.setInt(4, (Integer) arguments[3]);
                statement.setInt(5, (Integer) arguments[4]);
                statement.setInt(6, (Integer) arguments[5]);
                statement.setInt(7, (Integer) arguments[6]);
                statement.setInt(8, (Integer) arguments[7]);
                statement.setInt(9, (Integer) arguments[8]);
                statement.execute();
            }
            System.out.println("DataDictDAO: the inserts were successfully committed to the table statistics");
        } catch ( SQLException exception )
        {
            super.throwException(exception, query);
        } finally
        {
            try
            {
                super.prepareStatement("commit;");
            } catch ( SQLException exception )
            {
                super.throwException(exception, query);
            }
        }
    }
    /**
     * Inserts the test registers on the table items.
     */
    private void insertIntoItems()
    {
        String query = "insert into items (statistics, first_item, second_item, third_item, fourth_item, fifth_item, sixth_item) values (?, ?, ?, ?, ?, ?, ?);";
        Object[][] data =
                {
                        {1, "Mystical Mail", "Jotuns Wrath", "Void Shield", "Ancile", "Stone of Gaia", "Blackthorn Hammer"},
                        {2, "Deaths Toll", "Transcendence", "Deathbringer", "Executioner", "Wind Demon", "Heartseeker"},
                        {3, "Sentinels Embrace", "Emperors Armor", "Gauntlet of Thebes", "Sovereignty", "Heartward Amulet", "Midgardian Mail"},
                        {4, "Pendulum of Ages", "Obsidian Shard", "Warlocks Staff", "Chronos Pendant", "Rod of Tahuti", "Spear of the Magus"},
                        {5, "Deaths Toll", "Warlocks Staff", "Rod of Tahuti", "Demonic Grip", "Telkhines Ring", "Obsidian Shard"},
                        {6, "Mystical Mail", "Jotuns Wrath", "Void Shield", "Ancile", "Stone of Gaia", "Blackthorn Hammer"},
                        {7, "Deaths Toll", "Transcendence", "Deathbringer", "Executioner", "Wind Demon", "Heartseeker"},
                        {8, "Sentinels Embrace", "Emperors Armor", "Gauntlet of Thebes", "Sovereignty", "Heartward Amulet", "Midgardian Mail"},
                        {9, "Pendulum of Ages", "Obsidian Shard", "Warlocks Staff", "Chronos Pendant", "Rod of Tahuti", "Spear of the Magus"},
                        {10, "Deaths Toll", "Warlocks Staff", "Rod of Tahuti", "Demonic Grip", "Telkhines Ring", "Obsidian Shard"}
                };
        try
        {
            System.out.println("DataDictDAO: inserting registers...");
            super.prepareStatement("begin;");
            PreparedStatement statement = Database.getInstance().getConnection().prepareStatement(query);
            for ( Object[] arguments : data )
            {
                statement.setInt(1, (Integer) arguments[0]);
                statement.setString(2, (String) arguments[1]);
                statement.setString(3, (String) arguments[2]);
                statement.setString(4, (String) arguments[3]);
                statement.setString(5, (String) arguments[4]);
                statement.setString(6, (String) arguments[5]);
                statement.setString(7, (String) arguments[6]);
                statement.execute();
            }
            System.out.println("DataDictDAO: the inserts were successfully committed to the table items");
        } catch ( SQLException exception )
        {
            super.throwException(exception, query);
        } finally
        {
            try
            {
                super.prepareStatement("commit;");
            } catch ( SQLException exception )
            {
                super.throwException(exception, query);
            }
        }
    }
    /**
     * Inserts the test registers on all the tables.
     */
    public void insertIntoAllTables()
    {
        this.insertIntoTeammate();
        this.insertIntoTeam();
        this.insertIntoMatch();
        this.insertIntoStatistics();
        this.insertIntoItems();
    }
    /**
     * Selects from a table given a column and a where clause all the registers matched for the arguments introduced.
     * If the clause where is null, it is not applied. If the column is asterisk '*', searches for a given table all the registers for all its columns.
     * @param table The table from which is desired to get registers from.
     * @param column The column from which is desired to get registers from a given table, it can be '*' for all the columns.
     * @param where The clause which filters a basic select query.
     * @return A list of strings containing all the matched registers from the query.
     */
    public List<String> selectAllFromATable(String table, String column, String where)
    {
        List<String> registers = new ArrayList<>();
        String select = String.format("select %s from %s%s;", column, table, where == null ? "" : " " + where);
        try
        {
            System.out.println("DataDictDAO: initializing process of accessing data through query...");
            ResultSet result = super.initializeResultSet(select);
            System.out.println("DataDictDAO: query data was accessed successfully");
            while ( result.next() )
            {
                if ( column.equals("*") )
                {
                    List<String> columns = this.selectColumnNameFromATable(table);
                    for ( String columnRegister : columns )
                    {
                        registers.add(result.getString(columnRegister));
                    }
                } else
                {
                    registers.add(result.getString(column));
                }
            }
            if ( registers.size() < 1 ) Exceptions.printError("Error: the query did not return any register\n\n");
            return registers;
        } catch ( SQLException exception )
        {
            super.throwException(exception, select);
        }
        return registers;
    }
}
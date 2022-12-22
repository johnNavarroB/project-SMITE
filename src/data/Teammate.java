package data;
/**
 * Object Teammate, stores data related to its identification String
 * and a vector of String that are its in-game items.
 */
public class Teammate {
    private String id;
    private Statistics statistics;
    /**
     * Defines the parameters needed to create a new object Teammate.
     * @param id Main identification of a player, it has special restrictions.
     * @param statistics Object that holds the name of all in-game items, gold, kills, deaths and assists.
     */
    public Teammate(String id, Statistics statistics)
    {
        this.id = id;
        this.statistics = statistics;
    }
    /**
     * Obtains the main identification of a player from the Teammate instance.
     * @return The main identification of a player, a String with restrictions.
     */
    public String getId()
    {
        return this.id;
    }
    /**
     * Obtains the object that holds the name of all in-game items, gold, kills, deaths and assists of a player.
     * @return Object that holds the name of all in-game items, gold, kills, deaths and assists.
     */
    public Statistics getStatistics()
    {
        return this.statistics;
    }
    /**
     * Obtains an integer that reflects the amount of gold a player has when the game ends.
     * @return An integer with the information of the amount of gold a player has when the game ends.
     */
    public int getGold()
    {
        return this.statistics.getGold();
    }
    /**
     * Obtains an integer that reflects how many times a player eliminated another player.
     * @return An integer, it has information about how many times a player performed an elimination.
     */
    public int getKills()
    {
        return this.statistics.getKills();
    }
    /**
     * Obtains an integer that reflects how many times a player has been eliminated.
     * @return An integer, it has information about how many times a player has been eliminated.
     */
    public int getDeaths()
    {
        return this.statistics.getDeaths();
    }
    /**
     * Obtains an integer that reflects how many times a player has been involved in an elimination.
     * @return An integer, it has information about how many times a player has been involved in an elimination.
     */
    public int getAssists()
    {
        return this.statistics.getAssists();
    }
    /**
     * Obtains an integer that reflects the amount of damage a player has done to towers and phoenixes.
     * @return Integer that reflects the amount of damage a player has done to towers and phoenixes.
     */
    public int getStructureDamage()
    {
        return this.statistics.getStructureDamage();
    }
    /**
     * Obtains an integer that reflects the amount of damage a player has done to the environment entities.
     * @return Integer that reflects the amount of damage a player has done to the environment entities.
     */
    public int getMinionDamage()
    {
        return this.statistics.getMinionDamage();
    }
    /**
     * Obtains an integer that reflects the amount of damage a player has done to other players.
     * @return Integer that reflects the amount of damage a player has done to other players.
     */
    public int getPlayerDamage()
    {
        return this.statistics.getPlayerDamage();
    }
}
package data;
/**
 * Object Stadistics holds all the data related to a teammate on a specified match.
 */
public class Statistics {
    private String[] items;
    private int gold;
    private int kills;
    private int deaths;
    private int assists;
    private int structureDamage;
    private int minionDamage;
    private int playerDamage;
    /**
     * Defines the parameters needed to create a new object Stadistics.
     * @param items Vector of Strings of length six that holds the in-game items the player has.
     * @param gold Integer that refers to the in-game currency a player has in a match.
     * @param kills Integer that counts the times a player eliminated another.
     * @param deaths Integer that counts the times a player has been eliminated.
     * @param assists Integer that counts the times a player has been involved in another player's elimination.
     * @param structureDamage Integer that reflects the amount of damage a player has done to towers and phoenixes.
     * @param minionDamage Integer that reflects the amount of damage a player has done to the environment entities.
     * @param playerDamage Integer that reflects the amount of damage a player has done to other players.
     */
    public Statistics(String[] items, int gold, int kills, int deaths, int assists, int structureDamage, int minionDamage, int playerDamage)
    {
        this.items = items;
        this.gold = gold;
        this.kills = kills;
        this.deaths = deaths;
        this.assists = assists;
        this.structureDamage = structureDamage;
        this.minionDamage = minionDamage;
        this.playerDamage = playerDamage;
    }
    /**
     * Obtains a vector of String that has data about item names hold in-game when a game ends.
     * @return A vector of String, it has information of the identification about six items per player.
     */
    public String[] getItems()
    {
        return this.items;
    }
    /**
     * Obtains an integer that has data about the amount of currency a player has when a match ends.
     * @return Integer that refers to the in-game currency a player has in a match.
     */
    public int getGold()
    {
        return this.gold;
    }
    /**
     * Obtains an integer that has data about the amount of eliminations a player has when a match ends.
     * @return Integer that counts the times a player eliminated another.
     */
    public int getKills()
    {
        return this.kills;
    }
    /**
     * Obtains an integer that has data about the amount of times a player has been eliminated.
     * @return Integer that counts the times a player has been eliminated.
     */
    public int getDeaths()
    {
        return this.deaths;
    }
    /**
     * Obtains an integer that has data about the amount of times a player has been involved in another player's elimination.
     * @return Integer that counts the times a player has been involved in another player's elimination.
     */
    public int getAssists()
    {
        return this.assists;
    }
    /**
     * Obtains an integer that has data about the amount of damage a player has done to towers and phoenixes.
     * @return Integer that reflects the amount of damage a player has done to towers and phoenixes.
     */
    public int getStructureDamage()
    {
        return this.structureDamage;
    }
    /**
     * Obtains an integer that has data about the amount of damage a player has done to the environment entities.
     * @return Integer that reflects the amount of damage a player has done to the environment entities.
     */
    public int getMinionDamage()
    {
        return this.minionDamage;
    }
    /**
     * Obtains an integer that has data about the amount of damage a player has done to other players.
     * @return Integer that reflects the amount of damage a player has done to other players.
     */
    public int getPlayerDamage()
    {
        return this.playerDamage;
    }
}
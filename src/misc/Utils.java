package misc;
/**
 * This class holds miscellaneous methods related to work with data.
 */
public class Utils {
    /**
     * Converts an unmodifiable String into a modifiable vector of char
     * in order to use "for each" on each char of a String.
     * @param string Any String that is wished to be modified.
     * @return A vector of char with the same length as the parameter String's length and the same chars.
     */
    public static char[] myToCharArray(String string)
    {
        char[] charArray = new char[string.length()];
        for (int i = 0 ; i < string.length() ; i += 1)
        {
            charArray[i] = string.charAt(i);
        }
        return charArray;
    }
    /**
     * Calculates the average of kills and assists from deaths.
     * @param kills An integer that reflects how many gods has defeated a player.
     * @param deaths An integer that reflects how many times a player died.
     * @param assists An integer that reflects how many times a player has been involved in another player's kill.
     * @return Average of the addition of kills between the number of deaths.
     */
    public static double KDACalculation(int kills, int deaths, int assists)
    {
        if ( deaths > 0 ) return (double) (kills + assists) / (deaths + 1);
        return kills + assists;
    }
}
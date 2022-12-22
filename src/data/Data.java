package data;
import misc.Input;
/**
 * This class holds test data created to try the software with ease.
 */
public class Data {
    public static String[] items = {"gem of focus2", "thoths book2", "rod of tahuti2", "chronos pendant2", "soul gem2", "doom orb2"};
    public static Statistics statistics = new Statistics(items,15000, 21, 1, 2, 23000, 80000, 25000);
    public static Teammate teammate = new Teammate("TBone2", statistics);
    public static Teammate teammate1 = new Teammate("Jackhammer2", statistics);
    public static Teammate teammate2 = new Teammate("Aqua2", statistics);
    public static Teammate teammate3 = new Teammate("Grim2", statistics);
    public static Teammate teammate4 = new Teammate("Cannonball2", statistics);
    public static Teammate teammate5 = new Teammate("Speed2", statistics);
    public static Teammate teammate6 = new Teammate("Wiggles2", statistics);
    public static Teammate teammate7 = new Teammate("Captain2", statistics);
    public static Teammate teammate8 = new Teammate("Flame2", statistics);
    public static Teammate teammate9 = new Teammate("Lucky2", statistics);
    public static Teammate[] teammates = {teammate, teammate1, teammate2, teammate3, teammate4};
    public static Teammate[] teammates2 = {teammate5, teammate6, teammate7, teammate8, teammate9};
    public static Team team = new Team("Controlling Pidgeons2", teammates);
    public static Team team2 = new Team("Forgetful Hotspurs2", teammates2);
    public static Match match = new Match(team, team2, 135);
    /**
     * Registers a specific amount of matches depending on the parameter "index".
     * The matches registered are a predefined clone and this method serves the purpose to introduce matches faster.
     * @param index An integer introduced by the user that goes between one and ten.
     */
    public static void importMatches(int index)
    {
        for (int i = 0 ; i < index ; i++)
        {
            Input.saveNewMatch(match);
        }
    }
}
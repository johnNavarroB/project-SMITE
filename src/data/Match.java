package data;
/**
 * Object Match contains two teams and the time it lasted in minutes.
 */
public class Match {
    private Team order;
    private Team chaos;
    private int minutes;
    private int matchId;
    /**
     * Defines the parameters needed to create a new object Match.
     * A match is composed by two teams, named "order" and "chaos".
     * @param order Object Team with all the information related to a team.
     * @param chaos Object Team with all the information related to a team.
     * @param minutes Integer that has the information about a period of time that the match lasted.
     */
    public Match(Team order, Team chaos, int minutes)
    {
        this.order = order;
        this.chaos = chaos;
        this.minutes = minutes;
    }
    /**
     * A new revolutionary way of obtaining the match id from a match.
     * @param order Object Team with all the information related to a team.
     * @param chaos Object Team with all the information related to a team.
     * @param minutes Integer that has the information about a period of time that the match lasted.
     * @param matchId The match id from a match.
     */
    public Match(Team order, Team chaos, int minutes, int matchId)
    {
        this.order = order;
        this.chaos = chaos;
        this.minutes = minutes;
        this.matchId = matchId;
    }
    /**
     * Obtains a Team that has part in the current Match's instance.
     * @return Object Team with all the information related to a team.
     */
    public Team getOrder()
    {
        return this.order;
    }
    /**
     * Obtains a Team that has part in the current Match's instance.
     * @return Object Team with all the information related to a team.
     */
    public Team getChaos()
    {
        return this.chaos;
    }
    /**
     * Obtains an integer that has the information about a period of time that the match lasted.
     * @return Integer that has the information about a period of time that the match lasted.
     */
    public int getMinutes()
    {
        return this.minutes;
    }
    /**
     * Obtains the match id from a match.
     * @return The match id from the match as an integer.
     */
    public int getId()
    {
        return this.matchId;
    }
}
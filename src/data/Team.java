package data;
/**
 * Object Team, stores five Teammates that will form part of a Match.
 * Teammates are saved in a vector of object Teammate and have a role
 * depending on the order of how they were introduced.
 */
public class Team {
    private String teamName;
    private Teammate[] teammates;
    /**
     * Defines the parameters needed to create a new object Team.
     * @param teamName Representative trait of each team, a String with a max length of sixteen characters.
     * @param teammates A vector of five objects Teammate and all their relative information.
     */
    public Team(String teamName, Teammate[] teammates)
    {
        this.teamName = teamName;
        this.teammates = teammates;
    }
    /**
     * Obtains the team name of a team from the Team instance.
     * @return The distinguishing name of each Team with a length of one to sixteen characters.
     */
    public String getTeamName()
    {
        return this.teamName;
    }
    /**
     * Obtains a vector of teammates being part of the team.
     * @return A vector of Teammate, holds five players per Team.
     */
    public Teammate[] getTeammates()
    {
        return this.teammates;
    }
    /**
     * Uses an index to determine the role of a teammate to help the user know what role each teammate is.
     * @param i An integer index provided by a loop to choose a return String.
     * @return A String that will be printed at the screen showing a role for each teammate.
     */
    public String createTeammatesUpperShowRole(int i)
    {
        switch(i)
        {
            case 0:
                return "Support";
            case 1:
                return "ADC";
            case 2:
                return "Mid";
            case 3:
                return "Jungle";
            case 4:
                return "Solo";
        }
        return "none";
    }
}
package data;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TeamTest {
    private static final String[] items = {"item1", "item2", "item3", "item4", "item5", "item6"};
    private static final Statistics statistics = new Statistics(items, 15000, 7, 2, 4, 5000, 30000, 60000);
    private static final Teammate teammate = new Teammate("nickname", statistics);
    private static final Teammate[] teammates = {teammate, teammate, teammate, teammate, teammate};
    private static final Team team = new Team("teamID", teammates);
    @Test
    void getTeamName() {
        assertEquals(team.getTeamName(), "teamID");
    }
    @Test
    void getTeammates() {
        for (int i = 0 ; i < teammates.length ; i ++)
        {
            assertEquals(team.getTeammates()[i].getId(), teammates[i].getId());
        }
    }
    @Test
    void createTeammatesUpperShowRole() {
        for (int i = 0 ; i < 6 ; i ++)
        {
            String role;
            if (i == 0) role = "Support";
            else if (i == 1) role = "ADC";
            else if (i == 2) role = "Mid";
            else if (i == 3) role = "Jungle";
            else if (i == 4) role = "Solo";
            else role = "none";
            assertEquals(team.createTeammatesUpperShowRole(i), role);
        }
    }
}
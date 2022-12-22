package data;
import misc.Input;
import misc.Utils;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class MatchTest {
    private static final String[] items = {"item1", "item2", "item3", "item4", "item5", "item6"};
    private static final Statistics statistics = new Statistics(items, 15000, 7, 2, 4, 5000, 30000, 60000);
    private static final Teammate teammate = new Teammate("nickname", statistics);
    private static final Teammate[] teammates = {teammate, teammate, teammate, teammate, teammate};
    private static final Team team = new Team("teamID", teammates);
    private static final Match match = new Match(team, team, 80);
    @Test
    void getOrder() {
        assertEquals(match.getOrder().getTeamName(), "teamID");
    }
    @Test
    void getChaos() {
        assertEquals(match.getChaos().getTeamName(), "teamID");
    }
    @Test
    void showMinutes(){
        assertEquals(Input.showMinutes(80), "01:20");
    }
    @Test
    void KDACalculation(){
        assertEquals(Utils.KDACalculation(7, 2, 4), 3.6666666666666665);
    }
}
package data;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class TeammateTest {
    private static final String[] items = {"item1", "item2", "item3", "item4", "item5", "item6"};
    private static final Statistics statistics = new Statistics(items, 15000, 7, 2, 4, 5000, 30000, 60000);
    private static final Teammate teammate = new Teammate("nickname", statistics);
    @Test
    void getId() {
        assertEquals(teammate.getId(), "nickname");
    }
    @Test
    void getGold() {
        assertEquals(teammate.getGold(), 15000);
    }
    @Test
    void getKills() {
        assertEquals(teammate.getKills(), 7);
    }
    @Test
    void getDeaths() {
        assertEquals(teammate.getDeaths(), 2);
    }
    @Test
    void getAssists() {
        assertEquals(teammate.getAssists(), 4);
    }
    @Test
    void getStructureDamage() {
        assertEquals(teammate.getStructureDamage(), 5000);
    }
    @Test
    void getMinionDamage() {
        assertEquals(teammate.getMinionDamage(), 30000);
    }
    @Test
    void getPlayerDamage() {
        assertEquals(teammate.getPlayerDamage(), 60000);
    }
}
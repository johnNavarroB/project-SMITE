package data;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
class StatisticsTest {
    private static final String[] items = {"item1", "item2", "item3", "item4", "item5", "item6"};
    private static final Statistics statistics = new Statistics(items, 15000, 7, 2, 4, 5000, 30000, 60000);
    @Test
    void getItems() {
        for (int i = 0 ; i < items.length ; i ++)
        {
            assertEquals(items[i], statistics.getItems()[i]);
        }
    }
    @Test
    void getGold() {
        assertEquals(statistics.getGold(), 15000);
    }
    @Test
    void getKills() {
        assertEquals(statistics.getKills(), 7);
    }
    @Test
    void getDeaths() {
        assertEquals(statistics.getDeaths(), 2);
    }
    @Test
    void getAssists() {
        assertEquals(statistics.getAssists(), 4);
    }
    @Test
    void getStructureDamage() {
        assertEquals(statistics.getStructureDamage(), 5000);
    }
    @Test
    void getMinionDamage() {
        assertEquals(statistics.getMinionDamage(), 30000);
    }
    @Test
    void getPlayerDamage() {
        assertEquals(statistics.getPlayerDamage(), 60000);
    }
}
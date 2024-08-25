package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LightBulbTest {
    @Test
    @Tag("16-1")
    @DisplayName("Testing functionality of OR & AND Logic rule")
    public void lightBulbOrAndTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_LightBulbTest_OrAnd", "simple");

        assertEquals(2, TestUtils.getEntities(res, "light_bulb_off").size());


        // Turn on light bulbs
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("16-2")
    @DisplayName("Testing functionality of COAND Logic rule")
    public void lightBulbCoAndTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_LightBulbTest_CoAnd", "simple");

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
        assertEquals(1, TestUtils.countType(res, "light_bulb_off"));


        // Turn on COAND light bulb
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }

    @Test
    @Tag("16-3")
    @DisplayName("Testing functionality of XOR Logic rule")
    public void lightBulbXORTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_LightBulbTest_Xor", "simple");

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());

        // Turn on XOR light bulb
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());

        // Turn off XOR light bulb
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());
    }

    @Test
    @Tag("16-4")
    @DisplayName("Testing functionality of light bulb without any wires")
    public void lightBulbNoWiresTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_LightBulbTest_noWires", "simple");

        assertEquals(1, TestUtils.getEntities(res, "light_bulb_off").size());

        // Turn on light bulb
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "light_bulb_on").size());
    }
}

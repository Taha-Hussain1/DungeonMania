package dungeonmania.mvp;

import dungeonmania.DungeonManiaController;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class SwitchDoorTest {
    @Test
    @Tag("17-1")
    @DisplayName("Testing functionality of OR & AND Logic rule")
    public void switchDoorOrAndTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwitchDoorTest_OrAnd", "simple");

        assertEquals(2, TestUtils.getEntities(res, "switch_door").size());

        // Open the doors
        res = dmc.tick(Direction.RIGHT);
        assertEquals(2, TestUtils.getEntities(res, "switch_door_open").size());
    }

    @Test
    @Tag("17-2")
    @DisplayName("Testing functionality of COAND Logic rule")
    public void switchDoorCoAndTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwitchDoorTest_CoAnd", "simple");

        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
        assertEquals(1, TestUtils.countType(res, "switch_door"));


        // open co_and switch_door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "switch_door_open").size());
    }

    @Test
    @Tag("17-3")
    @DisplayName("Testing functionality of XOR Logic rule")
    public void swithDoorXorTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwitchDoorTest_Xor", "simple");

        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());

        // Open Xor door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(1, TestUtils.getEntities(res, "switch_door_open").size());

        // Close Xor door
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getEntities(res, "switch_door").size());
    }

    @Test
    @Tag("17-4")
    @DisplayName("Testing functionality of walking through switch door")
    public void lightBulbXORTest() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_SwitchDoorTest_walkThroughDoor", "simple");

        Position pos = TestUtils.getEntities(res, "player").get(0).getPosition();
        // Try to walkthrough closed door
        res = dmc.tick(Direction.RIGHT);
        assertEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());

        // Open Door and walk through it
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.DOWN);
        res = dmc.tick(Direction.RIGHT);
        assertNotEquals(pos, TestUtils.getEntities(res, "player").get(0).getPosition());
    }
}

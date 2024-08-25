package dungeonmania.mvp.Task2Tests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;

public class EnemyGoalTests {
    @Test
    @DisplayName("Test to achieve a single enemy goal")
    public void basicEnemy() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTests_basic", "c_EnemyGoalTests_basic");

        // move player to right
        res = dmc.tick(Direction.RIGHT);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        List<EntityResponse> entities = res.getEntities();
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        res = dmc.tick(Direction.RIGHT);
        entities = res.getEntities();
        assertTrue(TestUtils.countEntityOfType(entities, "zombie_toast") == 0);
        String spawnerId = TestUtils.getEntities(res, "zombie_toast_spawner").get(0).getId();
        assertEquals(":enemies", TestUtils.getGoals(res));
        // move player to exit
        res = dmc.tick(Direction.UP);
        assertEquals(1, TestUtils.getInventory(res, "sword").size());
        res = dmc.tick(Direction.UP);

        res = assertDoesNotThrow(() -> dmc.interact(spawnerId));
        assertEquals(0, TestUtils.countType(res, "zombie_toast_spawner"));

        // assert goal met
        assertEquals("", TestUtils.getGoals(res));
    }

    @Test
    @DisplayName("Test to achieve an enemy goal and exit goal")
    public void compoundEnemyExit() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTests_compoundOne", "c_EnemyGoalTests_basic");
        assertTrue(TestUtils.getGoals(res).contains(":exit"));
        // move player to right
        res = dmc.tick(Direction.UP);

        // assert goal not met
        assertTrue(TestUtils.getGoals(res).contains(":enemies"));

        res = dmc.tick(Direction.DOWN);

        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        // move player to exit
        res = dmc.tick(Direction.LEFT);
        res = dmc.tick(Direction.LEFT);
        // assert goal met
        assertTrue(TestUtils.getGoals(res).contains(":exit"));

        res = dmc.tick(Direction.UP);
        assertEquals("", TestUtils.getGoals(res));

    }

    @Test
    @DisplayName("Test to achieve an enemy goal or a treasure goal")
    public void compoundEnemyExittwo() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_EnemyGoalTests_compoundTwo", "c_EnemyGoalTests_basic");

        assertTrue(TestUtils.getGoals(res).contains(":enemies"));
        assertTrue(TestUtils.getGoals(res).contains(":treasure"));

        res = dmc.tick(Direction.RIGHT);

        List<EntityResponse> entities = res.getEntities();

        assertTrue(TestUtils.countEntityOfType(entities, "zombie_toast_spawner") == 0);
        // move player to exit
        res = dmc.tick(Direction.UP);

        assertEquals("", TestUtils.getGoals(res));

    }
}

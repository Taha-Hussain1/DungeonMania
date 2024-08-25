package dungeonmania.mvp.Task2Tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import java.util.List;

import dungeonmania.DungeonManiaController;
import dungeonmania.mvp.TestUtils;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.DungeonResponse;
import dungeonmania.response.models.EntityResponse;
import dungeonmania.util.Direction;
import dungeonmania.util.Position;

public class SnakesTest {
    @Test
    @DisplayName("Test basic movement of snake")
    public void basicMovement() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_snake_basicMovement", "c_snake_basicMovement");
        Position pos = TestUtils.getEntities(res, "snake_head").get(0).getPosition();

        for (int i = 0; i < 2; i++) {
            res = dmc.tick(Direction.DOWN);
        }
        List<EntityResponse> listsnek = TestUtils.getEntities(res, "snake_head");
        pos = listsnek.get(0).getPosition();

        assertEquals("Position [x=3, y=0, z=0]", pos.toString());
        assertTrue(TestUtils.getEntities(res, "snake_head").size() == 1);
        assertTrue(TestUtils.getEntities(res, "snake_body").size() == 1);

        for (int i = 0; i < 5; i++) {
            res = dmc.tick(Direction.DOWN);
        }
        assertTrue(TestUtils.getEntities(res, "snake_head").size() == 1);
        assertTrue(TestUtils.getEntities(res, "snake_body").size() == 2);

        pos = TestUtils.getEntities(res, "snake_head").get(0).getPosition();
        Position posbody = TestUtils.getEntities(res, "snake_body").get(1).getPosition();

        assertEquals("Position [x=0, y=1, z=0]", pos.toString());
        assertEquals("Position [x=0, y=0, z=0]", posbody.toString());

        for (int i = 0; i < 9; i++) {
            res = dmc.tick(Direction.UP);
        }

        assertTrue(TestUtils.getEntities(res, "snake_head").size() == 0);

    }

    @Test
    @DisplayName("Test snakes get treasure boosts")
    public void buffSnakes() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_snake_buffs", "c_snake_basicMovement");

        for (int i = 0; i < 4; i++) {
            res = dmc.tick(Direction.RIGHT);

        }
        res = dmc.tick(Direction.UP);
        res = dmc.tick(Direction.LEFT);
        List<BattleResponse> battles = res.getBattles();
        double snakehealth = battles.get(0).getInitialEnemyHealth();
        assertEquals(snakehealth, 12);
        assertTrue(TestUtils.getEntities(res, "snake_head").size() == 1);
        assertTrue(TestUtils.getEntities(res, "snake_body").size() == 3);

    }

    @Test
    @DisplayName("Test snakes get invisible")
    public void invisibileSnakes() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_snake_invisibility", "c_snake_basicMovement");
        for (int i = 0; i < 9; i++) {
            res = dmc.tick(Direction.DOWN);
        }
        Position pos = TestUtils.getEntities(res, "snake_head").get(0).getPosition();
        assertEquals("Position [x=0, y=1, z=0]", pos.toString());

    }

    @Test
    @DisplayName("Test snakes go invincible")
    public void invincibleSnakes() {
        DungeonManiaController dmc;
        dmc = new DungeonManiaController();
        DungeonResponse res = dmc.newGame("d_snake_invincibilty", "c_snake_invincibility");

        for (int i = 0; i < 8; i++) {
            res = dmc.tick(Direction.DOWN);
        }

        assertTrue(TestUtils.getEntities(res, "snake_head").size() == 1);
        assertTrue(TestUtils.getEntities(res, "snake_body").size() == 5);

        for (int i = 0; i < 9; i++) {
            res = dmc.tick(Direction.UP);
        }

        assertTrue(TestUtils.getEntities(res, "snake_head").size() == 2);

        res = dmc.tick(Direction.LEFT);

        assertTrue(TestUtils.getEntities(res, "snake_head").size() == 2);
        assertTrue(TestUtils.getEntities(res, "snake_body").size() == 3);

        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);
        res = dmc.tick(Direction.RIGHT);

        assertTrue(TestUtils.getEntities(res, "snake_head").size() == 2);

    }

}

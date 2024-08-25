package dungeonmania.entities.enemies;

import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SnakeBodyMove implements EnemyMoveStrategies {
    public Position moveEnemy(GameMap map, Enemy enemy, Player player) {
        return ((SnakeBody) enemy).getParent().getPosition();
    }
}

package dungeonmania.entities.enemies;

import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class MobBasicMove implements EnemyMoveStrategies {
    public Position moveEnemy(GameMap map, Enemy enemy, Player player) {
        return map.dijkstraPathFind(enemy.getPosition(), player.getPosition(), enemy);

    }
}

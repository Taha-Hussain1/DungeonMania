package dungeonmania.entities.enemies;

import dungeonmania.map.GameMap;
import dungeonmania.entities.Player;

import dungeonmania.util.Position;

public interface EnemyMoveStrategies {
    public Position moveEnemy(GameMap map, Enemy enemy, Player player);

}

package dungeonmania.entities.enemies;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SnakeInvisibleMove implements EnemyMoveStrategies {
    public Position moveEnemy(GameMap map, Enemy enemy, Player player) {
        Position nearestTreasure = findNearestTreasure(map, enemy);
        if (nearestTreasure == null) {
            return null;
        }
        return map.dijkstraPathFind(enemy.getPosition(), nearestTreasure, enemy);
    }

    private Position findNearestTreasure(GameMap map, Enemy enemy) {
        List<Entity> entities = map.getEntities();
        int mindist = 1000;
        Entity closest = null;
        for (Entity ent : entities) {
            if (ent instanceof Potion || ent instanceof Treasure || ent instanceof Key || ent instanceof Arrow) {
                int dist = manhattanDistance(enemy.getPosition(), ent.getPosition());
                if (dist < mindist) {
                    closest = ent;
                    mindist = dist;
                }
            }
        }
        if (closest == null) {
            return null;
        }
        return closest.getPosition();

    }

    private Integer manhattanDistance(Position source, Position target) {
        return Math.abs(source.getX() - target.getX()) + Math.abs(source.getY() - target.getY());
    }
}

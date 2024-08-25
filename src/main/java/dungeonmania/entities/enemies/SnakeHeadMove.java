package dungeonmania.entities.enemies;

import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

import java.util.List;

import dungeonmania.entities.Entity;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.potions.Potion;

public class SnakeHeadMove implements EnemyMoveStrategies {
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
                int dist = map.dijkstradistanceFind(enemy.getPosition(), ent.getPosition(), enemy);
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

}

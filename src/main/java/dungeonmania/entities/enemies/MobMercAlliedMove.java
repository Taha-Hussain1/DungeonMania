package dungeonmania.entities.enemies;

import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class MobMercAlliedMove implements EnemyMoveStrategies {
    public Position moveEnemy(GameMap map, Enemy enemy, Player player) {
        Mercenary merc = (Mercenary) enemy;
        Position nextPos = merc.isAdjacentToPlayer() ? player.getPreviousDistinctPosition()
                : map.dijkstraPathFind(merc.getPosition(), player.getPosition(), merc);
        if (!merc.isAdjacentToPlayer() && Position.isAdjacent(player.getPosition(), nextPos)) {
            merc.setAdjacentToPlayer(true);
        }
        return nextPos;
    }
}

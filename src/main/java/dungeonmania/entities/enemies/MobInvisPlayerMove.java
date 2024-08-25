package dungeonmania.entities.enemies;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;
import java.util.Random;

public class MobInvisPlayerMove implements EnemyMoveStrategies {
    private Random randGen = new Random();

    public Position moveEnemy(GameMap map, Enemy enemy, Player player) {
        List<Position> pos = enemy.getPosition().getCardinallyAdjacentPositions();
        pos = pos.stream().filter(p -> map.canMoveTo(enemy, p)).collect(Collectors.toList());
        if (pos.size() == 0) {
            return enemy.getPosition();
        } else {
            return pos.get(randGen.nextInt(pos.size()));
        }

    }

}

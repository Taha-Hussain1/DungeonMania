package dungeonmania.entities.collectables;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Overlapable;
import dungeonmania.entities.Player;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;
import dungeonmania.entities.enemies.SnakeHead;

public class Treasure extends Collectable implements InventoryItem, Overlapable {
    public Treasure(Position position) {
        super(position);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            if (!((Player) entity).pickUp(this))
                return;
            map.destroyEntity(this);
        } else if (entity instanceof SnakeHead) {
            ((SnakeHead) entity).snakeOverlap(this, map);
            map.destroyEntity(this);
        }
    }
}

package dungeonmania.entities;

import java.util.ArrayList;
import java.util.List;

import dungeonmania.entities.collectables.Bomb;
import dungeonmania.entities.logical.LogicalEntity;
import dungeonmania.entities.logical.Wire;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Switch extends Entity implements Overlapable, MovedAwayListener {
    private boolean activated;
    private List<Bomb> bombs = new ArrayList<>();
    private List<Wire> wires = new ArrayList<>();
    private List<LogicalEntity> logicalEntities = new ArrayList<>();

    public Switch(Position position) {
        super(position.asLayer(Entity.ITEM_LAYER));
    }

    public void subscribe(Entity entity) {
        if (entity instanceof Bomb) {
            bombs.add((Bomb) entity);
        } else if (entity instanceof Wire) {
            wires.add((Wire) entity);
        } else if (entity instanceof LogicalEntity) {
            logicalEntities.add((LogicalEntity) entity);
        }
    }

    public void subscribe(Bomb bomb, GameMap map) {
        bombs.add(bomb);
        if (activated) {
            bombs.stream().forEach(b -> b.notify(map));
        }
    }

    public void unsubscribe(Bomb b) {
        bombs.remove(b);
    }

    public void unsubscribe(Entity entity) {
        if (entity instanceof Bomb) {
            bombs.remove(entity);
        } else if (entity instanceof LogicalEntity) {
            logicalEntities.remove((LogicalEntity) entity);
        }
    }


    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = true;
            bombs.stream().forEach(b -> b.notify(map));
            wires.stream().forEach(w -> w.notify(map, this));
            logicalEntities.stream().forEach(le -> le.notify(map));
        }
    }

    @Override
    public void onMovedAway(GameMap map, Entity entity) {
        if (entity instanceof Boulder) {
            activated = false;
        }
        wires.stream().forEach(w -> w.notify(map, this));
        logicalEntities.stream().forEach(le -> le.notify(map));

    }

    public boolean isActivated() {
        return activated;
    }
}

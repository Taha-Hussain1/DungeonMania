package dungeonmania.entities.logical;

import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.Spider;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SwitchDoor extends Entity implements LogicalEntity {
    private boolean isOpen;
    private LogicalRule rule;

    public SwitchDoor(Position position, String rule) {
        super(position);
        this.isOpen = false; // always created off
        this.rule = LogicalRuleFactory.getRule(rule);

    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (isOpen || entity instanceof Spider) {
            return true;
        }
        return false;
    }

    @Override
    public void notify(GameMap map) {
        this.isOpen = rule.checkActivation(this, map);
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void open() {
        isOpen = true;
    }

    public void close() {
        isOpen = false;
    }
}

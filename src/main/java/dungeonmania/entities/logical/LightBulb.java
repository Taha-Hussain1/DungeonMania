package dungeonmania.entities.logical;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class LightBulb extends Entity implements LogicalEntity {
    private boolean isOn;
    private LogicalRule rule;

    public LightBulb(Position position, String rule) {
        super(position);
        this.isOn = false; // Always created off
        this.rule = LogicalRuleFactory.getRule(rule);
    }

    @Override
    public void notify(GameMap map) {
        this.isOn = rule.checkActivation(this, map);
    }

    public boolean isOn() {
        return isOn;
    }

    public void turnOn() {
        this.isOn = true;
    }

    public void turnOff() {
        this.isOn = false;
    }
 }

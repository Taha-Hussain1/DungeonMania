package dungeonmania.entities.logical;

import dungeonmania.entities.Entity;
import dungeonmania.map.GameMap;

public class AndRule extends LogicalRule {
    @Override
    public boolean checkActivation(Entity entity, GameMap map) {
        return activatedSwitch(entity, map) > 1;
    }
}

package dungeonmania.entities.logical;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;

public class CoAndRule extends LogicalRule {
    @Override
    public boolean checkActivation(Entity entity, GameMap map) {
        List<Entity> wireNeighbours = getWireNeighbours(map, entity.getPosition());
        Map<Switch, Integer> connectedSwitch = new HashMap<>();

        for (Entity wire : wireNeighbours) {
            List<Switch> switches = new ArrayList<>(((Wire) wire).getCircuitSwitchs());
            for (Switch currSwitch : switches) {
                if (!connectedSwitch.containsKey(currSwitch)) {
                    connectedSwitch.put(currSwitch, 0);
                }

                if (currSwitch.isActivated()) {
                    int currentValue = connectedSwitch.get(currSwitch);
                    connectedSwitch.put(currSwitch, currentValue + 1);
                }

            }
        }

        for (Map.Entry<Switch, Integer> entry : connectedSwitch.entrySet()) {
            Integer value = entry.getValue();
            if (value >= 2) {
                return true;
            }
        }

        return false;
    }
}

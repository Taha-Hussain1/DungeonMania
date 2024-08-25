package dungeonmania.entities.logical;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class LogicalRule {
    public abstract boolean checkActivation(Entity entity, GameMap map);

    public int activatedSwitch(Entity entity, GameMap map) {
        List<Entity> switchNeighbours = getSwitchNeighbours(map, entity.getPosition());
        List<Entity> wireNeighbours = getWireNeighbours(map, entity.getPosition());
        int activated = 0;

        for (Entity wire : wireNeighbours) {
            for (Switch currSwitch : ((Wire) wire).getCircuitSwitchs()) {
                if (currSwitch.isActivated()) {
                    activated++;
                    break;
                }
            }
        }

        for (Entity currSwitch : switchNeighbours) {
            if (((Switch) currSwitch).isActivated()) {
                activated++;
            }
        }

        return activated;
    }

    public List<Entity> getWireNeighbours(GameMap map, Position currPosition) {
        List<Position> positions = getNeighbourPositions(currPosition);

        List<Entity> neighbours = positions.stream().flatMap(position -> map.getEntities(position).stream())
                .filter(e -> e instanceof Wire).collect(Collectors.toList());

        return neighbours;
    }

    public List<Entity> getSwitchNeighbours(GameMap map, Position currPosition) {
        List<Position> positions = getNeighbourPositions(currPosition);

        List<Entity> neighbours = positions.stream().flatMap(position -> map.getEntities(position).stream())
                .filter(e -> e instanceof Switch).collect(Collectors.toList());

        return neighbours;
    }

    public List<Position> getNeighbourPositions(Position currPosition) {
        List<Position> positions = new ArrayList<>();
        int x = currPosition.getX();
        int y = currPosition.getY();

        positions.add(new Position(x - 1, y));
        positions.add(new Position(x + 1, y));
        positions.add(new Position(x, y - 1));
        positions.add(new Position(x, y + 1));

        return positions;
    }
}

package dungeonmania.entities.logical;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class Wire extends Entity {
    private Set<Switch> adjacentSwitches = new HashSet<>();
    private Set<Switch> circuitSwitches = new HashSet<>();

    public Wire(Position position) {
        super(position);
    }

    public void subscribe(Switch s) {
        this.adjacentSwitches.add(s);
    }

    public void circuitSubscribe(Switch s) {
        this.circuitSwitches.add(s);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return true; // any moveable entity can walk onto a wire
    }

    public void notify(GameMap map, Switch cSwitch) {
        Set<Entity> endpoints = new HashSet<>();
        Set<Entity> visited = new HashSet<>();
        Set<Entity> logicalEntites = new HashSet<>();
        dfs(this, map, endpoints, visited, logicalEntites);

        endpoints.forEach(wire -> ((Wire) wire).circuitSubscribe(cSwitch));
        logicalEntites.forEach(logicalEntity -> {
            ((LogicalEntity) logicalEntity).notify(map);
        });
    }

    public void dfs(Wire wire, GameMap map, Set<Entity> endpoints, Set<Entity> visited, Set<Entity> logicalEntities) {

        List<Entity> neighbourLogicalEntites = getNeighbours(map, wire.getPosition()).stream()
                .filter(logicalEntity -> logicalEntity instanceof LogicalEntity).collect(Collectors.toList());
        for (Entity logical : neighbourLogicalEntites) {
            if (!logicalEntities.contains(logical)) {
                logicalEntities.add(logical);
                endpoints.add(wire);
            }
        }

        if (visited.contains(wire)) {
            return;
        }

        visited.add(wire);
        List<Entity> neighbours = getWireNeighbours(map, wire.getPosition());
        neighbours.removeIf(visited::contains);

        if (neighbours.isEmpty()) {
            endpoints.add(wire);
        } else {
            for (Entity neighbour : neighbours) {
                if (!visited.contains(neighbour)) {
                    dfs((Wire) neighbour, map, endpoints, visited, logicalEntities);
                }
            }
        }
    }

    public List<Entity> getWireNeighbours(GameMap map, Position currPosition) {
        List<Position> positions = getNeighbourPositions(currPosition);

        List<Entity> neighbours = positions.stream().flatMap(position -> map.getEntities(position).stream())
                .filter(e -> e instanceof Wire).collect(Collectors.toList());

        return neighbours;
    }

    public List<Entity> getNeighbours(GameMap map, Position currPosition) {
        List<Position> positions = getNeighbourPositions(currPosition);
        List<Entity> neighbours = positions.stream().flatMap(position -> map.getEntities(position)
                .stream()).collect(Collectors.toList());

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

    public Set<Switch> getCircuitSwitchs() {
        return circuitSwitches;
    }

}

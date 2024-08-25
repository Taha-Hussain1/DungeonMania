package dungeonmania.entities.logical;

import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.entities.Entity;
import dungeonmania.entities.Switch;
import dungeonmania.entities.collectables.Bomb;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class LogicalBomb extends Bomb implements LogicalEntity {
    private LogicalRule rule;

    public LogicalBomb(Position position, int radius, String ruleType) {
        super(position, radius);
        this.rule = LogicalRuleFactory.getRule(ruleType);
    }

    @Override
    public void notify(GameMap map) {
        super.notify(map);
        if (rule.checkActivation(this, map)) {
            explode(map);
        }
    }

    public void onPutDown(GameMap map, Position p) {
        setPosition(Position.translateBy(this.getPosition(), Position.calculatePositionBetween(getPosition(), p)));

        map.addEntity(this);
        setState(State.PLACED);
        List<Position> adjPosList = getPosition().getCardinallyAdjacentPositions();
        adjPosList.stream().forEach(node -> {
            List<Entity> entities = map.getEntities(node).stream().filter(e -> (e instanceof Switch))
                    .collect(Collectors.toList());
            entities.stream().map(Switch.class::cast).forEach(s -> s.subscribe(this, map));
            entities.stream().map(Switch.class::cast).forEach(s -> this.subscribe(s));
        });
        notify(map);
    }

    public List<Switch> getSubs() {
        return super.getSubs();
    }

    public void setState(State state) {
        super.setState(state);
    }

}

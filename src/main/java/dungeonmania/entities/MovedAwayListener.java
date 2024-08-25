package dungeonmania.entities;

import dungeonmania.map.GameMap;

public interface MovedAwayListener {
    public void onMovedAway(GameMap map, Entity entity);
}

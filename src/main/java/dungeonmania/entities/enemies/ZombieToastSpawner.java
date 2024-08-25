package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.Destroyable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Interactable;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ZombieToastSpawner extends Entity implements Interactable, Destroyable {
    public static final int DEFAULT_SPAWN_INTERVAL = 0;
    private boolean isActive = true;

    public ZombieToastSpawner(Position position, int spawnInterval) {
        super(position);
        this.isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }

    public void spawn(Game game) {
        game.getEntityFactory().spawnZombie(game, this);
    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
    }

    @Override
    public void interact(Player player, Game game) {
        player.getInventory().getWeapon().use(game);
        this.isActive = false;
        GameMap map = game.getMap();
        map.destroyEntity(this);
    }

    @Override
    public boolean isInteractable(Player player) {
        return Position.isAdjacent(player.getPosition(), getPosition()) && player.hasWeapon();
    }
}

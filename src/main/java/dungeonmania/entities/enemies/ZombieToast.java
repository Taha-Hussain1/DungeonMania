package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class ZombieToast extends Enemy {
    public static final double DEFAULT_HEALTH = 5.0;
    public static final double DEFAULT_ATTACK = 6.0;

    public ZombieToast(Position position, double health, double attack) {
        super(position, health, attack);
        this.setStrategy(new MobInvisPlayerMove());
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        GameMap map = game.getMap();
        this.setStrategy(new MobInvisPlayerMove());
        if (map.getPlayer().getEffectivePotion() instanceof InvincibilityPotion) {
            this.setStrategy(new MobInvinsibilitymove());
        }
        nextPos = this.getStrategy().moveEnemy(map, this, null);
        game.getMap().moveTo(this, nextPos);

    }

}

package dungeonmania.entities.enemies;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.battles.Battleable;
import dungeonmania.entities.Destroyable;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Overlapable;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public abstract class Enemy extends Entity implements Battleable, Destroyable, Overlapable {
    private BattleStatistics battleStatistics;
    private EnemyMoveStrategies strategy;

    public EnemyMoveStrategies getStrategy() {
        return strategy;
    }

    public void setStrategy(EnemyMoveStrategies strategy) {
        this.strategy = strategy;
    }

    public Enemy(Position position, double health, double attack) {
        super(position.asLayer(Entity.CHARACTER_LAYER));
        battleStatistics = new BattleStatistics(health, attack, 0, BattleStatistics.DEFAULT_DAMAGE_MAGNIFIER,
                BattleStatistics.DEFAULT_ENEMY_DAMAGE_REDUCER);
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        return entity instanceof Player;
    }

    @Override
    public BattleStatistics getBattleStatistics() {
        return battleStatistics;
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.getGame().battle(player, this);
            if (this.getBattleStatistics().getHealth() <= 0) {
                player.addEnemyKilled();
            }
        }

    }

    @Override
    public void onDestroy(GameMap map) {
        Game g = map.getGame();
        g.unsubscribe(getId());
    }

    public abstract void move(Game game);

}

package dungeonmania.goals;

import java.util.List;

import dungeonmania.Game;
import dungeonmania.entities.Entity;
import dungeonmania.entities.enemies.ZombieToastSpawner;

public class EnemyGoal extends Goal {
    private int target;

    public EnemyGoal(int target) {
        this.target = target;
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        List<Entity> entities = game.getMap().getEntities();
        for (Entity ent : entities) {
            if (ent instanceof ZombieToastSpawner) {
                if (((ZombieToastSpawner) ent).isActive()) {
                    return false;
                }
            }
        }
        return (game.getPlayer().getEnemiesKilled() >= target);
    }

    public String toString(Game game) {
        if (this.achieved(game)) {
            return "";
        }
        return ":enemies";
    }
}

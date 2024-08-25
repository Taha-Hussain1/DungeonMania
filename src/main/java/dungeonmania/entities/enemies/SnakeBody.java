package dungeonmania.entities.enemies;

import java.util.LinkedList;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SnakeBody extends SnakeHead {
    private SnakeHead parent;
    private SnakeHead lead;

    public SnakeBody(Position position, double health, double attack, boolean isinvis, boolean isinvinc,
            SnakeHead parent, SnakeHead lead) {
        super(position, health, attack, lead, null);
        this.setStrategy(new SnakeBodyMove());
        this.setChild(null);
        this.parent = parent;
        this.lead = lead;

    }

    public SnakeHead getParent() {
        return parent;
    }

    public void move(Game game) {
        return;
    }

    public void follow(Game game) {
        Position nextPos;
        this.setPrevpos(this.getPosition());
        nextPos = parent.getPrevpos();
        game.getMap().moveTo(this, nextPos);
    }

    public void convertToHead(LinkedList<SnakeHead> children, GameMap map) {
        BattleStatistics snakeStats = this.getBattleStatistics();
        SnakeHead immchil = this.getChild();
        LinkedList<SnakeHead> childList = null;
        if (immchil != null) {
            childList = lead.getMychildren(immchil);
        }
        Position newPos = this.getPosition();
        SnakeHead newLeader = new SnakeHead(newPos, snakeStats.getHealth(), snakeStats.getAttack(), this.lead,
                childList);
        if (immchil != null) {
            newLeader.setChild(immchil.getChild());
        } else {
            newLeader.setChild(null);
        }
        this.parent.setChild(null);
        map.addEntity(newLeader);
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.getGame().battle(player, this);
        }
        if (!this.isInvincible() && this.getBattleStatistics().getHealth() <= 0) {
            LinkedList<SnakeHead> children = this.lead.getMychildren(this);
            for (SnakeHead snakes : children) {
                if (!snakes.equals(this)) {
                    map.destroyEntity(snakes);
                }
            }
            map.destroyEntity(this);
        } else if (this.isInvincible() && this.getBattleStatistics().getHealth() <= 0) {
            if (this.getChild() != null) {
                ((SnakeBody) this).parent.setChild(null);
                ((SnakeBody) this.getChild()).convertToHead(lead.getChildren(), map);
            }
            lead.getChildren().remove(this);
            map.destroyEntity(this);
        }
    }
}

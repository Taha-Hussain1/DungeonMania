package dungeonmania.entities.enemies;

import java.util.LinkedList;
import java.util.List;

import dungeonmania.Game;
import dungeonmania.battles.BattleStatistics;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.Wall;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.potions.InvincibilityPotion;
import dungeonmania.entities.collectables.potions.InvisibilityPotion;
import dungeonmania.map.GameMap;
import dungeonmania.util.Position;

public class SnakeHead extends Enemy {
    private boolean isInvincible;
    private boolean isInvisible;
    private double treasureBuff;
    private double keyBuff;
    private double arrowBuff;
    public static final double DEFAULT_ATTACK = 5.0;
    public static final double DEFAULT_HEALTH = 10.0;
    public static final double SNAKE_HEALTH_TREASURE_BUFF = 1;
    public static final double SNAKE_HEALTH_KEY_BUFF = 1;
    public static final double SNAKE_ATTACK_ARROW_BUFF = 1;
    private Position prevpos;

    private LinkedList<SnakeHead> children = new LinkedList<SnakeHead>();
    private SnakeHead child;

    public SnakeHead(Position position, double attack, double health, double treasure, double key, double arrow) {
        super(position, health, attack);
        this.setStrategy(new SnakeHeadMove());
        this.isInvincible = false;
        this.isInvisible = false;
        this.children.add(this);
        this.treasureBuff = treasure;
        this.keyBuff = key;
        this.arrowBuff = arrow;
    }

    public SnakeHead(Position position, double health, double attack, SnakeHead original,
            LinkedList<SnakeHead> children) {
        super(position, health, attack);
        this.setStrategy(new SnakeHeadMove());
        this.isInvincible = original.isInvincible;
        this.isInvisible = original.isInvisible;
        this.treasureBuff = original.treasureBuff;
        this.arrowBuff = original.arrowBuff;
        this.keyBuff = original.keyBuff;
        this.children = children;
    }

    public LinkedList<SnakeHead> getChildren() {
        return children;
    }

    public boolean isInvincible() {
        return isInvincible;
    }

    public boolean isInvisible() {
        return isInvisible;
    }

    public Position getPrevpos() {
        return prevpos;
    }

    public void setPrevpos(Position prevpos) {
        this.prevpos = prevpos;
    }

    public SnakeHead getChild() {
        return child;
    }

    public void setChild(SnakeHead child) {
        this.child = child;
    }

    @Override
    public boolean canMoveOnto(GameMap map, Entity entity) {
        if (this.isInvisible) {
            if (this instanceof SnakeBody) {
                return !(entity instanceof SnakeHead);
            } else {
                return !(this.getChildren().contains(entity));
            }
        }
        return !(entity instanceof SnakeHead || entity instanceof Wall);
    }

    public void snakeOverlap(Entity entity, GameMap map) {
        BattleStatistics snakeStats = this.getBattleStatistics();
        if (entity instanceof Treasure) {
            snakeStats.setHealth(snakeStats.getHealth() + treasureBuff);
        } else if (entity instanceof Key) {
            snakeStats.setHealth((snakeStats.getHealth() * keyBuff));
        } else if (entity instanceof Arrow) {
            snakeStats.setAttack((snakeStats.getAttack() + arrowBuff));
        } else if (entity instanceof InvincibilityPotion) {
            this.isInvincible = true;
        } else if (entity instanceof InvisibilityPotion) {
            this.isInvisible = true;
        }
        this.buffChildren(snakeStats.getHealth(), snakeStats.getAttack());
        this.addBody(map);
        return;
    }

    private void buffChildren(double newHealth, double newAttack) {
        for (SnakeHead child : children) {
            child.getBattleStatistics().setHealth(newHealth);
            child.getBattleStatistics().setAttack(newAttack);

        }
    }

    @Override
    public void move(Game game) {
        Position nextPos;
        this.setPrevpos(this.getPosition());
        GameMap map = game.getMap();
        if (this.isInvisible) {
            this.setStrategy(new SnakeInvisibleMove());
        } else {
            this.setStrategy(new SnakeHeadMove());
        }
        nextPos = this.getStrategy().moveEnemy(map, this, null);
        if (nextPos != null) {
            game.getMap().moveTo(this, nextPos);
        }
        for (SnakeHead child : children) {
            if (child instanceof SnakeBody) {
                ((SnakeBody) child).follow(game);
            }
        }

    }

    public void overlappedPlayer(GameMap map) {
        deadSnaketest(map);
    }

    @Override
    public void onOverlap(GameMap map, Entity entity) {
        if (entity instanceof Player) {
            Player player = (Player) entity;
            map.getGame().battle(player, this);
        }
        deadSnaketest(map);
    }

    private void deadSnaketest(GameMap map) {
        if (!this.isInvincible && this.getBattleStatistics().getHealth() <= 0) {
            for (SnakeHead snakes : children) {
                if (snakes instanceof SnakeBody) {
                    map.destroyEntity(snakes);

                }
            }
            map.destroyEntity(this);
        } else if (this.isInvincible && this.getBattleStatistics().getHealth() <= 0) {
            if (this.getChild() != null) {
                ((SnakeBody) this.getChild()).convertToHead(children, map);

            }
            map.destroyEntity(this);
        }
    }

    private void addBody(GameMap map) {
        BattleStatistics snakeStats = this.getBattleStatistics();
        SnakeHead last = children.getLast();
        SnakeBody child = new SnakeBody(last.getPrevpos(), snakeStats.getHealth(), snakeStats.getAttack(), isInvisible,
                isInvincible, last, this);
        map.addEntity(child);
        last.setChild(child);
        children.add(child);
    }

    public LinkedList<SnakeHead> getMychildren(SnakeHead snake) {
        int index = children.indexOf(snake);
        int last = children.indexOf(children.getLast());
        List<SnakeHead> kids = children.subList(index, last + 1);
        LinkedList<SnakeHead> results = new LinkedList<>();
        results.addAll(kids);
        children.get(index - 1).setChild(null);
        for (SnakeHead child : children) {
            if (kids.contains(child)) {
                children.remove(child);
            }
        }
        return results;
    }

}

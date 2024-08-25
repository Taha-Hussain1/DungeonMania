package dungeonmania.goals;

import dungeonmania.Game;

public class TreasureGoal extends Goal {
    private int target;

    public TreasureGoal(int target) {
        this.target = target;
    }

    public boolean achieved(Game game) {
        if (game.getPlayer() == null)
            return false;
        return game.getCollectedTreasureCount() >= target;
    }

    public String toString(Game game) {
        if (this.achieved(game))
            return "";
        return ":treasure";
    }
}

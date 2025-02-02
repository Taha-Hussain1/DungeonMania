package dungeonmania.battles;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import dungeonmania.Game;
import dungeonmania.entities.BattleItem;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Player;
import dungeonmania.entities.collectables.potions.Potion;
import dungeonmania.entities.enemies.Enemy;
import dungeonmania.entities.enemies.Mercenary;
import dungeonmania.entities.inventory.InventoryItem;
import dungeonmania.response.models.BattleResponse;
import dungeonmania.response.models.ResponseBuilder;
import dungeonmania.util.NameConverter;

public class BattleFacade {
    private List<BattleResponse> battleResponses = new ArrayList<>();

    public void battle(Game game, Player player, Enemy enemy) {
        // 0. init
        double initialPlayerHealth = player.getBattleStatistics().getHealth();
        double initialEnemyHealth = enemy.getBattleStatistics().getHealth();
        String enemyString = NameConverter.toSnakeCase(enemy);

        // 1. apply buff provided by the game and player's inventory
        // getting buffing amount
        List<BattleItem> battleItems = new ArrayList<>();
        BattleStatistics playerBuff = new BattleStatistics(0, 0, 0, 1, 1);
        List<Mercenary> mercs = game.getMap().getEntities(Mercenary.class);
        playerBuff = applyBuff(player, playerBuff, battleItems, mercs);

        // 2. Battle the two stats and update health
        BattleStatistics playerBattleStatistics = BattleStatistics.applyBuff(player.getBattleStatistics(), playerBuff);
        BattleStatistics enemyBattleStatistics = enemy.getBattleStatistics();
        if (!playerBattleStatistics.isEnabled() || !enemyBattleStatistics.isEnabled())
            return;
        List<BattleRound> rounds = BattleStatistics.battle(playerBattleStatistics, enemyBattleStatistics);

        // 3. update health to the actual statistics and call to decrease durability of items
        updateStatistics(game, player, enemy, playerBattleStatistics, enemyBattleStatistics, battleItems);

        // 4. Log the battle - solidate it to be a battle response
        battleResponses.add(new BattleResponse(enemyString,
                rounds.stream().map(ResponseBuilder::getRoundResponse).collect(Collectors.toList()),
                battleItems.stream().map(Entity.class::cast).map(ResponseBuilder::getItemResponse)
                        .collect(Collectors.toList()),
                initialPlayerHealth, initialEnemyHealth));
    }

    public BattleStatistics applyBuff(Player player, BattleStatistics playerBuff, List<BattleItem> battleItems,
            List<Mercenary> mercs) {
        Potion effectivePotion = player.getEffectivePotion();
        if (effectivePotion != null) {
            playerBuff = player.applyBuff(playerBuff);
        } else {
            for (BattleItem item : player.getInventory().getEntities(BattleItem.class)) {
                if (item instanceof Potion)
                    continue;
                playerBuff = item.applyBuff(playerBuff);
                battleItems.add(item);
            }
        }
        for (Mercenary merc : mercs) {
            if (!merc.isAllied())
                continue;
            playerBuff = BattleStatistics.applyBuff(playerBuff, merc.getBattleStatistics());
        }
        return playerBuff;
    }

    public void updateStatistics(Game game, Player player, Enemy enemy, BattleStatistics playerBattleStatistics,
            BattleStatistics enemyBattleStatistics, List<BattleItem> battleItems) {
        // update health
        player.getBattleStatistics().setHealth(playerBattleStatistics.getHealth());
        enemy.getBattleStatistics().setHealth(enemyBattleStatistics.getHealth());

        // call to decrease durability of items
        for (BattleItem item : battleItems) {
            if (item instanceof InventoryItem)
                item.use(game);
        }
    }

    public List<BattleResponse> getBattleResponses() {
        return battleResponses;
    }
}

package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Key;
import dungeonmania.entities.collectables.Treasure;
import dungeonmania.entities.collectables.Wood;

public class ShieldBuildStrategy implements BuildStrategy {
    @Override
    public String canBuild(Inventory inv) {
        int wood = inv.count(Wood.class);
        int treasures = inv.count(Treasure.class);
        int keys = inv.count(Key.class);
        return wood >= 2 && (treasures >= 1 || keys >= 1) ? "shield" : null;
    }

    @Override
    public InventoryItem build(Inventory inv, boolean remove, List<InventoryItem> items, EntityFactory factory) {
        List<Wood> wood = inv.getEntities(Wood.class);
        List<Treasure> treasures = inv.getEntities(Treasure.class);
        List<Key> keys = inv.getEntities(Key.class);

        if (this.canBuild(inv) != null) {
            if (remove) {
                items.remove(wood.get(0));
                items.remove(wood.get(1));
                if (treasures.size() >= 1) {
                    items.remove(treasures.get(0));
                } else {
                    items.remove(keys.get(0));
                }
            }
            return factory.buildShield();
        }

        return null;
    }
}

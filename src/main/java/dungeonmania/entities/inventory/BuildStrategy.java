package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.EntityFactory;

public interface BuildStrategy {
    String canBuild(Inventory inv);

    InventoryItem build(Inventory inv, boolean remove, List<InventoryItem> items, EntityFactory factory);
}

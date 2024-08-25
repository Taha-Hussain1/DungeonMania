package dungeonmania.entities.inventory;

import java.util.List;

import dungeonmania.entities.EntityFactory;
import dungeonmania.entities.collectables.Arrow;
import dungeonmania.entities.collectables.Wood;

public class BowBuildStrategy implements BuildStrategy {
    @Override
    public String canBuild(Inventory inv) {
        int wood = inv.count(Wood.class);
        int arrows = inv.count(Arrow.class);
        return wood >= 1 && arrows >= 3 ? "bow" : null;
    }

    @Override
    public InventoryItem build(Inventory inv, boolean remove, List<InventoryItem> items, EntityFactory factory) {
        List<Wood> wood = inv.getEntities(Wood.class);
        List<Arrow> arrows = inv.getEntities(Arrow.class);

        if (this.canBuild(inv) != null) {
            if (remove) {
                items.remove(wood.get(0));
                items.remove(arrows.get(0));
                items.remove(arrows.get(1));
                items.remove(arrows.get(2));
            }
            return factory.buildBow();
        }

        return null;
    }

}

package dungeonmania.util;

import java.util.Arrays;
import java.util.Iterator;

import dungeonmania.entities.Door;
import dungeonmania.entities.Entity;
import dungeonmania.entities.Portal;
import dungeonmania.entities.logical.LightBulb;
import dungeonmania.entities.logical.LogicalBomb;
import dungeonmania.entities.logical.SwitchDoor;


public class NameConverter {
    public static String toSnakeCase(Entity entity) {
        String nameBasic = toSnakeCase(entity.getClass().getSimpleName());
        if (entity instanceof Portal) {
            String color = "_" + ((Portal) entity).getColor().toLowerCase();
            return nameBasic + color;
        }
        if (entity instanceof Door) {
            String open = ((Door) entity).isOpen() ? "_open" : "";
            return nameBasic + open;
        }
        if (entity instanceof LightBulb) {
            return ((LightBulb) entity).isOn() ? "light_bulb_on" : "light_bulb_off";
        }
        if (entity instanceof SwitchDoor) {
            return ((SwitchDoor) entity).isOpen() ? "switch_door_open" : "switch_door";
        }
        if (entity instanceof LogicalBomb) {
            return "bomb";
        }
        return nameBasic;
    }

    public static String toSnakeCase(String name) {
        String[] words = name.split("(?=[A-Z])");
        if (words.length == 1)
            return words[0].toLowerCase();

        StringBuilder builder = new StringBuilder();
        Iterator<String> iter = Arrays.stream(words).iterator();
        builder.append(iter.next().toLowerCase());

        while (iter.hasNext())
            builder.append("_").append(iter.next().toLowerCase());

        return builder.toString();
    }

    public static String toSnakeCase(Class<?> clazz) {
        return toSnakeCase(clazz.getSimpleName());
    }
}

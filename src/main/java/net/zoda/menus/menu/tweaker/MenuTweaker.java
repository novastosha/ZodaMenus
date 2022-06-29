package net.zoda.menus.menu.tweaker;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import net.zoda.menus.menu.base.arguments.ArgumentType;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

@RequiredArgsConstructor
public enum MenuTweaker {


    ANVIL(false, InventoryType.ANVIL, "anvil", new SectionValue[]{
            new SectionValue("repair-cost", ArgumentType.INTEGER, true)
    }, AnvilInventory.class, new MenuTweakerImplementation() {
        @Override
        public void run(ConfigurationSection section, Inventory inventory) {
            AnvilInventory anvilInventory = (AnvilInventory) inventory;

            int xp_required = section.getInt("repair-cost");

            anvilInventory.setRepairCost(xp_required);
        }

        @Override
        public boolean checkValues(Logger logger, ConfigurationSection section) {
            return true;
        }
    });

    public static Map<InventoryType,MenuTweaker> mapToInventoryType() {
        Map<InventoryType,MenuTweaker> map  = new HashMap<>();
        for(MenuTweaker tweaker : values()) {
            map.put(tweaker.inventoryType,tweaker);
        }
        return map;
    }


    public final boolean required;
    public final InventoryType inventoryType;
    public final String sectionName;
    public final SectionValue[] sectionValues;
    public final Class<? extends Inventory> inventoryClass;
    public final MenuTweakerImplementation implementation;


    public interface MenuTweakerImplementation {
        void run(ConfigurationSection section, Inventory inventory);

        boolean checkValues(Logger logger, ConfigurationSection section);
    }

    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class SectionValue {
        public final String name;
        public final ArgumentType type;
        public final boolean required;
    }
}

package net.zoda.menus.menu.loader;

import lombok.Getter;
import net.zoda.menus.menu.Menu;
import net.zoda.menus.menu.items.MenuItem;
import net.zoda.menus.menu.tweaker.MenuTweaker;
import net.zoda.menus.plugin.ZodaMenusPlugin;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.AnvilInventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public final class MenuLoader {

    @Getter
    @NotNull
    private final InventoryType[] DISALLOWED_INVENTORY_TYPES = new InventoryType[]{
            InventoryType.BARREL,
            InventoryType.PLAYER,
            InventoryType.CREATIVE,
            InventoryType.SHULKER_BOX,
            InventoryType.ENDER_CHEST,
    };

    @NotNull
    private final Logger logger = ZodaMenusPlugin.getPluginLogger();
    @Getter
    @NotNull
    private final FileFilter menuFileFilter = (file -> file.isFile() && file.getName().endsWith(".yml") && !file.getName().startsWith("-"));
    @NotNull
    private final Map<String, Menu> menus;


    public Map<String, Menu> getMenus() {
        return Collections.unmodifiableMap(menus);
    }

    private MenuLoader() {
        this.menus = new HashMap<>();
    }

    public void loadMenus(@NotNull File loadDir) {
        logger.info("Loading menus at: " + loadDir.getPath());
        if (!loadDir.isDirectory()) return;

        for (File file : Objects.requireNonNull(loadDir.listFiles(menuFileFilter))) {
            logger.info("Loading menu: " + file.getName());

            YamlConfiguration configuration = new YamlConfiguration();

            try {
                configuration.load(file);
            } catch (InvalidConfigurationException | IOException e) {
                logger.log(Level.SEVERE, "Couldn't load menu configuration: " + file.getName(), e);
                continue;
            }

            ConfigurationSection dataSection = configuration.getConfigurationSection("data");

            if (dataSection == null) {
                logger.severe("Data section missing from menu config: " + file.getName());
                continue;
            }

            if (!checkMissingData(dataSection, file)) continue;

            Map<InventoryType, MenuTweaker> tweakersMapped = MenuTweaker.mapToInventoryType();

            @NotNull String name = Objects.requireNonNull(dataSection.getString("name"));
            int rows = dataSection.getInt("rows", 0);

            @NotNull InventoryType inventoryType = InventoryType.valueOf(Objects.requireNonNull(dataSection.getString("type")).toUpperCase());

            @Nullable MenuTweaker tweaker = null;
            if (tweakersMapped.containsKey(inventoryType)) {
                tweaker = tweakersMapped.get(inventoryType);
            }

            if (tweaker != null) tweakerCheck: {
                if (!dataSection.isConfigurationSection(tweaker.sectionName))  {
                    if (tweaker.required) {
                        logger.severe("Menu tweaker section is missing from menu config: " + file.getName());
                        return;
                    }

                    break tweakerCheck;
                }

                @NotNull ConfigurationSection tweakerSection = dataSection.getConfigurationSection(tweaker.sectionName);
                for(MenuTweaker.SectionValue sectionValue : tweaker.sectionValues) {
                    if(!sectionValue.required) continue;

                    if(!tweakerSection.contains(sectionValue.name)) {
                        logger.severe("Menu tweaker value: "+sectionValue.name+" is missing from menu config: "+file.getName());
                        return;
                    }

                    Object obj = tweakerSection.get(sectionValue.name);

                    if(!obj.getClass().equals(sectionValue.type.clazz)) {
                        logger.severe("Menu tweaker value type mismatch: "+sectionValue.name+" (expected: "+sectionValue.type.clazz.getSimpleName()+" got: "+obj.getClass().getSimpleName()
                                +") from menu config: "+file.getName()
                        );
                        return;
                    }
                }

                if(!tweaker.implementation.checkValues(logger,tweakerSection)) {
                    logger.severe("Invalid values in tweaker section!");
                    return;
                }

            }

            boolean sharedMenu = dataSection.getBoolean("shared", false);
            @NotNull String title = Objects.requireNonNull(dataSection.getString("title"));

            ConfigurationSection itemsSection = configuration.getConfigurationSection("items");

            MenuItem[] items = new MenuItem[0];

            if (itemsSection != null) {
                items = loadItems(itemsSection);
            }

            menus.put(name, new Menu(title, inventoryType, name, rows, sharedMenu));
        }
    }

    private MenuItem[] loadItems(ConfigurationSection itemsSection) {
        List<E>
        for (String key : itemsSection.getKeys(false)) {

        }
        return new MenuItem[0];
    }

    private boolean checkMissingData(@NotNull ConfigurationSection dataSection, @NotNull File file) {
        if (!dataSection.contains("name")) {
            logger.severe("Missing menu \"name\" from menu config: " + file.getName());
            return false;
        }

        if (!dataSection.contains("title")) {
            logger.severe("Missing title from menu config: " + file.getName());
            return false;
        }

        if (!dataSection.contains("type")) {
            logger.severe("Missing inventory type from menu config: " + file.getName());
            return false;
        } else {
            try {
                InventoryType inventoryType = InventoryType.valueOf(dataSection.getString("type").toUpperCase());

                if (List.of(DISALLOWED_INVENTORY_TYPES).contains(inventoryType)) {
                    logger.severe("Inventory type: " + dataSection.getString("type").toUpperCase() + " is not allowed!");
                    return false;
                }

                if (inventoryType.equals(InventoryType.CHEST)) {
                    if (!dataSection.contains("rows")) {
                        logger.severe("Missing menu \"rows\" from menu config: " + file.getName());
                        return false;
                    } else if (dataSection.getInt("rows") < 1) {
                        logger.severe("Rows number must be bigger or equal to 1 from menu config: " + file.getName());
                        return false;
                    }
                }
            } catch (IllegalArgumentException e) {
                logger.severe("Unknown inventory type: " + dataSection.getString("type").toUpperCase());
                return false;
            }
        }

        return true;
    }

    private static MenuLoader instance;

    public static MenuLoader getInstance() {
        if (instance == null) instance = new MenuLoader();
        return instance;
    }
}

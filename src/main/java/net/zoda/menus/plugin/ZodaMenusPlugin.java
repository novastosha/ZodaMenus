package net.zoda.menus.plugin;

import lombok.Getter;
import net.zoda.menus.menu.actions.SendMessageAction;
import net.zoda.menus.menu.actions.loader.ActionLoader;
import net.zoda.menus.menu.loader.MenuLoader;
import net.zoda.menus.utils.Files;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.logging.Logger;

public class ZodaMenusPlugin extends JavaPlugin {

    @Getter private static Logger pluginLogger;

    @Getter private static ZodaMenusPlugin instance;

    @Override
    public void onEnable() {
        instance = this;
        pluginLogger = getLogger();

        for (Files file : Files.values()) {
            if (file.isToCreate()) {
                if (file.isFile()) {
                    try {
                        file.getFile().createNewFile();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    continue;
                }
                file.getFile().mkdirs();
            }
        }

        ActionLoader.getInstance().register(SendMessageAction.class);
        MenuLoader.getInstance().loadMenus(Files.MENUS_FOLDER.getFile());
    }
}

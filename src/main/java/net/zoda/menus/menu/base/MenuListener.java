package net.zoda.menus.menu.base;

import net.zoda.menus.menu.Menu;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public interface MenuListener {

    void onMenuOpen(Menu menu, @Nullable Player player);
    void onMenuClose(Menu menu, @Nullable Player player);

}

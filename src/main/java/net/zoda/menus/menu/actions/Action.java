package net.zoda.menus.menu.actions;


import net.zoda.menus.menu.Menu;
import net.zoda.menus.menu.base.loader.Type;
import net.zoda.menus.menu.items.MenuItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Action extends Type {

    void onClick(MenuItem item, Menu menu, @NotNull Player player);
}

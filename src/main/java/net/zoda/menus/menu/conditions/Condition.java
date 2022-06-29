package net.zoda.menus.menu.conditions;

import net.zoda.menus.menu.Menu;
import net.zoda.menus.menu.base.loader.Type;
import net.zoda.menus.menu.items.MenuItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface Condition extends Type {

    boolean check(@NotNull Player player, Menu menu, MenuItem item);

}

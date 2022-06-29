package net.zoda.menus.menu.items;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.zoda.menus.menu.Menu;
import net.zoda.menus.menu.actions.Action;
import net.zoda.menus.menu.base.MenuListener;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public class MenuItem implements MenuListener {

    @Getter @Setter
    private Menu menu;

    @Getter @Setter(AccessLevel.PROTECTED) private ItemStack itemStack;
    @Getter private final Action[] clickActions;

    @Override
    public void onMenuOpen(Menu menu, @Nullable Player player) {
    }

    @Override
    public void onMenuClose(Menu menu, @Nullable Player player) {
    }
}

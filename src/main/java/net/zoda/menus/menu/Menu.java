package net.zoda.menus.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@RequiredArgsConstructor
public final class Menu {

    @Getter

    @NotNull
    private final String title;
    @Getter
    @NotNull
    private final InventoryType type;

    @Getter
    @NotNull
    private final String name;
    @Getter
    private final int rows;
    @Getter
    private final boolean shared;
    @Getter
    @Nullable
    private final Inventory sharedInventory = createInventory(null);

    private Inventory createInventory(@Nullable Player player) {
        Inventory newInventory = type == InventoryType.CHEST
                ? Bukkit.createInventory(null, rows * 9, Component.text(title))
                : Bukkit.createInventory(null, type, Component.text(title));

        return newInventory;
    }

    public void open(Player player) {

    }

}

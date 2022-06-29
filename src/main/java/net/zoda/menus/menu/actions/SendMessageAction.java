package net.zoda.menus.menu.actions;

import lombok.RequiredArgsConstructor;
import net.zoda.menus.menu.Menu;
import net.zoda.menus.menu.base.arguments.ArgumentType;
import net.zoda.menus.menu.base.arguments.TypeInfo;
import net.zoda.menus.menu.base.arguments.array.ArrayVerifier;
import net.zoda.menus.menu.items.MenuItem;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

@TypeInfo(name = "send_message", displayName = "Send Message",
        arguments = {
        @TypeInfo.ArgumentInfo(type = ArgumentType.STRING, name = "message"),
        @TypeInfo.ArgumentInfo(type = ArgumentType.ARRAY, name = "some_2d_array")
}
)
@RequiredArgsConstructor
public class SendMessageAction implements Action {

    private final String message;
    @ArrayVerifier(length = 5)
    private final int[][] some_2d_array;

    @Override
    public void onClick(MenuItem item, Menu menu, @NotNull Player player) {
        player.sendMessage(message);
    }
}

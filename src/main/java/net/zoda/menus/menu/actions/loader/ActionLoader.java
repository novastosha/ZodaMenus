package net.zoda.menus.menu.actions.loader;

import net.zoda.menus.menu.actions.Action;
import net.zoda.menus.menu.base.loader.TypeLoader;

public final class ActionLoader extends TypeLoader<Action> {

    private static ActionLoader instance;

    public static ActionLoader getInstance() {
        if(instance == null) instance = new ActionLoader();
        return instance;
    }
}

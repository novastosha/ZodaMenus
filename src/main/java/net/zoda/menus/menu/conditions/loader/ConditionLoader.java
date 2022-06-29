package net.zoda.menus.menu.conditions.loader;

import net.zoda.menus.menu.actions.loader.ActionLoader;
import net.zoda.menus.menu.base.loader.TypeLoader;
import net.zoda.menus.menu.conditions.Condition;

public final class ConditionLoader extends TypeLoader<Condition> {

    private static ActionLoader instance;

    public static ActionLoader getInstance() {
        if(instance == null) instance = new ActionLoader();
        return instance;
    }

}

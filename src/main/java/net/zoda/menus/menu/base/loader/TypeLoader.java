package net.zoda.menus.menu.base.loader;

import net.zoda.menus.menu.base.arguments.ArgumentType;
import net.zoda.menus.menu.base.arguments.TypeInfo;
import net.zoda.menus.plugin.ZodaMenusPlugin;
import net.zoda.menus.utils.Pair;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;

public abstract class TypeLoader<T extends Type> {

    @NotNull
    private final Logger logger = ZodaMenusPlugin.getPluginLogger();
    @NotNull
    private final Map<String, Pair<Class<? extends T>, TypeInfo>> registeredTypes = new HashMap<>();

    public Map<String, Pair<Class<? extends T>, TypeInfo>> getRegisteredTypes() {
        return Collections.unmodifiableMap(registeredTypes);
    }

    protected TypeLoader() {
    }

    public void register(Class<? extends T> actionClass) {
        if (!actionClass.isAnnotationPresent(TypeInfo.class)) {
            logger.severe("Missing TypeInfo annotation from action: " + actionClass.getSimpleName());
            return;
        }

        TypeInfo actionInfo = actionClass.getAnnotation(TypeInfo.class);
        List<Class<?>> constructorParameters = new ArrayList<>();
        Map<Class<?>, ArgumentType> typesMapped = ArgumentType.mapToClass();

        for (TypeInfo.ArgumentInfo info : actionInfo.arguments()) {
            if (info.inConstructor()) {
                constructorParameters.add(info.type().clazz);
            }
            if (info.ignoreCheck()) continue;

            try {
                Field field = actionClass.getDeclaredField(info.name());

                if (info.type().equals(ArgumentType.ARRAY)) {
                    if (!field.getType().isArray()) {
                        logger.severe("Type mismatch with argument field: " + info.name() + " (expected: array got: " + field.getType().getSimpleName());
                        return;
                    }

                    if (!typesMapped.containsKey(field.getType().getComponentType())) {
                        logger.severe("Arrays only accept existing argument types, on argument: " + info.name());
                        return;
                    }
                }

                if (!field.getType().isAssignableFrom(info.type().clazz)
                        || !info.type().clazz.isAssignableFrom(field.getType())) {
                    logger.severe("Type mismatch with argument field: " + info.name() + " (expected: " + info.type().clazz.getCanonicalName() + " got: " + field.getType().getCanonicalName() + ")");
                    return;
                }

            } catch (NoSuchFieldException ignored) {
                logger.severe("Argument: " + info.name() + " is not implemented as a field in: " + actionClass.getSimpleName());
                return;
            }
        }

        try {
            actionClass.getDeclaredConstructor(constructorParameters.toArray(new Class[0]));
        } catch (NoSuchMethodException e) {
            logger.severe("Cannot find constructor: " + actionClass.getSimpleName() + formClasses(constructorParameters));
            return;
        }

        logger.info("Registered action: " + actionInfo.displayName());
        registeredTypes.put(actionInfo.name(), new Pair<>(actionClass, actionInfo));
    }

    private String formClasses(List<Class<?>> constructorParameters) {
        StringBuilder builder = new StringBuilder("(");
        for (Class<?> clazz : constructorParameters) {
            builder.append(clazz.getCanonicalName()).append((constructorParameters.indexOf(clazz) == constructorParameters.size()) ? "" : ", ");
        }
        return builder.append(")").toString();
    }


}

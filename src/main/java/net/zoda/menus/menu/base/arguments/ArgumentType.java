package net.zoda.menus.menu.base.arguments;

import lombok.RequiredArgsConstructor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@RequiredArgsConstructor
public enum ArgumentType {

    STRING((ConfigConverter<String>) (rawData,logger) -> (String) rawData, String.class),
    INTEGER((ConfigConverter<Integer>) (rawData,logger) -> (Integer) rawData,Integer.class),
    BOOLEAN((ConfigConverter<Boolean>) (rawData,logger) -> (Boolean) rawData,Boolean.class),
    ITEM((ConfigConverter<ItemStack>) (rawData,logger) -> (ItemStack) rawData,ItemStack.class),
    FLOAT((ConfigConverter<Float>) (rawData,logger) -> ((Double) rawData).floatValue(),Float.class),
    DOUBLE((ConfigConverter<Double>) (rawData,logger) -> (Double) rawData,Double.class),
    LOCATION((ConfigConverter<Location>) (rawData,logger) -> (Location) rawData,Location.class),
    ARRAY((ConfigConverter<Object[]>) (rawData, logger) -> {
        if(!(rawData instanceof ConfigurationSection config)) {
            logger.warning("Input is not a configuration section!");
            return null;
        }

        if(!config.contains("type")) {
            logger.warning("Config is missing array type!");
            return null;
        }

        List<Object> objects = new ArrayList<>();
        int index = 0;
        try {
            ArgumentType actionArgument = ArgumentType.valueOf(config.getString("type").toUpperCase());

            // Evade "illegal self reference" compile error
            if (actionArgument.equals(ArgumentType.valueOf("ARRAY"))) {
                throw new RuntimeException("Multi-dimensional arrays are not supported, yet.");
            }

            for(String key : config.getKeys(false)) {
                if(key.equalsIgnoreCase("type")) continue;

                objects.add(actionArgument.configConverter.convert(config.get(key),logger));
                index++;
            }

            return objects.toArray(new Object[0]);
        }catch (IllegalArgumentException exception) {
            logger.warning("Unknown argument type: "+config.getString("type").toUpperCase());
            return null;
        }catch (Exception e) {
            logger.warning("Unable to convert element at index: "+index);
            return null;
        }
    },Object[].class);

    public interface ConfigConverter<T> {
        T convert(Object rawData, Logger logger);
    }

    public final ConfigConverter<?> configConverter;
    public final Class<?> clazz;

    public static Map<Class<?>,ArgumentType> mapToClass() {
        Map<Class<?>,ArgumentType> map = new HashMap<>();

        for(ArgumentType type : values()) {
            map.put(type.clazz,type);
        }

        return map;
    }
}

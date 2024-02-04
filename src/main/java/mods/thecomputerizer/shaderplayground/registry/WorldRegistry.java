package mods.thecomputerizer.shaderplayground.registry;

import mods.thecomputerizer.shaderplayground.registry.dimensions.PlaygroundType;
import net.minecraft.world.WorldType;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class WorldRegistry {

    public static final List<WorldType> ALL_WORLD_TYPES = new ArrayList<>();
    public static final WorldType PLAYGROUND = makeWorldType("playground_world",PlaygroundType::new);

    @SuppressWarnings("SameParameterValue")
    private static <W extends WorldType> W makeWorldType(String name, Function<String,W> constructor) {
        W type = constructor.apply(name);
        ALL_WORLD_TYPES.add(type);
        return type;
    }
}

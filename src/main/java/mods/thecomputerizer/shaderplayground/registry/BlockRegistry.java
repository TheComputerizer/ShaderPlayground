package mods.thecomputerizer.shaderplayground.registry;

import mods.thecomputerizer.shaderplayground.core.SPRef;
import net.minecraft.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class BlockRegistry {

    private static final List<Block> ALL_BLOCKS = new ArrayList<>();

    @SuppressWarnings("SameParameterValue")
    private static Block makeBlock(final String name, final Supplier<Block> constructor, final Consumer<Block> config) {
        final Block block = constructor.get();
        config.accept(block);
        block.setRegistryName(SPRef.MODID, name);
        block.setTranslationKey(SPRef.MODID+"."+name);
        ALL_BLOCKS.add(block);
        return block;
    }

    public static Block[] getBlocks() {
        return ALL_BLOCKS.toArray(new Block[0]);
    }
}

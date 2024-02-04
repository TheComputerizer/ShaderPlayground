package mods.thecomputerizer.shaderplayground.registry;

import mods.thecomputerizer.shaderplayground.core.SPRef;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static mods.thecomputerizer.shaderplayground.registry.RegistryHandler.SHADER_PLAYGROUND_TAB;

@SuppressWarnings({"unused","SameParameterValue"})
@ParametersAreNonnullByDefault
public final class ItemRegistry {

    private static final List<Item> ALL_ITEMS = new ArrayList<>();

    private static <I extends Item> I makeItem(final String name, final Supplier<I> constructor) {
        return makeItem(name,constructor,null);
    }

    private static <I extends Item> I makeItem(
            final String name, final Supplier<I> constructor, final @Nullable Consumer<I> config) {
        final I item = constructor.get();
        item.setCreativeTab(SHADER_PLAYGROUND_TAB);
        item.setMaxStackSize(1);
        item.setTranslationKey(SPRef.MODID+"."+name);
        item.setRegistryName(SPRef.MODID, name);
        if(Objects.nonNull(config)) config.accept(item);
        ALL_ITEMS.add(item);
        return item;
    }

    private static ItemBlock makeEpicItemBlock(final Block constructor) {
        return makeEpicItemBlock(constructor,null);
    }

    private static ItemBlock makeEpicItemBlock(final Block constructor, final @Nullable Consumer<ItemBlock> config) {
        final ItemBlock item = new ItemBlock(constructor);
        item.setCreativeTab(SHADER_PLAYGROUND_TAB);
        item.setMaxStackSize(1);
        item.setRegistryName(Objects.requireNonNull(constructor.getRegistryName()));
        item.setTranslationKey(constructor.getTranslationKey());
        if(Objects.nonNull(config)) config.accept(item);
        ALL_ITEMS.add(item);
        return item;
    }

    public static Item[] getItems() {
        return ALL_ITEMS.toArray(new Item[0]);
    }
}

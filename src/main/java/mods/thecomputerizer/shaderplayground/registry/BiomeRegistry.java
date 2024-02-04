package mods.thecomputerizer.shaderplayground.registry;

import mods.thecomputerizer.shaderplayground.core.SPRef;
import mods.thecomputerizer.shaderplayground.registry.biomes.PlaygroundBiome;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.common.BiomeDictionary;
import net.minecraftforge.common.BiomeDictionary.Type;
import net.minecraftforge.common.BiomeManager;
import net.minecraftforge.common.BiomeManager.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class BiomeRegistry {

    private static final Map<Biome,Consumer<Biome>> ALL_BIOMES = new HashMap<>();
    public static final PlaygroundBiome PLAYGROUND = makeBiome("playground_biome",PlaygroundBiome::new,biome -> {
        BiomeManager.addBiome(BiomeType.COOL,new BiomeEntry(biome,1));
        BiomeDictionary.addTypes(biome,Type.VOID);
    });

    @SuppressWarnings("SameParameterValue")
    private static <B extends Biome> B makeBiome(final String name, final Function<String,B> constructor,
                                                 final Consumer<Biome> onDictionaryInit) {
        final B biome = constructor.apply(name);
        biome.setRegistryName(SPRef.MODID,name);
        ALL_BIOMES.put(biome,onDictionaryInit);
        return biome;
    }

    public static Biome[] getBiomes() {
        return ALL_BIOMES.keySet().toArray(new Biome[0]);
    }

    public static void initBiomeInfo() {
        for(Map.Entry<Biome,Consumer<Biome>> entry : ALL_BIOMES.entrySet())
            entry.getValue().accept(entry.getKey());
    }
}

package mods.thecomputerizer.shaderplayground.registry.dimensions;

import mods.thecomputerizer.shaderplayground.registry.BiomeRegistry;
import mods.thecomputerizer.shaderplayground.world.generators.PlaygroundGenerator;
import net.minecraft.world.World;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

public class PlaygroundType extends WorldType {
    private final BiomeProvider biomes;

    public PlaygroundType(String name) {
        super(name);
        this.biomes = new BiomeProviderSingle(BiomeRegistry.PLAYGROUND);
    }

    public BiomeProvider getBiomeProvider(World world) {
        return this.biomes;
    }

    public boolean canBeCreated() {
        return false;
    }

    public IChunkGenerator getChunkGenerator(World world, String generatorOptions) {
        return new PlaygroundGenerator(world);
    }

    public float getCloudHeight() {
        return 1f;
    }
}

package mods.thecomputerizer.shaderplayground.world.providers;

import mods.thecomputerizer.shaderplayground.registry.BiomeRegistry;
import mods.thecomputerizer.shaderplayground.registry.DimensionRegistry;
import mods.thecomputerizer.shaderplayground.world.generators.PlaygroundGenerator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.DimensionType;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeProvider;
import net.minecraft.world.biome.BiomeProviderSingle;
import net.minecraft.world.gen.IChunkGenerator;

public class PlaygroundProvider extends WorldProvider {

    private final BiomeProvider biomes;

    public PlaygroundProvider() {
        this.biomes = new BiomeProviderSingle(BiomeRegistry.PLAYGROUND);
    }

    public DimensionType getDimensionType() {
        return DimensionRegistry.PLAYGROUND;
    }

    protected void init() {
        super.init();
        this.hasSkyLight = false;
    }

    public float calculateCelestialAngle(long time, float partialTicks) {
        return 0.5f;
    }

    public boolean isSurfaceWorld() {
        return false;
    }

    public boolean isSkyColored() {
        return false;
    }

    public double getVoidFogYFactor() {
        return 63d;
    }

    public boolean doesXZShowFog(int x, int z) {
        return true;
    }

    public BiomeProvider getBiomeProvider() {
        return this.biomes;
    }

    public boolean hasSkyLight() {
        return false;
    }

    public boolean shouldMapSpin(String entity, double x, double z, double rotation) {
        return true;
    }

    public WorldProvider.WorldSleepResult canSleepAt(EntityPlayer player, BlockPos pos) {
        return WorldSleepResult.BED_EXPLODES;
    }

    public Biome getBiomeForCoords(BlockPos pos) {
        return this.biomes.getBiome(pos);
    }

    public boolean isDaytime() {
        return false;
    }

    public float getSunBrightnessFactor(float par1) {
        return 0f;
    }

    public IChunkGenerator createChunkGenerator() {
        return new PlaygroundGenerator(this.world);
    }
}
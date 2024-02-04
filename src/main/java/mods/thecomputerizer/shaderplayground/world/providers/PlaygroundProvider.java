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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class PlaygroundProvider extends WorldProvider {

    private final BiomeProvider biomes;

    public PlaygroundProvider() {
        this.biomes = new BiomeProviderSingle(BiomeRegistry.PLAYGROUND);
    }

    @Override
    public DimensionType getDimensionType() {
        return DimensionRegistry.PLAYGROUND;
    }

    @Override
    protected void init() {
        super.init();
        this.hasSkyLight = false;
    }

    @Override
    public float calculateCelestialAngle(long time, float partialTicks) {
        return 0.5f;
    }

    @Override
    public boolean isSurfaceWorld() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean isSkyColored() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public double getVoidFogYFactor() {
        return 63d;
    }

    @Override
    public BiomeProvider getBiomeProvider() {
        return this.biomes;
    }

    @Override
    public boolean hasSkyLight() {
        return false;
    }

    @Override
    public boolean shouldMapSpin(String entity, double x, double z, double rotation) {
        return true;
    }

    @Override
    public WorldProvider.WorldSleepResult canSleepAt(EntityPlayer player, BlockPos pos) {
        return WorldSleepResult.BED_EXPLODES;
    }

    @Override
    public Biome getBiomeForCoords(BlockPos pos) {
        return this.biomes.getBiome(pos);
    }

    @Override
    public boolean isDaytime() {
        return false;
    }

    @Override
    public float getSunBrightnessFactor(float par1) {
        return 0f;
    }

    @Override
    public IChunkGenerator createChunkGenerator() {
        return new PlaygroundGenerator(this.world);
    }
}
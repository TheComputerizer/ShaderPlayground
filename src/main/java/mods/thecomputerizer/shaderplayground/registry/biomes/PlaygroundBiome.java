package mods.thecomputerizer.shaderplayground.registry.biomes;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeDecorator;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.feature.WorldGenAbstractTree;
import net.minecraft.world.gen.feature.WorldGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlaygroundBiome extends Biome {

    public PlaygroundBiome(String name) {
        super(new Biome.BiomeProperties(name).setRainDisabled().setWaterColor(2621537));
    }

    public BiomeDecorator createBiomeDecorator() {
        return new BiomeDecorator() {
            public void decorate(World world, Random rand, Biome biome, BlockPos pos) {}

            protected void genDecorations(Biome biome, World world, Random rand) {}

            protected void generateOres(World world, Random rand) {}

            protected void genStandardOre1(World world, Random rand, int blockCount, WorldGenerator generator, int minHeight, int maxHeight) {}

            protected void genStandardOre2(World world, Random rand, int blockCount, WorldGenerator generator, int centerHeight, int spread) {}
        };
    }

    public WorldGenAbstractTree getRandomTreeFeature(Random rand) {
        return new WorldGenAbstractTree(false) {
            public boolean generate(World world, Random rand, BlockPos pos) {
                return true;
            }

            protected void setDirtAt(World world, BlockPos pos) {}
        };
    }

    public WorldGenerator getRandomWorldGenForGrass(Random rand) {
        return new WorldGenerator() {
            public boolean generate(World world, Random rand, BlockPos pos) {
                return true;
            }
        };
    }

    public int getSkyColorByTemp(float currentTemperature) {
        return 0;
    }

    public List<SpawnListEntry> getSpawnableList(EnumCreatureType creatureType) {
        return new ArrayList<>();
    }

    public void decorate(World world, Random rnd, BlockPos pos) {}

    public void genTerrainBlocks(World world, Random rand, ChunkPrimer primer, int x, int z, double noiseVal) {}

    public Class<? extends PlaygroundBiome> getBiomeClass() {
        return this.getClass();
    }

    public Biome.TempCategory getTempCategory() {
        return TempCategory.COLD;
    }

    public void addFlower(IBlockState state, int weight) {}

    public void plantFlower(World world, Random rand, BlockPos pos) {}

    public void addDefaultFlowers() {}
}

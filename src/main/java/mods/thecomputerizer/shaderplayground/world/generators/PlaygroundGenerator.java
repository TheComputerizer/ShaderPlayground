package mods.thecomputerizer.shaderplayground.world.generators;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EnumCreatureType;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.ChunkPrimer;
import net.minecraft.world.gen.IChunkGenerator;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Random;

@ParametersAreNonnullByDefault
public class PlaygroundGenerator implements IChunkGenerator {
    
    private static final int HEIGHT = 64;
    private static final IBlockState FILLER_STATE = Blocks.STONE.getDefaultState();
    private static final IBlockState BOTTOM_STATE = Blocks.BEDROCK.getDefaultState();
    private final World world;
    private final Random rand;

    public PlaygroundGenerator(World world) {
        this.world = world;
        this.rand = new Random();
    }

    @Nonnull
    public Chunk generateChunk(int x, int z) {
        this.rand.setSeed(this.world.getSeed()+(long)x*341873128712L+(long)z*132897987541L);
        ChunkPrimer primer = new ChunkPrimer();
        for(int cx=0; cx<16; cx++)
            for(int cy=0; cy<=HEIGHT; cy++)
                for(int cz=0; cz<16; cz++)
                    primer.setBlockState(cx,cy,cz,cy==0 ? BOTTOM_STATE : FILLER_STATE);
        return new Chunk(this.world, primer,x,z);
    }

    public void populate(int x, int z) {}

    public boolean generateStructures(Chunk chunk, int x, int z) {
        return false;
    }

    public List<Biome.SpawnListEntry> getPossibleCreatures(EnumCreatureType type, BlockPos pos) {
        Biome biome = this.world.getBiome(pos);
        return biome.getSpawnableList(type);
    }

    public @Nullable BlockPos getNearestStructurePos(World world, String structureName, BlockPos pos, boolean findUnexplored) {
        return null;
    }

    public void recreateStructures(Chunk chunk, int x, int z) {
    }

    public boolean isInsideStructure(World world, String structureName, BlockPos pos) {
        return false;
    }
}

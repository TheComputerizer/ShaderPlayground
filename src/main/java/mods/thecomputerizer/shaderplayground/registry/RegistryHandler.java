package mods.thecomputerizer.shaderplayground.registry;

import mods.thecomputerizer.shaderplayground.core.SPRef;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.SoundEvent;
import net.minecraft.world.biome.Biome;
import net.minecraftforge.event.RegistryEvent.*;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityEntry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistryEntry;

@SuppressWarnings("unused")
@EventBusSubscriber(modid = SPRef.MODID)
public final class RegistryHandler {

    public static final CreativeTabs SHADER_PLAYGROUND_TAB = new CreativeTabs(SPRef.MODID) {
        @SideOnly(Side.CLIENT)
        public ItemStack createIcon() {
            return new ItemStack(Blocks.BEDROCK);
        }
    };

    public static void onPreInit(FMLPreInitializationEvent event) {
        DimensionRegistry.register();
    }

    public static void onInit(FMLInitializationEvent event) {
        BiomeRegistry.initBiomeInfo();
    }

    public static void onPostInit(FMLPostInitializationEvent event) {
        if(event.getSide().isClient()) ParticleRegistry.postInit();
    }

    public static void onServerStarting(FMLServerStartingEvent event) {
        SPRef.LOGGER.info("Registering commands");
    }

    @SubscribeEvent
    public static void registerBiomes(Register<Biome> event) {
        register(event,BiomeRegistry.getBiomes());
    }

    @SubscribeEvent
    public static void registerBlocks(Register<Block> event) {
        register(event,BlockRegistry.getBlocks());
        //GameRegistry.registerTileEntity();
    }

    @SubscribeEvent
    public static void registerEntities(Register<EntityEntry> event) {
        register(event,EntityRegistry.getEntityEntries());
    }

    @SubscribeEvent
    public static void registerItems(Register<Item> event) {
        register(event,ItemRegistry.getItems());
    }

    @SubscribeEvent
    public static void updateMappings(MissingMappings<Item> event) {

    }

    @SubscribeEvent
    public static void registerSoundEvents(Register<SoundEvent> event) {
        register(event,SoundRegistry.getSounds());
    }

    private static <E extends IForgeRegistryEntry<E>> void register(Register<E> event, E[] toRegister) {
        event.getRegistry().registerAll(toRegister);
    }
}

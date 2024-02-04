package mods.thecomputerizer.shaderplayground;

import mods.thecomputerizer.shaderplayground.core.SPRef;
import mods.thecomputerizer.shaderplayground.registry.RegistryHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

@SuppressWarnings("unused")
@Mod(modid = SPRef.MODID, name = SPRef.NAME, version = SPRef.VERSION, dependencies = SPRef.DEPENDENCIES)
public class ShaderPlayground {

    public ShaderPlayground() {
        SPRef.LOGGER.info("Starting mod construction");
        SPRef.LOGGER.info("Completed mod construction");
    }

    @EventHandler
    public static void preInit(FMLPreInitializationEvent event) {
        SPRef.LOGGER.info("Starting pre-init");
        RegistryHandler.onPreInit(event);
        SPRef.LOGGER.info("Completed pre-init");
    }

    @EventHandler
    public static void init(FMLInitializationEvent event) {
        SPRef.LOGGER.info("Starting init");
        RegistryHandler.onInit(event);
        SPRef.LOGGER.info("Completed init");
    }

    @EventHandler
    public static void postInit(FMLPostInitializationEvent event) {
        SPRef.LOGGER.info("Starting post-init");
        RegistryHandler.onPostInit(event);
        SPRef.LOGGER.info("Completed post-init");
    }

    @EventHandler
    public void start(FMLServerStartingEvent event) {
        SPRef.LOGGER.info("Running server starting stuff");
        RegistryHandler.onServerStarting(event);
        SPRef.LOGGER.info("Completed server starting stuff");
    }
}
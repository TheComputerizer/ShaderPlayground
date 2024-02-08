package mods.thecomputerizer.shaderplayground.core;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SPRef {

    public static final boolean CLIENT = FMLLaunchHandler.side().isClient();
    public static final String DEPENDENCIES = "required-after:forge@[14.23.5.2860,);";
    public static final Logger LOGGER = LogManager.getLogger("Shader Playground");
    public static final String MODID = "shaderplayground";
    public static final String NAME = "Shader Playground";
    public static final String VERSION = "1.12.2-0.0.1";

    public static ResourceLocation res(String path) {
        return new ResourceLocation(MODID,path);
    }
}

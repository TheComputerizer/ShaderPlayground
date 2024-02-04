package mods.thecomputerizer.shaderplayground.client.shader;

import mods.thecomputerizer.shaderplayground.core.SPRef;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.relauncher.Side;

@EventBusSubscriber(modid = SPRef.MODID, value = Side.CLIENT)
public class ShaderEvents {

    @SubscribeEvent
    public static void preRenderTick(RenderTickEvent event) {
        if(event.phase==Phase.START)
            ShaderManager.getInstance().initShaderFrame(Minecraft.getMinecraft().world);
    }
}

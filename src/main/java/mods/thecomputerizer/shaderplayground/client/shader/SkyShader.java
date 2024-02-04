package mods.thecomputerizer.shaderplayground.client.shader;

import mods.thecomputerizer.shaderplayground.core.SPRef;
import mods.thecomputerizer.shaderplayground.registry.DimensionRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import org.lwjgl.BufferUtils;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.util.Objects;

public class SkyShader extends Shader {

    public SkyShader() {
        super(SPRef.res("shaders/sky/skytest.fsh"),SPRef.res("shaders/sky/skytest.vsh"));
        addUniformFloat("millis",() -> (float)System.nanoTime()/1000000f);
        addUniformFloat("width",() -> (float)Minecraft.getMinecraft().displayWidth);
        addUniformFloat("height",() -> (float)Minecraft.getMinecraft().displayHeight);
    }

    @Override
    public boolean canRender(@Nullable WorldClient world) {
        return Objects.nonNull(world) && world.provider.getDimensionType()==DimensionRegistry.PLAYGROUND;
    }

    @Override
    public void render(float partialTicks) {
    }
}

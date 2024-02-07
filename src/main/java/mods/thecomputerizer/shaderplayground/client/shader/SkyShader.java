package mods.thecomputerizer.shaderplayground.client.shader;

import mods.thecomputerizer.shaderplayground.client.shader.uniform.UniformFloat;
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
        addUniform(new UniformFloat("time",this::getTime));
    }

    public float getTime(float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        float time = Objects.nonNull(mc.world) ? mc.world.getWorldTime()%65536L : 1f;
        return Objects.nonNull(mc.world) ? mc.world.getWorldTime()%65536L : 1f;
    }

    @Override
    public boolean canRender(@Nullable WorldClient world) {
        return Objects.nonNull(world) && world.provider.getDimensionType()==DimensionRegistry.PLAYGROUND;
    }

    @Override
    public void render(float partialTicks) {
    }
}

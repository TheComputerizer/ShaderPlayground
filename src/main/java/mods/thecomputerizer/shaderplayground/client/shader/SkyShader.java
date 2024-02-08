package mods.thecomputerizer.shaderplayground.client.shader;

import mods.thecomputerizer.shaderplayground.client.shader.uniform.UniformFloat;
import mods.thecomputerizer.shaderplayground.core.SPRef;
import mods.thecomputerizer.shaderplayground.registry.DimensionRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Objects;

@SideOnly(Side.CLIENT)
public class SkyShader extends Shader {

    public SkyShader() {
        super(SPRef.res("shaders/sky/skytest.fsh"),SPRef.res("shaders/sky/skytest.vsh"));
        addUniform(new UniformFloat("time",this::getTime));
        addUniform(new UniformFloat("timeScale",this::getTimeScale));
        addUniform(new UniformFloat("radius",this::getRadius));
        addUniform(new UniformFloat("outlineThickness",this::getOutlineThickness));
        addUniform(new UniformFloat("animationScale",this::getAnimationScale));
    }

    public float getTime(float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        return Objects.nonNull(mc.world) ? mc.world.getWorldTime()%65536L : 1f;
    }

    public float getTimeScale(float partialTicks) {
        return 1f/15f;
    }

    public float getRadius(float partialTicks) {
        return 0.25f;
    }

    public float getOutlineThickness(float partialTicks) {
        return 0.01f;
    }

    public float getAnimationScale(float partialTicks) {
        return 3f;
    }

    @Override
    public boolean canRender(@Nullable WorldClient world) {
        return Objects.nonNull(world) && world.provider.getDimensionType()==DimensionRegistry.PLAYGROUND;
    }
}

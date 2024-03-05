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
        addUniform(new UniformFloat("uvScale",this::getUVScale));
        addUniform(new UniformFloat("radius",this::getRadius));
        addUniform(new UniformFloat("outlineThickness",this::getOutlineThickness));
        addUniform(new UniformFloat("animationScale",this::getAnimationScale));
        addUniform(new UniformFloat("crackScale",this::getCrackScale));
    }

    public float getTime(float partialTicks) {
        Minecraft mc = Minecraft.getMinecraft();
        return Objects.nonNull(mc.world) ? (mc.world.getWorldTime()%65536L)+partialTicks : 1f;
    }

    public float getTimeScale(float partialTicks) {
        return 1f/15f;
    }

    public float getUVScale(float partialTicks) {
        return 1/16f;
    }

    public float getRadius(float partialTicks) {
        return 0.11f;
    }

    public float getOutlineThickness(float partialTicks) {
        return 0.02f;
    }

    public float getAnimationScale(float partialTicks) {
        return 3f;
    }

    public float getCrackScale(float partialTicks) {
        return 0.1f*getUVScale(partialTicks);
    }

    @Override
    public boolean canRender(@Nullable WorldClient world) {
        return Objects.nonNull(world) && world.provider.getDimensionType()==DimensionRegistry.PLAYGROUND;
    }
}

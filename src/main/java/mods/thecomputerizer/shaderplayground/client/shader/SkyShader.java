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

    private float time;

    public SkyShader() {
        super(SPRef.res("shaders/sky/skytest.fsh"),SPRef.res("shaders/sky/skytest.vsh"));
        addUniform(new UniformFloat("time",1,(buffer, partialTicks) ->
                ShaderManager.getInstance().uploadFloats(buffer,getTime(partialTicks))));
        addUniformFloat("width",() -> {
            float width = (float)Minecraft.getMinecraft().displayWidth;
            //SPRef.LOGGER.error("WIDTH IS {}",width);
            return width;
        });
        addUniformFloat("height",() -> {
            float height = (float)Minecraft.getMinecraft().displayWidth;
            //SPRef.LOGGER.error("HEIGHT IS {}",height);
            return height;
        });
    }

    public float getTime(float partialTicks) {
        this.time+=partialTicks;
        SPRef.LOGGER.error("TIME IS {}",this.time);
        return this.time;
    }

    @Override
    public boolean canRender(@Nullable WorldClient world) {
        return Objects.nonNull(world) && world.provider.getDimensionType()==DimensionRegistry.PLAYGROUND;
    }

    @Override
    public void render(float partialTicks) {
    }
}

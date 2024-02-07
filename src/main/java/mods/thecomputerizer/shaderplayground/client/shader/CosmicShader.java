package mods.thecomputerizer.shaderplayground.client.shader;

import mods.thecomputerizer.shaderplayground.core.SPRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.entity.player.EntityPlayer;
import org.lwjgl.BufferUtils;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.util.Objects;

public class CosmicShader extends Shader {

    //private static final FloatBuffer LIGHT_BUFFER = createBuffer(new float[]{1f,1f,1f});
    //private static final FloatBuffer COSMIC_UV_BUFFER = createBuffer(new float[40]); //Currently does not do anything

    private static FloatBuffer createBuffer(float[] defVals) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(defVals.length);
        for(float defVal : defVals) buffer.put(defVal);
        buffer.position(0);
        return buffer;
    }

    public CosmicShader() {
        super(SPRef.res("shaders/cosmic/cosmic.fsh"),SPRef.res("shaders/cosmic/cosmic.vsh"));
    }

    @Override
    public boolean canRender(@Nullable WorldClient world) {
        return Objects.nonNull(world) && world.provider.getDimension()==2;
    }

    public int getTime() {
        EntityPlayer player = Minecraft.getMinecraft().player;
        if(Objects.nonNull(player)) {
            return (int)(player.getEntityWorld().getWorldTime()%65536L);
        }
        return 0;
    }

    @Override
    void render(float partialTicks) {

    }
}

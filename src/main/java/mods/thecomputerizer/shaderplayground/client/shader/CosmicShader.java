package mods.thecomputerizer.shaderplayground.client.shader;

import mods.thecomputerizer.shaderplayground.core.SPRef;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Objects;

@SideOnly(Side.CLIENT)
public class CosmicShader extends Shader {

    public CosmicShader() {
        super(SPRef.res("shaders/cosmic/cosmic.fsh"),SPRef.res("shaders/cosmic/cosmic.vsh"));
    }

    @Override
    public boolean canRender(@Nullable WorldClient world) {
        return Objects.nonNull(world) && world.provider.getDimension()==222;
    }
}

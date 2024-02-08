package mods.thecomputerizer.shaderplayground.client.shader.uniform;

import mods.thecomputerizer.shaderplayground.client.shader.ShaderManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.function.Function;

@SideOnly(Side.CLIENT)
public class UniformFloat extends Uniform<Float> {

    private final Function<Float,Float> valFunc;

    public UniformFloat(String name, Function<Float,Float> valFunc) {
        super(name);
        this.valFunc = valFunc;
    }

    @Override
    public void upload(float partialTicks, int programID) {
        ShaderManager.getInstance().uploadFloat(programID,getName(),this.valFunc.apply(partialTicks));
    }
}

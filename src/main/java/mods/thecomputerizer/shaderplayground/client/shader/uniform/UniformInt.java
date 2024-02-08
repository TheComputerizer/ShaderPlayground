package mods.thecomputerizer.shaderplayground.client.shader.uniform;

import net.minecraft.client.renderer.GLAllocation;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.nio.IntBuffer;
import java.util.function.BiConsumer;

@SideOnly(Side.CLIENT)
public class UniformInt extends Uniform<Integer> {

    private final IntBuffer buffer;
    private final BiConsumer<IntBuffer,Float> bufferFunc;

    public UniformInt(String name, int size, BiConsumer<IntBuffer,Float> bufferFunc, int ... defaults) {
        super(name);
        this.buffer = GLAllocation.createDirectIntBuffer(size);
        this.buffer.put(defaults);
        this.bufferFunc = bufferFunc;
    }

    @Override
    public void upload(float partialTicks, int programID) {
        setID(OpenGlHelper.glGetUniformLocation(programID,getName()));
        this.bufferFunc.accept(this.buffer,partialTicks);
        OpenGlHelper.glUniform1(getID(),this.buffer);
    }
}
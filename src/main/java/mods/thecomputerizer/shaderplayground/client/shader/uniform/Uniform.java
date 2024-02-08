package mods.thecomputerizer.shaderplayground.client.shader.uniform;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public abstract class Uniform<U> {

    private final String name;
    private int uniformID;

    protected Uniform(String name) {
        this.name = name;
    }

    public int getID() {
        return this.uniformID;
    }

    public String getName() {
        return this.name;
    }

    public void setID(int id) {
        this.uniformID = id;
    }

    public abstract void upload(float partialTicks, int programID);
}

package mods.thecomputerizer.shaderplayground.client.shader;

import mods.thecomputerizer.shaderplayground.client.shader.uniform.Uniform;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@SideOnly(Side.CLIENT)
public abstract class Shader {

    private final List<Uniform<?>> uniforms;
    private final ResourceLocation fragmentLocation;
    private final ResourceLocation vertexLocation;
    private int programID;
    private boolean previousLighting;

    public Shader(ResourceLocation fragmentLocation, ResourceLocation vertexLocation) {
        this.uniforms = new ArrayList<>();
        this.fragmentLocation = fragmentLocation;
        this.vertexLocation = vertexLocation;
    }

    protected void addUniform(Uniform<?> uniform) {
        this.uniforms.add(uniform);
    }

    public boolean canRender(@Nullable WorldClient world) {
        return true;
    }

    public void delete() {
        if(this.programID!=0) OpenGlHelper.glDeleteProgram(this.programID);
    }

    public int getProgramID() {
        if(this.programID==0)
            this.programID = ShaderManager.initShaderProgram(this.fragmentLocation,this.vertexLocation);
        return this.programID;
    }

    public void init() {
        if(this.programID!=0) return;
        ShaderManager.getInstance().allocateUniforms(getProgramID(),this.uniforms);
    }

    public void release() {
        if(this.previousLighting) GlStateManager.enableLighting();
        OpenGlHelper.glUseProgram(0);
    }

    public void upload(float partialTicks) {
        ShaderManager.getInstance().uploadUniforms(partialTicks,this.programID,this.uniforms);
    }

    public void use(float partialTicks) {
        if(OpenGlHelper.areShadersSupported()) {
            this.previousLighting = GL11.glGetBoolean(GL11.GL_LIGHTING);
            GlStateManager.disableLighting();
            OpenGlHelper.glUseProgram(this.programID);
            if(this.programID>0) upload(partialTicks);
        }
    }
}

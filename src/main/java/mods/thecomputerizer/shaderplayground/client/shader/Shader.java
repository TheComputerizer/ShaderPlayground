package mods.thecomputerizer.shaderplayground.client.shader;

import mods.thecomputerizer.shaderplayground.client.shader.uniform.Uniform;
import mods.thecomputerizer.shaderplayground.client.shader.uniform.UniformFloat;
import mods.thecomputerizer.shaderplayground.client.shader.uniform.UniformInt;
import mods.thecomputerizer.shaderplayground.client.shader.uniform.UniformMatrix;
import mods.thecomputerizer.shaderplayground.core.SPRef;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.*;

import javax.annotation.Nullable;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public abstract class Shader {

    private final List<Uniform<?>> uniforms;
    private final ResourceLocation fragmentLocation;
    private final ResourceLocation vertexLocation;
    private int programID;
    private int fragmentID;
    private int vertexID;
    private boolean previousLighting;

    public Shader(ResourceLocation fragmentLocation, ResourceLocation vertexLocation) {
        this.uniforms = new ArrayList<>();
        this.fragmentLocation = fragmentLocation;
        this.vertexLocation = vertexLocation;
    }

    protected void addUniform(Uniform<?> uniform) {
        this.uniforms.add(uniform);
    }

    protected void addUniformFloat(String name, Supplier<Float> valSupplier) {
        addUniform(new UniformFloat(name,1,(buffer, partialTicks) ->
                ShaderManager.getInstance().uploadFloats(buffer,valSupplier.get())));
    }

    protected void addUniformFloatBuffer(String name, int size, Supplier<FloatBuffer> valSupplier) {
        addUniform(new UniformFloat(name,size,(buffer,partialTicks) ->
                ShaderManager.getInstance().uploadFloatBuffer(buffer,valSupplier.get())));
    }

    protected void addUniformInt(String name, Supplier<Integer> valSupplier) {
        addUniform(new UniformInt(name,1,(buffer, partialTicks) ->
                ShaderManager.getInstance().uploadInts(buffer,valSupplier.get())));
    }

    protected void addUniformMatrix(String name, Supplier<FloatBuffer> valSupplier) {
        addUniform(new UniformMatrix(name, pt -> valSupplier.get()));
    }

    public boolean canRender(@Nullable WorldClient world) {
        return true;
    }

    public void delete() {
        if(this.programID!=0) OpenGlHelper.glDeleteProgram(this.programID);
    }

    public ResourceLocation getFragmentLocation() {
        return this.fragmentLocation;
    }

    public int getProgramID() {
        if(this.programID==0) {
            this.programID = ARBShaderObjects.glCreateProgramObjectARB();
            this.vertexID = ShaderManager.getInstance().createShader(this.vertexLocation,ARBVertexShader.GL_VERTEX_SHADER_ARB);
            this.fragmentID = ShaderManager.getInstance().createShader(this.fragmentLocation,ARBFragmentShader.GL_FRAGMENT_SHADER_ARB);
            OpenGlHelper.glAttachShader(this.programID,this.vertexID);
            OpenGlHelper.glAttachShader(this.programID,this.fragmentID);
            OpenGlHelper.glLinkProgram(this.programID);
            validateShaderProgram();
            OpenGlHelper.glDeleteShader(this.vertexID);
            OpenGlHelper.glDeleteShader(this.fragmentID);
            OpenGlHelper.glUseProgram(0);
        }
        return this.programID;
    }

    private void validateShaderProgram() {
        try {
            if(OpenGlHelper.glGetProgrami(this.programID,ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB)==GL11.GL_FALSE)
                throw new IllegalArgumentException(ShaderManager.getInstance().logShaderError(this.programID,"Failed to link shader!"));
            GL20.glValidateProgram(this.programID);
            if(OpenGlHelper.glGetProgrami(this.programID,ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB)==GL11.GL_FALSE)
                throw new IllegalArgumentException(ShaderManager.getInstance().logShaderError(this.programID,"Failed to validate shader!"));
        } catch(Exception ex) {
            SPRef.LOGGER.error("Failed to validate shader from resources {} {}!",this.vertexLocation,this.fragmentLocation,ex);
            if(this.programID!=0) OpenGlHelper.glDeleteShader(this.programID);
        }
    }

    public ResourceLocation getVertexLocation() {
        return this.vertexLocation;
    }

    public Collection<Uniform<?>> getUniforms() {
        return this.uniforms;
    }

    public void init() {
        if(this.programID!=0) return;
        ShaderManager.getInstance().allocateUniforms(getProgramID(),this.uniforms);
    }

    public void release() {
        if(this.previousLighting) GlStateManager.enableLighting();
        OpenGlHelper.glUseProgram(0);
    }

    abstract void render(float partialTicks);

    public void upload(float partialTicks) {
        ShaderManager.getInstance().uploadUniforms(partialTicks,this.uniforms);
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

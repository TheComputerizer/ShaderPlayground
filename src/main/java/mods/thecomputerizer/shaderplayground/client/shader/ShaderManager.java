package mods.thecomputerizer.shaderplayground.client.shader;

import mods.thecomputerizer.shaderplayground.client.shader.uniform.Uniform;
import mods.thecomputerizer.shaderplayground.core.SPRef;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.resource.IResourceType;
import net.minecraftforge.client.resource.ISelectiveResourceReloadListener;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.ARBShaderObjects;
import org.lwjgl.opengl.GL20;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.Collection;
import java.util.Objects;
import java.util.function.Predicate;

@ParametersAreNonnullByDefault
public class ShaderManager implements ISelectiveResourceReloadListener {

    private static ShaderManager INSTANCE;

    public static ShaderManager getInstance() {
        if(Objects.isNull(INSTANCE)) new ShaderManager();
        return INSTANCE;
    }

    public final SkyShader skyShader;
    public final CosmicShader cosmicShader;

    public ShaderManager() {
        this.skyShader = new SkyShader();
        this.cosmicShader = new CosmicShader();
        INSTANCE = this;
    }

    public void allocateUniforms(int programID, Collection<Uniform<?>> uniforms) {
        for(Uniform<?> uniform : uniforms)
            uniform.setID(OpenGlHelper.glGetUniformLocation(programID,uniform.getName()));
    }

    public int createShader(ResourceLocation shaderLocation, int shaderType) {
        int shaderID = 0;
        try {
            shaderID = OpenGlHelper.glCreateShader(shaderType);
            if(shaderID==0) throw new IllegalArgumentException("Unknown shader type "+shaderType);
            byte[] shaderBytes = getResourceAsString(Minecraft.getMinecraft().getResourceManager(),shaderLocation).getBytes();
            ByteBuffer shaderBuffer = BufferUtils.createByteBuffer(shaderBytes.length);
            shaderBuffer.put(shaderBytes);
            shaderBuffer.position(0);
            OpenGlHelper.glShaderSource(shaderID,shaderBuffer);
            OpenGlHelper.glCompileShader(shaderID);
            return shaderID;
        } catch(Exception ex) {
            SPRef.LOGGER.error("Failed to create shader from resource {}!",shaderLocation,ex);
            if(shaderID!=0) OpenGlHelper.glDeleteShader(shaderID);
        }
        return 0;
    }

    public String getResourceAsString(IResourceManager manager, ResourceLocation resource) throws IOException {
        StringWriter writer = new StringWriter();
        IOUtils.copy(manager.getResource(resource).getInputStream(),writer,"UTF-8");
        return writer.toString();
    }

    public void initShaderFrame(WorldClient world) {
        if(this.skyShader.canRender(world)) this.skyShader.init();
        //if(this.cosmicShader.canRender(world)) this.cosmicShader.init();
    }

    public String logShaderError(int program, String baseMsg) {
        int length = GL20.glGetProgrami(program,ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB);
        return baseMsg+" `"+GL20.glGetProgramInfoLog(program,length)+"1";
    }

    @Override
    public void onResourceManagerReload(IResourceManager manager, Predicate<IResourceType> resourcePredicate) {
        this.skyShader.delete();
        //this.cosmicShader.delete();
    }

    public void uploadFloats(FloatBuffer buffer, float ... vals) {
        buffer.position(0);
        buffer.put(vals);
    }

    public void uploadFloatBuffer(FloatBuffer buffer, FloatBuffer otherBuffer) {
        buffer.position(0);
        otherBuffer.position(0);
        buffer.put(otherBuffer);
    }

    public void uploadInts(IntBuffer buffer, int ... vals) {
        buffer.position(0);
        buffer.put(vals);
    }

    public void uploadUniforms(float partialTicks, Collection<Uniform<?>> uniforms) {
        for(Uniform<?> uniform : uniforms) uniform.upload(partialTicks);
    }
}

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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.io.IOUtils;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.*;

import javax.annotation.Nullable;
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
@SideOnly(Side.CLIENT)
public class ShaderManager implements ISelectiveResourceReloadListener {

    private static final int FRAGMENT_ARB = ARBFragmentShader.GL_FRAGMENT_SHADER_ARB;
    private static final int LINK_STATUS = ARBShaderObjects.GL_OBJECT_LINK_STATUS_ARB;
    private static final int LOG_LENGTH = ARBShaderObjects.GL_OBJECT_INFO_LOG_LENGTH_ARB;
    private static final int VALIDATE_STATUS = ARBShaderObjects.GL_OBJECT_VALIDATE_STATUS_ARB;
    private static final int VERTEX_ARB = ARBVertexShader.GL_VERTEX_SHADER_ARB;
    private static ShaderManager INSTANCE;

    public static String getShaderError(int programID, String baseMsg) {
        int length = GL20.glGetProgrami(programID,LOG_LENGTH);
        return baseMsg+" `"+GL20.glGetProgramInfoLog(programID,length)+"`";
    }

    public static ShaderManager getInstance() {
        if(Objects.isNull(INSTANCE)) new ShaderManager();
        return INSTANCE;
    }

    public static int initShaderProgram(@Nullable ResourceLocation fragmentRes, @Nullable ResourceLocation vertexRes) {
        if(Objects.isNull(fragmentRes) && Objects.isNull(vertexRes)) return 0;
        int programID = ARBShaderObjects.glCreateProgramObjectARB();
        int vertexID = Objects.nonNull(vertexRes) ? INSTANCE.createShader(vertexRes,VERTEX_ARB) : 0;
        int fragmentID = Objects.nonNull(fragmentRes) ? INSTANCE.createShader(fragmentRes,FRAGMENT_ARB) : 0;
        programID = linkShaders(programID,vertexID,fragmentID);
        OpenGlHelper.glUseProgram(0);
        return programID;
    }

    public static int linkShaders(int programID, int ... shaderIDs) {
        if(programID!=0) {
            for(int shaderID : shaderIDs)
                if(shaderID!=0) OpenGlHelper.glAttachShader(programID,shaderID);
            OpenGlHelper.glLinkProgram(programID);
            programID = validateLinkedShader(programID,"Shader link validation failed!");
            for(int shaderID : shaderIDs)
                if(shaderID!=0) OpenGlHelper.glDeleteShader(shaderID);
        }
        return programID;
    }

    public static int validateLinkedShader(int programID, String errorMsg) {
        if(!validateShader(programID,LINK_STATUS,"Failed to link shader!")) {
            SPRef.LOGGER.error("Shader link validation failed! {}",errorMsg);
            return 0;
        }
        GL20.glValidateProgram(programID);
        if(!validateShader(programID,VALIDATE_STATUS,"Failed to validate shader!")) {
            SPRef.LOGGER.error("Shader link validation failed! {}",errorMsg);
            return 0;
        }
        return programID;
    }

    public static boolean validateShader(int programID, int parameter, String errorMsg) {
        if(OpenGlHelper.glGetProgrami(programID,parameter)==GL11.GL_FALSE) {
            SPRef.LOGGER.error(getShaderError(programID,errorMsg));
            OpenGlHelper.glDeleteShader(programID);
            return false;
        }
        return true;
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

    public void uploadFloat(int programID, String name, float val) {
        int uniformID = ARBShaderObjects.glGetUniformLocationARB(programID,name);
        ARBShaderObjects.glUniform1fARB(uniformID,val);
    }

    public void uploadFloats(int programID, String name, float ... vals) {
        int uniformID = ARBShaderObjects.glGetUniformLocationARB(programID,name);
        switch(vals.length) {
            case 1: ARBShaderObjects.glUniform1fARB(uniformID,vals[0]);
            case 2: ARBShaderObjects.glUniform2fARB(uniformID,vals[0],vals[1]);
            case 3: ARBShaderObjects.glUniform3fARB(uniformID,vals[0],vals[1],vals[2]);
        }
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

    public void uploadUniforms(float partialTicks, int programID, Collection<Uniform<?>> uniforms) {
        for(Uniform<?> uniform : uniforms) uniform.upload(partialTicks,programID);
    }
}

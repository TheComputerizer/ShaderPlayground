package mods.thecomputerizer.shaderplayground.registry;

import mods.thecomputerizer.shaderplayground.client.particle.ParticleAscii;
import mods.thecomputerizer.shaderplayground.core.SPRef;
import mods.thecomputerizer.theimpossiblelibrary.util.TextUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Objects;

@Mod.EventBusSubscriber(modid = SPRef.MODID)
public final class ParticleRegistry {

    public static final ResourceLocation PARTICLE_TEXTURES = new ResourceLocation("textures/particle/particles.png");
    private static final Class<?>[] PARTICLE_INIT_CLASSES = {String.class, int.class, boolean.class};
    public static final EnumParticleTypes RANDOM_ASCII = registerParticle("RANDOM_ASCII",true);
    private static TextureAtlasSprite FONT_ATLAS = null;

    private static EnumParticleTypes registerParticle(String name, boolean ignoreRange) {
        String camelName = TextUtil.makeCaseTypeFromSnake(name,TextUtil.TextCasing.CAMEL);
        int id = EnumParticleTypes.values().length;
        SPRef.LOGGER.info("Registrering particle with name {}",camelName);
        EnumParticleTypes ret = EnumHelper.addEnum(EnumParticleTypes.class,name,PARTICLE_INIT_CLASSES,camelName,id,ignoreRange);
        if(Objects.nonNull(ret)) {
            EnumParticleTypes.PARTICLES.put(ret.getParticleID(), ret);
            EnumParticleTypes.BY_NAME.put(ret.getParticleName(), ret);
        } else SPRef.LOGGER.error("Failed to register particle {}!",camelName);
        return ret;
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void stitchEvent(TextureStitchEvent.Pre ev) {
        String texPath = Minecraft.getMinecraft().fontRenderer.locationFontTexture.getPath();
        texPath = texPath.substring(0,texPath.lastIndexOf(".")).replace("textures/","");
        FONT_ATLAS = ev.getMap().registerSprite(new ResourceLocation(texPath));
    }

    @SideOnly(Side.CLIENT)
    public static void postInit() {
        ParticleManager manager = Minecraft.getMinecraft().effectRenderer;
        manager.registerParticle(RANDOM_ASCII.getParticleID(),new ParticleAscii.Factory());
    }

    @SideOnly(Side.CLIENT)
    public static TextureAtlasSprite getFontAtlas() {
        return FONT_ATLAS;
    }
}

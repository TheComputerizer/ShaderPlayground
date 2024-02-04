package mods.thecomputerizer.shaderplayground.registry;

import mods.thecomputerizer.shaderplayground.core.SPRef;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unused")
public final class SoundRegistry {

    private static final List<SoundEvent> ALL_SOUNDS = new ArrayList<>();
    public static final SoundEvent BELL = makeSoundEvent("bell");
    public static final SoundEvent REVERSE_BELL = makeSoundEvent("reversebell");

    private static SoundEvent makeSoundEvent(final String name) {
        ResourceLocation id = SPRef.res(name);
        SoundEvent sound = new SoundEvent(id).setRegistryName(name);
        ALL_SOUNDS.add(sound);
        return sound;
    }

    public static SoundEvent[] getSounds() {
        return ALL_SOUNDS.toArray(new SoundEvent[0]);
    }
}

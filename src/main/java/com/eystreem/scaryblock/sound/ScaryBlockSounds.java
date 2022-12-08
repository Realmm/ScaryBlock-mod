package com.eystreem.scaryblock.sound;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ScaryBlockSounds {

    private static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, ScaryBlockMod.MOD_ID);

    public static final RegistryObject<SoundEvent> JUMPSCARE = registerSoundEvent("jumpscare");
    public static final RegistryObject<SoundEvent> ALARM = registerSoundEvent("alarm");
    public static final RegistryObject<SoundEvent> DOUBLE_JUMP = registerSoundEvent("double_jump");

    private static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(ScaryBlockMod.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }

}

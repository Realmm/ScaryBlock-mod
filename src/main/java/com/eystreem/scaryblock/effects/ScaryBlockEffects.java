package com.eystreem.scaryblock.effects;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraft.potion.Effect;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ScaryBlockEffects {

    private static final DeferredRegister<Effect> EFFECTS = DeferredRegister.create(ForgeRegistries.POTIONS, ScaryBlockMod.MOD_ID);

    public static final RegistryObject<Effect> SPEED = EFFECTS.register("speed", SpeedEffect::new);
    public static final RegistryObject<Effect> DAMAGE_RESISTANCE = EFFECTS.register("damage_resistance", DamageResistanceEffect::new);

    public static void register(IEventBus eventBus) {
        EFFECTS.register(eventBus);
    }

}

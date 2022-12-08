package com.eystreem.scaryblock.entities;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.entities.bloodgolem.BloodGolemEntity;
import com.eystreem.scaryblock.entities.herobrine.HerobrineEntity;
import com.eystreem.scaryblock.entities.thatthing.ThatThingEntity;
import net.minecraft.entity.EntityType;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ScaryBlockMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityEvents {

    @SubscribeEvent
    public static void addEntityAttributes(EntityAttributeCreationEvent e) {
        e.put(ScaryBlockEntityTypes.HEROBRINE_ENTITY.get(), HerobrineEntity.setCustomAttributes().build());
        e.put(ScaryBlockEntityTypes.BLOOD_GOLEM.get(), BloodGolemEntity.setCustomAttributes().build());
        e.put(ScaryBlockEntityTypes.THAT_THING.get(), ThatThingEntity.setCustomAttributes().build());
    }

}

package com.eystreem.scaryblock.entities;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.entities.bloodgolem.BloodGolemEntity;
import com.eystreem.scaryblock.entities.herobrine.HerobrineEntity;
import com.eystreem.scaryblock.entities.thatthing.ThatThingEntity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ScaryBlockEntityTypes {

    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES,
            ScaryBlockMod.MOD_ID);

    public static final RegistryObject<EntityType<HerobrineEntity>> HEROBRINE_ENTITY =
            ENTITY_TYPES.register("herobrine_entity", () -> EntityType.Builder.<HerobrineEntity>of((type, w) ->
                            new HerobrineEntity(w), EntityClassification.MONSTER)
                    .sized(1, 2)
                    .build(new ResourceLocation(ScaryBlockMod.MOD_ID, "herobrine_entity").toString()));

    public static final RegistryObject<EntityType<BloodGolemEntity>> BLOOD_GOLEM =
            ENTITY_TYPES.register("blood_golem", () -> EntityType.Builder.<BloodGolemEntity>of((type, w) ->
                            new BloodGolemEntity(w), EntityClassification.MONSTER)
                    .sized(1, 2)
                    .build(new ResourceLocation(ScaryBlockMod.MOD_ID, "blood_golem").toString()));

    public static final RegistryObject<EntityType<ThatThingEntity>> THAT_THING =
            ENTITY_TYPES.register("that_thing", () -> EntityType.Builder.<ThatThingEntity>of((type, w) ->
                            new ThatThingEntity(w), EntityClassification.MONSTER)
                    .sized(1, 2)
                    .build(new ResourceLocation(ScaryBlockMod.MOD_ID, "that_thing").toString()));

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }

}

package com.eystreem.scaryblock.block.entities.spawner;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EntitySpawnerBlockModel extends AnimatedGeoModel<EntitySpawnerBlockEntity> {

    @Override
    public ResourceLocation getModelLocation(EntitySpawnerBlockEntity object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "geo/entity_spawner.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySpawnerBlockEntity object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "textures/machines/entity_spawner.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntitySpawnerBlockEntity animatable) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "animations/entity_spawner.json");
    }

}

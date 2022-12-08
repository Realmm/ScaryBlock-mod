package com.eystreem.scaryblock.item.items.spawner;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class EntitySpawnerBlockItemModel extends AnimatedGeoModel<EntitySpawnerBlockItem> {

    @Override
    public ResourceLocation getModelLocation(EntitySpawnerBlockItem object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "geo/entity_spawner.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(EntitySpawnerBlockItem object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "textures/machines/entity_spawner.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(EntitySpawnerBlockItem animatable) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "animations/entity_spawner.json");
    }

}

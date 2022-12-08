package com.eystreem.scaryblock.item.items.eye;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class Entity303EyeModel extends AnimatedGeoModel<Entity303EyeItem> {

    @Override
    public ResourceLocation getModelLocation(Entity303EyeItem object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "geo/eye.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Entity303EyeItem object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "textures/models/armor/eye.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Entity303EyeItem animatable) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "animations/eye.json");
    }

}

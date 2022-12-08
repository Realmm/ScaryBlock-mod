package com.eystreem.scaryblock.item.items.giantalex;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class GiantAlexHandModel extends AnimatedGeoModel<GiantAlexHandItem> {

    @Override
    public ResourceLocation getModelLocation(GiantAlexHandItem object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "geo/giant_alex.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(GiantAlexHandItem object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "textures/item/giant_alex.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(GiantAlexHandItem animatable) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "animations/giant_alex.json");
    }

}

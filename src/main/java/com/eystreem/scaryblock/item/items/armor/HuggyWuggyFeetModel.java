package com.eystreem.scaryblock.item.items.armor;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class HuggyWuggyFeetModel extends AnimatedGeoModel<HuggyWuggyFeetItem> {

    @Override
    public ResourceLocation getModelLocation(HuggyWuggyFeetItem object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "geo/huggywuggy_feet.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(HuggyWuggyFeetItem object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "textures/models/armor/huggywuggy_feet.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(HuggyWuggyFeetItem animatable) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "animations/huggywuggy_feet.json");
    }

}

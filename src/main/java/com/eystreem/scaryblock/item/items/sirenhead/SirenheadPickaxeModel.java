package com.eystreem.scaryblock.item.items.sirenhead;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class SirenheadPickaxeModel extends AnimatedGeoModel<SirenheadPickaxeItem> {

    @Override
    public ResourceLocation getModelLocation(SirenheadPickaxeItem object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "geo/sirenhead_pickaxe.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(SirenheadPickaxeItem object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "textures/item/sirenhead_pickaxe.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(SirenheadPickaxeItem animatable) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "animations/sirenhead_pickaxe.json");
    }

}

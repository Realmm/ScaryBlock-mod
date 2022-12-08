package com.eystreem.scaryblock.entities.thatthing;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.entity.model.EntityModel;
import net.minecraft.client.renderer.entity.model.IronGolemModel;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ThatThingModel extends AnimatedGeoModel<ThatThingEntity> {

    @Override
    public ResourceLocation getModelLocation(ThatThingEntity object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "geo/that_thing.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ThatThingEntity object) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "textures/entity/that_thing.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ThatThingEntity animatable) {
        return new ResourceLocation(ScaryBlockMod.MOD_ID, "animations/that_thing.json");
    }
}

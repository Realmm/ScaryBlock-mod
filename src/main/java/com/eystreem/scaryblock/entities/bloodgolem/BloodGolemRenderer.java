package com.eystreem.scaryblock.entities.bloodgolem;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class BloodGolemRenderer extends MobRenderer<BloodGolemEntity, BloodGolemModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ScaryBlockMod.MOD_ID,
            "textures/entity/blood_golem.png");

    public BloodGolemRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new BloodGolemModel(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(BloodGolemEntity entity) {
        return TEXTURE;
    }
}

package com.eystreem.scaryblock.entities.herobrine;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;

public class HerobrineRenderer extends MobRenderer<HerobrineEntity, HerobrineModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ScaryBlockMod.MOD_ID,
            "textures/entity/herobrine_entity.png");

    public HerobrineRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new HerobrineModel(), 0.5F);
    }

    @Override
    public ResourceLocation getTextureLocation(HerobrineEntity entity) {
        return TEXTURE;
    }
}

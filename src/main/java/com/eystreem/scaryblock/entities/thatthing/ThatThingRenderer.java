package com.eystreem.scaryblock.entities.thatthing;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.util.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

public class ThatThingRenderer extends GeoEntityRenderer<ThatThingEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation(ScaryBlockMod.MOD_ID,
            "textures/entity/that_thing.png");

    public ThatThingRenderer(EntityRendererManager rendererManager) {
        super(rendererManager, new ThatThingModel());
        shadowRadius = 0.7F;
    }

    @Override
    public ResourceLocation getTextureLocation(ThatThingEntity entity) {
        return TEXTURE;
    }
}

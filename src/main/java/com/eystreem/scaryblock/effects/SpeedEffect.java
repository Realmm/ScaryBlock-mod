package com.eystreem.scaryblock.effects;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

import java.util.UUID;

public class SpeedEffect extends Effect {

    public SpeedEffect() {
        super(EffectType.BENEFICIAL, 8171462);
        addAttributeModifier(Attributes.MOVEMENT_SPEED, "7a496d37-ac05-4705-8fdd-009837197641", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

}

package com.eystreem.scaryblock.effects;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;

public class DamageResistanceEffect extends Effect {

    public DamageResistanceEffect() {
        super(EffectType.BENEFICIAL, 8171462);
        addAttributeModifier(Attributes.ARMOR_TOUGHNESS, "e808cd73-9aef-4383-9341-63431de07dd7", 0.5, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

}

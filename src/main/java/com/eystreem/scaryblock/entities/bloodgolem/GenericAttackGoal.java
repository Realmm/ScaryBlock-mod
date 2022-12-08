package com.eystreem.scaryblock.entities.bloodgolem;

import net.minecraft.entity.CreatureEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;

public class GenericAttackGoal extends MeleeAttackGoal {

    private final CreatureEntity e;

    public GenericAttackGoal(CreatureEntity e, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(e, speedModifier, followingTargetEvenIfNotSeen);
        this.e = e;
    }

    public void tick() {
        super.tick();
        if (this.getTicksUntilNextAttack() < this.getAttackInterval() / 2) {
            e.setAggressive(true);
        } else {
            e.setAggressive(false);
        }

    }
}

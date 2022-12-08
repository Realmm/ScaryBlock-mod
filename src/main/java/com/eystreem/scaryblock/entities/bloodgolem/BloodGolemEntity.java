package com.eystreem.scaryblock.entities.bloodgolem;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.entities.ScaryBlockEntityTypes;
import com.eystreem.scaryblock.item.ScaryBlockItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.ai.attributes.AttributeModifierMap;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class BloodGolemEntity extends IronGolemEntity {

    private long lastHurtRain;

    public BloodGolemEntity(World w) {
        super(ScaryBlockEntityTypes.BLOOD_GOLEM.get(), w);
        MinecraftForge.EVENT_BUS.register(this);
    }

    public static AttributeModifierMap.MutableAttribute setCustomAttributes() {
        return MobEntity.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.5D)
                .add(Attributes.ATTACK_DAMAGE, 6.0D)
                .add(Attributes.FOLLOW_RANGE, 50.0D)
                .add(Attributes.SPAWN_REINFORCEMENTS_CHANCE);
    }

    @Override
    public void tick() {
        super.tick();
        spawnParticles(3, 1);
    }

    private void spawnParticles(double horizontalOffset, double yOffset) {
        if (level.isClientSide) return;
        ServerWorld w = (ServerWorld) level;
        if (System.currentTimeMillis() - lastHurtRain >= 1000) {
            List<PlayerEntity> nearby = w.getNearbyEntities(PlayerEntity.class, new EntityPredicate(), null,
                    new AxisAlignedBB(new BlockPos(getX() - horizontalOffset,
                            getY() - yOffset, getZ() - horizontalOffset),
                            new BlockPos(getX() + horizontalOffset,
                                    getY() + yOffset, getZ() + horizontalOffset)));
            nearby.forEach(p -> p.hurt(DamageSource.GENERIC, 2));

            lastHurtRain = System.currentTimeMillis();
        }

        for (int i = 0; i < 10; i++) {
            double xRandom = ThreadLocalRandom.current().nextDouble(-horizontalOffset, horizontalOffset);
            double yRandom = ThreadLocalRandom.current().nextDouble(-yOffset, yOffset);
            double zRandom = ThreadLocalRandom.current().nextDouble(-horizontalOffset, horizontalOffset);
            BlockState state = Blocks.REDSTONE_BLOCK.getBlock().defaultBlockState();
            w.sendParticles(new BlockParticleData(ParticleTypes.BLOCK, state), getX() + xRandom,
                    getY() + yRandom + 4, getZ() + zRandom, 1,
                    0, 0, 0, 0);
        }
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(2, new GenericAttackGoal(this, 1.0D, false));
        this.goalSelector.addGoal(8, new LookAtGoal(this, PlayerEntity.class, 8.0F));
        this.goalSelector.addGoal(8, new LookRandomlyGoal(this));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomWalkingGoal(this, 0.5));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, PlayerEntity.class, true));
    }

}

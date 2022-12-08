package com.eystreem.scaryblock.block.entities.spawner;

import com.eystreem.scaryblock.block.ScaryBlockBlocks;
import com.eystreem.scaryblock.block.entities.ScaryBlockBlockEntities;
import com.eystreem.scaryblock.entities.ScaryBlockEntityTypes;
import com.eystreem.scaryblock.entities.thatthing.ThatThingEntity;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.MobSpawnerTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.WeightedSpawnerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.spawner.AbstractSpawner;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.builder.ILoopType;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.concurrent.ThreadLocalRandom;

public class EntitySpawnerBlockEntity extends TileEntity implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int count;

    public EntitySpawnerBlockEntity() {
        super(ScaryBlockBlockEntities.ENTITY_SPAWNER_BLOCK_ENTITY.get());
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    @SubscribeEvent
    public void on(TickEvent.ServerTickEvent e) {
        if (count % 100 == 0) spawn(40);
        count++;
    }

    public void spawn(double chance) {
        if (level == null || level.isClientSide) return;
        if (chance < 0 || chance > 100) throw new IllegalStateException("Unable to spawn, illegal chance");
        double random = ThreadLocalRandom.current().nextInt(1, 100);
        if (random <= chance) {
            int range = 5;
            double xRange = ThreadLocalRandom.current().nextInt(-range, range);
            double zRange = ThreadLocalRandom.current().nextInt(-range, range);
            double x = getBlockPos().getX() + xRange;
            double z = getBlockPos().getZ() + zRange;
            double maxHeight = getBlockPos().getY() + 1;
            BlockPos pos = new BlockPos(x, maxHeight, z);

            ThatThingEntity e = ScaryBlockEntityTypes.THAT_THING.get().create(level);
            if (e == null) return;
            e.setPos(pos.getX(), pos.getY(), pos.getZ());
            level.addFreshEntity(e);
        }
    }

}

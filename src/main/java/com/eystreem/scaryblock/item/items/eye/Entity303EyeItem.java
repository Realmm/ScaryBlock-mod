package com.eystreem.scaryblock.item.items.eye;

import com.eystreem.scaryblock.armor.ScaryBlockArmorMaterial;
import com.eystreem.scaryblock.effects.ScaryBlockEffects;
import com.eystreem.scaryblock.item.ScaryBlockItemGroup;
import com.eystreem.scaryblock.sound.ScaryBlockSounds;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.potion.EffectInstance;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.item.GeoArmorItem;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

public class Entity303EyeItem extends GeoArmorItem implements IAnimatable {

    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);

    public Entity303EyeItem() {
        super(ScaryBlockArmorMaterial.ENTITY_303, EquipmentSlotType.HEAD,
                new Properties().tab(ScaryBlockItemGroup.SCARY_BLOCK_ITEM_GROUP));
    }

    public void shoot(ServerPlayerEntity p) {
        if (p.getCooldowns().isOnCooldown(this)) return;
        shoot(p, (ServerWorld) p.level, p.getEyePosition(1), p.getLookAngle());
        p.getCooldowns().addCooldown(this, 20);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return this.factory;
    }


    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity p) {

    }

    private <P extends IAnimatable> PlayState predicate(AnimationEvent<P> event) {
//        event.getController().setAnimation(new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP));
        return PlayState.CONTINUE;
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    private void spawnParticle(World w, double x, double y, double z, double offset) {
        double xRandom = ThreadLocalRandom.current().nextDouble(-offset, offset);
        double zRandom = ThreadLocalRandom.current().nextDouble(-offset, offset);
        w.addParticle(ParticleTypes.SMOKE, xRandom + x, y, zRandom + z,
                0, 0, 0);

    }

    private static void shoot(ServerPlayerEntity p, ServerWorld w, Vector3d start, Vector3d dir) {
        Vector3d dirClone = new Vector3d(dir.x, dir.y, dir.z).normalize().scale(1);
        Vector3d[] prev = {start};
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int count = 0;

            @Override
            public void run() {
                List<LivingEntity> list = w.getNearbyEntities(LivingEntity.class, new EntityPredicate(), null,
                        new AxisAlignedBB(new BlockPos(prev[0].x, prev[0].y, prev[0].z))).stream().filter(l ->
                        !l.getUUID().equals(p.getUUID())).collect(Collectors.toList());
                if (count >= 1000 || list.size() > 0) {
                    timer.cancel();
                    list.forEach(e -> {
                        e.setSecondsOnFire(5);
                        if (e instanceof PlayerEntity) e.hurt(DamageSource.playerAttack(p), 10);
                    });
                    return;
                }
                prev[0] = prev[0].add(dirClone);
                w.sendParticles(new RedstoneParticleData(1, 0, 0, 1),
                        prev[0].x, prev[0].y, prev[0].z, 1, 0, 0, 0, 0);
                count++;
            }
        }, 0, 10);
    }

}

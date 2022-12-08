package com.eystreem.scaryblock.item.items;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.item.ScaryBlockItemGroup;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MobEntity;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.CowEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemTier;
import net.minecraft.item.SwordItem;
import net.minecraft.network.play.server.SSpawnParticlePacket;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Mod.EventBusSubscriber(modid = ScaryBlockMod.MOD_ID)
public class VllrStaffItem extends SwordItem {

    private static final Map<UUID, KillTask> killing = new HashMap<>();
    private long lastUsed;

    public VllrStaffItem() {
        super(ItemTier.WOOD, 3, -2.4F, new Properties().tab(ScaryBlockItemGroup.SCARY_BLOCK_ITEM_GROUP));
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return false;
    }

    @Override
    public boolean hurtEnemy(ItemStack i, LivingEntity hit, LivingEntity player) {
        if (!(player instanceof ServerPlayerEntity)) return false;
        ServerPlayerEntity p = (ServerPlayerEntity) player;
        if (killing.containsKey(hit.getUUID())) return false;
        int secondsCooldown = 10;
        if (TimeUnit.MILLISECONDS.toSeconds(System.currentTimeMillis() - lastUsed) > secondsCooldown) {
            KillTask t = new KillTask(this, p, hit);
            killing.put(hit.getUUID(), t);
            t.kill();
            lastUsed = System.currentTimeMillis();
            p.getCooldowns().addCooldown(this, secondsCooldown * 20);
        }

        return false;
    }


    private static class KillTask {

        private Timer timer;
        private final VllrStaffItem item;
        private final ServerPlayerEntity p;
        private final LivingEntity e;

        private KillTask(VllrStaffItem item, ServerPlayerEntity p, LivingEntity e) {
            this.item = item;
            this.p = p;
            this.e = e;
        }

        private Collection<Vector3d> sphere(Vector3d loc) {
            Set<Vector3d> points = new HashSet<>();
            int samples = 50;
            double phi = Math.PI * (3 - Math.sqrt(5)); //golden angle in radians
            for (int i = 0; i < samples; i++) {
                double y = 1 - (i / (float) (samples - 1)) * 2; //y goes from 1 to -1
                double radius = Math.sqrt(1 - y * y); //radius at y
                double theta = phi * i; //golden angle increment
                double x = Math.cos(theta) * radius;
                double z = Math.sin(theta) * radius;
                Vector3d cloneLoc = new Vector3d(loc.x, loc.y, loc.z);
                points.add(cloneLoc.add(new Vector3d(x, y, z).scale(2)));
            }
            return points;
        }

        private void kill() {
            if (timer != null) timer.cancel();
            this.timer = new Timer();
            e.setNoGravity(true);
            if (e instanceof MobEntity) {
                MobEntity mobEntity = (MobEntity) e;
                mobEntity.setNoAi(true);
            }
            e.noPhysics = false;
            timer.schedule(new TimerTask() {
                int max = 150;
                double current = 0;

                @Override
                public void run() {
                    if (current < max && e.isAlive()) {
                        e.move(MoverType.SELF, new Vector3d(0, 0.03, 0));
                        Vector3d pos = new Vector3d(e.position().x, e.position().y, e.position().z);
                        Collection<Vector3d> coll = sphere(pos.add(0, 1, 0));
                        coll.forEach(v -> {
                            if (e.level instanceof ServerWorld) {
                                ServerWorld w = (ServerWorld) e.level;
                                if (current % 10 == 0)
                                    w.sendParticles(new RedstoneParticleData(166/255F, 17/255F,
                                                    186/255F, 1), v.x, v.y, v.z, 1,
                                            0, 0, 0, 0);
                            }
                        });

                        current++;
                    } else {
                        killing.remove(e.getUUID());
                        e.remove();
                        timer.cancel();
                        timer = null;
                    }
                }
            }, 0, 10);
        }

    }

}

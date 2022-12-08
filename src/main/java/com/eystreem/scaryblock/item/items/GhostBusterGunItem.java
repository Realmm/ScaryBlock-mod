package com.eystreem.scaryblock.item.items;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.entities.thatthing.ThatThingEntity;
import com.eystreem.scaryblock.item.ScaryBlockItemGroup;
import net.minecraft.entity.EntityPredicate;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.monster.MonsterEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUseContext;
import net.minecraft.particles.RedstoneParticleData;
import net.minecraft.util.ActionResult;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ScaryBlockMod.MOD_ID)
public class GhostBusterGunItem extends Item {

    public GhostBusterGunItem() {
        super(new Properties().tab(ScaryBlockItemGroup.SCARY_BLOCK_ITEM_GROUP));
    }

    @SubscribeEvent
    public static void on(PlayerInteractEvent e) {
        ItemStack i = e.getItemStack();
        if (!(i.getItem() instanceof GhostBusterGunItem)) return;
        if (!(e.getWorld() instanceof ServerWorld)) return;
        Vector3d dir = e.getPlayer().getLookAngle();
        shoot((ServerWorld) e.getWorld(), e.getPlayer().getEyePosition(1), dir);
    }

    private static void shoot(ServerWorld w, Vector3d start, Vector3d dir) {
        Vector3d dirClone = new Vector3d(dir.x, dir.y, dir.z).normalize().scale(1);
        Vector3d[] prev = {start};
        final Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int count = 0;

            @Override
            public void run() {
                List<MonsterEntity> list = w.getNearbyEntities(MonsterEntity.class, new EntityPredicate(), null, new AxisAlignedBB(new BlockPos(prev[0].x, prev[0].y, prev[0].z)));
                if (count >= 1000 || list.size() > 0) {
                    timer.cancel();
                    list.stream()
                            .filter(e -> e instanceof ThatThingEntity)
                            .map(e -> (ThatThingEntity) e)
                            .filter(e -> !e.canAttackOtherMobs())
                            .forEach(e -> {
                                e.setNoAi(true);
                                e.setAttackOtherMobs();
                                Timer timer = new Timer();
                                timer.schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        e.setNoAi(false);
                                    }
                                }, 3000);
                                spawnParticles(w, () -> new BlockPos(e.getX(), e.getY() + 1.5, e.getZ()), 1, 1.5, 50);
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

    private static void spawnParticles(ServerWorld w, Supplier<BlockPos> pos, double horizontalOffset, double verticalOffset, int amount) {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            int count = 0;

            @Override
            public void run() {
                for (int i = 0; i < amount; i++) {
                    double x = pos.get().getX();
                    double y = pos.get().getY();
                    double z = pos.get().getZ();
                    double xRandom = ThreadLocalRandom.current().nextDouble(-horizontalOffset, horizontalOffset);
                    double yRandom = ThreadLocalRandom.current().nextDouble(-verticalOffset, verticalOffset);
                    double zRandom = ThreadLocalRandom.current().nextDouble(-horizontalOffset, horizontalOffset);
                    w.sendParticles(new RedstoneParticleData(1, 1, 1, 1),
                            x + xRandom, y + yRandom, z + zRandom,
                            1, 0, 0, 0, 0);
                }
                if (count >= 12) {
                    timer.cancel();
                    count = 0;
                } else count++;
            }
        }, 0, 250);
    }

}

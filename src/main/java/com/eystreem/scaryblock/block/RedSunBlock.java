package com.eystreem.scaryblock.block;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.item.ScaryBlockItemGroup;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.client.renderer.entity.EntityRendererManager;
import net.minecraft.client.renderer.entity.FallingBlockRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.ArmorStandEntity;
import net.minecraft.entity.item.FallingBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.item.ItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ScaryBlockMod.MOD_ID)
public class RedSunBlock extends FallingBlock {

    private static final Set<Asteroid> asteroids = new HashSet<>();

    public RedSunBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).lightLevel(i -> 14).strength(2));
    }

    @SubscribeEvent
    public static void onPlace(BlockEvent.EntityPlaceEvent e) {
        if (!(e.getPlacedBlock().getBlock() instanceof RedSunBlock)) return;
        e.setCanceled(true);
    }

    @Override
    public void onLand(World w, BlockPos pos, BlockState s1, BlockState s2, FallingBlockEntity e) {
        explode(w, pos, e);
    }

    @Override
    public void onBroken(World w, BlockPos pos, FallingBlockEntity e) {
        explode(w, pos, e);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ServerTickEvent e) {
        new ArrayList<>(asteroids).forEach(Asteroid::serverTick);
    }

    @SubscribeEvent
    public static void onTick(TickEvent.ClientTickEvent e) {
        new ArrayList<>(asteroids).forEach(a -> a.clientTick());
    }

    @SubscribeEvent
    public static void onDamage(LivingHurtEvent e) {
        if (!(e.getSource().getEntity() instanceof ArmorStandEntity)) return;
        if (!(e.getEntity() instanceof ServerPlayerEntity)) return;
        e.setCanceled(true);
    }

    public static void shootAsteroid(ServerPlayerEntity p) {
        ServerWorld w = p.getLevel();
        if (w.isClientSide) return;
        Item item = p.getMainHandItem().getItem();
        if (!(item instanceof BlockItem)) return;
        BlockItem blockItem = (BlockItem) item;
        if (!(blockItem.getBlock() instanceof RedSunBlock)) return;
        Vector3d start = new Vector3d(p.getX() + ThreadLocalRandom.current().nextInt(20, 100),
                300, p.getZ() + ThreadLocalRandom.current().nextInt(20, 100));
        Vector3d to = p.position();
        Asteroid asteroid = new Asteroid(w, start, to);
        asteroids.add(asteroid);
    }

    @Override
    public boolean canDropFromExplosion(BlockState state, IBlockReader world, BlockPos pos, Explosion explosion) {
        return false;
    }

    private static class RedSunFallingBlock extends FallingBlockEntity {

        private final World w;
        private final Asteroid asteroid;

        public RedSunFallingBlock(Asteroid asteroid, ServerWorld w, double x, double y, double z) {
            super(w, x, y, z, ScaryBlockBlocks.RED_SUN.get().defaultBlockState());
            this.asteroid = asteroid;
            this.w = w;
            time = 1;
            dropItem = false;
            setNoGravity(true);
            setViewScale(100);
            setInvulnerable(true);
        }

        @Override
        public void remove() {
        }

        private void delete() {
            super.remove();
        }

        private void spawn() {
            w.addFreshEntity(this);
        }

    }

    @Mod.EventBusSubscriber
    private static class Asteroid {

        private final RedSunFallingBlock block;
        private final Vector3d direction;

        private Asteroid(ServerWorld w, Vector3d from, Vector3d to) {
            this.block = new RedSunFallingBlock(this, w, from.x, from.y, from.z);
            block.spawn();

            int speed = 5;
            direction = new Vector3d(to.x, to.y, to.z).subtract(from).normalize().scale(speed);
        }

        @SubscribeEvent
        public static void onDespawn(LivingSpawnEvent.AllowDespawn e) {
            if (e.getWorld().isClientSide()) return;
            if (!(e.getEntity() instanceof RedSunFallingBlock)) return;
            e.setCanceled(true);
        }

        private void clientTick() {
            if (!asteroids.contains(this)) return;
            block.noPhysics = false;
            block.setDeltaMovement(direction);
            block.getLevel().addParticle(ParticleTypes.LARGE_SMOKE, true, block.getX(), block.getY(), block.getZ(), 0, 0, 0);
        }

        private void serverTick() {
            if (!asteroids.contains(this)) return;
            if (block.isOnGround() || !block.isAlive()) {
                explode(block.w, block.blockPosition(), block);
            }
        }

    }

    private static void explode(World w, BlockPos pos, FallingBlockEntity e) {
        if (!(e instanceof RedSunFallingBlock)) return;
        RedSunFallingBlock b = (RedSunFallingBlock) e;
        if (!asteroids.contains(b.asteroid)) return;
        b.delete();
        w.explode(e, null, null, pos.getX(), pos.getY(), pos.getZ(), 3, true, Explosion.Mode.DESTROY);
        asteroids.remove(b.asteroid);
    }

}

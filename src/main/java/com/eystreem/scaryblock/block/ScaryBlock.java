package com.eystreem.scaryblock.block;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.entities.ScaryBlockEntityTypes;
import com.eystreem.scaryblock.entities.bloodgolem.BloodGolemEntity;
import com.eystreem.scaryblock.states.SoulsEscapedBossBarState;
import com.eystreem.scaryblock.util.Config;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.passive.IronGolemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Explosion;
import net.minecraft.world.IWorld;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ToolType;
import net.minecraftforge.event.entity.living.LivingDestroyBlockEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.system.CallbackI;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ScaryBlock extends Block {

    public ScaryBlock() {
        super(AbstractBlock.Properties.of(Material.GRASS)
                .strength(2));
    }

    private static void spawnGolem(BlockEvent.BreakEvent e) {
        PlayerEntity p = e.getPlayer();
        if (p.level.isClientSide) return;
        ServerWorld w = (ServerWorld) e.getPlayer().level;
        BlockPos pos = new BlockPos(e.getPos().getX() + .5, e.getPos().getY() + 1, e.getPos().getZ() + .5);
        IronGolemEntity ironGolem = EntityType.IRON_GOLEM.create(w);
        if (ironGolem == null) return;
        ironGolem.setPos(pos.getX(), pos.getY(), pos.getZ());
        ironGolem.setNoAi(true);
        w.addFreshEntity(ironGolem);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!ironGolem.isAlive()) return;
                BloodGolemEntity bloodGolem = ScaryBlockEntityTypes.BLOOD_GOLEM.get().create(w);
                if (bloodGolem == null) return;
                bloodGolem.yRot = ironGolem.yRot;
                bloodGolem.xRot = ironGolem.xRot;
                bloodGolem.setPos(ironGolem.getX(), ironGolem.getY(), ironGolem.getZ());
                ironGolem.remove();
                w.addFreshEntity(bloodGolem);
                timer.cancel();
            }
        }, 15 * 1000);
    }

    @SubscribeEvent
    public static void onBreak(BlockEvent.BreakEvent e) {
        if (!(e.getState().getBlock() instanceof ScaryBlock)) return;
        e.setCanceled(true);
        PlayerEntity p = e.getPlayer();
        if (p.level.isClientSide) return;

        ServerWorld w = (ServerWorld) e.getPlayer().level;
        int random = ThreadLocalRandom.current().nextInt(1, 101); //1-100 inclusive

        BiFunction<ItemStack, String[], Boolean> selectBlocks = (i, blocks) -> {
            ResourceLocation resourceLocation = i.getItem().getRegistryName();
            if (resourceLocation == null) return false;
            return Arrays.stream(blocks).anyMatch(resourceLocation.getPath()::contains);
        };

        List<ItemStack> drops = Block.getDrops(e.getState(), w, e.getPos(), null); //3 drops
        String[] customItems = new String[]{"sirenhead_pickaxe",
                "huggywuggy_feet", "red_sun", "vllr_staff", "ghostbuster_gun", "giant_alex", "entity_spawner", "eye"};
        String[] blockItems = new String[]{"redstone_block", "netherrack", "soul_sand", "cobweb", "coarse_dirt"};
        ItemStack customItem = drops.stream().filter(i -> selectBlocks.apply(i, customItems))
                .findFirst().orElseThrow(() -> new IllegalStateException("Custom item not found"));
        ItemStack blockItem = drops.stream().filter(i -> selectBlocks.apply(i, blockItems)).findFirst().orElseThrow(() ->
                new IllegalStateException("Block item not found"));
        ItemStack normalItem = drops.stream().filter(i -> i != customItem && i != blockItem).findFirst()
                .orElseThrow(() -> new IllegalStateException("Item not found"));

        ItemStack stack = null;
        if (random <= 10) {
            boolean spawnGolem = random <= 2; //chance to spawn blood golem
            boolean dropCustomItem = ThreadLocalRandom.current().nextInt(1, 101) <= 50;
            boolean droppedCustomItem = false;
            if (ScaryBlockMod.getInstance().getCut() == 1 && spawnGolem) {
                spawnGolem(e);
                return;
            }
            if (dropCustomItem) {
                //drop custom item depending on current cut
                ResourceLocation name = Objects.requireNonNull(customItem.getItem().getRegistryName());
                if (ScaryBlockMod.getInstance().getCut() == 1) {
                    if (Arrays.stream(new String[]{"sirenhead_pickaxe",
                            "huggywuggy_feet", "red_sun", "vllr_staff", "eye"}).anyMatch(s -> name.getPath().contains(s))) {
                        stack = customItem;
                        droppedCustomItem = true;
                    }
                } else {
                    if (Arrays.stream(new String[]{"ghostbuster_gun", "giant_alex",
                            "entity_spawner"}).anyMatch(s -> name.getPath().contains(s))) {
                        stack = customItem;
                        droppedCustomItem = true;
                    }
                }
            }

            if (!droppedCustomItem) {
                //drop normal item
                stack = normalItem;
            } else {
                ScaryBlockMod.getInstance().getStateManager()
                        .getCurrentState(SoulsEscapedBossBarState.class).ifPresent(state ->
                                state.setSoulsEscaped(state.getSoulsEscaped() + Config.SOULS_ESCAPED_CUSTOM_ITEM.get()));
            }
        } else {
            stack = blockItem;
        }
        if (stack == null) stack = blockItem;

        ItemEntity itemEntity = new ItemEntity(w, e.getPos().getX() + .5, e.getPos().getY() + 1, e.getPos().getZ() + .5, stack);
        w.addFreshEntity(itemEntity);
    }

}

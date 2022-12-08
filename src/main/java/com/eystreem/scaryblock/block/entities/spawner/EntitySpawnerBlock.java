package com.eystreem.scaryblock.block.entities.spawner;

import com.eystreem.scaryblock.block.entities.ScaryBlockBlockEntities;
import com.eystreem.scaryblock.entities.ScaryBlockEntityTypes;
import com.eystreem.scaryblock.entities.thatthing.ThatThingEntity;
import com.eystreem.scaryblock.item.ScaryBlockItemGroup;
import net.minecraft.block.*;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.tileentity.ChestTileEntityRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.ForgeSpawnEggItem;
import software.bernie.geckolib3.core.IAnimatable;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;

public class EntitySpawnerBlock extends SpawnerBlock {

    public EntitySpawnerBlock() {
        super(AbstractBlock.Properties.of(Material.STONE).strength(2).noOcclusion());
    }

    @Override
    public TileEntity newBlockEntity(IBlockReader reader) {
        return ScaryBlockBlockEntities.ENTITY_SPAWNER_BLOCK_ENTITY.get().create();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState state) {
        return BlockRenderType.ENTITYBLOCK_ANIMATED;
    }
}

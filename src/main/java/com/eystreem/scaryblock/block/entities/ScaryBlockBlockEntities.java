package com.eystreem.scaryblock.block.entities;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.block.ScaryBlockBlocks;
import com.eystreem.scaryblock.block.entities.spawner.EntitySpawnerBlockEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class ScaryBlockBlockEntities {

    private static final DeferredRegister<TileEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, ScaryBlockMod.MOD_ID);

    public static final RegistryObject<TileEntityType<EntitySpawnerBlockEntity>> ENTITY_SPAWNER_BLOCK_ENTITY =
            BLOCK_ENTITIES.register("entity_spawner_entity", () ->
                    TileEntityType.Builder.of(EntitySpawnerBlockEntity::new,
                            ScaryBlockBlocks.ENTITY_SPAWNER.get()).build(null));

    public static void register(IEventBus bus) {
        BLOCK_ENTITIES.register(bus);
    }

}

package com.eystreem.scaryblock.block;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.block.entities.spawner.EntitySpawnerBlock;
import com.eystreem.scaryblock.item.ScaryBlockItemGroup;
import com.eystreem.scaryblock.item.ScaryBlockItems;
import net.minecraft.block.Block;
import net.minecraft.block.RedstoneBlock;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class ScaryBlockBlocks {

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, ScaryBlockMod.MOD_ID);

    public static final RegistryObject<Block> SCARY_BLOCK = registerBlock("scary_block", ScaryBlock::new);
    public static final RegistryObject<Block> RED_SUN = registerBlock("red_sun", RedSunBlock::new);
    public static final RegistryObject<Block> ENTITY_SPAWNER = registerBlock("entity_spawner", EntitySpawnerBlock::new, false);

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block, boolean registerItem) {
        RegistryObject<T> o = BLOCKS.register(name, block);
        if (registerItem) registerBlockItem(name, o);
        return o;
    }

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        return registerBlock(name, block, true);
    }

    private static <T extends Block> void registerBlockItem(String name, RegistryObject<T> block) {
        ScaryBlockItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties().tab(ScaryBlockItemGroup.SCARY_BLOCK_ITEM_GROUP)));
    }

}

package com.eystreem.scaryblock.item;

import com.eystreem.scaryblock.block.ScaryBlockBlocks;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ScaryBlockItemGroup {

    public static final ItemGroup SCARY_BLOCK_ITEM_GROUP = new ItemGroup("ScaryBlockItemTab") {

        @Override
        public ItemStack makeIcon() {
            return new ItemStack(ScaryBlockBlocks.SCARY_BLOCK.get());
        }
    };

}

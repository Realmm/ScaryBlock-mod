package com.eystreem.scaryblock.item.items;

import com.eystreem.scaryblock.armor.ScaryBlockArmorMaterial;
import com.eystreem.scaryblock.item.ScaryBlockItemGroup;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.RedstoneBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.BlockParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class BloodChestplateItem extends ArmorItem {

    public BloodChestplateItem() {
        super(ScaryBlockArmorMaterial.BLOOD_ARMOR, EquipmentSlotType.CHEST,
                    new Item.Properties().tab(ScaryBlockItemGroup.SCARY_BLOCK_ITEM_GROUP));
    }

    @Override
    public void onArmorTick(ItemStack stack, World world, PlayerEntity player) {
        if (!world.isClientSide) return;
        BlockState state = Blocks.REDSTONE_BLOCK.getBlock().defaultBlockState();
        world.addParticle(new BlockParticleData(ParticleTypes.BLOCK, state), player.getX(), player.getY(), player.getZ(), 0, 0, 0);
    }
}

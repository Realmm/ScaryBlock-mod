package com.eystreem.scaryblock.keybind;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.block.RedSunBlock;
import com.eystreem.scaryblock.item.items.armor.HuggyWuggyFeetItem;
import com.eystreem.scaryblock.item.items.eye.Entity303EyeItem;
import com.eystreem.scaryblock.network.NetworkMessage;
import com.eystreem.scaryblock.network.packets.DoubleJumpC2SPacket;
import com.eystreem.scaryblock.network.packets.Entity303EyeShootC2SPacket;
import com.eystreem.scaryblock.network.packets.SpawnRedSunAsteroidC2SPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.player.ClientPlayerEntity;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.function.Supplier;

@Mod.EventBusSubscriber(modid = ScaryBlockMod.MOD_ID, value = Dist.CLIENT)
public class KeybindEvents {

    @SubscribeEvent
    public static void onKeyInput(InputEvent.KeyInputEvent e) {
        if (Keybind.RED_SUN_KEY.consumeClick()) {
            ClientPlayerEntity p = Minecraft.getInstance().player;
            if (p == null) return;
            Item i = p.getMainHandItem().getItem();
            boolean isBlockItem = i instanceof BlockItem;
            boolean isRedSunBlock = false;
            if (isBlockItem) {
                BlockItem item = (BlockItem) i;
                isRedSunBlock = item.getBlock() instanceof RedSunBlock;
            }
            if (!isRedSunBlock) {
                p.sendMessage(new StringTextComponent("You must hold the Red Sun block to use")
                        .withStyle(TextFormatting.RED), p.getUUID());
                return;
            }

            NetworkMessage.sendToServer(new SpawnRedSunAsteroidC2SPacket());
        }

        if (Keybind.DOUBLE_JUMP_KEY.consumeClick()) {
            ClientPlayerEntity p = Minecraft.getInstance().player;
            if (p == null) return;
            if (p.inventory.armor.stream().noneMatch(i -> i.getItem() instanceof HuggyWuggyFeetItem)) {
                p.sendMessage(new StringTextComponent("You must equip Huggy Wuggy Feet to use")
                        .withStyle(TextFormatting.RED), p.getUUID());
                return;
            }
            HuggyWuggyFeetItem item = p.inventory.armor.stream().filter(i -> i.getItem() instanceof HuggyWuggyFeetItem)
                    .map(i -> (HuggyWuggyFeetItem) i.getItem()).findFirst().orElse(null);
            if (item == null) throw new IllegalStateException("Unable to find Huggy Wuggy Feet item on player");
            if (!item.canDoubleJump()) return;

            NetworkMessage.sendToServer(new DoubleJumpC2SPacket());
        }

        if (Keybind.ENTITY_303_SHOOT.consumeClick()) {
            ClientPlayerEntity p = Minecraft.getInstance().player;
            if (p == null) return;
            if (p.inventory.armor.stream().noneMatch(i -> i.getItem() instanceof Entity303EyeItem)) {
                p.sendMessage(new StringTextComponent("You must equip Entity303 Eye to use")
                        .withStyle(TextFormatting.RED), p.getUUID());
                return;
            }
            Entity303EyeItem item = p.inventory.armor.stream().filter(i -> i.getItem() instanceof Entity303EyeItem)
                    .map(i -> (Entity303EyeItem) i.getItem()).findFirst().orElse(null);
            if (item == null) throw new IllegalStateException("Unable to find Entity303 Eye item on player");
            if (p.getCooldowns().isOnCooldown(item)) return;

            NetworkMessage.sendToServer(new Entity303EyeShootC2SPacket());
        }
    }

}

package com.eystreem.scaryblock.network.packets;

import com.eystreem.scaryblock.block.RedSunBlock;
import com.eystreem.scaryblock.item.items.armor.HuggyWuggyFeetItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Spawns and plays the red sun asteroid effect
 * Client to Server packet
 */
public class DoubleJumpC2SPacket extends ScaryBlockPacket {

    public DoubleJumpC2SPacket() {

    }

    public DoubleJumpC2SPacket(PacketBuffer packetBuffer) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity p = context.getSender();
            if (p == null) return;
            HuggyWuggyFeetItem item = p.inventory.armor.stream().filter(i -> i.getItem() instanceof HuggyWuggyFeetItem)
                    .map(i -> (HuggyWuggyFeetItem) i.getItem()).findFirst().orElse(null);
            if (item == null) throw new IllegalStateException("Unable to find Huggy Wuggy Feet item on player");
            if (!item.canDoubleJump()) return;
            item.doubleJump(p);
        });
        return true;
    }

}

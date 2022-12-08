package com.eystreem.scaryblock.network.packets;

import com.eystreem.scaryblock.item.items.armor.HuggyWuggyFeetItem;
import com.eystreem.scaryblock.item.items.eye.Entity303EyeItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

/**
 * Spawns and plays the red sun asteroid effect
 * Client to Server packet
 */
public class Entity303EyeShootC2SPacket extends ScaryBlockPacket {

    public Entity303EyeShootC2SPacket() {

    }

    public Entity303EyeShootC2SPacket(PacketBuffer packetBuffer) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity p = context.getSender();
            if (p == null) return;
            Entity303EyeItem item = p.inventory.armor.stream().filter(i -> i.getItem() instanceof Entity303EyeItem)
                    .map(i -> (Entity303EyeItem) i.getItem()).findFirst().orElse(null);
            if (item == null) throw new IllegalStateException("Unable to find Entity303 item on player");
            item.shoot(p);
        });
        return true;
    }

}

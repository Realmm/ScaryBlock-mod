package com.eystreem.scaryblock.network.packets;

import com.eystreem.scaryblock.block.RedSunBlock;
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
public class SpawnRedSunAsteroidC2SPacket extends ScaryBlockPacket {

    public SpawnRedSunAsteroidC2SPacket() {

    }

    public SpawnRedSunAsteroidC2SPacket(PacketBuffer packetBuffer) {

    }

    @Override
    public boolean handle(Supplier<NetworkEvent.Context> supplier) {
        NetworkEvent.Context context = supplier.get();
        context.enqueueWork(() -> {
            ServerPlayerEntity p = context.getSender();
            if (p == null) return;
            Item item = p.getMainHandItem().getItem();
            if (!(item instanceof BlockItem)) return;
            BlockItem blockItem = (BlockItem) item;
            if (!(blockItem.getBlock() instanceof RedSunBlock)) return;
            RedSunBlock.shootAsteroid(p);
        });
        return true;
    }

}

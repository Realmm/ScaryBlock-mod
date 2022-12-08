package com.eystreem.scaryblock.network.packets;

import com.eystreem.scaryblock.item.items.armor.HuggyWuggyFeetItem;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public abstract class ScaryBlockPacket {

    public void toBytes(PacketBuffer packetBuffer) {

    }

    public abstract boolean handle(Supplier<NetworkEvent.Context> supplier);

}

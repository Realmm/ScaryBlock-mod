package com.eystreem.scaryblock.network;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.network.packets.DoubleJumpC2SPacket;
import com.eystreem.scaryblock.network.packets.Entity303EyeShootC2SPacket;
import com.eystreem.scaryblock.network.packets.ScaryBlockPacket;
import com.eystreem.scaryblock.network.packets.SpawnRedSunAsteroidC2SPacket;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Function;

public class NetworkMessage {

    private static SimpleChannel net;
    private static int id;

    private static int id() {
        return id++;
    }

    public static void register() {
        net = NetworkRegistry.ChannelBuilder
                .named(new ResourceLocation(ScaryBlockMod.MOD_ID, "messages"))
                .networkProtocolVersion(() -> "1.0")
                .clientAcceptedVersions(s -> true)
                .serverAcceptedVersions(s -> true)
                .simpleChannel();

        net.messageBuilder(SpawnRedSunAsteroidC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(SpawnRedSunAsteroidC2SPacket::new)
                .encoder(SpawnRedSunAsteroidC2SPacket::toBytes)
                .consumer(SpawnRedSunAsteroidC2SPacket::handle)
                .add();
        net.messageBuilder(DoubleJumpC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(DoubleJumpC2SPacket::new)
                .encoder(DoubleJumpC2SPacket::toBytes)
                .consumer(DoubleJumpC2SPacket::handle)
                .add();
        net.messageBuilder(Entity303EyeShootC2SPacket.class, id(), NetworkDirection.PLAY_TO_SERVER)
                .decoder(Entity303EyeShootC2SPacket::new)
                .encoder(Entity303EyeShootC2SPacket::toBytes)
                .consumer(Entity303EyeShootC2SPacket::handle)
                .add();
    }

    public static <T> void sendToServer(T msg) {
        net.sendToServer(msg);
    }

    public static <T> void sendToPlayer(T msg, ServerPlayerEntity p) {
        net.send(PacketDistributor.PLAYER.with(() -> p), msg);
    }

}

package com.eystreem.scaryblock.states;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.block.ScaryBlock;
import com.eystreem.scaryblock.util.Config;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.*;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The first block break state listens for the first break of the {@link ScaryBlock}, and performs actions
 * <p>
 * Setup on start of mod
 * Tears down after a set of events
 * <p>
 * Wait for {@link ScaryBlock} to be broken for the first time
 * Once it is broken show title
 * Run /setblock command to set a redstone block to a set of coords defined in config
 * Show herobrine/obfuscated text in chat
 * Setup the {@link SoulsEscapedBossBarState} and the {@link SecondBlockBreakState}
 */
public class FirstBlockBreakState extends State {

    @Override
    protected void onSetup() {

    }

    @Override
    protected void onTeardown() {

    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockEvent.BreakEvent e) {
        if (!(e.getState().getBlock() instanceof ScaryBlock)) return;
        if (e.getWorld().isClientSide() || !(e.getPlayer() instanceof ServerPlayerEntity)) return;
        ServerPlayerEntity p = (ServerPlayerEntity) e.getPlayer();
        BlockState state = e.getState();
        if (!(state.getBlock() instanceof ScaryBlock)) return;
        setRedstoneBlock();
        sendMessagesAndTitles(p);
        ScaryBlockMod.getInstance().getStateManager().teardown(FirstBlockBreakState.class);
        ScaryBlockMod.getInstance().getStateManager().setup(SoulsEscapedBossBarState.class);
        ScaryBlockMod.getInstance().getStateManager().setup(SecondBlockBreakState.class);
    }



    private void setRedstoneBlock() {
        MinecraftServer source = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        source.getCommands().performCommand(source.createCommandSourceStack(),
                "setblock " + Config.REDSTONE_BLOCK_X.get() + " " +
                        Config.REDSTONE_BLOCK_Y.get() + " " + Config.REDSTONE_BLOCK_Z.get() + " minecraft:redstone_block replace");
    }

    private void sendMessagesAndTitles(ServerPlayerEntity p) {
        Supplier<Integer> random = () -> ThreadLocalRandom.current().nextInt(3, 5); //3-4 inclusive
        Function<Boolean, IFormattableTextComponent> obfuscate = red -> {
            StringTextComponent c = new StringTextComponent(random.get() == 3 ? "abc" : "abcd");
            if (red) {
                c.withStyle(TextFormatting.DARK_RED, TextFormatting.BOLD, TextFormatting.OBFUSCATED);
            } else {
                c.withStyle(TextFormatting.WHITE, TextFormatting.OBFUSCATED);
            }
            return c;
        };
        showTitle(p, new StringTextComponent("Herobrine Added").withStyle(TextFormatting.DARK_RED, TextFormatting.BOLD));
        showSubTitle(p, new StringTextComponent("")
                .append(obfuscate.apply(true))
                .append(new StringTextComponent(" Herobrine Added ").withStyle(TextFormatting.DARK_RED, TextFormatting.BOLD))
                .append(obfuscate.apply(true)));
        BiConsumer<IFormattableTextComponent, IFormattableTextComponent> sendObfuscatedMessage = (c1, c2) ->
                p.sendMessage(c1.append(obfuscate.apply(false))
                        .append(c2).append(obfuscate.apply(false)), p.getUUID());
        sendObfuscatedMessage.accept(new StringTextComponent("<Admin> ").withStyle(TextFormatting.WHITE),
                new StringTextComponent("")
                        .append(new StringTextComponent("Loading metadata: ").withStyle(TextFormatting.WHITE))
                        .append("Java Alpha 1.0.16_02").withStyle(TextFormatting.AQUA));
        sendObfuscatedMessage.accept(new StringTextComponent("<Admin> ").withStyle(TextFormatting.WHITE),
                new StringTextComponent("")
                        .append(new StringTextComponent("Confirm ").withStyle(TextFormatting.WHITE))
                        .append(new StringTextComponent("delete ").withStyle(TextFormatting.RED))
                        .append(new StringTextComponent("code: ").withStyle(TextFormatting.WHITE))
                        .append(new StringTextComponent("\"Herobrine Removed\"").withStyle(TextFormatting.LIGHT_PURPLE)));
        sendObfuscatedMessage.accept(new StringTextComponent("<Admin> ").withStyle(TextFormatting.WHITE),
                new StringTextComponent("")
                        .append(new StringTextComponent("Confirmed").withStyle(TextFormatting.GREEN)));
        sendObfuscatedMessage.accept(new StringTextComponent("<Herobrine> ").withStyle(TextFormatting.DARK_RED),
                new StringTextComponent("")
                        .append(new StringTextComponent("Thank you for freeing my ").withStyle(TextFormatting.WHITE))
                        .append(new StringTextComponent("soul!").withStyle(TextFormatting.LIGHT_PURPLE)));
        sendObfuscatedMessage.accept(new StringTextComponent("<Herobrine> ").withStyle(TextFormatting.DARK_RED),
                new StringTextComponent("")
                        .append(new StringTextComponent("Now you, and every other entity will be trapped ").withStyle(TextFormatting.WHITE))
                        .append(new StringTextComponent("FOREVER!").withStyle(TextFormatting.BLACK, TextFormatting.BOLD)));
    }

    private void showTitle(ServerPlayerEntity p, ITextComponent title) {
        try {
            p.connection.send(new STitlePacket(STitlePacket.Type.TITLE,
                    TextComponentUtils.updateForEntity(p.createCommandSourceStack(), title, p, 0)));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }

    private void showSubTitle(ServerPlayerEntity p, ITextComponent title) {
        try {
            p.connection.send(new STitlePacket(STitlePacket.Type.SUBTITLE,
                    TextComponentUtils.updateForEntity(p.createCommandSourceStack(), title, p, 0)));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }

}

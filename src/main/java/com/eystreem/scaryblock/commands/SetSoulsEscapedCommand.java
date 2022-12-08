package com.eystreem.scaryblock.commands;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.states.JumpScareState;
import com.eystreem.scaryblock.states.SoulsEscapedBossBarState;
import com.eystreem.scaryblock.states.StateManager;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.ArgumentTypes;
import net.minecraft.command.impl.TeleportCommand;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

/**
 * Can type /setsoulsescaped <0-666> to set souls escaped
 */
public class SetSoulsEscapedCommand {

    public SetSoulsEscapedCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("setsoulsescaped")
                .then(Commands.argument("amount", IntegerArgumentType.integer(0, SoulsEscapedBossBarState.MAX_SOULS))
                        .executes(command -> {
                            StateManager sm = ScaryBlockMod.getInstance().getStateManager();
                            SoulsEscapedBossBarState state = sm.getCurrentState(SoulsEscapedBossBarState.class).orElse(null);
                            if (state == null) {
                                command.getSource().sendFailure(new StringTextComponent(
                                        "Unable to set souls escaped amount, " +
                                                "soul state not yet started, break a scaryblock first"));
                                return -1;
                            }
                            int arg = command.getArgument("amount", Integer.class);
                            state.setSoulsEscaped(arg);
                            command.getSource().sendSuccess(new StringTextComponent(
                                    "Set souls escaped to " + arg).withStyle(TextFormatting.GREEN), false);
                            return 1;
                        })));
    }

}

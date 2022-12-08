package com.eystreem.scaryblock.commands;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.states.StateManager;
import com.eystreem.scaryblock.states.TimeTillInjectionBossBarState;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Can type /injection set <x> where 'x' is a number to set the injection timer to
 * Can type /injection pause to pause/unpause the injection timer
 */
public class InjectionCommand {

    public InjectionCommand(CommandDispatcher<CommandSource> dispatcher) {
        Function<CommandContext<CommandSource>, Optional<TimeTillInjectionBossBarState>> checkStarted = source -> {
            StateManager sm = ScaryBlockMod.getInstance().getStateManager();
            TimeTillInjectionBossBarState state = sm.getCurrentState(TimeTillInjectionBossBarState.class).orElse(null);
            if (state == null) {
                source.getSource().sendFailure(new StringTextComponent("Time Till Injection state not yet begun, unable to set time"));
                return Optional.empty();
            }
            return Optional.of(state);
        };
        dispatcher.register(Commands.literal("injection")
                .then(Commands.literal("pause").executes(command -> {
                    TimeTillInjectionBossBarState state = checkStarted.apply(command).orElse(null);
                    if (state == null) return -1;
                    state.setPaused(!state.isPaused());
                    command.getSource().sendSuccess(new StringTextComponent(
                            state.isPaused() ? "Paused time till injection" : "Unpaused time till injection")
                            .withStyle(TextFormatting.GREEN), false);
                    return 1;
                })));

        dispatcher.register(Commands.literal("injection").then(Commands.literal("set")
                .then(Commands.argument("time", IntegerArgumentType.integer(0,
                                TimeTillInjectionBossBarState.MAX_DURATION_SECONDS))
                        .executes(command -> {
                            TimeTillInjectionBossBarState state = checkStarted.apply(command).orElse(null);
                            if (state == null) return -1;
                            int arg = command.getArgument("time", Integer.class);
                            state.setTime(arg);
                            command.getSource().sendSuccess(new StringTextComponent(
                                    "Set time till injection to " + arg)
                                    .withStyle(TextFormatting.GREEN), false);
                            return 1;
                        }))));
    }

}

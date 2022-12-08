package com.eystreem.scaryblock.commands;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;

/**
 * Can type /setcut <0-5>
 */
public class SetCutCommand {

    public SetCutCommand(CommandDispatcher<CommandSource> dispatcher) {
        dispatcher.register(Commands.literal("setcut")
                .then(Commands.argument("cut", IntegerArgumentType.integer(1, 2)).executes(command -> {
                    int cut = command.getArgument("cut", Integer.class);
                    ScaryBlockMod.getInstance().setCut(cut, true);
                    command.getSource().sendSuccess(new StringTextComponent("Set cut to " + cut)
                            .withStyle(TextFormatting.GREEN), false);
                    return 1;
                })));
    }
}

package com.eystreem.scaryblock.util;

import net.minecraftforge.common.ForgeConfigSpec;

public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<Integer> REDSTONE_BLOCK_X;
    public static final ForgeConfigSpec.ConfigValue<Integer> REDSTONE_BLOCK_Y;
    public static final ForgeConfigSpec.ConfigValue<Integer> REDSTONE_BLOCK_Z;
    public static final ForgeConfigSpec.ConfigValue<Integer> SPAWNER_BOSS_BAR_ADD_AMOUNT;
    public static final ForgeConfigSpec.ConfigValue<Integer> SOULS_ESCAPED_CUSTOM_ITEM;

    static {
        BUILDER.push("Config for ScaryBlock");

        REDSTONE_BLOCK_X = BUILDER.comment("Set the X location to spawn the redstone block at").define("X", 0);
        REDSTONE_BLOCK_Y = BUILDER.comment("Set the Y location to spawn the redstone block at").define("Y", 0);
        REDSTONE_BLOCK_Z = BUILDER.comment("Set the Z location to spawn the redstone block at").define("Z", 0);

        SPAWNER_BOSS_BAR_ADD_AMOUNT = BUILDER.comment("Set the amount to add to the souls escaped on killing spawner entities").define("DEATH_ADD_AMOUNT", 50);
        SOULS_ESCAPED_CUSTOM_ITEM = BUILDER.comment("Set the souls escaped amount to modify boss bar when find a custom item").define("SOULS_ESCAPED_CUSTOM_ITEM", 0);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

}

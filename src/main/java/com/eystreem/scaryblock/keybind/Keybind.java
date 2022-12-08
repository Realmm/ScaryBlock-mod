package com.eystreem.scaryblock.keybind;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.util.InputMappings;
import net.minecraftforge.client.settings.KeyBindingMap;
import net.minecraftforge.client.settings.KeyConflictContext;
import org.lwjgl.glfw.GLFW;

public class Keybind {

    public static final String KEY_CATEGORY_SCARYBLOCK = "key.category.scaryblock.scaryblock";

    public static final KeyBinding RED_SUN_KEY = new KeyBinding("key.scaryblock.red_sun", KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_K, KEY_CATEGORY_SCARYBLOCK);

    public static final KeyBinding DOUBLE_JUMP_KEY = new KeyBinding("key.scaryblock.double_jump", KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_M, KEY_CATEGORY_SCARYBLOCK);

    public static final KeyBinding ENTITY_303_SHOOT = new KeyBinding("key.scaryblock.entity_303_shoot", KeyConflictContext.IN_GAME,
            InputMappings.Type.KEYSYM, GLFW.GLFW_KEY_N, KEY_CATEGORY_SCARYBLOCK);

}

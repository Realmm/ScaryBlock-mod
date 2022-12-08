package com.eystreem.scaryblock.states;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.block.ScaryBlock;
import com.eystreem.scaryblock.sound.ScaryBlockSounds;
import com.eystreem.scaryblock.util.Config;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.block.BlockState;
import net.minecraft.client.MainWindow;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.screen.inventory.InventoryScreen;
import net.minecraft.client.renderer.OverlayRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.PlayerContainer;
import net.minecraft.network.play.server.STitlePacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.text.*;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.GuiContainerEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.gui.ForgeIngameGui;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.Nonnull;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * The first block break state listens for the first break of the {@link ScaryBlock}, and performs actions
 * <p>
 * Setup after second block break
 * Tears down after finishing jump scare
 * <p>
 * Perform a jump scare using image of blood villager
 * Scary scream sound as well played
 * Stop after a few seconds, and teardown
 */
public class JumpScareState extends State {

    private Timer timer = null;

    @Override
    protected void onSetup() {
        Minecraft.getInstance().pushGuiLayer(new JumpScareScreen());
        MinecraftServer source = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        source.getPlayerList().getPlayers().forEach(p -> p.getLevel()
                .playSound(null, p, ScaryBlockSounds.JUMPSCARE.get(),
                        SoundCategory.MASTER, 1, 1));
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                StateManager m = ScaryBlockMod.getInstance().getStateManager();
                if (m.isSetup(JumpScareState.class)) m.teardown(JumpScareState.class);
            }
        }, 8000);
    }

    @Override
    protected void onTeardown() {
        if (timer != null) timer.cancel();
        timer = null;
        Minecraft.getInstance().popGuiLayer();
    }

    private static class JumpScareScreen extends Screen {

        public JumpScareScreen() {
            super(new StringTextComponent(""));
        }

        @Override
        public void onClose() {
            StateManager m = ScaryBlockMod.getInstance().getStateManager();
            if (m.isSetup(JumpScareState.class)) m.teardown(JumpScareState.class);
            super.onClose();
        }

        @Override
        public void render(MatrixStack stack, int x, int y, float partialTicks) {
            renderBackground(stack);
            renderBg(stack);
            super.render(stack, x, y, partialTicks);
        }

        protected void renderBg(MatrixStack stack) {
            if (minecraft == null) return;
            minecraft.getTextureManager().bind(new ResourceLocation("scaryblock", "textures/gui/blood_villager.png"));
            MainWindow window = Minecraft.getInstance().getWindow();
            int width = window.getGuiScaledWidth();
            int height = window.getGuiScaledHeight();
            blit(stack, 0, 0, getBlitOffset(), 0, 0, width, height, height, width);
        }

    }

}

package com.eystreem.scaryblock.states;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.block.ScaryBlock;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SUpdateBossInfoPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Shows the boss bar to display time until injection
 * <p>
 * Setup after souls escaped bar is maxed out
 * Stays alive unless reset by restart command
 * Can set time til injection with command manually
 * <p>
 * Have 10 minutes, boss bar ticks up throughout the 10 minutes
 * Once filled up, show obfuscated messages in chat
 */
public class TimeTillInjectionBossBarState extends State implements BossBarState {

    private int timeTillInjectionTicks = 0;

    public static final int MAX_DURATION_SECONDS = 60 * 10;
    private final int tickDuration = MAX_DURATION_SECONDS * 20;
    private final ServerBossInfo bossInfo = new ServerBossInfo(new StringTextComponent("Time Till Injection"),
            BossInfo.Color.YELLOW, BossInfo.Overlay.PROGRESS);
    private Timer timer = null;
    private boolean paused = false;

    /**
     * Check if the state is paused
     * @return True if the state is paused, otherwise false
     */
    public boolean isPaused() {
        return paused;
    }

    /**
     * Set the timer as paused, or unpause the timer
     * @param paused True if pausing the timer, otherwise false
     */
    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    /**
     * Set the time until injection
     * @param time The time, in seconds, until injection
     * @throws IndexOutOfBoundsException If attempting to set injection time less than 0, or greater than max duration
     */
    public void setTime(int time) {
        if (time < 0 || time > MAX_DURATION_SECONDS) throw new IndexOutOfBoundsException("Unable to set time to less than 0 or greater than max duration");
        this.timeTillInjectionTicks = time * 20;
        updateBossBar();
        if (timeTillInjectionTicks >= tickDuration) sendMessages();
    }

    @Override
    public void onSetup() {
        updateBossBar();
    }

    @Override
    public void onTeardown() {
        if (timer != null) timer.cancel();
        timeTillInjectionTicks = 0;
        timer = null;
        paused = false;
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent e) {
        if (paused || timeTillInjectionTicks >= tickDuration) return;
        timeTillInjectionTicks++;
        if (timeTillInjectionTicks % 20 == 0) updateBossBar();
        if (timeTillInjectionTicks >= tickDuration) {
            sendMessages();
        }
    }

    private void sendMessages() {
        Queue<IFormattableTextComponent> messages = new ArrayDeque<>();
        messages.add(new StringTextComponent("asdahsdahsdhasasd").withStyle(TextFormatting.DARK_RED, TextFormatting.OBFUSCATED));

        Function<String, IFormattableTextComponent> letterTailObfuscate = s -> new StringTextComponent("")
                .append(new StringTextComponent(s).withStyle(TextFormatting.DARK_RED))
                .append(new StringTextComponent("asjkdhashjasdjad").withStyle(TextFormatting.DARK_RED, TextFormatting.OBFUSCATED));

        IFormattableTextComponent injectingComponent = new StringTextComponent("")
                .append(letterTailObfuscate.apply("I"))
                .append(letterTailObfuscate.apply("N"))
                .append(letterTailObfuscate.apply("J"))
                .append(letterTailObfuscate.apply("E"))
                .append(letterTailObfuscate.apply("C"))
                .append(letterTailObfuscate.apply("T"))
                .append(letterTailObfuscate.apply("I"))
                .append(letterTailObfuscate.apply("N"))
                .append(letterTailObfuscate.apply("G"));

        messages.add(injectingComponent);
        messages.add(new StringTextComponent("I N J E C T I N G...").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
        messages.add(new StringTextComponent("I N J E C T I N G...").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
        messages.add(new StringTextComponent("I N J E C T I N G...").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
        messages.add(new StringTextComponent("I N J E C T I N G...").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
        messages.add(new StringTextComponent("I N J E C T I N G...").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
        messages.add(new StringTextComponent("I N J E C T I N G...").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
        messages.add(new StringTextComponent("I N J E C T I N G...").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
        messages.add(new StringTextComponent("I N J E C T I N G...").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
        messages.add(new StringTextComponent("I N J E C T I N G...").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
        messages.add(new StringTextComponent("I N J E C T I N G...").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));
        messages.add(new StringTextComponent("I N J E C T I N G...").withStyle(TextFormatting.DARK_RED, TextFormatting.ITALIC));

        String[] letters = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R",
        "S", "T", "U", "V", "W", "X", "Y", "Z"};
        Supplier<String> randomLetter = () -> letters[ThreadLocalRandom.current().nextInt(0, letters.length)];

        Supplier<IFormattableTextComponent> randomLetters = () -> new StringTextComponent("")
                .append(letterTailObfuscate.apply(randomLetter.get()))
                .append(letterTailObfuscate.apply(randomLetter.get()))
                .append(letterTailObfuscate.apply(randomLetter.get()))
                .append(letterTailObfuscate.apply(randomLetter.get()))
                .append(letterTailObfuscate.apply(randomLetter.get()))
                .append(letterTailObfuscate.apply(randomLetter.get()))
                .append(letterTailObfuscate.apply(randomLetter.get()))
                .append(letterTailObfuscate.apply(randomLetter.get()));

        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);
        messages.add(randomLetters.get());
        messages.add(injectingComponent);

        messages.add(new StringTextComponent("asdahsdaasdahsda").withStyle(TextFormatting.DARK_RED, TextFormatting.OBFUSCATED));

        if (timer != null) timer.cancel();
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                IFormattableTextComponent component = messages.isEmpty() ? null : messages.poll();
                if (component == null) {
                    timer.cancel();
                    ScaryBlockMod.getInstance().getStateManager().teardown(TimeTillInjectionBossBarState.class);
                } else getPlayers().forEach(p -> p.sendMessage(component, p.getUUID()));
            }
        }, 0, 200);
    }

    private Collection<ServerPlayerEntity> getPlayers() {
        MinecraftServer source = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        return source.getPlayerList().getPlayers();
    }

    private void updateBossBar() {
        bossInfo.setPercent(timeTillInjectionTicks / (float) tickDuration);
        getPlayers().forEach(this::sendBossBar);
    }

    private void sendBossBar(ServerPlayerEntity p) {
        if (bossInfo.getPlayers().stream().anyMatch(pl -> pl.getUUID().equals(p.getUUID()))) {
            p.connection.send(new SUpdateBossInfoPacket(SUpdateBossInfoPacket.Operation.ADD, bossInfo));
        } else bossInfo.addPlayer(p);
    }

    @Override
    public void clearBossBars() {
        getPlayers().forEach(bossInfo::removePlayer);
    }

}

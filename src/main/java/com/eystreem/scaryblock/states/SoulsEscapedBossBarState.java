package com.eystreem.scaryblock.states;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.block.ScaryBlock;
import com.eystreem.scaryblock.entities.thatthing.ThatThingEntity;
import com.eystreem.scaryblock.util.Config;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.play.server.SUpdateBossInfoPacket;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.BossInfo;
import net.minecraft.world.server.ServerBossInfo;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.LogicalSide;
import net.minecraftforge.fml.LogicalSidedProvider;

import java.util.Collection;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Shows the boss bar for souls escaped
 * <p>
 * Setup after first block break
 * Stays alive unless reset by restart command
 * Can set souls escaped with command manually
 * <p>
 * If {@link ScaryBlock} breaks, add a soul escaped
 * Custom items have unique soul escaped values
 * Once filled, display messages in chat from herobrine
 * Also then begins {@link TimeTillInjectionBossBarState}
 */
public class SoulsEscapedBossBarState extends State implements BossBarState {

    private int soulsEscaped = 0;
    public static final int MAX_SOULS = 666;
    private final Supplier<IFormattableTextComponent> title = () -> new StringTextComponent("")
            .append(new StringTextComponent("Souls Escaped: " + soulsEscaped + "/"))
            .append(new StringTextComponent(String.valueOf(MAX_SOULS)).withStyle(TextFormatting.RED));

    private final ServerBossInfo bossInfo = new ServerBossInfo(title.get(), BossInfo.Color.PURPLE, BossInfo.Overlay.PROGRESS);

    @Override
    public void onSetup() {
        updateBossBar();
    }

    @Override
    public void onTeardown() {
        soulsEscaped = 0;
    }

    @SubscribeEvent
    public void onKill(LivingDamageEvent e) {
        if (!(e.getSource().getEntity() instanceof PlayerEntity)) return;
        if (!(e.getEntity() instanceof ThatThingEntity)) return;
        ThatThingEntity thatThingEntity = (ThatThingEntity) e.getEntity();
        if (e.getAmount() < thatThingEntity.getHealth()) return;
        setSoulsEscaped(Math.min(MAX_SOULS, Math.max(0, soulsEscaped + Config.SPAWNER_BOSS_BAR_ADD_AMOUNT.get())));
    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBreak(BlockEvent.BreakEvent e) {
        if (!(e.getState().getBlock() instanceof ScaryBlock)) return;
        if (soulsEscaped >= MAX_SOULS) return;
        setSoulsEscaped(soulsEscaped + 1);
    }

    public int getSoulsEscaped() {
        return soulsEscaped;
    }

    /**
     * Set the souls escaped
     * @param soulsEscaped The souls escaped amount
     * @throws IndexOutOfBoundsException If setting souls less than 0 or greater than 666
     */
    public void setSoulsEscaped(int soulsEscaped) {
        if (soulsEscaped < 0 || soulsEscaped > MAX_SOULS) throw new IndexOutOfBoundsException("Unable to set souls escaped to " + soulsEscaped);
        this.soulsEscaped = soulsEscaped;
        if (isSetup()) updateBossBar();
        if (isSetup() && this.soulsEscaped >= MAX_SOULS) {
            sendMessages();
            ScaryBlockMod.getInstance().getStateManager().teardown(SoulsEscapedBossBarState.class);
            ScaryBlockMod.getInstance().getStateManager().setup(TimeTillInjectionBossBarState.class);
        }
    }

    private void sendMessages() {
        Supplier<Integer> random = () -> ThreadLocalRandom.current().nextInt(3, 5); //3-4 inclusive
        Supplier<IFormattableTextComponent> obfuscate = () -> new StringTextComponent(random.get() == 3 ? "abc" : "abcd")
                    .withStyle(TextFormatting.WHITE, TextFormatting.OBFUSCATED);
        Consumer<IFormattableTextComponent> send = text -> {
            getPlayers().forEach(p -> {
                p.sendMessage(
                        new StringTextComponent("")
                                .append(new StringTextComponent("<Herobrine> ").withStyle(TextFormatting.DARK_RED))
                                .append(obfuscate.get())
                                .append(text)
                                .append(obfuscate.get()),
                        p.getUUID());
            });
        };
        send.accept(new StringTextComponent("I AM WAITING FOR U").withStyle(TextFormatting.WHITE));
        send.accept(
                new StringTextComponent("")
                        .append(new StringTextComponent("YOUR ").withStyle(TextFormatting.WHITE))
                        .append(new StringTextComponent("SOUL ").withStyle(TextFormatting.LIGHT_PURPLE))
                        .append(new StringTextComponent("WILL BE MINE").withStyle(TextFormatting.WHITE)));
    }

    private Collection<ServerPlayerEntity> getPlayers() {
        MinecraftServer source = LogicalSidedProvider.INSTANCE.get(LogicalSide.SERVER);
        return source.getPlayerList().getPlayers();
    }

    private void updateBossBar() {
        bossInfo.setName(title.get());
        bossInfo.setPercent(soulsEscaped / (float) MAX_SOULS);
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

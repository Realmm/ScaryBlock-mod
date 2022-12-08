package com.eystreem.scaryblock;

import com.eystreem.scaryblock.block.ScaryBlock;
import com.eystreem.scaryblock.block.ScaryBlockBlocks;
import com.eystreem.scaryblock.block.entities.ScaryBlockBlockEntities;
import com.eystreem.scaryblock.block.entities.spawner.EntitySpawnerBlockRenderer;
import com.eystreem.scaryblock.commands.InjectionCommand;
import com.eystreem.scaryblock.commands.SetCutCommand;
import com.eystreem.scaryblock.commands.SetSoulsEscapedCommand;
import com.eystreem.scaryblock.effects.ScaryBlockEffects;
import com.eystreem.scaryblock.entities.ScaryBlockEntityTypes;
import com.eystreem.scaryblock.entities.bloodgolem.BloodGolemRenderer;
import com.eystreem.scaryblock.entities.herobrine.HerobrineRenderer;
import com.eystreem.scaryblock.entities.thatthing.ThatThingRenderer;
import com.eystreem.scaryblock.item.ScaryBlockItems;
import com.eystreem.scaryblock.item.items.armor.HuggyWuggyFeetItem;
import com.eystreem.scaryblock.item.items.armor.HuggyWuggyFeetRenderer;
import com.eystreem.scaryblock.item.items.eye.Entity303EyeItem;
import com.eystreem.scaryblock.item.items.eye.Entity303EyeRenderer;
import com.eystreem.scaryblock.keybind.Keybind;
import com.eystreem.scaryblock.network.NetworkMessage;
import com.eystreem.scaryblock.sound.ScaryBlockSounds;
import com.eystreem.scaryblock.states.BossBarState;
import com.eystreem.scaryblock.states.FirstBlockBreakState;
import com.eystreem.scaryblock.states.SoulsEscapedBossBarState;
import com.eystreem.scaryblock.states.StateManager;
import com.eystreem.scaryblock.util.Config;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.server.command.ConfigCommand;
import software.bernie.geckolib3.renderers.geo.GeoArmorRenderer;

@Mod(ScaryBlockMod.MOD_ID)
public class ScaryBlockMod {
    public static final String MOD_ID = "scaryblock";

    private static ScaryBlockMod mod;
    private final StateManager stateManager;
    private int cut = 1;

    public ScaryBlockMod() {
        mod = this;

        stateManager = new StateManager();

        IEventBus eventBus = FMLJavaModLoadingContext.get().getModEventBus();
        ScaryBlockItems.register(eventBus);
        ScaryBlockBlocks.register(eventBus);
        ScaryBlockSounds.register(eventBus);
        ScaryBlockBlockEntities.register(eventBus);
        ScaryBlockEntityTypes.register(eventBus);
        ScaryBlockEffects.register(eventBus);

        MinecraftForge.EVENT_BUS.register(ScaryBlock.class);
        MinecraftForge.EVENT_BUS.register(this);

        eventBus.addListener(this::onSetup);
        eventBus.addListener(this::onClientSetup);

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "scaryblock-config.toml");
    }

    public void setCut(int cut, boolean resetState) {
        this.cut = Math.max(1, Math.min(cut, 2));
        if (!resetState) return;
        stateManager.getCurrentStates().forEach(s -> {
            if (s instanceof BossBarState) {
                BossBarState state = (BossBarState) s;
                state.clearBossBars();
            }
        });
        stateManager.reset();
        switch (cut) {
            case 1:
                stateManager.setup(FirstBlockBreakState.class);
                break;
            case 2:
                stateManager.setup(SoulsEscapedBossBarState.class);
                break;
        }
    }

    public int getCut() {
        return cut;
    }

    /**
     * Get the {@link ScaryBlockMod} instance
     *
     * @return The {@link ScaryBlockMod} instance
     */
    public static ScaryBlockMod getInstance() {
        return mod;
    }

    /**
     * Get the {@link StateManager}
     *
     * @return The {@link StateManager}
     */
    public StateManager getStateManager() {
        return stateManager;
    }

    @SubscribeEvent
    public void onSetup(FMLCommonSetupEvent e) {
        stateManager.setup(FirstBlockBreakState.class);
        NetworkMessage.register();
    }

    @SubscribeEvent
    public void onClientSetup(FMLClientSetupEvent e) {
        ClientRegistry.registerKeyBinding(Keybind.RED_SUN_KEY);
        ClientRegistry.registerKeyBinding(Keybind.DOUBLE_JUMP_KEY);
        ClientRegistry.registerKeyBinding(Keybind.ENTITY_303_SHOOT);
        GeoArmorRenderer.registerArmorRenderer(HuggyWuggyFeetItem.class, HuggyWuggyFeetRenderer::new);
        GeoArmorRenderer.registerArmorRenderer(Entity303EyeItem.class, Entity303EyeRenderer::new);
        ClientRegistry.bindTileEntityRenderer(ScaryBlockBlockEntities.ENTITY_SPAWNER_BLOCK_ENTITY.get(), EntitySpawnerBlockRenderer::new);

        RenderingRegistry.registerEntityRenderingHandler(ScaryBlockEntityTypes.HEROBRINE_ENTITY.get(), HerobrineRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ScaryBlockEntityTypes.BLOOD_GOLEM.get(), BloodGolemRenderer::new);
        RenderingRegistry.registerEntityRenderingHandler(ScaryBlockEntityTypes.THAT_THING.get(), ThatThingRenderer::new);
    }

    @SubscribeEvent
    public void onCommandRegister(RegisterCommandsEvent e) {
        new SetSoulsEscapedCommand(e.getDispatcher());
        new InjectionCommand(e.getDispatcher());
        new SetCutCommand(e.getDispatcher());

        ConfigCommand.register(e.getDispatcher());
    }

}

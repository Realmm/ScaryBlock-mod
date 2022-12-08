package com.eystreem.scaryblock.states;

import com.eystreem.scaryblock.ScaryBlockMod;
import com.eystreem.scaryblock.block.ScaryBlock;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * The second block break state listens for the second break of the {@link ScaryBlock}, and performs actions
 * <p>
 * Setup after first block break
 * Tears down after a set of events
 * <p>
 * Wait for {@link ScaryBlock} to be broken for the second time
 * Setup the {@link JumpScareState}
 */
public class SecondBlockBreakState extends State {

    @Override
    protected void onSetup() {

    }

    @Override
    protected void onTeardown() {

    }

    @SubscribeEvent(priority = EventPriority.HIGH)
    public void onBlockBreak(BlockEvent.BreakEvent e) {
        if (!(e.getState().getBlock() instanceof ScaryBlock)) return;
        if (e.getWorld().isClientSide()) return;
        ScaryBlockMod.getInstance().getStateManager().teardown(SecondBlockBreakState.class);
        ScaryBlockMod.getInstance().getStateManager().setup(JumpScareState.class);
    }

}

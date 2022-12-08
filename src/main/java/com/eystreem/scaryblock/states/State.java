package com.eystreem.scaryblock.states;

import com.eystreem.scaryblock.ScaryBlockMod;
import net.minecraftforge.common.MinecraftForge;

public abstract class State {

    private boolean setup;

    /**
     * Triggered on setup of a state
     */
    protected abstract void onSetup();

    /**
     * Triggered on teardown a state
     */
    protected abstract void onTeardown();

    /**
     * Set up a state
     */
    void setup() {
        if (setup) return;
        setup = true;
        onSetup();
        MinecraftForge.EVENT_BUS.register(this);
    }

    /**
     * Teardown a state
     */
    void teardown() {
        if (!setup) return;
        setup = false;
        onTeardown();
        MinecraftForge.EVENT_BUS.unregister(this);
    }

    /**
     * Check if this state is setup
     * @return True if this state is setup, otherwise false
     */
    boolean isSetup() {
        return setup;
    }

}

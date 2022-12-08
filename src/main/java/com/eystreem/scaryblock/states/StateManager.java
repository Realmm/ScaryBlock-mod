package com.eystreem.scaryblock.states;

import java.util.*;

/**
 * A state manager manages the states for the gameplay
 */
public class StateManager {

    private final List<State> states = new LinkedList<>();
    private final Set<State> activeStates = new HashSet<>();

    public StateManager() {
        addState(new FirstBlockBreakState());
        addState(new SoulsEscapedBossBarState());
        addState(new TimeTillInjectionBossBarState());
        addState(new SecondBlockBreakState());
        addState(new JumpScareState());
    }

    /**
     * Get the current active state, and filter based on the {@link Class} of the active state
     *
     * @return The current active state, if found, otherwise empty
     */
    public <T extends State> Optional<T> getCurrentState(Class<T> clazz) {
        return filterState(activeStates, clazz);
    }

    /**
     * Get the current active states
     *
     * @return The current active states
     */
    public Set<State> getCurrentStates() {
        return new HashSet<>(activeStates);
    }

    /**
     * Check if a {@link State} is setup
     * @param clazz The class of the {@link State}
     * @return True if the {@link State} is setup, otherwise false
     * @param <T> The type of {@link State}
     */
    public <T extends State> boolean isSetup(Class<T> clazz) {
        return getCurrentState(clazz).isPresent();
    }

    /**
     * Reset the states, and ensure no states are active
     */
    public void reset() {
        activeStates.forEach(State::teardown);
        activeStates.clear();
    }

    /**
     * Setup a {@link State}
     * @param clazz The class of the {@link State}
     * @param <T> The type of {@link State}
     * @throws IllegalStateException If the {@link State} has already setup, or if unable to find the {@link State}
     */
    public <T extends State> void setup(Class<T> clazz) throws IllegalStateException {
        if (getCurrentState(clazz).isPresent()) throw new IllegalStateException("Unable to setup state, already setup");
        State state = filterState(states, clazz).orElse(null);
        if (state == null) throw new IllegalStateException("Unable to find state with class " + clazz.getSimpleName());
        state.setup();
        activeStates.add(state);
    }

    /**
     * Teardown a {@link State}
     * @param clazz The class of the {@link State}
     * @param <T> The type of {@link State}
     * @throws IllegalStateException If the {@link State} is not setup, or if unable to find the {@link State}
     */
    public <T extends State> void teardown(Class<T> clazz) throws IllegalStateException {
        if (!getCurrentState(clazz).isPresent()) throw new IllegalStateException("Unable to teardown state, state not started");
        State state = filterState(states, clazz).orElse(null);
        if (state == null) throw new IllegalStateException("Unable to find state with class " + clazz.getSimpleName());
        state.teardown();
        activeStates.remove(state);
    }

    private void addState(State state) {
        if (states.stream().anyMatch(s -> state.getClass().isAssignableFrom(s.getClass())))
            throw new IllegalStateException("Duplicate state added");
        states.add(state);
    }

    private <T extends State> Optional<T> filterState(Collection<State> list, Class<T> clazz) {
        return list.stream()
                .filter(s -> clazz.isAssignableFrom(s.getClass()))
                .map(clazz::cast)
                .findFirst();
    }

}

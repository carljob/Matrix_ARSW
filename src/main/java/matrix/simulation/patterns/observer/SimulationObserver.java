package matrix.simulation.patterns.observer;

@FunctionalInterface
public interface SimulationObserver {
    void onEvent(SimulationEvent event);
}

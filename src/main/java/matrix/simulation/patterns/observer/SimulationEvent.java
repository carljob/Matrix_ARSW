package matrix.simulation.patterns.observer;

public class SimulationEvent {

    public enum Type {
        NEO_MOVED,
        AGENT_MOVED,
        NEO_ESCAPED,
        NEO_CAUGHT
    }

    private final Type type;
    private final int row;
    private final int col;

    public SimulationEvent(Type type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    @Override
    public String toString() {
        return "SimulationEvent{type=" + type + ", row=" + row + ", col=" + col + "}";
    }
}
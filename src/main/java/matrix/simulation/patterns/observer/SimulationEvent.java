package matrix.simulation.patterns.observer;

public class SimulationEvent {

    public enum Type {
        NEO_MOVED,
        NEO_ESCAPED,
        NEO_CAUGHT,
        SIMULATION_ENDED
    }

    private final Type type;
    private final int row;
    private final int col;

    public SimulationEvent(Type type, int row, int col) {
        this.type = type;
        this.row = row;
        this.col = col;
    }

    public Type getType() { return type; }
    public int getRow()   { return row; }
    public int getCol()   { return col; }

    @Override
    public String toString() {
        return "SimulationEvent{type=" + type + ", row=" + row + ", col=" + col + "}";
    }
}
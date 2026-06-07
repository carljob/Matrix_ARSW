package matrix.simulation.entities;

import matrix.simulation.GameState;
import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;
import matrix.simulation.patterns.observer.SimulationEvent;
import matrix.simulation.patterns.observer.SimulationObserver;
import matrix.simulation.patterns.strategy.AgentSmartMovement;
import matrix.simulation.patterns.strategy.MovementStrategy;

import java.util.ArrayList;
import java.util.List;

public class Agent extends Thread {

    private int row;
    private int col;
    private final Matrix matrix;
    private final Neo neo;
    private final int speed;

    public volatile boolean active = true;

    private MovementStrategy movementStrategy;
    private final List<SimulationObserver> observers = new ArrayList<>();

    public Agent(int row, int col, Matrix matrix, Neo neo, int speed) {
        this.row = row;
        this.col = col;
        this.matrix = matrix;
        this.neo = neo;
        this.speed = speed;
        this.movementStrategy = new AgentSmartMovement(neo);
    }

    public void setMovementStrategy(MovementStrategy strategy) {
        this.movementStrategy = strategy;
    }

    public void addObserver(SimulationObserver observer) {
        observers.add(observer);
    }

    private void notifyObservers(SimulationEvent event) {
        for (SimulationObserver obs : observers) {
            obs.onEvent(event);
        }
    }

    @Override
    public void run() {
        while (active && neo.alive && !neo.escaped) {
            try {
                Thread.sleep(speed);
                GameState.waitIfPaused();
                move();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    private void move() {
        int[] result = movementStrategy.move(row, col, matrix);

        if (result == null) {
            neo.alive = false;
            matrix.setCell(row, col, Cell.EMPTY);
            System.out.println("Agent caught Neo!");
            notifyObservers(new SimulationEvent(SimulationEvent.Type.NEO_CAUGHT, row, col));
            active = false;
            return;
        }

        if (result[0] != row || result[1] != col) {
            matrix.setCell(row, col, Cell.EMPTY);
            row = result[0];
            col = result[1];
            matrix.setCell(row, col, Cell.AGENT);
        }

        notifyObservers(new SimulationEvent(SimulationEvent.Type.AGENT_MOVED, row, col));
    }
}
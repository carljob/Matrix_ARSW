package matrix.simulation.entities;

import matrix.simulation.GameState;
import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;
import matrix.simulation.patterns.observer.SimulationObserver;
import matrix.simulation.patterns.observer.SimulationEvent;
import matrix.simulation.patterns.strategy.MovementStrategy;
import matrix.simulation.patterns.strategy.NeoSmartMovement;

import java.util.ArrayList;
import java.util.List;

public class Neo extends Thread {

    private int row;
    private int col;
    private final Matrix matrix;
    private final int speed;

    public volatile boolean alive = true;
    public volatile boolean escaped = false;

    private MovementStrategy movementStrategy;

    private final List<SimulationObserver> observers = new ArrayList<>();

    public Neo(int row, int col, Matrix matrix, int speed) {
        this.row = row;
        this.col = col;
        this.matrix = matrix;
        this.speed = speed;
        this.movementStrategy = new NeoSmartMovement();
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
        while (alive && !escaped) {
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
            escaped = true;
            matrix.setCell(row, col, Cell.EMPTY);
            System.out.println("Neo escaped!");
            notifyObservers(new SimulationEvent(SimulationEvent.Type.NEO_ESCAPED, row, col));
            return;
        }

        if (result[0] != row || result[1] != col) {
            matrix.setCell(row, col, Cell.EMPTY);
            row = result[0];
            col = result[1];
            matrix.setCell(row, col, Cell.NEO);
        }
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
}
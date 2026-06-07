package matrix.simulation.patterns.strategy;

import matrix.simulation.model.Matrix;

public interface MovementStrategy {
    int[] move(int row, int col, Matrix matrix);
}
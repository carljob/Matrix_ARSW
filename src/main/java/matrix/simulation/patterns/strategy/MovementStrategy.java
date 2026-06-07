package matrix.simulation.patterns.strategy;

import matrix.simulation.entities.Neo;
import matrix.simulation.model.Matrix;

@FunctionalInterface
public interface MovementStrategy {
    int[] move(int row, int col, Matrix matrix, Neo neo);
}

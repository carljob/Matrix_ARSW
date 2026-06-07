package matrix.simulation.patterns.strategy;

import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;

public class NeoSmartMovement implements MovementStrategy {

    private static final int[][] DIRS = {
            {-1,0},{1,0},{0,-1},{0,1},
            {-1,-1},{-1,1},{1,-1},{1,1}
    };

    @Override
    public int[] move(int row, int col, Matrix matrix) {
        int bestRow = row, bestCol = col;
        double bestDist = Double.MAX_VALUE;

        for (int[] dir : DIRS) {
            int nr = row + dir[0];
            int nc = col + dir[1];
            if (!isValid(nr, nc, matrix)) continue;

            Cell cell = matrix.getCell(nr, nc);
            if (cell == Cell.TELEPHONE) {
                return null; // llegó al teléfono
            }
            if (cell == Cell.EMPTY) {
                double dist = distToNearestPhone(nr, nc, matrix);
                if (dist < bestDist) {
                    bestDist = dist;
                    bestRow = nr;
                    bestCol = nc;
                }
            }
        }
        return new int[]{bestRow, bestCol};
    }

    private boolean isValid(int r, int c, Matrix matrix) {
        if (r < 0 || r >= matrix.getRows() || c < 0 || c >= matrix.getCols()) return false;
        Cell cell = matrix.getCell(r, c);
        return cell != Cell.WALL && cell != Cell.AGENT;
    }

    private double distToNearestPhone(int r, int c, Matrix matrix) {
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                if (matrix.getCell(i, j) == Cell.TELEPHONE) {
                    double dist = Math.sqrt(Math.pow(r - i, 2) + Math.pow(c - j, 2));
                    if (dist < minDist) minDist = dist;
                }
            }
        }
        return minDist;
    }
}
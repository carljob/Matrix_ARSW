package matrix.simulation.patterns.strategy;

import matrix.simulation.entities.Neo;
import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;

public class AgentSmartMovement implements MovementStrategy {

    private static final int[][] DIRS = {
            {-1,0},{1,0},{0,-1},{0,1},
            {-1,-1},{-1,1},{1,-1},{1,1}
    };

    private final Neo neo;

    public AgentSmartMovement(Neo neo) {
        this.neo = neo;
    }

    @Override
    public int[] move(int row, int col, Matrix matrix) {
         int neoRow = neo.getRow();
         int neoCol = neo.getCol();

          double currentDist = Math.sqrt(Math.pow(row - neoRow, 2) + Math.pow(col - neoCol, 2));
         int bestRow = -1, bestCol = -1;
         double bestDist = currentDist;

        for (int[] dir : DIRS) {
            int nr = row + dir[0];
            int nc = col + dir[1];
            if (!isValid(nr, nc, matrix)) continue;

             Cell cell = matrix.getCell(nr, nc);
             if (cell == Cell.NEO) {
                 return null;
             }
            if (cell == Cell.EMPTY) {
                double dist = Math.sqrt(
                        Math.pow(nr - neoRow, 2) +
                                Math.pow(nc - neoCol, 2)
                );
                if (dist < bestDist) {
                    bestDist = dist;
                    bestRow = nr;
                    bestCol = nc;
                }
            }
        }

        if (bestRow == -1) return new int[]{row, col};
        return new int[]{bestRow, bestCol};
    }

    private boolean isValid(int r, int c, Matrix matrix) {
        if (r < 0 || r >= matrix.getRows() || c < 0 || c >= matrix.getCols()) return false;
        Cell cell = matrix.getCell(r, c);
        return cell != Cell.WALL && cell != Cell.TELEPHONE && cell != Cell.AGENT;
    }
}
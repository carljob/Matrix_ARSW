package matrix.simulation.patterns.strategy;

import matrix.simulation.entities.Neo;
import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RandomMovement implements MovementStrategy {

    private static final int[][] DIRS = {
            {-1,0},{1,0},{0,-1},{0,1},
            {-1,-1},{-1,1},{1,-1},{1,1}
    };
    private final Random random = new Random();

    @Override
    public int[] move(int row, int col, Matrix matrix, Neo neo) {
        List<int[]> available = new ArrayList<>();

        for (int[] dir : DIRS) {
            int nr = row + dir[0];
            int nc = col + dir[1];
            if (isValid(nr, nc, matrix, neo)) {
                Cell cell = matrix.getCell(nr, nc);
                if (neo == null && cell == Cell.TELEPHONE) return null;
                if (neo != null && cell == Cell.NEO)       return null;
                if (cell == Cell.EMPTY) {
                    available.add(new int[]{nr, nc});
                }
            }
        }

        if (available.isEmpty()) return new int[]{row, col};
        return available.get(random.nextInt(available.size()));
    }

    private boolean isValid(int r, int c, Matrix matrix, Neo neo) {
        if (r < 0 || r >= matrix.getRows() || c < 0 || c >= matrix.getCols()) return false;
        Cell cell = matrix.getCell(r, c);
        if (cell == Cell.WALL || cell == Cell.AGENT) return false;
        if (neo != null && cell == Cell.TELEPHONE)   return false;
        return true;
    }
}

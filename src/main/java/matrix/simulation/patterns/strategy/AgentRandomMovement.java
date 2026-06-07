package matrix.simulation.patterns.strategy;

import matrix.simulation.entities.Neo;
import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AgentRandomMovement implements MovementStrategy {

    private static final int[][] DIRS = {
            {-1,0},{1,0},{0,-1},{0,1},
            {-1,-1},{-1,1},{1,-1},{1,1}
    };
    private final Random random = new Random();
    private final Neo neo;

    public AgentRandomMovement(Neo neo) {
        this.neo = neo;
    }

    @Override
    public int[] move(int row, int col, Matrix matrix) {
        List<int[]> available = new ArrayList<>();

         for (int[] dir : DIRS) {
             int nr = row + dir[0];
             int nc = col + dir[1];
             if (!isValid(nr, nc, matrix)) continue;

             Cell cell = matrix.getCell(nr, nc);
             if (cell == Cell.NEO)   return null;
             if (cell == Cell.EMPTY) available.add(new int[]{nr, nc});
        }

        if (available.isEmpty()) return new int[]{row, col};
        return available.get(random.nextInt(available.size()));
    }

    private boolean isValid(int r, int c, Matrix matrix) {
        if (r < 0 || r >= matrix.getRows() || c < 0 || c >= matrix.getCols()) return false;
        Cell cell = matrix.getCell(r, c);
        return cell != Cell.WALL && cell != Cell.TELEPHONE && cell != Cell.AGENT;
    }
}
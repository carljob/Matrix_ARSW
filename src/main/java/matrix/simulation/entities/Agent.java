package matrix.simulation.entities;

import matrix.simulation.GameState;
import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;

public class Agent extends Thread {

    private int row;
    private int col;
    private Matrix matrix;
    private Neo neo;
    public boolean active = true;
    private int speed;

    public Agent(int row, int col, Matrix matrix, Neo neo, int speed) {
        this.row = row;
        this.col = col;
        this.matrix = matrix;
        this.neo = neo;
        this.speed = speed;
    }

    @Override
    public void run() {
        while (active && neo.alive && !neo.escaped) {
            try {
                Thread.sleep(speed);
                while (GameState.isPaused()) {
                    Thread.sleep(50);
                }
                move();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void move() {
        int[][] dirs = {
                {-1,0},{1,0},{0,-1},{0,1},
                {-1,-1},{-1,1},{1,-1},{1,1}
        };

        int bestRow = -1;
        int bestCol = -1;
        double bestDist = Double.MAX_VALUE;

        for (int[] dir : dirs) {
            int nr = row + dir[0];
            int nc = col + dir[1];

            if (isValid(nr, nc)) {
                if (matrix.getCell(nr, nc) == Cell.NEO) {
                    neo.alive = false;
                    matrix.setCell(row, col, Cell.EMPTY);
                    System.out.println("Agent caught Neo!");
                    active = false;
                    return;
                }
                if (matrix.getCell(nr, nc) == Cell.EMPTY) {
                    double dist = Math.sqrt(
                            Math.pow(nr - neo.getRow(), 2) +
                                    Math.pow(nc - neo.getCol(), 2)
                    );
                    if (dist < bestDist) {
                        bestDist = dist;
                        bestRow = nr;
                        bestCol = nc;
                    }
                }
            }
        }

        if (bestRow != -1) {
            matrix.setCell(row, col, Cell.EMPTY);
            row = bestRow;
            col = bestCol;
            matrix.setCell(row, col, Cell.AGENT);
        }
    }

    private boolean isValid(int r, int c) {
        return r >= 0 && r < matrix.getRows()
                && c >= 0 && c < matrix.getCols()
                && matrix.getCell(r, c) != Cell.WALL
                && matrix.getCell(r, c) != Cell.TELEPHONE
                && matrix.getCell(r, c) != Cell.AGENT;
    }
}
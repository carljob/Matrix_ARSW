package matrix.simulation.entities;

import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;

public class Agent extends Thread {

    private int row;
    private int col;
    private Matrix matrix;
    private Neo neo;
    public boolean active = true;

    public Agent(int row, int col, Matrix matrix, Neo neo) {
        this.row = row;
        this.col = col;
        this.matrix = matrix;
        this.neo = neo;
    }

    @Override
    public void run() {
        while (active && neo.alive && !neo.escaped) {
            try {
                Thread.sleep(600);
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
                    matrix.setCell(row, col, Cell.EMPTY);
                    row = nr;
                    col = nc;
                    matrix.setCell(row, col, Cell.AGENT);
                    return;
                }
            }
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
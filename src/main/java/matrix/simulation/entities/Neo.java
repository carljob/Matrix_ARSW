package matrix.simulation.entities;

import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;

public class Neo extends Thread {

    private int row;
    private int col;
    private Matrix matrix;
    public boolean alive = true;
    public boolean escaped = false;

    public Neo(int row, int col, Matrix matrix) {
        this.row = row;
        this.col = col;
        this.matrix = matrix;
    }

    @Override
    public void run() {
        while (alive && !escaped) {
            try {
                Thread.sleep(500);
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
                if (matrix.getCell(nr, nc) == Cell.TELEPHONE) {
                    escaped = true;
                    matrix.setCell(row, col, Cell.EMPTY);
                    System.out.println("Neo escaped!");
                    return;
                }
                if (matrix.getCell(nr, nc) == Cell.EMPTY) {
                    matrix.setCell(row, col, Cell.EMPTY);
                    row = nr;
                    col = nc;
                    matrix.setCell(row, col, Cell.NEO);
                    return;
                }
            }
        }
    }

    private boolean isValid(int r, int c) {
        return r >= 0 && r < matrix.getRows()
                && c >= 0 && c < matrix.getCols()
                && matrix.getCell(r, c) != Cell.WALL
                && matrix.getCell(r, c) != Cell.AGENT;
    }

    public int getRow() { return row; }
    public int getCol() { return col; }
}
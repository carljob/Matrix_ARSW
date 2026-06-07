package matrix.simulation.entities;

import matrix.simulation.GameState;
import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;

public class Neo extends Thread {

    private int row;
    private int col;
    private Matrix matrix;
    public boolean alive = true;
    public boolean escaped = false;
    private int speed;

    public Neo(int row, int col, Matrix matrix, int speed) {
        this.row = row;
        this.col = col;
        this.matrix = matrix;
        this.speed = speed;
    }

    @Override
    public void run() {
        while (alive && !escaped) {
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
                if (matrix.getCell(nr, nc) == Cell.TELEPHONE) {
                    escaped = true;
                    matrix.setCell(row, col, Cell.EMPTY);
                    System.out.println("Neo escaped!");
                    return;
                }
                if (matrix.getCell(nr, nc) == Cell.EMPTY) {
                    double dist = distToNearestPhone(nr, nc);
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
            matrix.setCell(row, col, Cell.NEO);
        }
    }

    private double distToNearestPhone(int r, int c) {
        double minDist = Double.MAX_VALUE;
        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                if (matrix.getCell(i, j) == Cell.TELEPHONE) {
                    double dist = Math.sqrt(
                            Math.pow(r - i, 2) + Math.pow(c - j, 2)
                    );
                    if (dist < minDist) minDist = dist;
                }
            }
        }
        return minDist;
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
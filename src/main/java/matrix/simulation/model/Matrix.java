package matrix.simulation.model;

public class Matrix {
    private final int rows;
    private final int cols;
    private final Cell[][] grid;

    public Matrix(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new Cell[rows][cols];
        initializeGrid();
    }

    private void initializeGrid() {
        for (int i = 0; i < rows; i++)
            for (int j = 0; j < cols; j++)
                grid[i][j] = Cell.EMPTY;
    }

    public synchronized Cell getCell(int row, int col) {
        return grid[row][col];
    }

    public synchronized void setCell(int row, int col, Cell value) {
        grid[row][col] = value;
    }

    public int getRows() { return rows; }
    public int getCols() { return cols; }

    public synchronized void print() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                switch (grid[i][j]) {
                    case NEO       -> System.out.print(" N ");
                    case AGENT     -> System.out.print(" A ");
                    case TELEPHONE -> System.out.print(" T ");
                    case WALL      -> System.out.print(" # ");
                    case EMPTY     -> System.out.print(" . ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }
}

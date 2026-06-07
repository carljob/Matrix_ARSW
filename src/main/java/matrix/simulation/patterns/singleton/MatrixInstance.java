package matrix.simulation.patterns.singleton;

import matrix.simulation.model.Matrix;

public class MatrixInstance {

    private static volatile MatrixInstance instance = null;

    private Matrix matrix;

    private MatrixInstance(int rows, int cols) {
        this.matrix = new Matrix(rows, cols);
    }

    public static MatrixInstance getInstance(int rows, int cols) {
        if (instance == null) {
            synchronized (MatrixInstance.class) {
                if (instance == null) {
                    instance = new MatrixInstance(rows, cols);
                }
            }
        }
        return instance;
    }

    public static synchronized void reset() {
        instance = null;
    }

    public Matrix getMatrix() { return matrix; }
}

package matrix.simulation.ui;

import matrix.simulation.model.Matrix;

import javax.swing.*;

public class SimulationFrame extends JFrame {

    private MatrixPanel matrixPanel;

    public SimulationFrame(Matrix matrix) {
        setTitle("Matrix Neo Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        matrixPanel = new MatrixPanel(matrix);
        add(matrixPanel);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void update(Matrix matrix) {
        matrixPanel.updateMatrix(matrix);
    }
}
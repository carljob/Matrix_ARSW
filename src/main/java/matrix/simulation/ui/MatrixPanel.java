package matrix.simulation.ui;

import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;

import javax.swing.*;
import java.awt.*;

public class MatrixPanel extends JPanel {

    private Matrix matrix;
    private static final int CELL_SIZE = 60;

    public MatrixPanel(Matrix matrix) {
        this.matrix = matrix;
        setPreferredSize(new Dimension(
                matrix.getCols() * CELL_SIZE,
                matrix.getRows() * CELL_SIZE
        ));
    }

    public void updateMatrix(Matrix matrix) {
        this.matrix = matrix;
        repaint(); // redibuja la pantalla
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                Cell cell = matrix.getCell(i, j);

                // Color según tipo de celda
                switch (cell) {
                    case NEO       -> g.setColor(Color.BLUE);
                    case AGENT     -> g.setColor(Color.RED);
                    case TELEPHONE -> g.setColor(Color.GREEN);
                    case WALL      -> g.setColor(Color.BLACK);
                    case EMPTY     -> g.setColor(Color.WHITE);
                }

                // Dibujar celda
                g.fillRect(
                        j * CELL_SIZE,
                        i * CELL_SIZE,
                        CELL_SIZE,
                        CELL_SIZE
                );

                // Borde de la celda
                g.setColor(Color.GRAY);
                g.drawRect(
                        j * CELL_SIZE,
                        i * CELL_SIZE,
                        CELL_SIZE,
                        CELL_SIZE
                );
            }
        }
    }
}
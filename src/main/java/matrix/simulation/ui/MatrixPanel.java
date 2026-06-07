package matrix.simulation.ui;

import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;

import javax.swing.*;
import java.awt.*;

public class MatrixPanel extends JPanel {

    private Matrix matrix;
    private int cellSize  = 60;

    public MatrixPanel(Matrix matrix) {
        this.matrix = matrix;
        setBackground(Color.BLACK);
        cellSize  = 500 / Math.max(matrix.getCols(), matrix.getRows());
        setPreferredSize(new Dimension(
                cellSize  * matrix.getCols(),
                cellSize  * matrix.getRows()
        ));
    }

    public void updateMatrix(Matrix matrix) {
        this.matrix = matrix;
        SwingUtilities.invokeLater(this::repaint);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (matrix == null) return;

        Graphics2D g2 = (Graphics2D) g.create();
        g2.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON
        );

        for (int i = 0; i < matrix.getRows(); i++) {
            for (int j = 0; j < matrix.getCols(); j++) {
                Cell cell = matrix.getCell(i, j);
                int x = j * cellSize ;
                int y = i * cellSize ;

                switch (cell) {
                    case NEO       -> g2.setColor(new Color(0, 100, 255));
                    case AGENT     -> g2.setColor(new Color(220, 0, 0));
                    case TELEPHONE -> g2.setColor(new Color(0, 200, 0));
                    case WALL -> g2.setColor(new Color(180, 80, 0));
                    case EMPTY     -> g2.setColor(new Color(15, 15, 15));
                }
                g2.fillRect(x + 2, y + 2, cellSize  - 4, cellSize  - 4);

                g2.setColor(new Color(0, 50, 0));
                g2.drawRect(x, y, cellSize , cellSize );
            }
        }
        g2.dispose();
    }
}
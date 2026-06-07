package matrix.simulation.ui;

import matrix.simulation.GameState;
import matrix.simulation.model.Matrix;

import javax.swing.*;
import java.awt.*;

public class SimulationFrame extends JFrame {

    private MatrixPanel matrixPanel;
    private JButton pauseButton;
    private JLabel statusLabel;
    private boolean paused = false;

    public SimulationFrame(Matrix matrix) {
        setTitle("Matrix Neo Simulation");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        getContentPane().setBackground(Color.BLACK);

        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.BLACK);
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        statusLabel = new JLabel("Running...");
        statusLabel.setForeground(new Color(100, 200, 255));
        statusLabel.setFont(new Font("Monospaced", Font.BOLD, 12));

        pauseButton = new JButton("⏸ PAUSE");
        pauseButton.setFont(new Font("Monospaced", Font.BOLD, 12));
        pauseButton.setBackground(new Color(0, 100, 255));
        pauseButton.setForeground(Color.WHITE);
        pauseButton.setBorderPainted(false);
        pauseButton.setCursor(new Cursor(Cursor.HAND_CURSOR));

        pauseButton.addActionListener(e -> togglePause());

        topPanel.add(statusLabel, BorderLayout.WEST);
        topPanel.add(pauseButton, BorderLayout.EAST);

        matrixPanel = new MatrixPanel(matrix);

        add(topPanel, BorderLayout.NORTH);
        add(matrixPanel, BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void repaintPanel() {
        SwingUtilities.invokeLater(matrixPanel::repaint);
    }

    private void togglePause() {
        paused = !paused;
        if (paused) {
            GameState.pause();
            pauseButton.setText("▶ RESUME");
            statusLabel.setText("Paused");
            statusLabel.setForeground(new Color(255, 200, 0));

            int opt = JOptionPane.showOptionDialog(
                    this,
                    "Game Paused\n\nWhat do you want to do?",
                    "Paused",
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    new String[]{"Resume", "Quit to Menu", "Exit"},
                    "Resume"
            );

            if (opt == 0) {
                paused = false;
                GameState.resume();
                pauseButton.setText("⏸ PAUSE");
                statusLabel.setText("Running...");
                statusLabel.setForeground(new Color(100, 200, 255));
            } else if (opt == 1) {
                GameState.resume();
                dispose();
                SwingUtilities.invokeLater(MenuFrame::new);
            } else {
                System.exit(0);
            }
        }
    }

    public void update(Matrix matrix) {
        matrixPanel.updateMatrix(matrix);
    }


    public void setPaused(boolean paused) {
        this.paused = paused;
        SwingUtilities.invokeLater(() -> {
            pauseButton.setText(paused ? "▶ RESUME" : "⏸ PAUSE");
            statusLabel.setText(paused ? "Paused" : "Running...");
        });
    }
}
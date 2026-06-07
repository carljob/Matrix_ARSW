package matrix.simulation.ui;

import matrix.simulation.DifficultyConfig;
import matrix.simulation.Simulation;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class MenuFrame extends JFrame {

    static class MatrixRainPanel extends JPanel {
        private final int cols;
        private final int rows;
        private final int[] drops;
        private final char[][] chars;
        private final Random rand = new Random();
        private static final int FONT_SIZE = 14;

        public MatrixRainPanel(int width, int height) {
            setPreferredSize(new Dimension(width, height));
            setBackground(Color.BLACK);
            cols = width / FONT_SIZE;
            rows = height / FONT_SIZE + 1;
            drops = new int[cols];
            chars = new char[cols][rows];

            for (int i = 0; i < cols; i++) {
                drops[i] = rand.nextInt(rows);
                for (int j = 0; j < rows; j++) {
                    chars[i][j] = rand.nextBoolean() ? '0' : '1';
                }
            }

            new Timer(60, e -> {
                for (int i = 0; i < cols; i++) {
                    drops[i]++;
                    if (drops[i] >= rows) drops[i] = 0;
                    chars[i][drops[i]] = rand.nextBoolean() ? '0' : '1';
                }
                repaint();
            }).start();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setFont(new Font("Monospaced", Font.BOLD, FONT_SIZE));

            for (int i = 0; i < cols; i++) {
                for (int j = 0; j < rows; j++) {
                    if (j == drops[i]) {
                        g2.setColor(new Color(255, 255, 255));
                    } else if (j > drops[i] - 5 && j < drops[i]) {
                        g2.setColor(new Color(100, 200, 255));
                    } else {
                        g2.setColor(new Color(0, 30, 80));
                    }
                    g2.drawString(
                            String.valueOf(chars[i][j]),
                            i * FONT_SIZE,
                            j * FONT_SIZE
                    );
                }
            }
        }
    }

    public MenuFrame() {
        setTitle("Matrix Neo");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        int WIDTH  = 500;
        int HEIGHT = 500;

        JLayeredPane layered = new JLayeredPane();
        layered.setPreferredSize(new Dimension(WIDTH, HEIGHT));

        MatrixRainPanel rain = new MatrixRainPanel(WIDTH, HEIGHT);
        rain.setBounds(0, 0, WIDTH, HEIGHT);
        layered.add(rain, JLayeredPane.DEFAULT_LAYER);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setOpaque(false);
        panel.setBounds(0, 0, WIDTH, HEIGHT);
        panel.setBorder(BorderFactory.createEmptyBorder(50, 80, 50, 80));

        JLabel title = new JLabel("MATRIX NEO");
        title.setFont(new Font("Monospaced", Font.BOLD, 30));
        title.setForeground(new Color(100, 200, 255));
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("~ Simulation ~");
        subtitle.setFont(new Font("Monospaced", Font.PLAIN, 13));
        subtitle.setForeground(Color.WHITE);
        subtitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel boardLabel = makeLabel("Board Size:");
        String[] sizes = {"Small (12x12)", "Medium (30x30)", "Large (64x64)"};
        JComboBox<String> sizeBox = makeCombo(sizes, 1);

        JLabel diffLabel = makeLabel("Difficulty:");
        String[] diffs = {"Easy", "Medium", "Hard"};
        JComboBox<String> diffBox = makeCombo(diffs, 1);

        JLabel simLabel = makeLabel("Simulations:");
        SpinnerNumberModel spinModel = new SpinnerNumberModel(3, 1, 10, 1);
        JSpinner simSpinner = new JSpinner(spinModel);
        simSpinner.setMaximumSize(new Dimension(200, 30));
        simSpinner.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton playButton = new JButton("▶  PLAY");
        playButton.setFont(new Font("Monospaced", Font.BOLD, 16));
        playButton.setBackground(new Color(0, 100, 255));
        playButton.setForeground(Color.WHITE);
        playButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        playButton.setMaximumSize(new Dimension(200, 40));
        playButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        playButton.setBorderPainted(false);

        playButton.addActionListener(e -> {
            int[] boardSize = switch (sizeBox.getSelectedIndex()) {
                case 0  -> new int[]{12, 12};
                case 2  -> new int[]{64, 64};
                default -> new int[]{30, 30};
            };

            DifficultyConfig.Level level = switch (diffBox.getSelectedIndex()) {
                case 0  -> DifficultyConfig.Level.EASY;
                case 2  -> DifficultyConfig.Level.HARD;
                default -> DifficultyConfig.Level.MEDIUM;
            };

            DifficultyConfig config = DifficultyConfig.of(level, boardSize[0]);
            int numSims = (int) simSpinner.getValue();
            dispose();

            Simulation sim = new Simulation(
                    boardSize[0], boardSize[1],
                    config.numAgents, config.numWalls, config.numTelephones,
                    config.neoSpeed, config.agentSpeed
            );
            new Thread(() -> sim.run(numSims, 1)).start();
        });

        panel.add(title);
        panel.add(Box.createVerticalStrut(5));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(35));
        panel.add(boardLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(sizeBox);
        panel.add(Box.createVerticalStrut(20));
        panel.add(diffLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(diffBox);
        panel.add(Box.createVerticalStrut(20));
        panel.add(simLabel);
        panel.add(Box.createVerticalStrut(5));
        panel.add(simSpinner);
        panel.add(Box.createVerticalStrut(35));
        panel.add(playButton);

        layered.add(panel, JLayeredPane.PALETTE_LAYER);

        add(layered);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private JLabel makeLabel(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        label.setFont(new Font("Monospaced", Font.PLAIN, 13));
        label.setAlignmentX(Component.CENTER_ALIGNMENT);
        return label;
    }

    private JComboBox<String> makeCombo(String[] options, int selected) {
        JComboBox<String> box = new JComboBox<>(options);
        box.setSelectedIndex(selected);
        box.setMaximumSize(new Dimension(200, 30));
        box.setAlignmentX(Component.CENTER_ALIGNMENT);
        return box;
    }
}
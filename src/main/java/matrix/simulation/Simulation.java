package matrix.simulation;

import matrix.simulation.entities.Agent;
import matrix.simulation.entities.Neo;
import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;
import matrix.simulation.patterns.factory.EntityFactory;
import matrix.simulation.ui.MenuFrame;
import matrix.simulation.ui.SimulationFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {

    private final int rows;
    private final int cols;
    private final int numAgents;
    private final int numWalls;
    private final int numTelephones;
    private final Random random;
    private SimulationFrame frame;
    private int neoWins;
    private int neoLosses;
    private final int neoSpeed;
    private final int agentSpeed;

    public Simulation(int rows, int cols, int numAgents,
                      int numWalls, int numTelephones) {
        this(rows, cols, numAgents, numWalls, numTelephones, 400, 450);
    }

    public Simulation(int rows, int cols, int numAgents,
                      int numWalls, int numTelephones,
                      int neoSpeed, int agentSpeed) {
        this.random        = new Random();
        this.neoWins       = 0;
        this.neoLosses     = 0;
        this.rows          = rows;
        this.cols          = cols;
        this.numAgents     = numAgents;
        this.numWalls      = numWalls;
        this.numTelephones = numTelephones;
        this.neoSpeed      = neoSpeed;
        this.agentSpeed    = agentSpeed;
    }

    public void run(int totalSimulations, int currentSimulation) {
        for (int i = currentSimulation; i <= totalSimulations; i++) {
            System.out.println("\n=== SIMULATION " + i + " ===\n");

            GameState.resume();

            Matrix matrix = new Matrix(rows, cols);
            place(matrix, Cell.WALL, numWalls);
            place(matrix, Cell.TELEPHONE, numTelephones);

            EntityFactory.Difficulty difficulty = getDifficulty();

            int[] neoPos = randomEmpty(matrix);
            Neo neo = EntityFactory.createNeo(
                    neoPos[0], neoPos[1], matrix, neoSpeed, difficulty);

            List<Agent> agents = new ArrayList<>();
            for (int a = 0; a < numAgents; a++) {
                int[] pos = randomEmpty(matrix);
                agents.add(EntityFactory.createAgent(
                        pos[0], pos[1], matrix, neo, agentSpeed, difficulty));
            }

            if (frame == null) {
                frame = new SimulationFrame(matrix);
            } else {
                frame.update(matrix);
                frame.setPaused(false);
            }

            final SimulationFrame currentFrame = frame;

            neo.addObserver(event -> {
                System.out.println("[Observer] " + event);
                currentFrame.repaintPanel();
            });

            for (Agent agent : agents) {
                agent.addObserver(event -> currentFrame.repaintPanel());
            }

            neo.start();
            for (Agent agent : agents) agent.start();

            try {
                neo.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

            for (Agent agent : agents) {
                agent.active = false;
                agent.interrupt();
            }

            frame.update(matrix);

            if (neo.escaped) {
                neoWins++;
                System.out.println("NEO ESCAPED!");
            } else {
                neoLosses++;
                System.out.println("NEO WAS CAUGHT!");
            }

            String result = neo.escaped ? "Neo escaped!" : "Neo was caught!";
            final int[] choice = {0};
            final int simNum = i;

            Runnable showDialog = () ->
                    choice[0] = JOptionPane.showOptionDialog(
                            frame,
                            result + "\n\nSimulation " + simNum
                                    + " of " + totalSimulations,
                            "Simulation ended",
                            JOptionPane.DEFAULT_OPTION,
                            neo.escaped ? JOptionPane.INFORMATION_MESSAGE
                                    : JOptionPane.WARNING_MESSAGE,
                            null,
                            new String[]{"Next Simulation", "Quit to Menu", "Exit"},
                            "Next Simulation"
                    );

            if (SwingUtilities.isEventDispatchThread()) {
                showDialog.run();
            } else {
                try {
                    SwingUtilities.invokeAndWait(showDialog);
                } catch (Exception e) {
                    Thread.currentThread().interrupt();
                }
            }

            if (choice[0] == 1) {
                if (frame != null) frame.dispose();
                SwingUtilities.invokeLater(MenuFrame::new);
                return;
            } else if (choice[0] == 2) {
                System.exit(0);
            }
        }

        showSummary();
    }

    private EntityFactory.Difficulty getDifficulty() {
        if (agentSpeed >= 550) return EntityFactory.Difficulty.EASY;
        if (agentSpeed <= 350) return EntityFactory.Difficulty.HARD;
        return EntityFactory.Difficulty.MEDIUM;
    }

    private void showSummary() {
        String overall;
        if (neoWins > neoLosses)      overall = "Neo wins overall!";
        else if (neoLosses > neoWins) overall = "Agents win overall!";
        else                          overall = "It's a tie!";

        String summary =
                "ALL SIMULATIONS COMPLETED!\n\n" +
                        "Neo escaped:    " + neoWins   + " time(s)\n" +
                        "Neo was caught: " + neoLosses + " time(s)\n\n" +
                        overall;

        final int[] choice = {0};

        Runnable showSummaryDialog = () ->
                choice[0] = JOptionPane.showOptionDialog(
                        frame, summary, "Game Summary",
                        JOptionPane.DEFAULT_OPTION,
                        JOptionPane.INFORMATION_MESSAGE,
                        null,
                        new String[]{"Back to Menu", "Exit"},
                        "Back to Menu"
                );

        if (SwingUtilities.isEventDispatchThread()) {
            showSummaryDialog.run();
        } else {
            try {
                SwingUtilities.invokeAndWait(showSummaryDialog);
            } catch (Exception e) {
                Thread.currentThread().interrupt();
            }
        }

        if (frame != null) frame.dispose();
        if (choice[0] == 0) SwingUtilities.invokeLater(MenuFrame::new);
        else System.exit(0);
    }

    private void place(Matrix matrix, Cell cell, int count) {
        for (int i = 0; i < count; i++) {
            int[] pos = randomEmpty(matrix);
            matrix.setCell(pos[0], pos[1], cell);
        }
    }

    private int[] randomEmpty(Matrix matrix) {
        while (true) {
            int r = random.nextInt(rows);
            int c = random.nextInt(cols);
            if (matrix.getCell(r, c) == Cell.EMPTY)
                return new int[]{r, c};
        }
    }
}
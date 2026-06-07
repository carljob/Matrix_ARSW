package matrix.simulation;

import matrix.simulation.entities.Agent;
import matrix.simulation.entities.Neo;
import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;
import matrix.simulation.ui.MenuFrame;
import matrix.simulation.ui.SimulationFrame;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {

    private int rows;
    private int cols;
    private int numAgents;
    private int numWalls;
    private int numTelephones;
    private Random random;
    private SimulationFrame frame;
    private int neoWins;
    private int neoLosses;
    private int neoSpeed;
    private int agentSpeed;

    public Simulation(int rows, int cols, int numAgents, int numWalls, int numTelephones) {
        this(rows, cols, numAgents, numWalls, numTelephones, 400, 450);
    }

    public Simulation(int rows, int cols, int numAgents, int numWalls, int numTelephones, int neoSpeed, int agentSpeed) {
        this.random = new Random();
        this.neoWins = 0;
        this.neoLosses = 0;
        this.rows = rows;
        this.cols = cols;
        this.numAgents = numAgents;
        this.numWalls = numWalls;
        this.numTelephones = numTelephones;
        this.neoSpeed = neoSpeed;
        this.agentSpeed = agentSpeed;
    }

    public void run(int totalSimulations, int currentSimulation) {
        if (currentSimulation > totalSimulations) {
            showSummary();
            return;
        }

        System.out.println("Simulation " + currentSimulation);

        Matrix matrix = new Matrix(rows, cols);

        place(matrix, Cell.WALL, numWalls);
        place(matrix, Cell.TELEPHONE, numTelephones);

        int[] neoPos = randomEmpty(matrix);
        matrix.setCell(neoPos[0], neoPos[1], Cell.NEO);
        Neo neo = new Neo(neoPos[0], neoPos[1], matrix, neoSpeed);

        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < numAgents; i++) {
            int[] agentPos = randomEmpty(matrix);
            matrix.setCell(agentPos[0], agentPos[1], Cell.AGENT);
            agents.add(new Agent(agentPos[0], agentPos[1], matrix, neo, agentSpeed));
        }

        if (frame == null) {
            frame = new SimulationFrame(matrix, this);
        } else {
            frame.update(matrix);
            frame.setPaused(false);
        }

        neo.start();
        for (Agent agent : agents) {
            agent.start();
        }

        try {
            while (neo.alive && !neo.escaped) {
                Thread.sleep(100);
                if (!frame.isPaused()) {
                    frame.update(matrix);
                } else {
                    while (frame.isPaused()) {
                        Thread.sleep(100);
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        for (Agent agent : agents) {
            agent.active = false;
        }

        if (neo.escaped) {
            neoWins++;
            System.out.println("NEO ESCAPED!");
        } else {
            neoLosses++;
            System.out.println("NEO WAS CAUGHT!");
        }

        final String result = neo.escaped ? "Neo escaped!" : "Neo was caught!";
        final int[] choice = new int[1];

        try {
            SwingUtilities.invokeAndWait(() -> choice[0] = JOptionPane.showOptionDialog(
                    frame,
                    result + "\nSimulation " + currentSimulation,
                    "Game Summary",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Next Simulation", "Quit to Menu", "Exit"},
                    "Next Simulation"
            ));
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }

        if (choice[0] == 1) {
            if (frame != null) {
                frame.dispose();
            }
            SwingUtilities.invokeLater(MenuFrame::new);
            return;
        }

        if (choice[0] == 2) {
            System.exit(0);
            return;
        }

        run(totalSimulations, currentSimulation + 1);
    }

    private void showSummary() {
        String message;
        if (neoWins > neoLosses) {
            message = "Neo wins overall!";
        } else if (neoLosses > neoWins) {
            message = "Agents win overall!";
        } else {
            message = "It's a tie!";
        }

        final int[] choice = new int[1];
        try {
            SwingUtilities.invokeAndWait(() -> choice[0] = JOptionPane.showOptionDialog(
                    frame,
                    message + "\nNeo wins: " + neoWins + "\nNeo losses: " + neoLosses,
                    "Game Summary",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    new String[]{"Back to Menu", "Exit"},
                    "Back to Menu"
            ));
        } catch (Exception e) {
            Thread.currentThread().interrupt();
        }

        if (frame != null) {
            frame.dispose();
        }

        if (choice[0] == 0) {
            SwingUtilities.invokeLater(MenuFrame::new);
        } else {
            System.exit(0);
        }
    }

    public void pauseSimulation() {
        if (frame != null) {
            frame.setPaused(true);
        }
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
            if (matrix.getCell(r, c) == Cell.EMPTY) {
                return new int[]{r, c};
            }
        }
    }
}

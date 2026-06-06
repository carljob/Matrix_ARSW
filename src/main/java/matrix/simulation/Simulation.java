package matrix.simulation;

import matrix.simulation.entities.Agent;
import matrix.simulation.entities.Neo;
import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;
import matrix.simulation.ui.SimulationFrame;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Simulation {

    private int rows;
    private int cols;
    private int numAgents;
    private int numWalls;
    private int numTelephones;
    private Random random = new Random();
    private SimulationFrame frame;

    public Simulation(int rows, int cols, int numAgents,
                      int numWalls, int numTelephones) {
        this.rows = rows;
        this.cols = cols;
        this.numAgents = numAgents;
        this.numWalls = numWalls;
        this.numTelephones = numTelephones;
    }

    public void run(int maxSimulations, int current) {
        if (current > maxSimulations) {
            System.out.println("All simulations completed!");
            return;
        }

        System.out.println("\n=== SIMULATION " + current + " ===\n");

        Matrix matrix = new Matrix(rows, cols);

        place(matrix, Cell.WALL, numWalls);
        place(matrix, Cell.TELEPHONE, numTelephones);

        int[] neoPos = randomEmpty(matrix);
        matrix.setCell(neoPos[0], neoPos[1], Cell.NEO);
        Neo neo = new Neo(neoPos[0], neoPos[1], matrix);

        List<Agent> agents = new ArrayList<>();
        for (int i = 0; i < numAgents; i++) {
            int[] pos = randomEmpty(matrix);
            matrix.setCell(pos[0], pos[1], Cell.AGENT);
            agents.add(new Agent(pos[0], pos[1], matrix, neo));
        }

        if (frame == null) {
            frame = new SimulationFrame(matrix);
        } else {
            frame.update(matrix);
        }

        neo.start();
        for (Agent agent : agents) agent.start();

        while (neo.alive && !neo.escaped) {
            try {
                Thread.sleep(100);
                frame.update(matrix); // refrescar UI
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        for (Agent agent : agents) agent.active = false;

        System.out.println("\n=== SIMULATION " + current + " ENDED ===\n");

        try { Thread.sleep(2000); }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        run(maxSimulations, current + 1);
    }

    private void place(Matrix matrix, Cell type, int n) {
        int placed = 0;
        while (placed < n) {
            int[] pos = randomEmpty(matrix);
            matrix.setCell(pos[0], pos[1], type);
            placed++;
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
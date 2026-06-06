package matrix.simulation;

public class Main {

    public static void main(String[] args) {

        // Parametrizable!
        int rows         = 8;
        int cols         = 8;
        int numAgents    = 2;
        int numWalls     = 5;
        int numTelephones = 2;
        int numSims      = 3;

        Simulation sim = new Simulation(
                rows, cols, numAgents, numWalls, numTelephones
        );

        sim.run(numSims, 1);
    }
}
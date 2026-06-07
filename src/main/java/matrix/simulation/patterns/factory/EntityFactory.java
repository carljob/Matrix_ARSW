package matrix.simulation.patterns.factory;

import matrix.simulation.entities.Agent;
import matrix.simulation.entities.Neo;
import matrix.simulation.model.Cell;
import matrix.simulation.model.Matrix;
import matrix.simulation.patterns.strategy.*;

public class EntityFactory {

    public enum Difficulty { EASY, MEDIUM, HARD }

    public static Neo createNeo(int row, int col, Matrix matrix,
                                int speed, Difficulty difficulty) {
        matrix.setCell(row, col, Cell.NEO);
        Neo neo = new Neo(row, col, matrix, speed);

        MovementStrategy strategy = (difficulty == Difficulty.EASY)
                ? new NeoRandomMovement()
                : new NeoSmartMovement();

        neo.setMovementStrategy(strategy);
        return neo;
    }

    public static Agent createAgent(int row, int col, Matrix matrix,
                                    Neo neo, int speed, Difficulty difficulty) {
        matrix.setCell(row, col, Cell.AGENT);
        Agent agent = new Agent(row, col, matrix, neo, speed);

        MovementStrategy strategy = (difficulty == Difficulty.EASY)
                ? new AgentRandomMovement(neo)
                : new AgentSmartMovement(neo);

        agent.setMovementStrategy(strategy);
        return agent;
    }
}
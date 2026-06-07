package matrix.simulation;

public class DifficultyConfig {

    public enum Level { EASY, MEDIUM, HARD }

    public final int numAgents;
    public final int numWalls;
    public final int numTelephones;
    public final int neoSpeed;
    public final int agentSpeed;

    private DifficultyConfig(int numAgents, int numWalls, int numTelephones,
                             int neoSpeed, int agentSpeed) {
        this.numAgents     = numAgents;
        this.numWalls      = numWalls;
        this.numTelephones = numTelephones;
        this.neoSpeed      = neoSpeed;
        this.agentSpeed    = agentSpeed;
    }

    public static DifficultyConfig of(Level level, int boardSize) {
        return switch (level) {
            case EASY   -> new DifficultyConfig(
                    boardSize <= 12 ? 1 : boardSize <= 30 ? 2  : 4,
                    boardSize <= 12 ? 3 : boardSize <= 30 ? 8  : 20,
                    boardSize <= 12 ? 4 : boardSize <= 30 ? 8  : 15,
                    300, 600
            );
            case MEDIUM -> new DifficultyConfig(
                    boardSize <= 12 ? 2 : boardSize <= 30 ? 5  : 12,
                    boardSize <= 12 ? 4 : boardSize <= 30 ? 10 : 28,
                    boardSize <= 12 ? 2 : boardSize <= 30 ? 4  : 8,
                    400, 450
            );
            case HARD   -> new DifficultyConfig(
                    boardSize <= 12 ? 4 : boardSize <= 30 ? 10 : 25,
                    boardSize <= 12 ? 6 : boardSize <= 30 ? 15 : 40,
                    boardSize <= 12 ? 1 : boardSize <= 30 ? 2  : 3,
                    500, 300
            );
        };
    }
}
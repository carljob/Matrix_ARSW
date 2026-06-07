package matrix.simulation;

public class GameState {
    private static volatile boolean paused = false;

    public static void pause()  { paused = true; }
    public static void resume() { paused = false; }
    public static boolean isPaused() { return paused; }
}
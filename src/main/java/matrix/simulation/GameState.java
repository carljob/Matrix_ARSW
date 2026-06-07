package matrix.simulation;

public class GameState {

    private static volatile boolean paused = false;
    private static final Object lock = new Object();

    public static void pause() {
        synchronized (lock) {
            paused = true;
        }
    }

    public static void resume() {
        synchronized (lock) {
            paused = false;
            lock.notifyAll();
        }
    }

    public static void waitIfPaused() throws InterruptedException {
        synchronized (lock) {
            while (paused) {
                lock.wait();
            }
        }
    }
}

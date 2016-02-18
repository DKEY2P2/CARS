package helper;

/**
 * A simple timer to record how long something takes.
 * <p>
 * Overhead is around 6276 nanoseconds
 *
 * @author Kareem
 */
public class Timer {

    private static long time = 0;

    /**
     * Starts the timer
     */
    public static void start() {
        time = System.nanoTime();
    }

    /**
     * Ends the timer. Gives the answer in nano seconds
     */
    public static void nano() {
        System.out.println(System.nanoTime() - time);
    }

    /**
     * Ends the timer. Gives the answer in milliseconds
     */
    public static void milli() {
        System.out.println(1_000_000l / (System.nanoTime() - time));
    }
}

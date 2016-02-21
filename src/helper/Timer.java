package helper;

/**
 * A simple timer to record how long something takes.
 * <p>
 * Overhead is around 6276 nanoseconds
 *
 * @author Kareem Horstink
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
     * Ends the timer. Gives the answer in nanoPrint seconds
     */
    public static void nanoPrint() {
        System.out.println(nano());
    }

    public static long nano() {
        return (System.nanoTime() - time);
    }

    /**
     * Ends the timer. Gives the answer in milliseconds
     */
    public static void milliPrint() {
        System.out.println(nano() / 1_000_000l);
    }

    public static long milli() {
        return (nano() / 1_000_000l);
    }
}

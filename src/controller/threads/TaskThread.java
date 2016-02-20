package controller.threads;

/**
 * The thread to run the task runnable
 * <p>
 * Not done yet
 *
 * @author Kareem Horstink
 * @version 0.00001
 */
public class TaskThread extends Thread {

    /**
     * The default constructor
     *
     * @param runnable The target runnable
     */
    public TaskThread(TaskRunnable runnable) {
        super(runnable);
    }

    /**
     *
     * The default constructor
     *
     * @param runnable The target runnable
     * @param name The name of the thread
     */
    public TaskThread(TaskRunnable runnable, String name) {
        super(runnable);
        setName(name);
    }

}

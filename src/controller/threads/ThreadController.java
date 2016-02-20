package controller.threads;

import controller.Controller;
import controller.Observer;
import controller.Observerable;
import controller.Task;
import helper.Logger;
import java.util.concurrent.LinkedBlockingDeque;
import vehicle.Vehicle;
import vehicle.VehicleHolder;

/**
 * Controls all the threads. No clue if this works. Would be awesome if it did
 *
 * @author Kareem Horstink
 */
public class ThreadController implements Observer {

    /**
     * All the task that should be ran
     */
    private LinkedBlockingDeque<Task> tasks = new LinkedBlockingDeque<>();
    /**
     * The number of threads wanted
     */
    private int numberOfThreads = 1;
    /**
     * All the threads
     */
    private TaskThread[] threads;
    /**
     * All the task runner (connected to threads)
     */
    private TaskRunnable[] taskRunnable;

    /**
     * Get the value of numberOfThreads
     *
     * @return the value of numberOfThreads
     */
    public int getNumberOfThreads() {
        return numberOfThreads;
    }

    /**
     * The constructor of the thread controller
     *
     * @param numberOfThreads The number of threads you want to run
     * @param o The object that tells the controller to do a task
     */
    public ThreadController(int numberOfThreads, Observerable o) {
        //Creates some variables
        this.numberOfThreads = numberOfThreads;
        taskRunnable = new TaskRunnable[numberOfThreads];
        threads = new TaskThread[numberOfThreads];

        //Creates all the invidual threads and task runners
        for (int i = 0; i < numberOfThreads; i++) {
            //What runs the task
            taskRunnable[i] = new TaskRunnable();
            //The threads that actually run the task
            threads[i] = new TaskThread(taskRunnable[i], Integer.toString(i));
            //Logs the creation of the thread
            Logger.LogAny("Thread", "Thread created: " + threads[i].getName());
        }

        //Adds this object to observer list
        o.addObserver(this);
    }

    /**
     * Shows if the threads are still calculating the tasks
     */
    private boolean running = false;

    /**
     * Runs the controller
     * <p>
     * Runs by checking every thread if it is busy. If it is not, it would get
     * the next task to do and adds it to idling thread then the thread is
     * started.
     */
    private void run() {
        running = true;
        //Continues untill there is no more task to do
        while (!tasks.isEmpty()) {
            //Checks if any threads is just chilling
            for (int i = 0; i < numberOfThreads; i++) {
                //The check if its just chilling - new means it has never been ran before while terminated means it finished running a task
                if (threads[i].getState() == Thread.State.NEW
                        || threads[i].getState() == Thread.State.TERMINATED) {
                    giveTask(taskRunnable[i]);
                    threads[i].start();
                }
            }
        }
        running = false;
    }

    /**
     * Gets the new set of tasks. Currently only the vehicles
     */
    private void getTasks() {
        VehicleHolder vh = Controller.getInstance().getVehicles(); //Might change this to make thread safe
        tasks.addAll(vh);
    }

    /**
     * Give task to the task runner
     *
     * @param r The task runner to be given a task
     */
    private void giveTask(TaskRunnable r) {
        r.setTask(tasks.poll());
    }

    @Override
    public void update() {
        update("Tick");
    }

    @Override
    public void update(String args) {
        //Adds a task set to the list of toDo's
        getTasks();

        //Checks if the threads is currently running
        if (!running) {
            run();
        } else {
            Logger.LogAny("Thread",
                    "Threads were still calculating previous round of tasks");
        }
    }

}

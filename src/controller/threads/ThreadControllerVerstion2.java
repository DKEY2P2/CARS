package controller.threads;

import controller.Controller;
import controller.Observer;
import controller.Observerable;
import controller.Task;
import helper.Logger;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.logging.Level;
import vehicle.VehicleHolder;

/**
 * Controls all the threads. No clue if this works. Would be awesome if it did
 *
 * @author Kareem Horstink
 */
public class ThreadControllerVerstion2 implements Observer {

    /**
     * All the task that should be ran
     */
    private LinkedBlockingDeque<Callable<Object>> tasks = new LinkedBlockingDeque<>();
    /**
     * The number of threads wanted
     */
    private int numberOfThreads = 1;

    private ExecutorService executorService;

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
    public ThreadControllerVerstion2(int numberOfThreads, Observerable o) {
        executorService = Executors.newFixedThreadPool(numberOfThreads);
        //Adds this object to observer list
        o.addObserver(this);
    }

    /**
     * Shows if the threads are still calculating the tasks
     */
    private boolean running = false;

    /**
     * Gets the new set of tasks. Currently only the vehicles
     */
    private void getTasks() {
        VehicleHolder vh = Controller.getInstance().getVehicles(); //Might change this to make thread safe
        tasks.addAll(vh); //Might change this too
    }

    void run() {
        try {
            executorService.invokeAll(tasks);
        } catch (InterruptedException ex) {
            Logger.LogError(ex);
            System.err.println("An error has occured");
        }
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

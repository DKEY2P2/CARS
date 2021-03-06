package controller.threads;

import controller.Controller;
import controller.Observer;
import controller.Observerable;
import controller.Task;
import helper.Logger;
import map.Road;
import vehicle.Vehicle;
import vehicle.VehicleHolder;

import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * Controls all the threads. No clue if this works. Would be awesome if it did
 *
 * @author Kareem Horstink
 */
public class ThreadController implements Observer {

    /**
     * All the task that should be ran
     */
    private LinkedBlockingDeque<Callable<Object>> tasks = new LinkedBlockingDeque<>();
    /**
     * The number of threads wanted
     */
    private int numberOfThreads = 1;
    /**
     * The pool of threads that are going to run the tasks
     */
    private ExecutorService executorService;

    /**
     * The pool of threads that is going to run the UI
     */
    private ExecutorService UIService;

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
        /*
         * Creates all the pool of threads that are gonna run the tasks
         *
         * https://docs.oracle.com/javase/tutorial/essential/concurrency/pools.html
         */
        executorService = Executors.newFixedThreadPool(numberOfThreads);
        UIService = Executors.newSingleThreadExecutor();
        //Adds this object to observer list
        o.addObserver(this);
    }

    /**
     * Gets the new set of tasks. Currently only the vehicles
     */
    private void getTasks() {
        VehicleHolder vh = Controller.getInstance().getVehicles(); //Might change this to make thread safe
        tasks.addAll(vh); //Might change this too
    }

    /**
     * Adds all the new task to threads
     */
    private void run() {
        try {
            executorService.invokeAll(tasks);
            tasks.clear();
            Task c = Controller.getInstance().getUI();
            if (c != null) {
                UIService.submit(c);
            }
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


        for(Road r : Controller.getInstance().getMap().getRoads()){
            Object[] va = r.getVehicles().toArray();
            Arrays.sort(va);
            r.getVehicles().clear();
            for(Object o : va){
                r.addCar((Vehicle)o);
            }
        }
        getTasks();
        //Runs all the task
        run();
    }

}

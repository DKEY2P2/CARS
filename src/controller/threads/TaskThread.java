/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller.threads;

/**
 * The thread to run the task runnable
 *
 * @author Kareem
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

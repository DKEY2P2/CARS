package controller.threads;

import controller.Task;
import helper.Logger;

/**
 * The runnable to encapsulate the task
 * <p>
 * Not done yet
 *
 * @author Kareem Horstink
 * @version 0.0001
 */
public class TaskRunnable implements Runnable {

    private Task task;

    /**
     * Set the task that needs to be ran
     *
     * @param task new value of task
     */
    public void setTask(Task task) {
        this.task = task;
    }

    @Override
    public void run() {
        if (task == null) {
            Logger.LogAny("Task", "Task failed to run - Nullpointer");
        } else {
            Boolean results = task.update();
            if (!results) {
                Logger.LogAny("Task", "Task failed to run");
            }
        }
    }

}

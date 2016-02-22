package controller;

import java.util.concurrent.Callable;

/**
 * Allows a thread to update an object
 *
 * @author Kareem Horstink
 */
public interface Task extends Callable<Object> {

    @Override
    public default Object call() throws Exception {
        update();
        return null;
    }

    /**
     * Allows a thread to do some function of a passed object
     *
     * @return A confirmation of success or failure
     */
    public boolean update();
}

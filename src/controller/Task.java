package controller;

/**
 * Allows a thread to update an object
 *
 * @author Kareem Horstink
 */
public interface Task {

    /**
     * Allows a thread to do some function of a passed object
     *
     * @return A confirmation of success or failure
     */
    public boolean update();
}

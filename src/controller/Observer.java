package controller;

/**
 * Interface so that the observable class works
 *
 * @author Kareem
 */
public interface Observer {

    /**
     * Notifies the object that an event has happened
     */
    public void update();

    /**
     * Notifies the object that an event has happened with an argument
     *
     * @param args The argument that wants to passed along
     */
    public void update(String args);
}

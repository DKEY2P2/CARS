package controller;

import helper.Logger;
import java.util.ArrayList;

/**
 * A abstract class meant to make an object to observable.
 * <p>
 * The observer pattern is a software design pattern in which an object, called
 * the subject, maintains a list of its dependents, called observers, and
 * notifies them automatically of any state changes, usually by calling one of
 * their methods (update). It is mainly used to implement distributed event
 * handling systems. The Observer pattern is also a key part in the familiar
 * model–view–controller (MVC) architectural pattern.
 * <p>
 * Should be thread safe. Not proven
 *
 * @author Kareem Horstink
 * @see Observer
 */
public abstract class Observerable {

    /**
     * List of Observers
     */
    private final ArrayList<Observer> OBSERVER = new ArrayList<>();
    /**
     * List of Observers to add ~ to make sure its thread safe
     */
    private final ArrayList<Observer> add = new ArrayList<>();
    /**
     * List of Observers to remove ~ to make sure its thread safe
     */
    private final ArrayList<Observer> remove = new ArrayList<>();

    /**
     * If a data in the class is being edited ~ if its safe to read data from
     * the object
     */
    private boolean inUse = false;

    /**
     * Adds a new observer to this object.
     *
     * @param o The observer to be added
     */
    public synchronized void addObserver(Observer o) {
        if (!inUse) {
            inUse = true;
            OBSERVER.add(o);
            inUse = false;
        } else {
            add.add(o);
        }
    }

    /**
     * Removes a particular observer from this object
     *
     * @param o The observer to be removed
     */
    public synchronized void removeObserver(Observer o) {
        if (!inUse) {
            inUse = true;
            OBSERVER.remove(o);
            inUse = false;
        } else {
            remove.add(o);
        }
    }

    /**
     * Notifies all the observer that an update has happened.
     * <p>
     * Calls update() on the observer class
     */
    public synchronized void notifyObservers() {
        if (!inUse) {
            inUse = true;
            OBSERVER.stream().forEach((observer) -> {
                observer.update();
            });
            OBSERVER.addAll(add);
            OBSERVER.removeAll(remove);
            add.clear();
            remove.clear();
            inUse = false;
        } else {
            notifyObservers();
        }
    }

    /**
     * Notifies all the observer that an update has happened and passes the
     * arguments along.
     * <p>
     * Calls update(args) on the observer class
     *
     * @param args Arguments to be passed on
     */
    public synchronized void notifyObservers(String args) {
        if (!inUse) {
            inUse = true;
            OBSERVER.stream().forEach((observer) -> {
                observer.update(args);
            });
            OBSERVER.addAll(add);
            OBSERVER.removeAll(remove);
            add.clear();
            remove.clear();
            inUse = false;
        } else {
            notifyObservers(args);
        }
    }

    /**
     * Returns the number of observers that is observing this object
     *
     * @return The number of observers
     */
    public int getNumberOfObeservers() {
        return OBSERVER.size();
    }
}

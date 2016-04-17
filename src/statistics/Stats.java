package statistics;

import controller.Controller;
import controller.Observer;
import vehicle.VehicleFactory;
import vehicle.VehicleHolder;

/**
 * The class that contains <i>"all"</i> the statistics of the world and model.
 * Meant to allow the user to evaluate and benchmark the current system.<p>
 * This version is a immutable version of the stats, in other words, it does not
 * update when the simulation is on going
 *
 * @author Kareem Horstink
 */
public class Stats {

    private int totalNumberOfCarsMade = 0;
    private int currentNumberOfCars = 0;
    private int totalAmountOfTicks = 0;
    private double totalAmountOfTimePassed;

    /**
     * Made to allow StatsDynamic to make it own constructor
     */
    private Stats() {

    }

    /**
     * Meant to be used to clone the object. @TODO
     *
     * @param s The statistics to be copied over
     */
    private Stats(Stats s) {

    }

    /**
     * Get the waiting time done by every car
     *
     * @return Time in ticks
     */
    public int getTotalWaitTime() {//@TODO
        return -1000;
    }

    /**
     * Get the waiting time done by every car
     *
     * @return Time in seconds
     */
    public double getTotalWaitTimeSeconds() {//@TODO
        return -100d;
    }

    /**
     * Get the total number of cars
     *
     * @return The number of cars
     */
    public int getTotalNumberOfCarsMade() {
        return totalNumberOfCarsMade;
    }

    /**
     * Get the total amount of cars in the system
     *
     * @return The total amount of cars in the system
     */
    public int getCurrentNumberOfCars() {
        return currentNumberOfCars;
    }

    /**
     * Gets the total amount of ticks that has happened in the sim
     *
     * @return The total amount of ticks that have occurred
     */
    public int getTotalAmountOfTicks() {
        return totalAmountOfTicks;
    }

    /**
     * Gets the total amount of time that has happened in the sim
     *
     * @return The total amount of time that have passed in seconds
     */
    public double getTotalAmountOfTimePassed() {
        return totalAmountOfTimePassed;
    }

    private void update() {
        totalNumberOfCarsMade = VehicleFactory.getFactory().getTotalAmountMade();
        currentNumberOfCars = VehicleHolder.getInstance().size();
    }

    /**
     * Gets a copy of the this statistics that doesn't update it self
     *
     * @return
     */
    public Stats copy() {
        return new Stats(this);
    }

    /**
     * The class that contains <i>"all"</i> the statistics of the world and
     * model. Meant to allow the user to evaluate and benchmark the current
     * system.<p>
     * This version is a mutable version of the {@link Stats}, in other words,
     * it does update when the simulation is on going hence it implements
     * {@link Observer}
     *
     * @author Kareem Horstink
     */
    public static class StatsDynamic extends Stats implements Observer {

        private Controller world;

        public StatsDynamic(Controller c) {
            world = c;
            world.getTicker().addObserver(this);
        }

        @Override
        public void update() {
            super.update();
        }

        @Override
        public void update(String args) {
            update();
        }

    }
}

package statistics;

import controller.Controller;
import controller.Observer;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import vehicle.Vehicle;
import vehicle.VehicleFactory;
import vehicle.VehicleHolder;

/**
 * The class that contains <i>"all"</i> the statistics of the world and model.
 * Meant to allow the user to evaluate and benchmark the current system.<p>
 * This version is a immutable version of the stats, in other words, it does not
 * update when the simulation is on going
 *
 * <p>
 *
 * When actually calculating the the items that need to be calculated, look at
 * {@link StatsDynamic} and do it in there please. Thank you
 *
 * @author Kareem Horstink
 */
public class Stats {

    private int totalNumberOfCarsMade = 0;
    private int currentNumberOfCars = 0;
    private int waitingTimeTicks = 0;
    private double totalWaitTimeS = 0;
    private int totalWaitingTime = 0;
    protected int totalNumberTicks = 0;
    protected double totalTimePassed = 0;
    //Car index, Wait time (s)
    protected ArrayList<AbstractMap.SimpleEntry<Integer, Double>> waitingTimeS = new ArrayList<>();
    //Car  index, wait time (tick)
    protected ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> waitTime = new ArrayList<>();
    //A history of all the waiting time
    protected ArrayList<Double> historyWaitingTimeS = new ArrayList<>();
    //A history of all the cars (amount)
    protected ArrayList<Integer> historyNumberOfCars = new ArrayList<>();
    private double waitingTimeSeconds = 0;

    /**
     * Made to allow StatsDynamic to make it own constructor
     */
    private Stats() {
    }

    /**
     * Meant to be used to clone the object.
     *
     * @param s The statistics to be copied over
     */
    private Stats(Stats s) {
        currentNumberOfCars = s.currentNumberOfCars;
        totalNumberOfCarsMade = s.totalNumberOfCarsMade;
        totalNumberTicks = s.totalNumberTicks;
        totalTimePassed = s.totalTimePassed;
        totalWaitingTime = s.totalWaitingTime;
        waitingTimeSeconds = s.waitingTimeSeconds;
        waitingTimeTicks = s.waitingTimeTicks;
        historyNumberOfCars = (ArrayList<Integer>) s.historyNumberOfCars.clone();
        historyWaitingTimeS = (ArrayList<Double>) s.historyWaitingTimeS.clone();
    }

    /**
     * Get the waiting time history
     *
     * @return An arraylist in chronological order
     */
    public ArrayList<Double> getWaitingTimeHistory() {
        return historyWaitingTimeS;
    }

    /**
     * Get the number of cars in the simulation history
     *
     * @return An arraylist in chronological order
     */
    public ArrayList<Integer> getNumberOfCarsHistory() {
        return historyNumberOfCars;
    }

    /**
     * Get the waiting time history that in normalized
     *
     * @return An arraylist in chronological order
     * @TODO Think of doing this in a better way
     */
    public ArrayList<Double> getWaitingTimeNormalizedHistory() {
        //Gets the history of the waiting time and divides it by number of cars history
        ArrayList<Double> tmp = new ArrayList<>();
        for (int i = 0; i < historyWaitingTimeS.size(); i++) {
            Double get1 = historyWaitingTimeS.get(i);
            Integer get2 = historyNumberOfCars.get(i);
            tmp.add(get1 / get2);
        }
        return tmp;
    }

    /**
     * Get the waiting time done by every car
     *
     * @return Time in ticks
     */
    public int getTotalWaitTime() {
        return totalWaitingTime;
    }

    /**
     * Get the waiting time for the current tick in seconds
     *
     * @return Time in seconds
     */
    public double getWaitTimeSeconds() {
        return waitingTimeSeconds;
    }

    /**
     * Get the waiting time for the current tick in ticks (in other words, get
     * the total amount of cars waiting right now)
     *
     * @return The total amount of cars waiting
     */
    public int getWaitTimeTicks() {
        return waitingTimeTicks;
    }

    /**
     * Get the waiting time done by every car
     *
     * @return Time in seconds
     */
    public double getTotalWaitTimeSeconds() {
        return totalWaitTimeS;
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
        return totalNumberTicks;
    }

    /**
     * Gets the total amount of time that has happened in the sim
     *
     * @return The total amount of time that have passed in seconds
     */
    public double getTotalAmountOfTimePassed() {
        return totalTimePassed;
    }

    //A cluster fuck of different types of updates
    private void update() {
        totalNumberOfCarsMade = VehicleFactory.getFactory().getTotalAmountMade();
        currentNumberOfCars = VehicleHolder.getInstance().size();
        //Sorts the waiting times of the cars so we can eventually troubleshoot
        //what car has to wait the most
        waitingTimeS.sort(new Compare());
        //Gets the total waiting time in seconds
        double sum = 0;
        sum = waitingTimeS.stream().map((waitingTime1) -> waitingTime1.getValue()).reduce(sum, (accumulator, _item) -> accumulator + _item);
        waitingTimeSeconds = sum;
        totalWaitTimeS += sum;

        //Gets the total waiting time in ticks
        int sum2 = 0;
        sum2 = waitTime.stream().mapToInt((waitingTime) -> waitingTime.getValue()).reduce(sum2, (pizza, pie) -> pizza + pie);
        waitingTimeTicks = sum2;
        totalWaitingTime += sum2;

        //Adds the new values to the history arraylist
        historyWaitingTimeS.add(waitingTimeSeconds);
        historyNumberOfCars.add(currentNumberOfCars);
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

        //A flags of all the vehicles, to see if the car is still waiting
        private ArrayList<Integer> flags = new ArrayList<>();
        //The simulation
        private Controller world;

        /**
         * Creates the stats object which updates in <i>"real time"</i> with the
         * simulation
         *
         * @param c The simulation
         */
        public StatsDynamic(Controller c) {
            world = c;
            world.getTicker().addObserver(this);
        }

        @Override
        public void update() {
            //Gets the total amount of time has passed
            //Just takes it from the ticker
            totalTimePassed = world.getTicker().getTimeElapsed();
            totalNumberTicks = world.getTicker().getTickCount();

            //Updates the waiting time
            updateWaitingTimes();

            //Does the rest of the updating
            super.update();
        }

        /**
         * Checks if a car is waiting and updates the waiting time of the cars
         * that are waiting
         */
        private void updateWaitingTimes() {
            //Clears the previous tick's flags
            flags.clear();
            //Gets every car which is slower than 1m/s which is around 3.6 kmh 
            //and assumes that it is waiting
            world.getVehicles().
                    stream().
                    filter((vehicle) -> (vehicle.getSpeed() <= 1)&&(vehicle.getAcceleration()<0)).
                    forEach((vehicle) -> {
                        //Adds all the vehicles to the list of cars that are 
                        //currently waiting
                        addTime(vehicle);
                    });

            //Makes 2 temporay list so we can eventually remove cars that aren't waiting
            //anymore ~ Ask Kareem to explain more, hard to explain in just comments xD
            ArrayList<AbstractMap.SimpleEntry<Integer, Double>> tmp = new ArrayList<>();
            ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> tmp2 = new ArrayList<>();

            //Goes through all the cars and finds any car that isn't waiting in
            //the current tick.
            for (int i = 0; i < waitingTimeS.size(); i++) {
                AbstractMap.SimpleEntry<Integer, Double> get = waitingTimeS.get(i);
                AbstractMap.SimpleEntry<Integer, Integer> get2 = waitTime.get(i);
                if (!flags.contains(get.getKey())) {
                    tmp.add(get);
                    tmp2.add(get2);
                }
            }
            waitingTimeS.removeAll(tmp);
            waitTime.removeAll(tmp2);
        }

        /**
         * Adds the vehicle to list which holds what car is currently waiting
         *
         * @param v The vehicle in question
         */
        private void addTime(Vehicle v) {
            //Adds the vehicle to the flag
            flags.add(v.getIndex());

            //Updates the value if the entry is already in the list
            for (AbstractMap.SimpleEntry<Integer, Double> waitingTime1 : this.waitingTimeS) {
                if (waitingTime1.getKey() == v.getIndex()) {
                    waitingTime1.setValue(world.getTicker().tickTimeInS + waitingTime1.getValue());
                }
            }
            for (AbstractMap.SimpleEntry<Integer, Integer> waitTime1 : waitTime) {
                if (waitTime1.getKey() == v.getIndex()) {
                    waitTime1.setValue(1 + waitTime1.getValue());
                    return;
                }
            }
            
            //If it isn't in the list previously, than adds it to the list
            this.waitingTimeS.add(new AbstractMap.SimpleEntry<>(v.getIndex(), world.getTicker().tickTimeInS));
            this.waitTime.add(new AbstractMap.SimpleEntry<>(v.getIndex(), 1));
        }

        @Override
        public void update(String args) {
            update();
        }

    }

    /**
     * Compares a SimpleEntry<Integer, Double> based on the double part of it
     */
    private class Compare implements Comparator<AbstractMap.SimpleEntry<Integer, Double>> {

        @Override
        public int compare(AbstractMap.SimpleEntry<Integer, Double> o1, AbstractMap.SimpleEntry<Integer, Double> o2) {
            if (Objects.equals(o1.getValue(), o2.getValue())) {
                return 0;
            }
            if (o1.getValue() > o2.getValue()) {
                return 1;
            }
            if (o2.getValue() > o1.getValue()) {
                return -1;
            }
            return -193;
        }
    }
}

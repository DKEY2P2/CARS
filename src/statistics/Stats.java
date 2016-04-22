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

    private void update() {
        totalNumberOfCarsMade = VehicleFactory.getFactory().getTotalAmountMade();
        currentNumberOfCars = VehicleHolder.getInstance().size();
        waitingTimeS.sort(new Compare());
        double sum = 0;
        sum = waitingTimeS.stream().map((waitingTime1) -> waitingTime1.getValue()).reduce(sum, (accumulator, _item) -> accumulator + _item);
        waitingTimeSeconds = sum;
        totalWaitTimeS += sum;
        int sum2 = 0;
        sum2 = waitTime.stream().mapToInt((waitingTime) -> waitingTime.getValue()).reduce(sum2, (pizza, pie) -> pizza + pie);
        waitingTimeTicks = sum2;
        totalWaitingTime += sum2;
        historyWaitingTimeS.add(getTotalWaitTimeSeconds());
        historyNumberOfCars.add(currentNumberOfCars);
//        historyWaitingTimeS.add(getWaitTimeSeconds());
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

        private ArrayList<Integer> flags = new ArrayList<>();
        private Controller world;

        public StatsDynamic(Controller c) {
            world = c;
            world.getTicker().addObserver(this);
        }

        @Override
        public void update() {
            totalTimePassed = world.getTicker().getTimeElapsed();
            totalNumberTicks = world.getTicker().getTickCount();
            updateWaitingTimes();
            super.update();
        }

        private void updateWaitingTimes() {
            flags.clear();
            world.getVehicles().stream().filter((vehicle) -> (vehicle.getSpeed() <= 1)).forEach((vehicle) -> {
                addTime(vehicle);
            });
            ArrayList<AbstractMap.SimpleEntry<Integer, Double>> tmp = new ArrayList<>();
            ArrayList<AbstractMap.SimpleEntry<Integer, Integer>> tmp2 = new ArrayList<>();
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

        private void addTime(Vehicle v) {
            flags.add(v.getIndex());
            for (AbstractMap.SimpleEntry<Integer, Double> waitingTime1 : this.waitingTimeS) {
                if (waitingTime1.getKey() == v.getIndex()) {
                    waitingTime1.setValue(world.getTicker().tickTimeInS);
                    return;
                }
            }
            for (AbstractMap.SimpleEntry<Integer, Integer> waitTime1 : waitTime) {
                if (waitTime1.getKey() == v.getIndex()) {
                    waitTime1.setValue(1);
                    return;
                }
            }
            this.waitingTimeS.add(new AbstractMap.SimpleEntry<>(v.getIndex(), world.getTicker().tickTimeInS));
            this.waitTime.add(new AbstractMap.SimpleEntry<>(v.getIndex(), 1));
        }

        @Override
        public void update(String args) {
            update();
        }

    }

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

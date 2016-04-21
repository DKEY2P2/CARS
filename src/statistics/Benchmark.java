package statistics;

import controller.Controller;

/**
 * The object contains different ways to benchmark the simulation
 *
 * @author Kareem Horstink
 */
public class Benchmark {

    private Controller c;

    public Benchmark(Controller c) {
        this.c = c;
    }

    /**
     * Get the average waiting time, useful for a benchmark and we want to
     * reduce it to 0
     *
     * @return The average waiting time that is normalized
     */
    public double getWaitingTimeNormalized() {
        Stats s = c.getStats().copy();
        return s.getTotalWaitTimeSeconds() / s.getCurrentNumberOfCars() / c.getTicker().getTickTimeInS();
    }

    /**
     * Hopefully gets the percentage of the number cars waiting vs the total
     * number of cars
     *
     * @return The percentage
     */
    public double percentageWaitingVsTotalAmountOfCars() {
        Stats s = c.getStats().copy();
        return s.getCurrentNumberOfCars() / s.getWaitTimeTicks();
    }
}

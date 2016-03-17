package controller;

import vehicle.VehicleFactory;

import java.util.Timer;
import java.util.TimerTask;

/**
 * A ticker that meant to update the program
 *
 * @author jvacek, Kareem Horstink
 */
public class Ticker extends Observerable {

    /**
     * The amount of time a tick represents in Seconds
     */
    public double tickTimeInS;
    /**
     * The total number of ticks that have been ticked
     */
    public int tickCount;

    /**
     * How long between ticks in ms
     */
    private int timeBetweenTick = 1000;

    /**
     * Default constructor.
     *
     * @param TickTimeInS The time that one tick represent in seconds
     * @param timeBetweenTicks The time between ticks
     */
    public Ticker(double TickTimeInS, int timeBetweenTicks) {
        setTickTimeInS(TickTimeInS);
        setTimeBetweenTick(timeBetweenTicks);
    }

    /**
     * Get the value of timeBetweenTick in ms
     *
     * @return the value of timeBetweenTick in ms
     */
    public int getTimeBetweenTick() {
        return timeBetweenTick;
    }

    /**
     * Set the value of timeBetweenTick in ms
     * <p>
     * Not sure this will actually work once you start the timer
     *
     * @param timeBetweenTick new value of timeBetweenTick in ms
     */
    public void setTimeBetweenTick(int timeBetweenTick) {
        this.timeBetweenTick = timeBetweenTick;
    }

    /**
     * Use this to start the timer
     *
     * @param args The message passed to notifyObservers()
     */
    public void start(String args) {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                notifyObservers("tick");
                tickCount++;
                timeElapsed += 1 * tickTimeInS;

                if (tickCount % SimulationSettings.getInstance().getTimeUntilSpawn() == 0) {
                    for (int i = 0; i < SimulationSettings.getInstance().getNumberOfCarsToSpawn(); i++) {
                        VehicleFactory.getFactory().createVehicle(VehicleFactory.SPORT_CAR);
                    }
                }
            }
        }, 0, timeBetweenTick
        );

    }

    double timeElapsed = 0;

    public double getTimeElapsed() {
        return timeElapsed;
    }

    /**
     * Returns time in Seconds
     *
     * @return
     */
    public double getTickTimeInS() {
        return this.tickTimeInS;
    }

    /**
     * returns tick time in Millisceonds
     *
     * @return
     */
    public double getTickTimeInMS() {
        return (this.tickTimeInS * 1000);
    }

    /**
     * returns tick time in Nanoseconds
     *
     * @return
     */
    public double getTickTimeInNS() {
        return (getTickTimeInMS() * 1000000);
    }

    /**
     * Sets the TickTimeInS, rounds the value to 4 decimal places.
     *
     * @param x
     */
    public void setTickTimeInS(double x) {
        //rounds the number to 4 decimal places, as the lowes
        double y = (double) Math.round(x * 10000d) / 10000d;

        this.tickTimeInS = y;

    }

    /**
     *
     * @return
     */
    public int getTickCount() {
        return this.tickCount;
    }
    
    public void setTickTimeInMS(double x){
        this.tickTimeInS = x/1000d;
    }
}

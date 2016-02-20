package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.Timer;
import java.util.TimerTask;

public class Ticker extends Observerable {

    /**
     * The amount of time a tick represents in Seconds
     */
    public double tickTimeInS;
    /**
     * The total number of ticks that have been ticked
     */
    public int tickCount;

    public Ticker(double TickTimeInS) {
        setTickTimeInS(TickTimeInS);

    }

    /**
     * use this to start the timer
     *
     * @param args thd message passed to notifyObservers()
     */
    public void start(String args) {
        Timer t = new Timer();
        t.scheduleAtFixedRate(new TimerTask() {

            @Override
            public void run() {
                notifyObservers("tick");

            }
        }, 0, 100
        );

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
}

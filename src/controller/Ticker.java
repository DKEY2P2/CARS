package controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;

public class Ticker extends Observerable {
    
    /**
     * The amount of time a tick represents in Seconds
     */
    public double	      tickTimeInS;
    /**
     * The total number of ticks that have been ticked
     */
    public int		      tickCount;
			      
    private static final long serialVersionUID = 0L;
					       
    public Ticker(double TickTimeInS) {
	setTickTimeInS(TickTimeInS);

    }
    
    public void start(String args) {
	// the timer variable must be a javax.swing.Timer
	new javax.swing.Timer((int) getTickTimeInMS(), new ActionListener() {
	    public void actionPerformed(ActionEvent e) {
		notifyObservers(args);
	    }
	}).start();
	
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
     * 
     * @param x
     */
    public void setTickTimeInS(double x) {
	this.tickTimeInS = x;
	
    }
    
    public int getTickCount() {
	return this.tickCount;
    }
}

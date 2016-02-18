package map;

/**
 * A class made to represent the traffic lights on the road network
 *
 * @author Lucas Vanparijs
 * @since 18/02/2016
 */
public class TrafficLight {

    /**
     * The time in milliseconds it takes for a green light to turn red
     */
    private int timerLength = 20000;
    /**
     * The time in milliseconds left before the light turns red
     */
    private int timeLeft;
    /**
     * True if the light is green, false if it is red
     */
    private boolean green = false;
    /**
     * The maximum amount of vehicles that can go through 1 green light
     */
    private int maxFlow = 20;

    /**
     * Constructor for a traffic light
     */
    public TrafficLight(){
        timeLeft = timerLength;
    }

    /**
     * Returns whether the traffic light is green or not
     * @return true if the light is green, false if it is red.
     */
    public boolean isGreen(){
        return green;
    }

    /**
     * Gets the length of time in milliseconds of how long it takes for the light to switch from green to red
     * @return integer of how many milliseconds it takes to switch from green to red
     */
    public int getTimerLength(){
        return timerLength;
    }

    /**
     * Gets the current time in milliseconds that is left before the light turns red
     * @return integer of how many milliseconds are left before the light turns red
     */
    public int getTimeLeft(){
        return timeLeft;
    }

    /**
     * Gets the maximum amount of cars that can pass in 1 green light
     * @return ^
     */
    public int getMaxFlow(){
        return maxFlow;
    }

    /**
     * Sets the length of time it takes for the light to switch to red.
     * @param n the new length of time in milliseconds
     */
    public void setTimerLength(int n){
        timerLength = n;
    }

    /**
     * Sets the time there is left for the light to switch to red to whatever value the user desires.
     * @param n the new time left in milliseconds
     */
    public void setTimeLeft(int n){
        if(n>timerLength || n<0)
            return;
        else
            timeLeft = n;
    }

    /**
     * Sets the maximum amount of cars to pass in 1 green light to whatever the user desires
     * @param n the new maximum amount of cars to pass in 1 green light
     */
    public void setMaxFlow(int n){
        maxFlow = n;
    }

    /**
     * Sets the color of the light to whatever the user desires
     * @param b green(true), red(false)
     */
    public void setLight(boolean b){
        green = b;
    }
}

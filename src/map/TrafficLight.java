package map;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import vehicle.Vehicle;

/**
 * A class made to represent the traffic lights on the road network
 *
 * @author Lucas Vanparijs, Kareem Horstink
 * @since 27/02/2016
 */
public class TrafficLight {

    /**
     * The time in milliseconds it takes for a green light to turn red
     */
    private int timerLength = 20000;
    /**
     * The time in milliseconds left before the light turns red
     */
    private int timeLeft = 0;
    /**
     * True if the light is green, false if it is red
     */
    private boolean green = false;
    /**
     * The maximum amount of vehicles that can go through 1 green light
     */
    private int maxFlow = 20;

    /**
     * All the roads tied to this road
     */
    private ArrayList<Road> roads = new ArrayList<>();

    /**
     * All the cars waiting
     */
    private Deque<Vehicle> queue = new ArrayDeque<Vehicle>();

    public Deque<Vehicle> getQueue() {
        return queue;
    }


    /**
     * If it contains a road
     *
     * @param r The road to test
     * @return If it is contained in the traffic light
     */
    public boolean contain(Road r) {
        return roads.contains(r);
    }

    /**
     * Get the value of roads
     *
     * @return the value of roads
     */
    public ArrayList<Road> getRoads() {
        return roads;
    }

    /**
     * Constructor for a traffic light
     *
     * @param initial The initial road
     */
    public TrafficLight(Road initial) {
        timeLeft = timerLength;
        roads.add(initial);
    }

    /**
     * Adds a road to this traffic light
     *
     * @param r The road
     */
    public void addRoad(Road r) {
        roads.add(r);
    }

    /**
     * Returns whether the traffic light is green or not
     *
     * @return true if the light is green, false if it is red.
     */
    public boolean isGreen() {
        return green;
    }

    /**
     * Gets the length of time in milliseconds of how long it takes for the
     * light to switch from green to red
     *
     * @return integer of how many milliseconds it takes to switch from green to
     * red
     */
    public int getTimerLength() {
        return timerLength;
    }

    /**
     * Gets the current time in milliseconds that is left before the light turns
     * red
     *
     * @return integer of how many milliseconds are left before the light turns
     * red
     */
    public int getTimeLeft() {
        return timeLeft;
    }

    /**
     * Gets the maximum amount of cars that can pass in 1 green light
     *
     * @return ^
     */
    public int getMaxFlow() {
        return maxFlow * roads.size();
    }

    /**
     * Sets the length of time it takes for the light to switch to red.
     *
     * @param n the new length of time in milliseconds
     */
    public void setTimerLength(int n) {
        timerLength = n;
    }

    /**
     * Sets the time there is left for the light to switch to red to whatever
     * value the user desires.
     *
     * @param n the new time left in milliseconds
     */
    public void setTimeLeft(int n) {
        if (n > timerLength || n < 0) {
            return;
        } else {
            timeLeft = n;
        }
    }

    /**
     * Sets the maximum amount of cars to pass in 1 green light to whatever the
     * user desires
     *
     * @param n the new maximum amount of cars to pass in 1 green light
     */
    public void setMaxFlow(int n) {
        maxFlow = n;
    }

    /**
     * Sets the color of the light to whatever the user desires
     *
     * @param b green(true), red(false)
     */
    public void setLight(boolean b) {
        green = b;
    }
}

package map;

import vehicle.Vehicle;

import java.util.ArrayList;
import java.util.PriorityQueue;

/**
 * This class represents a road
 * A road is similar to an edge of a graph, they are also directed
 *
 * @author Lucas Vanparijs
 * @since 18-02-16
 */

public abstract class Road {

    /**
     * The speed limit on the road
     */
    private double speedLimit;
    /**
     * The length of the road
     */
    private final double LENGTH;
    /**
     * The starting intersection of the road
     */
    private Intersection start;
    /**
     * The ending intersection of the road
     */
    private Intersection end;
    /**
     * A parallel road, wee limit ourselves to 2 lane roads
     */
    private Road parallel;
    /**
     * Start and end intersection wrapped in an ArrayList
     */
    private ArrayList<Intersection> se = new ArrayList<Intersection>();
    /**
     * A queue of all the vehicles on this road
     */
    private PriorityQueue<Vehicle> qv = new PriorityQueue<Vehicle>();

    /**
     * The constructor of the Road class
     * @param start the starting intersection
     * @param end the ending intersection
     * @param length the length of the road/THIS WILL NEED TO BE CALCULATED AUTOMATICALLY
     */
    public Road(Intersection start, Intersection end, double length){
        this.start = start;
        this.end = end;
        se.add(start);
        se.add(end);
        LENGTH = length;
    }

    /**
     * Returns the 2 Intersection of the road
     * @return
     */
    public ArrayList<Intersection> getAdjacent(){
        return se;
    }

    /**
     * Returns the speed limit on this road
     * @return the Speed limit
     */
    public double getSpeedLimit(){
        return speedLimit;
    }

    /**
     * Returns the length of the road
     * @return LENGTH
     */
    public double getLength(){
        return LENGTH;
    }

    /**
     * Returns the starting Intersection of the Road
     * @return The starting Intersection
     */
    public Intersection getStart(){
        return start;
    }

    /**
     * Returns the ending Intersection of the Road
     * @return The ending Intersection
     */
    public Intersection getEnd(){
        return end;
    }

    /**
     * Returns all the vehicles on the road
     * @return a queue of all the vehicles on the road
     */
    public PriorityQueue<Vehicle> getVehicles(){
        return qv;
    }

    /**
     * Sets the road parallel to this one to the parameter
     * @param r the road to be parallel, WE CAN MAKE THIS GO AUTOMATICALLY
     * @return the new parallel road
     */
    public Road setParallel(Road r){
        parallel = r;
        return r;
    }

    /**
     * Returns the road parallel to this one, returns null if there is none
     * @return the parallel road
     */
    public Road getParallel(){
        return parallel;
    }

    /**
     * Sets the speed limit on the road
     * @param n the speed limit
     */
    public void setSpeedLimit(int n){
        speedLimit = n;
    }
}

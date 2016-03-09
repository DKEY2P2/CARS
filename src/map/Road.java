package map;

import helper.Logger;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.util.ArrayDeque;
import vehicle.Vehicle;

import java.util.ArrayList;
import java.util.Deque;
import java.util.PriorityQueue;
import ui.Drawable;

/**
 * This class represents a road A road is similar to an edge of a graph, they
 * are also directed
 *
 * @author Lucas Vanparijs
 * @since 18-02-16
 */
public abstract class Road implements Drawable {

    /**
     * The speed limit on the road in m/s
     */
    private double speedLimit;
    /**
     * The length of the road in meters
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
     * This is the friction coefficient for tarmac road in dry conditions
     */
    private double frictionCoefficient = 0.72;

    /**
     * Earths gravity in m/s2
     */
    private double gravityConstant = 9.80665;
    /**
     * Start and end intersection wrapped in an ArrayList
     */
    private ArrayList<Intersection> se = new ArrayList<Intersection>();
    /**
     * A queue of all the vehicles on this road
     */
    private Deque<Vehicle> qv = new ArrayDeque<Vehicle>();

    /**
     * The constructor of the Road class
     *
     * @param start the starting intersection
     * @param end the ending intersection
     * @param length the length of the road/THIS WILL NEED TO BE CALCULATED
     * AUTOMATICALLY
     */
    public Road(Intersection start, Intersection end, double length) {
        if (start == end) {
            Logger.LogError("Start and end point are the same", this);
            throw new IllegalArgumentException("Start and end points are the same");
        }
        this.start = start;
        this.end = end;
        se.add(start);
        se.add(end);
        end.addRoad(this);
        start.addRoad(this);
        LENGTH = length;
    }

    /**
     * Returns the 2 Intersection of the road
     *
     * @return
     */
    public ArrayList<Intersection> getAdjacent() {
        return se;
    }

    /**
     * Returns the speed limit on this road
     *
     * @return the Speed limit
     */
    public double getSpeedLimit() {
        return speedLimit;
    }

    /**
     * Returns the length of the road
     *
     * @return LENGTH
     */
    public double getLength() {
        return LENGTH;
    }

    /**
     * Returns the starting Intersection of the Road
     *
     * @return The starting Intersection
     */
    public Intersection getStart() {
        return start;
    }

    /**
     * Returns the ending Intersection of the Road
     *
     * @return The ending Intersection
     */
    public Intersection getEnd() {
        return end;
    }

    /**
     * Returns all the vehicles on the road
     *
     * @return a queue of all the vehicles on the road
     */
    public Deque<Vehicle> getVehicles() {
        return qv;
    }

    /**
     * Sets the road parallel to this one to the parameter
     *
     * @param r the road to be parallel, WE CAN MAKE THIS GO AUTOMATICALLY
     * @return the new parallel road
     */
    public Road setParallel(Road r) {
        parallel = r;
        return r;
    }

    /**
     * Returns the road parallel to this one, returns null if there is none
     *
     * @return the parallel road
     */
    public Road getParallel() {
        return parallel;
    }

    /**
     * Sets the speed limit on the road
     *
     * @param n the speed limit
     */
    public void setSpeedLimit(double n) {
        speedLimit = n;
    }

    @Override
    public void draw(Graphics g) {
        int startX = start.getX();
        int startY = start.getY();
        int endY = end.getY();
        int endX = end.getX();
        g.setColor(Color.BLACK);
        ((Graphics2D) g).setStroke(new BasicStroke(10));
        g.drawLine(startX, startY, endX, endY);
    }

    public double getFrictionCoefficient() {
        return frictionCoefficient;
    }

    public void setFrictionCoefficient(double frictionCoefficient) {
        this.frictionCoefficient = frictionCoefficient;
    }

    public double getGravityConstant() {
        return gravityConstant;
    }

    public void setGravityConstant(double gravityConstant) {
        this.gravityConstant = gravityConstant;
    }

}

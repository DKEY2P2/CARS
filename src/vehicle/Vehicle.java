package vehicle;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;

import controller.Task;
import java.awt.Graphics;
import java.awt.Rectangle;
import map.Intersection;
import map.Road;
import ui.Drawable;

/**
 * The abstract class for all vehicles and identities present in the simulation
 *
 * @author jvacek
 */
public abstract class Vehicle implements Task, Drawable {

    /**
     * The current value of the vehicle's acceleration km/h
     */
    private double speed;
    /**
     * The current value of the vehicle's acceleration in m/s^2
     */
    private double acceleration;
    /**
     * The place the vehicle wants to get to
     */
    private SimpleImmutableEntry<Road, Double> destination;
    /**
     * The starting point of the vehicle
     */
    private SimpleImmutableEntry<Road, Double> start;
    /**
     * The time since the vehicle has left the start point
     */
    private int timeOnRoad;
    /**
     * The current position of the vehicle on a road
     */
    private SimpleImmutableEntry<Road, Double> position;
    /**
     * The speed the vehicle driver would like to achieve
     */
    private double desiredSpeed;
    /**
     * The amount of distance travelled during the journey
     */
    private double distance;
    /**
     * Keeps track of all the intersections that a vehicle has passed while on
     * road
     */
    private ArrayList<Intersection> traceLog;

    /*
     * 
     * Constructor
     * 
     */
    /**
     * The default constructor for the Vehicle abstract class
     */
    public Vehicle() {
        setSpeed(0);
        setAcceleration(0);
        setDestination(null);
        setStart(null);
        setTimeOnRoad(0);
        setPosition(getStart());
        setDesiredSpeed(0);
        setDistance(0);
    }

    /*
     * 
     * Update function
     * 
     */
    public abstract boolean update();

    /*
     * 
     * Fun stuff
     * 
     */
    /**
     * Adds an intersection to the trace log of the journey
     *
     * @param e the traversed Intersection
     */
    public void addToTraceLog(Intersection e) {
        this.traceLog.add(e);
    }

    /**
     * Increases the amount of how much distance a vehicle has travelled by the
     * amount x
     *
     * @param x the amount to increase by (negative for decrease)
     */
    public void plusDistance(double x) {
        this.distance += x;
    }

    /**
     * Increases the amount of how much time a vehicle has spent on the road by
     * the amount x
     *
     * @param x
     */
    public void plusTimeOnRoad(double x) {
        this.timeOnRoad += x;
    }

    /*
     * 
     * Getters
     * 
     */
    /**
     * @return
     */
    public double getSpeed() {
        return this.speed;
    }

    /**
     * @return
     */
    public double getAcceleration() {
        return this.acceleration;
    }

    /**
     * @return
     */
    public SimpleImmutableEntry<Road, Double> getDestination() {
        return this.destination;
    }

    /**
     * @return
     */
    public SimpleImmutableEntry<Road, Double> getStart() {
        return this.start;
    }

    /**
     * @return
     */
    public int getTimeOnRoad() {
        return this.timeOnRoad;
    }

    /**
     * @return
     */
    public SimpleImmutableEntry<Road, Double> getPosition() {
        return this.position;
    }

    /**
     * @return
     */
    public double getDesiredSpeed() {
        return this.desiredSpeed;
    }

    /**
     * @return
     */
    public double getDistance() {
        return this.distance;
    }

    /**
     * @return
     */
    public ArrayList<Intersection> getTraceLog() {
        return this.traceLog;
    }

    /*
     * 
     * Setters
     * 
     */
    /**
     * @param speed
     */
    public void setSpeed(double speed) {
        this.speed = speed;
    }

    /**
     * @param acceleration
     */
    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    /**
     * @param destination
     */
    public void setDestination(SimpleImmutableEntry<Road, Double> destination) {
        this.destination = destination;
    }

    /**
     * @param start
     */
    public void setStart(SimpleImmutableEntry<Road, Double> start) {
        this.start = start;
    }

    /**
     * @param timeOnRoad
     */
    public void setTimeOnRoad(int timeOnRoad) {
        this.timeOnRoad = timeOnRoad;
    }

    /**
     * @param position
     */
    public void setPosition(SimpleImmutableEntry<Road, Double> position) {
        this.position = position;
    }

    /**
     * @param desiredSpeed
     */
    public void setDesiredSpeed(double desiredSpeed) {
        this.desiredSpeed = desiredSpeed;
    }

    /**
     * @param distance
     */
    public void setDistance(double distance) {
        this.distance = distance;
    }

    /**
     * @param traceLog
     */
    public void setTraceLog(ArrayList<Intersection> traceLog) {
        this.traceLog = traceLog;
    }

    @Override
    public void draw(Graphics g) {
        SimpleImmutableEntry<Road, Double> position = getPosition();
        Road road = position.getKey();
        double percentage = position.getValue();
        Intersection start = road.getStart();
        Intersection end = road.getEnd();
        int diameter = 8;
        Rectangle r = new Rectangle(diameter / 2, diameter / 2);
        //Waiting for Lucas
        int differentX = 0; // currently 0
        int differentY = 0; // currently 0 
        r.setLocation((int) (differentX * percentage), (int) (differentY * percentage));

    }

}

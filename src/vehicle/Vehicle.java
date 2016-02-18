package vehicle;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;

import map.Intersection;
import map.Road;

/**
 * The abstract class for all vehicles and identities present in the simulation
 * 
 * @author jvacek
 */
public abstract class Vehicle {
    /**
     * The current value of the vehicle's acceleration km/h
     */
    private double			       speed;
    /**
     * The current value of the vehicle's acceleration in km/
     */
    private double			       acceleration;
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
    private int				       timeOnRoad;
    /**
     * The current position of the vehicle on a road
     */
    private SimpleImmutableEntry<Road, Double> position;
    /**
     * The speed the vehicle driver would like to achieve
     */
    private double			       desiredSpeed;
    /**
     * The amount of distance travelled during the journey
     */
    private double			       distance;
    /**
     * Keeps track of all the intersections that a vehicle has passed while on
     * road
     */
    private ArrayList<Intersection>	       traceLog;

    /*
     * 
     * Update function
     * 
     */
    
    public abstract boolean update();
    
    /*
     * 
     * Getters
     * 
     */
    
    public double getSpeed() {
	return this.speed;
    }
    
    public double getAcceleration() {
	return this.acceleration;
    }
    
    public SimpleImmutableEntry<Road, Double> getDestination() {
	return this.destination;
    }
    
    public SimpleImmutableEntry<Road, Double> getStart() {
	return this.start;
    }
    
    public int getTimeOnRoad() {
	return this.timeOnRoad;
    }
    
    public SimpleImmutableEntry<Road, Double> getPosition() {
	return this.position;
    }
    
    public double getDesiredSpeed() {
	return this.desiredSpeed;
    }
    
    public double getDistance() {
	return this.distance;
    }
    
    public ArrayList<Intersection> getTraceLog() {
	return this.traceLog;
    }
    
    
    /*
     * 
     * Setters
     * 
     */
    
    public void setSpeed(double speed) {
	this.speed = speed;
    }
    
    public void setAcceleration(double acceleration) {
	this.acceleration = acceleration;
    }
    
    public void setDestination(SimpleImmutableEntry<Road, Double> destination) {
	this.destination = destination;
    }
    
    public void setStart(SimpleImmutableEntry<Road, Double> start) {
	this.start = start;
    }
    
    public void setTimeOnRoad(int timeOnRoad) {
	this.timeOnRoad = timeOnRoad;
    }
    
    public void setPosition(SimpleImmutableEntry<Road, Double> position) {
	this.position = position;
    }
    
    public void setDesiredSpeed(double desiredSpeed) {
	this.desiredSpeed = desiredSpeed;
    }
    
    public void setDistance(double distance) {
	this.distance = distance;
    }
    
    public void setTraceLog(ArrayList<Intersection> traceLog) {
	this.traceLog = traceLog;
    }
    
}

package vehicle;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;

import algorithms.Algorithm;
import controller.Task;
import java.awt.Graphics;
import java.util.Objects;
import map.Intersection;
import map.Road;
import models.Model;
import ui.Drawable;
import ui.ImageMap;

/**
 * The abstract class for all vehicles and identities present in the simulation
 *
 * @author jvacek, Kareem
 */
public abstract class Vehicle implements Task, Drawable {

    /**
     * The index counter of all the cars
     */
    private static int indexCounter = 0;
    /**
     * The index of the current car.
     * <p>
     * Also should be used as the key in vehicleHolder
     */
    private int index;

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
    private SimpleImmutableEntry<Intersection, Double> destination;
    /**
     * The starting point of the vehicle
     */
    private SimpleImmutableEntry<Intersection, Double> start;
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

    private String imageName = "Lambo";

    /**
     * Get the value of imageName
     *
     * @return the value of imageName
     */
    public String getImageName() {
        return imageName;
    }

    /**
     * Set the value of imageName
     *
     * @param imageName new value of imageName
     */
    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
    /**
     * Length of car in m
     */
    private int length = 0;

    /**
     * Get the value of length
     *
     * @return the value of length
     */
    public int getLength() {
        return length;
    }

    /**
     * Set the value of length
     *
     * @param length new value of length
     */
    public void setLength(int length) {
        this.length = length;
    }

    /**
     * Reaction time in seconds
     */
    private double reactionTime = 0;

    /**
     * Get the value of reactionTime
     *
     * @return the value of reactionTime
     */
    public double getReactionTime() {
        return reactionTime;
    }

    private double maxDecceleration = 0;

    /**
     * Get the value of maxDecceleration
     *
     * @return the value of maxDecceleration
     */
    public double getMaxDecceleration() {
        return maxDecceleration;
    }

    /**
     * Set the value of maxDecceleration
     *
     * @param maxDecceleration new value of maxDecceleration
     */
    public void setMaxDecceleration(double maxDeceleration) {
        this.maxDecceleration = maxDeceleration;
    }

    private double maxAcceleration = 0;

    /**
     * Get the value of maxAcceleration
     *
     * @return the value of maxAcceleration
     */
    public double getMaxAcceleration() {
        return maxAcceleration;
    }

    /**
     * Set the value of maxAcceleration
     *
     * @param maxAcceleration new value of maxAcceleration
     */
    public void setMaxAcceleration(double maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    /**
     * Set the value of reactionTime
     *
     * @param reactionTime new value of reactionTime
     */
    public void setReactionTime(double reactionTime) {
        this.reactionTime = reactionTime;
    }


    /*
     * 
     * Constructor
     * 
     */
    /**
     * The default constructor for the Vehicle abstract class
     *
     * @param start
     * @param percentage
     * @param m
     * @param a
     */
    public Vehicle(Road start, double percentage, Model m, Algorithm a) { //I change this - Kareem
        setSpeed(0);
        setAcceleration(0);
        setDestination(null); // This should be different I think
        setStart(new SimpleImmutableEntry<Road, Double>(start, percentage)); //I change this - Kareem
        start.getVehicles().offer(this); //I did this - Kareem
        setTimeOnRoad(0);
        setPosition(getStart());
        setDesiredSpeed(0);
        setDistance(0);
        setLength(0);
        index = indexCounter++;
        VehicleHolder.getInstance().add(this);
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

    /**
     * Get the value of index
     *
     * @return the value of index
     */
    public int getIndex() {
        return index;
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
        int differentX = end.getX() - start.getX();
        int differentY = end.getY() - start.getY();
        double angle = Math.tan(differentY / differentX);

        g.drawImage(ImageMap.getInstance().getImage(getImageName(), angle, 0.1),
                (int) (start.getX() + differentX * percentage),
                (int) (start.getY() + differentY * percentage), null);
    }

    /*
     Some stuff for hashing and to see if it is equal. Auto genrated xD
     */
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.speed) ^ (Double.doubleToLongBits(this.speed) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.acceleration) ^ (Double.doubleToLongBits(this.acceleration) >>> 32));
        hash = 41 * hash + Objects.hashCode(this.destination);
        hash = 41 * hash + Objects.hashCode(this.start);
        hash = 41 * hash + this.timeOnRoad;
        hash = 41 * hash + Objects.hashCode(this.position);
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.desiredSpeed) ^ (Double.doubleToLongBits(this.desiredSpeed) >>> 32));
        hash = 41 * hash + (int) (Double.doubleToLongBits(this.distance) ^ (Double.doubleToLongBits(this.distance) >>> 32));
        hash = 41 * hash + Objects.hashCode(this.traceLog);
        hash = 41 * hash + Objects.hashCode(this.imageName);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Vehicle other = (Vehicle) obj;
        if (Double.doubleToLongBits(this.speed) != Double.doubleToLongBits(other.speed)) {
            return false;
        }
        if (Double.doubleToLongBits(this.acceleration) != Double.doubleToLongBits(other.acceleration)) {
            return false;
        }
        if (!Objects.equals(this.destination, other.destination)) {
            return false;
        }
        if (!Objects.equals(this.start, other.start)) {
            return false;
        }
        if (this.timeOnRoad != other.timeOnRoad) {
            return false;
        }
        if (!Objects.equals(this.position, other.position)) {
            return false;
        }
        if (Double.doubleToLongBits(this.desiredSpeed) != Double.doubleToLongBits(other.desiredSpeed)) {
            return false;
        }
        if (Double.doubleToLongBits(this.distance) != Double.doubleToLongBits(other.distance)) {
            return false;
        }
        if (!Objects.equals(this.traceLog, other.traceLog)) {
            return false;
        }
        return Objects.equals(this.imageName, other.imageName);
    }

}

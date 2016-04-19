package vehicle;

import algorithms.Algorithm;
import controller.Controller;
import controller.Task;
import helper.Logger;
import map.Intersection;
import map.Road;
import map.TrafficLight;
import models.Model;
import ui.Drawable;
import ui.helper.TwoDTransformation;
import ui.setting.GraphicsSetting;

import java.awt.*;
import java.util.*;
import java.util.AbstractMap.SimpleImmutableEntry;

/**
 * The abstract class for all vehicles and identities present in the simulation
 *
 * @author jvacek, Kareem
 */
public abstract class Vehicle implements Task, Drawable, Comparable<Vehicle> {

    /**
     * The default constructor for the Vehicle abstract class
     *
     * @param start The starting location
     * @param percentage The percentage of the road it has currently completed
     * @param m The model to use
     * @param a The algorithm that the car should use for path finding
     */
    protected Vehicle(Road start, double percentage, Model m, Algorithm a) {
        if (m == null || start == null || a == null || percentage > 1 || percentage < 0) {
            throw new IllegalArgumentException("Parameters are not correct");
        }
        setSpeed(0);
        setAcceleration(0);
        setDestination(null);
        setStart(start.getStart());
        setTimeOnRoad(0);
        setPosition(new SimpleImmutableEntry<>(start, percentage));
        setDesiredSpeed(0);
        setDistance(0);
        setLength(0);
        setModel(m);
        this.a = a;
        index = indexCounter++;
        VehicleHolder.getInstance().add(this);
        start.getVehicles().offer(this);
    }

    @Override
    public void draw(Graphics g) {
        if (!VehicleHolder.getInstance().contains(this)) {
            return;
        }

        g.setColor(color);
        SimpleImmutableEntry<Road, Double> position = this.position;
        Road road = position.getKey();
        double percentage = position.getValue();
        if (road == null) {
            Logger.LogError("No road given", this);
            return;
        }
        Intersection start = road.getStart();
        Intersection end = road.getEnd();
        int width = (int) getLength();
        int height = (int) getLength();
        double zoom = GraphicsSetting.getInstance().getZoom();
        int differentX = end.getX() - start.getX();
        int differentY = end.getY() - start.getY();
        int x = (int) (((int) (start.getX() + differentX * percentage)) - width * zoom / 2);
        int y = (int) (((int) (start.getY() + differentY * percentage)) - height * zoom / 2);
        g.drawOval(
                x,
                y,
                (int) (width * zoom), (int) (height * zoom)
        );
    }

    public double getBreakingDistance() {
        Road r = getPosition().getKey();
        return Math.pow(speed, 2) / (2 * r.getFrictionCoefficient() * r.getGravityConstant());
    }

    /**
     * Get the value of imageName
     *
     * @return the value of imageName
     */
    public final String getImageName() {
        return imageName;
    }

    /**
     * Set the value of imageName
     *
     * @param imageName new value of imageName
     */
    public final void setImageName(String imageName) {
        this.imageName = imageName;
    }

    /**
     * Get the value of length
     *
     * @return the value of length
     */
    public final double getLength() {
        return length;
    }

    /**
     * Set the value of length
     *
     * @param length new value of length
     */
    public final void setLength(double length) {
        this.length = length;
    }

    /**
     * Get the value of reactionTime
     *
     * @return the value of reactionTime
     */
    public final double getReactionTime() {
        return reactionTime;
    }

    /**
     * Get the value of maxDecceleration
     *
     * @return the value of maxDecceleration
     */
    public final double getMaxDecceleration() {
        return maxDecceleration;
    }

    /**
     * Set the value of maxDecceleration
     *
     * @param maxDeceleration new value of maxDecceleration
     */
    public final void setMaxDecceleration(double maxDeceleration) {
        this.maxDecceleration = maxDeceleration;
    }

    /**
     * Get the value of maxAcceleration
     *
     * @return the value of maxAcceleration
     */
    public final double getMaxAcceleration() {
        return maxAcceleration;
    }

    /**
     * Set the value of maxAcceleration
     *
     * @param maxAcceleration new value of maxAcceleration
     */
    public final void setMaxAcceleration(double maxAcceleration) {
        this.maxAcceleration = maxAcceleration;
    }

    /**
     * Set the value of reactionTime
     *
     * @param reactionTime new value of reactionTime
     */
    public final void setReactionTime(double reactionTime) {
        this.reactionTime = reactionTime;
    }

    /**
     * Get the value of model
     *
     * @return the value of model
     */
    public final Model getModel() {
        return model;
    }

    /**
     * Set the value of model
     *
     * @param model new value of model
     */
    public final void setModel(Model model) {
        this.model = model;
    }

    public abstract boolean update();

    public final Intersection nextPlaceToGo() {
        Intersection i = a.findShortestPath(getPosition().getKey().getEnd(), destination).get(1);
        if (i == destination) {
            return destination;
        } else {
            return i;
        }

    }

    public final ArrayList<Intersection> getRoute() {
        return a.findShortestPath(getPosition().getKey().getEnd(), destination);
    }

    public final TrafficLight getNextLight() {
        Road r = getPosition().getKey();
        Intersection next = nextPlaceToGo();
        Road rNext = next.hasRoad(r.getEnd());
        return rNext.getStart().getTrafficLight(r);
    }

    private boolean equalArray(ArrayList<Road> array, Road r) {
        for (Road r1 : array) {
            if (r1 == r) {
                return true;
            }
        }
        return false;
    }

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

    public double getSpeed() {
        return this.speed;
    }

    public double getAcceleration() {
        return this.acceleration;
    }

    public double getDesiredDeceleration() {
        return this.desiredDeceleration;
    }

    public Intersection getDestination() {
        return this.destination;
    }

    public Intersection getStart() {
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

    public int getIndex() {
        return index;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setAcceleration(double acceleration) {
        this.acceleration = acceleration;
    }

    public void setDesiredDeceleration(double desiredDeceleration) {
        this.desiredDeceleration = desiredDeceleration;
    }

    public void setDestination(Intersection destination) {
        this.destination = destination;
    }

    public void setStart(Intersection start) {
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

    public void addPercentage(double d) {
        position.setValue(position.getValue() + d);
    }

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
     * The Vehicle's desired Deceleration/desired Braking speed in m/s^2
     */
    private double desiredDeceleration;
    /**
     * The place the vehicle wants to get to
     */
    private Intersection destination;
    /**
     * The starting point of the vehicle
     */
    private Intersection start;
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
    private ArrayList<Intersection> traceLog = new ArrayList<>();

    /**
     * What to use as the image
     */
    private String imageName = "Lambo";

    /**
     * Length of car in m
     */
    private double length = 0;

    /**
     * Reaction time in seconds
     */
    private double reactionTime = 0;

    private double maxDecceleration = 0;

    private double maxAcceleration = 0;

    private Model model;

    private Algorithm a;

    private Color color = Color.orange;

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

    @Override
    public int compareTo(Vehicle o) {
        Vehicle v;
        if (o instanceof Vehicle) {
            v = (Vehicle) o;
        } else {
            return 1;
        }
        if (getPosition().getKey() == v.getPosition().getKey()) {
            if (getPosition().getValue() > v.getPosition().getValue()) {
                return -1;
            } else if (getPosition().getValue() == v.getPosition().getValue()) {
                return 0;
            } else {
                return 1;
            }
        } else {
            return 1;
        }

    }

    public Vehicle getPredecessor() {
        Vehicle inFrontVehicle = null;
        Road r = getPosition().getKey();

        PriorityQueue<Vehicle> pq = r.getVehicles();
        Iterator<Vehicle> iv = pq.iterator();

        while(iv.hasNext()){
            Vehicle c = iv.next();
            if(c == this){
                return inFrontVehicle;
            }else{
                inFrontVehicle = c;
            }
        }

        return inFrontVehicle;
    }

    public void updateAll(double v, double acc,Road r){
        double t = Controller.getInstance().getTicker().getTickTimeInS();

        this.setAcceleration(acc);
        if(v < 0)
            this.setSpeed(0);
        else
            this.setSpeed(v);
        System.out.println(this.getSpeed());

        double p = this.getPosition().getValue() + ((this.getSpeed() * t / r.getLength())); //new position
        if(p>=1d){
            r.getVehicles().remove(this);
            if(this.getDestination() == r.getEnd()){
                VehicleHolder.getInstance().remove(this);
            }else{
                Road rn = this.nextPlaceToGo().hasRoad(r.getEnd());
                if(rn == r){
                    VehicleHolder.getInstance().remove(this);
                }else {
                    this.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(rn, 0d));
                    this.getPosition().getKey().addCar(this);
                    Vehicle pre = getPredecessor();
                    if (pre != null) {
                        setSpeed(pre.getSpeed());
                    }
                }
            }
        }else{
            this.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(r, p));
        }
    }

    /*@Override
     public String toString() {
     return "Index : " + index + " Speed: " + speed + "\n  Acceleration : " + acceleration + " Road : "
     + getPosition().getKey() + " Percentage " + position.getValue() + " Deceleration : "
     + getDesiredDeceleration();
     }*/
    @Override
    public String toString() {
        return "%" + position.getValue();
    }

}

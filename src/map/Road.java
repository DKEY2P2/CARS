package map;

import ui.Drawable;
import ui.setting.GraphicsSetting;
import vehicle.Vehicle;

import java.awt.*;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;
import java.util.PriorityQueue;
import ui.helper.TwoDTransformation;

/**
 * This class represents a road A road is similar to an edge of a graph, they
 * are also directed
 *
 * @author Lucas Vanparijs
 * @since 18-02-16
 */
public abstract class Road implements Drawable {

    /**
     * The length of a road
     */
    private double width = 3.25;

    /**
     * Get the value of width in meters
     *
     * @return the value of width in meters
     */
    public double getWidth() {
        return width;
    }

    /**
     * Set the value of width in meters
     *
     * @param width new value of width in meters
     */
    public void setWidth(double width) {
        this.width = width;
    }
    
    public TrafficLight getTrafficlight(){
    	return end.getTrafficLight(this);
    }

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

    private Comparator<Vehicle> cv = new VehicleComparator();
    /**
     * A queue of all the vehicles on this road
     */
    private PriorityQueue<Vehicle> pq = new PriorityQueue<Vehicle>(10, cv);

    /**
     * The constructor of the Road class
     *
     * @param start the starting intersection
     * @param end the ending intersection
     * @param length the length of the road
     *
     */
    public Road(Intersection start, Intersection end, double length) {
        if (start == end) {
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
    public PriorityQueue<Vehicle> getVehicles() {
        return pq;
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
        double zoom = GraphicsSetting.getInstance().getZoom();

        g.setColor(Color.WHITE);

        ((Graphics2D) g).setStroke(
                new BasicStroke((float) (getWidth() * zoom > 0 ? getWidth() * zoom : 1)));

        AbstractMap.SimpleImmutableEntry<Integer, Integer> tmpX = new AbstractMap.SimpleImmutableEntry<>(start.getX(), end.getX());
        AbstractMap.SimpleImmutableEntry<Integer, Integer> tmpY = new AbstractMap.SimpleImmutableEntry<>(start.getY(), end.getY());
        g.drawLine(tmpX.getKey(), tmpY.getKey(), tmpX.getValue(), tmpY.getValue());

        g.setColor(Color.BLACK);

        ((Graphics2D) g).setStroke(
                new BasicStroke((float) (getWidth() * 0.7 * zoom > 0 ? getWidth() * 0.7 * zoom : 1)));

        g.drawLine(tmpX.getKey(), tmpY.getKey(), tmpX.getValue(), tmpY.getValue());

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

class VehicleComparator implements Comparator<Vehicle> {

    public VehicleComparator() {
    }

    @Override
    public int compare(Vehicle o1, Vehicle o2) {
        if (o1.getPosition().getValue() < o2.getPosition().getValue()) {
            return 1;
        } else if (Objects.equals(o1.getPosition().getValue(), o2.getPosition().getValue())) {
            return 0;
        } else {
            return -1;
        }
    }
}

package map;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import vehicle.Vehicle;
import java.util.ArrayList;
import java.util.PriorityQueue;
import ui.Drawable;
import ui.ImageMap;

/**
 * The abstract class for all types of intersections present in the simulation
 * An intersection is similar to a Node/Vertex of a graph
 *
 * @author Lucas Vanparijs
 * @since 18-02-16
 */

public abstract class Intersection implements Drawable{

    /**
     * The x position of an intersection
     */
    private int x;
    /**
     * The y position of an intersection
     */
    private int y;
    /**
     * This is in an ArrayList that is parallel to the roads that have a certain traffic light,
     * with the index of the road and the corresponding traffic light being the same
     */
    private ArrayList<TrafficLight> tLights = new ArrayList<TrafficLight>();
    /**
     * All the roads that are connected to this intersection
     */
    private ArrayList<Road> roads = new ArrayList<Road>();

    /**
     * The constructor of the abstract class
     * @param x the x coordinate of the intersection
     * @param y the y coordinate of the intersection
     */
    public Intersection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds a road to the intersection
     * @param r the road to be added
     * @return the added road
     */
    public Road addRoad(Road r) {
        roads.add(r);
        tLights.add(new TrafficLight());
        return r;
    }

    /**
     * Returns all the roads connected to the intersection
     * @return all the connected roads
     */
    public ArrayList<Road> getRoads() {
        return roads;
    }

    /**
     * Removes a road from the intersection
     * @param r the road to be removed
     * @return the removed road
     */
    public Road removeRoad(Road r) {
        int i = roads.indexOf(r);
        roads.remove(r);
        tLights.remove(i);
        return r;
    }

    /**
     * Returns whether the light of a certain road connected to the intersection is green(true) or not(false)
     * @param r The road of which to check the light from
     * @return True if the light is green, false if it is red
     */
    public boolean isGreen(Road r) {
        int i = roads.indexOf(r);
        return tLights.get(i).isGreen();
    }

    /**
     * Returns all the vehicles that are on a certain road
     * @param r the road to return the vehicles from
     * @return A Queue of all vehicles that are on the specified road
     */
    public PriorityQueue<Vehicle> getQueue(Road r) {
        return r.getVehicles();
    }

    /**
     * Returns the X coordinate of the intersection
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the intersection
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Set the value of the x coordinate
     * @param n the new value of the x coordinate
     */
    public void setX(int n) {
        x = n;
    }

    /**
     * Set the value of the y coordinate
     * @param n the new value of the y coordinate
     */
    public void setY(int n) {
        y = n;
    }

    private String imageKey = "CircleRed";

    /**
     * Get the value of imageKey
     *
     * @return the value of imageKey
     */
    public String getImageKey() {
        return imageKey;
    }

    /**
     * Set the value of imageKey
     *
     * @param ImageKey new value of imageKey
     */
    public void setImageKey(String ImageKey) {
        this.imageKey = ImageKey;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage bi = ImageMap.getInstance().getImage(imageKey);
        g.drawImage(bi, x-bi.getWidth()/2, y-bi.getHeight()/2, null);
    }
}

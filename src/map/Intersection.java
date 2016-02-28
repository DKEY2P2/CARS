package map;

import helper.Logger;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import vehicle.Vehicle;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.PriorityQueue;
import ui.Drawable;
import ui.ImageMap;

/**
 * The abstract class for all types of intersections present in the simulation
 * <p>
 * An intersection is similar to a Node/Vertex of a graph
 *
 * @author Lucas Vanparijs, Kareem Horstink
 * @since 27-02-16
 */
public abstract class Intersection implements Drawable {

    int timerLength = 40000;

    /**
     * The x position of an intersection
     */
    private int x;
    /**
     * The y position of an intersection
     */
    private int y;
    /**
     * This is in an ArrayList that is parallel to the roads that have a certain
     * traffic light, with the index of the road and the corresponding traffic
     * light being the same
     */
    private ArrayList<TrafficLight> tLights = new ArrayList<TrafficLight>();
    /**
     * All the roads that are connected to this intersection
     */
    private ArrayList<Road> roads = new ArrayList<Road>();

    /**
     * Gets the traffic light system
     *
     * @return The traffic light system
     */
    public ArrayList<TrafficLight> getTrafficLights() {
        return tLights;
    }

    /**
     * The constructor of the abstract class
     *
     * @param x the x coordinate of the intersection
     * @param y the y coordinate of the intersection
     */
    public Intersection(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Adds a road to the intersection
     *
     * @param r the road to be added
     * @return the added road
     */
    public Road addRoad(Road r) {
        roads.add(r);
        tLights.add(new TrafficLight(r));
        tLights.get(0).setLight(true);

        return r;
    }

    /**
     *
     * @param periodOfTimeElapsed in ms
     */
    public void updateLights(int periodOfTimeElapsed) {
        int currentlyGreen = -1;
        for (int i = 0; i < tLights.size(); i++) {
            if (tLights.get(i).isGreen()) {
                currentlyGreen = i;
                break;
            }
        }
        if (currentlyGreen == -1) {
            Logger.LogError("Update Light failed, no green lights", this);
            return;
        }
        TrafficLight green = tLights.get(currentlyGreen);
        if (green.getTimeLeft() - periodOfTimeElapsed <= 0) {
            green.setLight(false);
            green.setTimeLeft(green.getTimerLength());
            if (currentlyGreen == tLights.size() - 1) {
                currentlyGreen = 0;
            } else {
                currentlyGreen++;
            }
            tLights.get(currentlyGreen).setLight(true);
        } else {
            green.setTimeLeft(green.getTimeLeft() - periodOfTimeElapsed);
        }
    }

    /**
     * Returns all the roads connected to the intersection
     *
     * @return all the connected roads
     */
    public ArrayList<Road> getRoads() {
        return roads;
    }

    /**
     * Removes a road from the intersection
     *
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
     * Returns whether the light of a certain road connected to the intersection
     * is green(true) or not(false)
     *
     * @param r The road of which to check the light from
     * @return True if the light is green, false if it is red
     */
    public boolean isGreen(Road r) {
        int i = roads.indexOf(r);
        return tLights.get(i).isGreen();
    }

    /**
     * Returns all the vehicles that are on a certain road
     *
     * @param r the road to return the vehicles from
     * @return A Queue of all vehicles that are on the specified road
     */
    public Deque<Vehicle> getQueue(Road r) {
        for (TrafficLight tLight : tLights) {
            if (tLight.contain(r)) {
                return tLight.getQueue();
            }
        }
        return null;
    }

    /**
     * Returns the X coordinate of the intersection
     *
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y coordinate of the intersection
     *
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * Set the value of the x coordinate
     *
     * @param n the new value of the x coordinate
     */
    public void setX(int n) {
        x = n;
    }

    /**
     * Set the value of the y coordinate
     *
     * @param n the new value of the y coordinate
     */
    public void setY(int n) {
        y = n;
    }

    /**
     * Gets the traffic light that is green
     *
     * @return
     */
    public TrafficLight getCurrentlyGreen() {
        for (TrafficLight tLight : tLights) {
            if (tLight.isGreen()) {
                return tLight;
            }
        }
        Logger.LogError("Update Light failed, no green lights", this);
        return null;
    }

    /**
     * What image to draw for this item
     */
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

    /**
     * Returns all the neighbouring intersections <p> Works by my understanding, moved one thing
     *
     * @author jvacek, Kareem
     * @return ArrayList&#60;Intersection&#62; with all the neighbouring
     * intersections
     */
    public ArrayList<Intersection> getNeighbours() {
        ArrayList<Intersection> ret = new ArrayList<>();
        for (Road r : getRoads()) {
            Intersection tmp = null;
            if (r.getEnd() == this) {	//if the end intersection is the same one as "this"...
                tmp = r.getStart();	//... add the OTHER one to the neighbours...
            } else {
                tmp = r.getEnd();	//... or the end one, if it wasn't...
            }
            if ((tmp != null) && !ret.contains(tmp)) {
                ret.add(tmp);		//... but only if it's not on the list already
            }

        }
        return ret;
    }

    @Override
    public void draw(Graphics g) {
        BufferedImage bi = ImageMap.getInstance().getImage(imageKey);
        g.drawImage(bi, x - bi.getWidth() / 2, y - bi.getHeight() / 2, null);
    }

    @Override
    public String toString() {
        return ("Intersection: x= " + getX()+ ", y= " + getY());
    }
    
    
    
    
}

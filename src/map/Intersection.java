package map;

import helper.Logger;
import ui.Drawable;
import ui.setting.GraphicsSetting;
import vehicle.Vehicle;

import java.awt.*;
import java.util.ArrayList;
import java.util.PriorityQueue;
import ui.helper.TwoDTransformation;

/**
 * The abstract class for all types of intersections present in the simulation
 * <p>
 * An intersection is similar to a Node/Vertex of a graph
 *
 * @author Lucas Vanparijs, Kareem Horstink
 * @since 27-02-16
 */
public abstract class Intersection implements Drawable {

    private static int indexs = 0;

    private int index;

    public int getIndex() {
        return index;
    }

    public static final int DIAMETER = 5;

    int timerLength = 4000;

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

    public TrafficLight getTrafficLight(Road r) {
        for (TrafficLight tLight : tLights) {
            if (tLight.getIn() == r) {
                return tLight;
            }
        }
        return null;
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
        index = indexs++;
    }

    /**
     * Adds a road to the intersection
     *
     * @param r the road to be added
     * @return the added road
     */
    public Road addRoad(Road r) {
        roads.add(r);
        return r;
    }

    public TrafficLight addTrafficLight(TrafficLight tl) {
        tLights.add(tl);
        //Checks if at least one light is green
        boolean hasGreen = false;
        for (TrafficLight tLight : tLights) {
            if (tLight.isGreen()) {
                hasGreen = true;
            }
        }
        if (!hasGreen) {
            tLights.get(0).flip();
            tLights.get(0).setTimeLeft(tLights.get(0).getTimerLength());
        }
        return tl;
    }

    public void updateLight(double elapsed) {
        if (tLights.isEmpty()) {
            return;
        }
        //Check which use case to use

        if (tLights.size() > 1) {
            //If more than one light at a traffic light

            //Goes through all the lights
            for (int i = 0; i < tLights.size(); i++) {
                //if the current light is green
                //set the tmp value to index of the the green light
                tLights.get(i).setTimeLeft(tLights.get(i).getTimeLeft() - elapsed);
                if (tLights.get(i).getTimeLeft() <= 0) {
                    tLights.get(i).flip();
                    tLights.get(i).setTimeLeft(tLights.get(i).getTimerLength());
                }

            }
        } else {
            tLights.get(0).setTimeLeft(tLights.get(0).getTimeLeft() - elapsed); //This is just because we will make the lights cycle so they will all change together. THIS WILL CHANGE -Lucas
            if (tLights.get(0).getTimeLeft() <= 0) {
                tLights.get(0).flip();
                tLights.get(0).setTimeLeft(tLights.get(0).getTimerLength());
            }
        }
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
    public PriorityQueue<Vehicle> getQueue(Road r) {
        if (roads.contains(r)) {
            return r.getVehicles();
        } else {
            return null;
        }
    }

    public ArrayList<Road> getIns() {
        ArrayList<Road> ins = new ArrayList<Road>();
        for (Road r : getRoads()) {
            if (r.getEnd() == this) {
                ins.add(r);
            }
        }
        return ins;
    }

    public ArrayList<Road> getOuts() {
        ArrayList<Road> outs = new ArrayList<Road>();
        for (Road r : getRoads()) {
            if (r.getStart() == this) {
                outs.add(r);
            }
        }
        return outs;
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
     * Returns all the neighbouring intersections
     * <p>
     * Works by my understanding, moved one thing
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

    public Road hasRoad(Intersection i) {
        for (Road r1 : getRoads()) {
            for (Road r2 : i.getRoads()) {
                if (r1 == r2) {
                    return r1;
                }
            }
        }
        return null;
    }

    @Override
    public void draw(Graphics g) {
        //BufferedImage bi = ImageMap.getInstance().getImage(imageKey, 0, GraphicsSetting.getInstance().getZoom());
        g.setColor(Color.WHITE);
        double zoom = GraphicsSetting.getInstance().getZoom();
        int tmpX = (int) (((int) (x)) - (DIAMETER * zoom) / 2);
        int tmpY = (int) (((int) (y)) - (DIAMETER * zoom) / 2);

        g.fillOval(tmpX,
                tmpY,
                (int) (DIAMETER * zoom),
                (int) (DIAMETER * zoom));
        int i = 0;
        if (GraphicsSetting.getInstance().isShowTraffficLight()) {
            for (TrafficLight tLight : tLights) {
                tLight.draw(g, i++);
            }
        }
    }

    @Override
    public String toString() {
        return ("Intersection: x= " + getX() + ", y= " + getY() + " ID: " + getIndex());
    }

}

package map;

import ui.helper.TwoDTransformation;
import ui.setting.GraphicsSetting;
import vehicle.Vehicle;

import java.awt.*;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;

/**
 * A class made to represent the traffic lights on the road network
 *
 * @author Lucas Vanparijs, Kareem Horstink
 * @since 07/03/2016
 */
public class TrafficLight {

    private static int indexGlobal = 0;
    /**
     * Index
     */
    private int index;

    /**
     * The time in milliseconds it takes for a green light to turn red
     */
    private int timerLength = 10000;
    /**
     * The time in milliseconds left before the light turns red
     */
    private double timeLeft = 0;
    /**
     * True if the light is green, false if it is red
     */
    private boolean green = false;
    /**
     * The maximum amount of vehicles that can go through 1 green light
     */
    private int maxFlow = 20;

    /**
     * The intersection on which the traffic light stands
     */
    private Intersection i;

    private Road in;

    private ArrayList<Road> out;

    private Deque<Vehicle> waiting = new LinkedList<>();

    public Deque<Vehicle> getWaiting() {
        return waiting;
    }

    /**
     * Gets the max flow
     *
     * @return ^
     */
    public int getMaxFlow() {
        return maxFlow;
    }

    /**
     * Constructor for a traffic light
     *
     * @param i
     * @param in The initial Intersection
     * @param out
     */
    public TrafficLight(Intersection i, Road in, ArrayList<Road> out) {
        index = indexGlobal++;
        timeLeft = timerLength;
        this.i = i;
        this.in = in;
        this.out = out;
//        i.addTrafficLight(this);
    }

    /**
     * Returns whether the traffic light is green or not
     *
     * @return true if the light is green, false if it is red.
     */
    public boolean isGreen() {
        return green;
    }

    /**
     * Gets the length of time in milliseconds of how long it takes for the
     * light to switch from green to red
     *
     * @return integer of how many milliseconds it takes to switch from green to
     * red
     */
    public int getTimerLength() {
        return timerLength;
    }

    /**
     * Gets the current time in milliseconds that is left before the light turns
     * red
     *
     * @return integer of how many milliseconds are left before the light turns
     * red
     */
    public double getTimeLeft() {
        return timeLeft;
    }

    /**
     * Gets the maximum amount of cars that can pass in 1 green light
     *
     * @return ^
     */
    /*public int getMaxFlow() {
     return maxFlow * roads.size();
     }*/
    /**
     * Sets the length of time it takes for the light to switch to red.
     *
     * @param n the new length of time in milliseconds
     */
    public void setTimerLength(int n) {
        timerLength = n;
    }

    /**
     * Sets the time there is left for the light to switch to red to whatever
     * value the user desires.
     *
     * @param n the new time left in milliseconds
     */
    public void setTimeLeft(double n) {
        if (n > timerLength) {
            timerLength = 0;
        } else if (n < 0) {
            return;
        } else {
            timeLeft = n;
        }
    }

    public Intersection getIntersection() {
        return i;
    }

    /**
     * Sets the maximum amount of cars to pass in 1 green light to whatever the
     * user desires
     *
     * @param n the new maximum amount of cars to pass in 1 green light
     */
    public void setMaxFlow(int n) {
        maxFlow = n;
    }

    /**
     * Sets the color of the light to whatever the user desires
     *
     * @param b green(true), red(false)
     */
    public void setLight(boolean b) {
        green = b;
    }

    public void flip() {
        green = !green;
    }

    public Road getIn() {
        return in;
    }

    public ArrayList<Road> getOut() {
        return out;
    }

    public void draw(Graphics g, int i) {
        double zoom = GraphicsSetting.getInstance().getZoom();
        if (isGreen()) {
            g.setColor(Color.green);
        } else {
            g.setColor(Color.red);
        }
        int size = (int) (3 * zoom > 1 ? 3 * zoom : 1);
        int x = TwoDTransformation.transformX(this.i.getX() + i * 5) - size / 2;
        int y = TwoDTransformation.transformY(this.i.getY()) - size / 2;
        g.fillOval(x, y, size, size);
        //g.drawString(Integer.toString(index), this.i.getX() + i * 10, this.i.getY());
    }

    @Override
    public String toString() {
        return "Traffic Light " + index + " is " + (isGreen() ? "green" : "red");
    }

    public void addOut(Road r) {
        out.add(r);
    }
}

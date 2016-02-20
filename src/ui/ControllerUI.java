package ui;

import controller.Controller;
import controller.Observer;
import controller.Ticker;
import helper.Logger;
import java.awt.Color;
import java.awt.Graphics;
import map.Intersection;
import map.Road;
import vehicle.Vehicle;

/**
 * Controls the UI
 *
 * @author Kareem
 */
public class ControllerUI implements Observer {

    private Canvas c;

    /**
     * Creates a canvas
     */
    public ControllerUI(Ticker t) {
        c = new Canvas();
        t.addObserver(this);

    }

    /**
     * Draws everything
     */
    public void draw() {
        drawEverything(c.getGraphic());
        c.getScene();
   
    }

    /**
     * Calls all the other draw function
     *
     * @param g The graphics needed to draw
     */
    public void drawEverything(Graphics g) {
        drawRoad(g);
        drawIntersection(g);
        drawCars(g);
        g.dispose();
    }

    /**
     * Draw the car
     *
     * @param g The graphics needed to draw
     */
    public void drawCars(Graphics g) {
        Controller controller = Controller.getInstance();
        for (Vehicle vehicle : controller.getVehicles()) {
            vehicle.draw(g);
        }
    }

    /**
     * Draws all the road. Might want to make thread safe
     *
     * @param g The graphics needed to draw
     */
    public void drawRoad(Graphics g) {
        Controller controller = Controller.getInstance();
        for (Road road : controller.getMap().getRoads()) {
            road.draw(g);
        }
    }

    /**
     * Draws all the intersection. Might want to make thread safe
     *
     * @param g The graphics needed to draw
     */
    public void drawIntersection(Graphics g) {
        Controller controller = Controller.getInstance();
        for (Intersection intersection : controller.getMap().getIntersections()) {
            intersection.draw(g);
        }
    }

    @Override
    public void update() {
        update("tick?");
    }

    @Override
    public void update(String args) {
        draw();

    }
}

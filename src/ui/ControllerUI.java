package ui;

import ui.frames.Canvas;
import controller.Controller;
import controller.Task;
import java.awt.Graphics;
import javax.swing.JOptionPane;
import map.Intersection;
import map.Road;
import ui.frames.SideBar;
import vehicle.Vehicle;

/**
 * Controls the UI
 *
 * @author Kareem
 */
public class ControllerUI implements Task {

    private Canvas c;
    private SideBar s;

    /**
     * Creates a canvas
     *
     */
    public ControllerUI() {
        c = new Canvas();
        s = new SideBar(c);
        c.toFront();
        update();
        JOptionPane.showMessageDialog(null, "Hold control and click to make a new intersection "
                + "\nHold shift and click to make a new road (click on the start intersection then the end intersection"
                + "\nPress start (on the side bar) to start the timer)");

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
    public boolean update() {
        draw();
        s.repaint();
        return true;
    }
}

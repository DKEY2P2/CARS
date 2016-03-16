package ui;

import controller.Controller;
import controller.Task;
import map.Intersection;
import map.Road;
import ui.frames.Canvas;
import ui.frames.SideBar;
import ui.setting.GraphicsSetting;
import vehicle.Vehicle;

import javax.swing.*;
import java.awt.*;

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
        Graphics g = c.getGraphic();
        drawEverything(g);
        c.getScene();
    }

    /**
     * Draws the scale
     *
     * @param g The graphics needed to draw
     */
    public void drawScale(Graphics g) {
        g.setColor(Color.WHITE);
        g.drawLine(20, c.getHeight() - 30, 120, c.getHeight() - 30);
        g.drawString(String.valueOf((int) (GraphicsSetting.getInstance().getZoom() * 100)) + "m", 20, c.getHeight() - 15);
    }

    /**
     * Draws the road that is currently being made
     *
     * @param g The graphics needed to draw
     */
    public void drawLineDragging(Graphics g) {
        if (Canvas.dragLine) {
            g.setColor(Color.WHITE);
            ((Graphics2D) g).setStroke(new BasicStroke((float) (12 * GraphicsSetting.getInstance().getZoom() > 0 ? 12 * GraphicsSetting.getInstance().getZoom() : 1)));
            g.drawLine((int) (Canvas.s.getX() * GraphicsSetting.getInstance().getZoom()), (int) (Canvas.s.getY() * GraphicsSetting.getInstance().getZoom()), (int) (Canvas.liveMouseX * GraphicsSetting.getInstance().getZoom()), (int) (Canvas.liveMouseY * GraphicsSetting.getInstance().getZoom()));
            g.setColor(Color.BLACK);
            ((Graphics2D) g).setStroke(
                    new BasicStroke((float) (10 * GraphicsSetting.getInstance().getZoom() > 0 ? 10 * GraphicsSetting.getInstance().getZoom() : 1)));
            g.drawLine((int) (Canvas.s.getX() * GraphicsSetting.getInstance().getZoom()), (int) (Canvas.s.getY() * GraphicsSetting.getInstance().getZoom()), (int) (Canvas.liveMouseX * GraphicsSetting.getInstance().getZoom()), (int) (Canvas.liveMouseY * GraphicsSetting.getInstance().getZoom()));
        }
    }

    /**
     * Calls all the other draw function
     *
     * @param g The graphics needed to draw
     */
    public void drawEverything(Graphics g) {
        drawLineDragging(g);
        drawRoad(g);
        drawIntersection(g);
        drawCars(g);
        drawScale(g);
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

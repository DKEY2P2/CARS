package ui;

import controller.Controller;
import controller.Task;
import helper.Logger;
import map.Intersection;
import map.Road;
import ui.frames.Canvas;
import ui.frames.SideBar;
import ui.setting.GraphicsSetting;
import vehicle.Vehicle;

import javax.swing.*;
import java.awt.*;
import java.util.AbstractMap;
import ui.helper.TwoDTransformation;

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
                + "\nPress start (on the side bar) to start the)"
                + "\nPress \"L\" to print out the logger"
                + "\nPress \"I\" to toggle the intersections"
                + "\nPress \"T\" to toggle the traffic lights"
                + "\nPress \"M\" to import a map"
                + "\nPress \"R\" to reset things");

    }

    /**
     * Draws everything
     */
    public void draw() {
        Graphics g = c.getGraphic();
        drawEverything(g);
        c.getScene();
        s.repaint();
    }

    /**
     * Draws the scale
     *
     * @param g The graphics needed to draw
     */
    public void drawScale(Graphics g) {
        g.setColor(Color.WHITE);
        ((Graphics2D) (g)).setStroke(new BasicStroke(2));
        g.drawLine(20, c.getHeight() - 30, 120, c.getHeight() - 30);
        String tmp = 1 / GraphicsSetting.getInstance().getZoom() * 100 < 1000
                ? String.valueOf((int) (1 / GraphicsSetting.getInstance().getZoom() * 100)) + "m"
                : String.valueOf((double) (Math.round(0.001 * (1 / GraphicsSetting.getInstance().getZoom() * 100) * 10)) / 10) + "km";
        tmp = GraphicsSetting.getInstance().getZoom() == 0 ? "infinity" : tmp;
        g.drawString(tmp, 20, c.getHeight() - 15);
    }

    /**
     * Draws the road that is currently being made
     *
     * @param g The graphics needed to draw
     */
    public void drawLineDragging(Graphics g) {
        if (Canvas.dragLine) {
            int panX = GraphicsSetting.getInstance().getPanX();
            int panY = GraphicsSetting.getInstance().getPanY();
            AbstractMap.SimpleImmutableEntry<Integer, Integer> x = TwoDTransformation.transformX(Canvas.s.getX(), Canvas.liveMouseX - panX);
            AbstractMap.SimpleImmutableEntry<Integer, Integer> y = TwoDTransformation.transformY(Canvas.s.getY(), Canvas.liveMouseY - panY);
            g.setColor(Color.WHITE);
            ((Graphics2D) g).setStroke(new BasicStroke((float) (12 * GraphicsSetting.getInstance().getZoom() > 0 ? 12 * GraphicsSetting.getInstance().getZoom() : 1)));
            g.drawLine(
                    x.getKey(),
                    y.getKey(),
                    x.getValue(),
                    y.getValue());
            g.setColor(Color.BLACK);
            ((Graphics2D) g).setStroke(
                    new BasicStroke((float) (10 * GraphicsSetting.getInstance().getZoom() > 0 ? 10 * GraphicsSetting.getInstance().getZoom() : 1)));
            g.drawLine(
                    x.getKey(),
                    y.getKey(),
                    x.getValue(),
                    y.getValue());
        }
    }

    /**
     * Calls all the other draw function
     *
     * @param g The graphics needed to draw
     */
    public void drawEverything(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        try {
            drawLineDragging(g);
            if (Controller.getInstance().getMap() != null) {
                drawRoad(g);
                if (GraphicsSetting.getInstance().isShowIntersection()) {
                    drawIntersection(g);
                }
                drawCars(g);
            } else {
                Logger.LogAny("Drawing", "No map loaded");
            }

            drawScale(g);
        } catch (Exception e) {
            Logger.LogError(e);
        }
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
        if (controller.getMap() != null) {
            for (Road road : controller.getMap().getRoads()) {
                road.draw(g);
            }
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

    /**
     * Get the canvas
     *
     * @return The canvas
     */
    public Canvas getC() {
        return c;
    }

    public SideBar getS() {
        return s;
    }

}

package ui;

import java.awt.Graphics;

/**
 *
 * @author Kareem
 */
public class ControllerUI {

    Canvas c;

    public ControllerUI() {
        c = new Canvas();
    }

    public void draw() {
        drawEverything(c.getGraphic());
        c.getScene();
    }

    public void drawEverything(Graphics g) {
        drawRoad(g);
        drawIntersection(g);
        drawCars(g);
    }

    public void drawCars(Graphics g) {

    }

    public void drawRoad(Graphics g) {
    }

    public void drawIntersection(Graphics g) {

    }
}

package ui;

import java.awt.Graphics;

/**
 * Allows an object to be drawn
 *
 * @author Kareem
 * @version 1.0
 */
public interface Drawable {

    /**
     * To be called when a object should be drawn
     *
     * @param g The graphics to draw stuff
     */
    public void draw(Graphics g);
}

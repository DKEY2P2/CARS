package ui.frames;

import ui.setting.GraphicsSetting;
import helper.Logger;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferStrategy;
import javax.swing.JFrame;

/**
 * The canvas to draw everything on. May be turn into a Jpanel at a later date
 *
 * @author Kareems
 */
public class Canvas extends JFrame {

    /**
     * Creates the canvas
     */
    public Canvas() {
        //Sets the title of window. Ironically not shown xD
        setTitle("Traffic simulator");

        //To get the size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //Sets the size of the UI
        int width = (int) (screenSize.getWidth() * GraphicsSetting.getInstance().getScale() * 0.7);
        int height = (int) (screenSize.getHeight() * GraphicsSetting.getInstance().getScale());

        setSize(width, height);

        //Allows the user to exit the program
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    //Prints what in the logs
                    Logger.print();
                    Logger.printOther();

                    //Exit the application
                    System.exit(0);
                }
            }

        });
        //Set the default operation to close the java application
        setDefaultCloseOperation(3);

        //Hides the System specfic (eg Windows, Apple) elments. Only to allow 
        //the double bufferStrategy to work
        if (!GraphicsSetting.getInstance().isDecorated()) {
            setUndecorated(true);
        }

        setIgnoreRepaint(true);

        //Makes the UI visable
        setVisible(true);

        //Creates a buffer strategy. To remove any flickering.
        createBufferStrategy(2);

        //Set the UI to be in the center of the screen
        setLocationRelativeTo(null);

    }

    private BufferStrategy bi = this.getBufferStrategy();

    /**
     * Get the item to draw everything
     *
     * @return The graphics
     */
    public Graphics getGraphic() {
        //Get what we want to draw on
        bi = this.getBufferStrategy();

        //Currently set the background to a dark grey color
        Graphics g = bi.getDrawGraphics();
        g.setColor(Color.gray.darker());
        g.fillRect(0, 0, getWidth(), getHeight());

        return g;
    }

    /**
     * Draws the image
     */
    public void getScene() {
        //Show what we wanted to show
        bi.show();

        //Tells the JFrame/JPanel to draw the image
        Toolkit.getDefaultToolkit().sync();
    }

}

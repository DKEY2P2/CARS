package ui.frames;

import map.TrafficLight;
import ui.Drawable;
import ui.setting.GraphicsSetting;
import helper.Logger;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import map.Intersection;
import map.Road;
import map.intersection.DefaultIntersection;
import map.road.NormalRoad;

/**
 * The canvas to draw everything on. May be turn into a Jpanel at a later date
 *
 * @author Kareems
 */
public class Canvas extends JFrame implements Drawable {

    private int keyCode = -Integer.MAX_VALUE;

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
                } else {
                    keyCode = e.getKeyCode();
                    if (e.isControlDown()) {
                        keyCode = -1;
                    } else if (e.isShiftDown()) {
                        keyCode = -2;
                    }
                }
            }

        });
        addMouseListener(new MouseAdapterCustom());
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

    @Override
    public void draw(Graphics g) {

    }

    private class MouseAdapterCustom extends MouseAdapter {

        private Intersection start;

        private void addIntersection(int x, int y) {
            controller.Controller.getInstance().getMap().addIntersection(
                    new DefaultIntersection(x, y, controller.Controller.getInstance().getTicker()));
        }

        private void connect(int x, int y) {
            map.Map m = controller.Controller.getInstance().getMap();
            if (start != null) {
                System.out.println("End point selected");
                Intersection end = findClosest(x, y);
                System.out.println(end);
                Road r = new NormalRoad(start, end);
                m.addRoad(r);
                start = null;
            } else {
                System.out.println("Start point selected");
                start = findClosest(x, y);
                System.out.println(start);
            }
        }

        private Intersection findClosest(int x, int y) {
            map.Map m = controller.Controller.getInstance().getMap();
            ArrayList<Intersection> r = m.getIntersections();
            double min = +Double.MAX_VALUE;
            int index = 0;
            int i = 0;
            for (Intersection r1 : r) {
                double tmp = Math.sqrt(Math.pow(r1.getX() - x, 2) + Math.pow(y - r1.getY(), 2));
                if (tmp < min) {
                    min = tmp;
                    index = i;
                }
                i++;
            }
            return r.get(index);
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            /*System.out.println("clicked");
            if (keyCode == -1) {
                addIntersection(e.getX(), e.getY());
            } else if (keyCode == -2) {
                connect(e.getX(), e.getY());
            }else{
                System.out.println("Reset");
                start = null;
            }
            controller.Controller.getInstance().getUI().update();*/
        }

        @Override
        public void mousePressed(MouseEvent e) {
            if(keyCode == -1){
                addIntersection(e.getX(), e.getY());
            }else if(keyCode == -2 && start == null){
                connect(e.getX(), e.getY());
            }
            controller.Controller.getInstance().getUI().update();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(keyCode == -2 && start != null){
                connect(e.getX(), e.getY());
            }else{
                start = null;
            }
            controller.Controller.getInstance().getUI().update();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
        }

        @Override
        public void mouseDragged(MouseEvent e){

        }

    }

}

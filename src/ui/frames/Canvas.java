package ui.frames;

import controller.StartDoingStuff;
import helper.Logger;
import map.Intersection;
import map.Road;
import map.intersection.DefaultIntersection;
import map.road.NormalRoad;
import ui.setting.GraphicsSetting;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

/**
 * The canvas to draw everything on. May be turn into a JPanel at a later date
 *
 * @author Kareem Horstink
 */
public class Canvas extends JFrame {

    private int keyCode = -Integer.MAX_VALUE;

    public static int liveMouseX = 0;
    public static int liveMouseY = 0;
    public static boolean dragLine = false;
    public static Intersection s = null;

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

        addMouseWheelListener(new MouseAdapter() {
            @Override
            public void mouseWheelMoved(MouseWheelEvent e) {
                GraphicsSetting.getInstance().setZoom(GraphicsSetting.getInstance().getZoom() + e.getPreciseWheelRotation() * 0.05);
            }
        });

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
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    StartDoingStuff.start();
                } else {
                    keyCode = e.getKeyCode();
                    if (e.isControlDown()) {
                        keyCode = -1;
                    } else if (e.isShiftDown()) {
                        keyCode = -2;
                    }
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_CONTROL || e.getKeyCode() == KeyEvent.VK_SHIFT) {
                    keyCode = 0;
                }
            }

        });
        MouseListenerCustom a = new MouseListenerCustom();
        addMouseListener(a);
        addMouseMotionListener(a);
        addMouseWheelListener(new MouseWheelCustom());
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

    private class MouseListenerCustom implements MouseInputListener {

        boolean click;

        private Intersection start;

        int x = 0;
        int y = 0;

        private void addIntersection(int x, int y) {
            Intersection i = new DefaultIntersection(x, y, controller.Controller.getInstance().getTicker());
            s = i;
            controller.Controller.getInstance().getMap().addIntersection(i);
        }

        private void connect(int x, int y) {
            map.Map m = controller.Controller.getInstance().getMap();
            if (start != null) {
                Intersection end = findClosest(x, y);
                Road r = null;
                try {
                    r = new NormalRoad(start, end);
                } catch (Exception e) {
                    Logger.LogError(e);
                }
                if (r != null) {
                    m.addRoad(r);
                }
                dragLine = false;
                start = null;
            } else {
                start = findClosest(x, y);
                s = start;
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
            }else{
                start = null;
            }
            click = true;
            controller.Controller.getInstance().getUI().update();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            if(keyCode == -2 && start != null){
                connect(e.getX(), e.getY());
            }else{
                start = null;
            }
            click = false;
            controller.Controller.getInstance().getUI().update();
        }

        @Override
        public void mouseEntered(MouseEvent e) {
        }

        @Override
        public void mouseExited(MouseEvent e) {
            controller.Controller.getInstance().getUI().update();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
            if(keyCode == 0) {
                GraphicsSetting.getInstance().setPanX(GraphicsSetting.getInstance().getPanX() + (e.getX() - x));
                GraphicsSetting.getInstance().setPanY(GraphicsSetting.getInstance().getPanY() + (e.getY() - y));
                x = e.getX();
                y = e.getY();
            }else{
                x = e.getX();
                y = e.getY();
                liveMouseX = x;
                liveMouseY = y;
                dragLine = true;
            }
            controller.Controller.getInstance().getUI().update();

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            if (!click) {
                x = e.getX();
                y = e.getY();
            }
        }

    }

    private class MouseWheelCustom implements MouseWheelListener{

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            if(e.getPreciseWheelRotation()<0){
                GraphicsSetting.getInstance().setZoom(GraphicsSetting.getInstance().getZoom() - 0.1);
            }else{
                GraphicsSetting.getInstance().setZoom(GraphicsSetting.getInstance().getZoom() + 0.1);
            }
            controller.Controller.getInstance().getUI().update();
        }
    }

}

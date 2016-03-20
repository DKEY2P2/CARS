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
import java.io.File;
import java.io.FileNotFoundException;
import json.GEOjson;
import map.Map;

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
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                Logger.exit();
            }
        }
        );

        //Sets the title of window. Ironically not shown xD
        setTitle(
                "Traffic simulator");

        //To get the size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        //Sets the size of the UI
        int width = (int) (screenSize.getWidth() * GraphicsSetting.getInstance().getScale() * 0.7);
        int height = (int) (screenSize.getHeight() * GraphicsSetting.getInstance().getScale());

        setSize(width, height);

        addMouseWheelListener(
                new MouseAdapter() {
                    @Override
                    public void mouseWheelMoved(MouseWheelEvent e
                    ) {
                        GraphicsSetting.getInstance().setZoom(GraphicsSetting.getInstance().getZoom() + e.getPreciseWheelRotation() * 0.05);
                    }
                }
        );

        //Allows the user to exit the program
        addKeyListener(
                new KeyAdapterImpl());
        MouseListenerCustom a = new MouseListenerCustom();

        addMouseListener(a);

        addMouseMotionListener(a);

        addMouseWheelListener(
                new MouseWheelCustom());

        //Set the default operation to close the java application
        setDefaultCloseOperation(
                3);

        //Hides the System specfic (eg Windows, Apple) elments. Only to allow 
        //the double bufferStrategy to work
        if (!GraphicsSetting.getInstance()
                .isDecorated()) {
            setUndecorated(true);
        }

        setIgnoreRepaint(
                true);

        //Makes the UI visable
        setVisible(
                true);

        //Creates a buffer strategy. To remove any flickering.
        createBufferStrategy(
                2);

        //Set the UI to be in the center of the screen
        setLocationRelativeTo(
                null);
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
    private boolean click;
    private Intersection start;

    /**
     * The custom mouse listener for this object
     */
    private class MouseListenerCustom implements MouseInputListener {

        private int x = 0;
        private int y = 0;

        /**
         * Adds a new intersection
         *
         * @param x
         * @param y
         */
        private void addIntersection(int x, int y) {
            Intersection i = new DefaultIntersection(x, y, controller.Controller.getInstance().getTicker());
            s = i;
            controller.Controller.getInstance().getMap().addIntersection(i);
        }

        /**
         * Connects two intersections
         *
         * @param x
         * @param y
         */
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

        /**
         * Gets the nearest intersection
         *
         * @deprecated
         * @param x
         * @param y
         * @return
         */
        private Intersection findClosest(int x, int y) {
            map.Map m = controller.Controller.getInstance().getMap();
            return m.findClosestIntersection(x, y);
        }

        @Override
        public void mouseClicked(MouseEvent e) {

        }

        @Override
        public void mousePressed(MouseEvent e) {
            x = e.getX();
            y = e.getY();
            int panX = GraphicsSetting.getInstance().getPanX();
            int panY = GraphicsSetting.getInstance().getPanY();
            if (keyCode == -1) {
                addIntersection(e.getX() - panX, e.getY() - panY);
            } else if (keyCode == -2 && start == null) {
                connect(e.getX() - panX, e.getY() - panY);
            } else {
                start = null;
            }
            click = true;
            controller.Controller.getInstance().getUI().update();
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            int panX = GraphicsSetting.getInstance().getPanX();
            int panY = GraphicsSetting.getInstance().getPanY();
            if (keyCode == -2 && start != null) {
                connect(e.getX() - panX, e.getY() - panY);
            } else {
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
        }

        @Override
        public void mouseDragged(MouseEvent e) {

            if (keyCode == Integer.MIN_VALUE) {
                keyCode = 0;
            }
            if (keyCode == 0) {
                double zoom = GraphicsSetting.getInstance().getZoom();
                int panX = GraphicsSetting.getInstance().getPanX();
                int panY = GraphicsSetting.getInstance().getPanY();
                panY += e.getY() - y;
                panX += e.getX() - x;
                GraphicsSetting.getInstance().setPanX(panX);
                GraphicsSetting.getInstance().setPanY(panY);
                x = e.getX();
                y = e.getY();
                keyCode = Integer.MIN_VALUE;
            } else {
                x = e.getX();
                y = e.getY();
                liveMouseX = x;
                liveMouseY = y;
                if (keyCode == -2) {
                    dragLine = true;
                }
            }
            controller.Controller.getInstance().getUI().update();

        }

        @Override
        public void mouseMoved(MouseEvent e) {
            x = e.getX();
            y = e.getY();
            GraphicsSetting.getInstance().setMouseX(e.getX());
            GraphicsSetting.getInstance().setMouseY(e.getY());
//            if (!click) {
//                x = e.getX();
//                y = e.getY();
//            }
        }

    }

    private class MouseWheelCustom implements MouseWheelListener {

        @Override
        public void mouseWheelMoved(MouseWheelEvent e) {
            double zoom = getZoom();
            if (e.getPreciseWheelRotation() > 0) {
                zoom *= e.getPreciseWheelRotation() * 1.05;
            } else {
                zoom /= -e.getPreciseWheelRotation() * 1.05;
            }
            if (zoom < 0) {
                zoom = 0;
            }
            setZoom(zoom);

            controller.Controller.getInstance().getUI().update();
        }

        double getZoom() {
            return GraphicsSetting.getInstance().getZoom();
        }

        void setZoom(double zoom) {
            GraphicsSetting.getInstance().setZoom(zoom);
        }
    }

    private class KeyAdapterImpl extends KeyAdapter {

        public KeyAdapterImpl() {
        }

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
            } else if (e.getKeyCode() == KeyEvent.VK_L) {
                Logger.print();
            } else if (e.getKeyCode() == KeyEvent.VK_T) {
                GraphicsSetting.getInstance().setShowTraffficLight(!GraphicsSetting.getInstance().isShowTraffficLight());
                controller.Controller.getInstance().getUI().draw();
            } else if (e.getKeyCode() == KeyEvent.VK_I) {
                GraphicsSetting.getInstance().setShowIntersection(!GraphicsSetting.getInstance().isShowIntersection());
                controller.Controller.getInstance().getUI().draw();
            } else if (e.getKeyCode() == KeyEvent.VK_M) {
                JFileChooser fileChooser = new JFileChooser(System.getProperty("user.dir"));
                int returnVal = fileChooser.showOpenDialog(controller.Controller.getInstance().getUI().getC());
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fileChooser.getSelectedFile();
                    try {
                        Map m = GEOjson.GEOJsonConverter(file.getAbsolutePath());
                        controller.Controller.getInstance().setMap(m);
                        controller.Controller.getInstance().getUI().draw();
                    } catch (FileNotFoundException ex) {
                        Logger.LogError(ex);
                    }
                } else {
                    Logger.LogError("JFileChooser has failed to get a file", fileChooser);
                }
            } else if (e.getKeyCode() == KeyEvent.VK_R) {
                controller.Controller.getInstance().getUI().getS().resetView();
                dragLine = false;
                click = false;
                start = null;
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
            keyCode = Integer.MIN_VALUE;
        }
    }

}

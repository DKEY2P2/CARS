package ui.frames;

import controller.Controller;
import controller.SimulationSettings;
import controller.StartDoingStuff;
import helper.Logger;
import json.GEOjson;
import map.Map;
import ui.setting.GraphicsSetting;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import ui.helper.RunLineChart;

/**
 * A sidebar to hold all the items that control the simulation
 *
 * @author Kareem Horstink
 */
public class SideBar extends JFrame {

    private JLabel tickCounterL;
    private JLabel timeL;

    public SideBar(JFrame parent) {
        setTitle("Options");
        JTabbedPane jTabbedPane = new JTabbedPane(JTabbedPane.NORTH);
        //Exits if escape key has been pressed
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
        //------------------------------general-------------------------------//
        JPanel general = new JPanel();
        //Set the layout
        general.setLayout(new GridLayout(0, 1));
        //Set it to hid when you close it
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        //Gets the size of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        //Set the size of the window
        setSize((int) (GraphicsSetting.getInstance().getScale() * 0.3 * screenSize.getWidth()), parent.getHeight());
        //Set the location of the window to be next to the main screen
        setLocation(parent.getX() + parent.getWidth(), parent.getY());

        //Whether the OS decoration should be enabled
        if (!GraphicsSetting.getInstance().isDecorated()) {
            setUndecorated(true);
        }
        /*Add the buttons to the interface*/
        //Start button is added
        JButton button = new JButton("Start");
        button.addActionListener((ActionEvent e) -> {
            if (!controller.Controller.getInstance().getMap().getRoads().isEmpty()) {
                StartDoingStuff.start();
            }
        });
        general.add(button);

        //The ticker counter is added
        JPanel tickCounterP = new JPanel();
        tickCounterL = new JLabel("0");
        tickCounterP.add(new JLabel("Tick counter:"));
        tickCounterP.add(tickCounterL);

        //The time elapsed is added
        JPanel timeP = new JPanel();
        timeP.add(new JLabel("Time elapsed (s):"));
        timeL = new JLabel("0");
        timeP.add(timeL);
        general.add(tickCounterP);
        general.add(timeP);

        //The speed of the simulation changer is added
        JPanel speedP = new JPanel();
        SpinnerModel spinnerModelSpeed = new SpinnerNumberModel(controller.Controller.getInstance().getTicker().getTickTimeInMS(), 1, 10000, 1);
        JSpinner speedSpinner = new JSpinner(spinnerModelSpeed);
        speedSpinner.addChangeListener(new ChangeListenerSpeed());
        speedP.add(new JLabel("Speed of animation (ms)"));
        speedP.add(speedSpinner);
        general.add(speedP);
        jTabbedPane.add("General", general);
        //------------------------------stats---------------------------------//
        JPanel stats = new JPanel();
        JButton historyNormalized = new JButton("Show history of waiting time when its normalized");
        historyNormalized.addActionListener((ActionEvent e) -> {
            RunLineChart.addData(Controller.getInstance().getStats().copy().getWaitingTimeNormalizedHistory(), "Normalized Waiting Time Data");
            RunLineChart.run();
        });
        JButton historyTotalCars = new JButton("Show history of the number of cars");
        historyTotalCars.addActionListener((ActionEvent e) -> {
            RunLineChart.addDataI(Controller.getInstance().getStats().copy().getNumberOfCarsHistory(), "Amount of Cars Data");
            RunLineChart.run();
        });
        stats.setLayout(new GridLayout(0, 1));
        stats.add(historyNormalized);
        stats.add(historyTotalCars);
        jTabbedPane.add("Statistics", stats);

        //---------------------------------map--------------------------------//
        JPanel mapControl = new JPanel();
        mapControl.setLayout(new GridLayout(0, 1));

        jTabbedPane.add("Map Controls", mapControl);

        JButton importButton = new JButton("Click to import map");
        importButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
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
            }
        });
        mapControl.add(importButton);

        JPanel speedLimitPanel = new JPanel();

        //-----------------------------vehicle--------------------------------//
        JPanel vehiclePanel = new JPanel();
        JPanel SpawnPanel = new JPanel();
        SpinnerModel spinnerModelSpawn
                = new SpinnerNumberModel(SimulationSettings.getInstance().getNumberOfCarsToSpawn(), -1, 100000, 1);

        spawnSpinner = new JSpinner(spinnerModelSpawn);
        spawnSpinner.addChangeListener(new ChangeListenerSpawn());
        SpawnPanel.add(new JLabel("Set the number of cars to spawn"));
        SpawnPanel.add(spawnSpinner);
        vehiclePanel.add(SpawnPanel);

        jTabbedPane.add("Vehicle Settings", vehiclePanel);

        //----------------------------view------------------------------------//
        JPanel viewPanel = new JPanel();
        viewPanel.setLayout(new GridLayout(0, 1));
        JPanel zoomPanel = new JPanel();
        JButton plusZoom = new JButton("+");
        plusZoom.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                double zoom = getZoom();
                zoom *= 1.05;

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
        });
        zoomLabel = new JLabel("Text");
        JButton minusZoom = new JButton("-");
        minusZoom.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                double zoom = getZoom();
                zoom /= 1.05;

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
        );
        zoomPanel.add(minusZoom);
        zoomPanel.add(zoomLabel);
        zoomPanel.add(plusZoom);
        viewPanel.add(zoomPanel);
        JPanel zoomControlPanel = new JPanel();
        SpinnerModel spinnerModelZoom
                = new SpinnerNumberModel(GraphicsSetting.getInstance().getZoom(), 0.00000000001, 10000, 0.01);

        JSpinner zoomSpinner = new JSpinner(spinnerModelZoom);
        zoomSpinner.addChangeListener(new ChangeListenerZoom());
        zoomControlPanel.add(new JLabel("Set the zoom level"));
        zoomControlPanel.add(zoomSpinner);
        viewPanel.add(zoomControlPanel);

        JButton resetButton = new JButton("Resets the view");
        resetButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                resetView();
            }

        });
        viewPanel.add(resetButton);

        jTabbedPane.add("View Control", viewPanel);
        add(jTabbedPane);

        //Set it to visiable
        setVisible(true);

    }

    private JLabel zoomLabel;
    private JSpinner spawnSpinner;

    @Override
    public void repaint() {
        //Updates some elements
        int n = controller.Controller.getInstance().getTicker().getTickCount();
        tickCounterL.setText(Integer.toString(n));
        timeL.setText(Double.toString(Math.round(controller.Controller.getInstance().getTicker().getTimeElapsed() * 100) / 100d));
        super.repaint();
        double zoom = GraphicsSetting.getInstance().getZoom();
        zoom = Math.round(zoom * 100) / 100d;
        zoomLabel.setText(Double.toString(zoom) + "x");
    }

    public void resetView() {
        GraphicsSetting setting = GraphicsSetting.getInstance();
        setting.setPanX(0);
        setting.setPanY(0);
        setting.setZoom(1);
        setting.setShowIntersection(true);
        setting.setShowTraffficLight(true);
        controller.Controller.getInstance().getUI().update();
    }

    //The change listener for the spinner
    private class ChangeListenerSpeed implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() instanceof JSpinner) {
                JSpinner s = (JSpinner) e.getSource();
                controller.Controller.getInstance().getTicker().setTickTimeInS(((double) s.getModel().getValue()) / 1000d);
            }
        }

    }

    private class ChangeListenerZoom implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() instanceof JSpinner) {
                JSpinner s = (JSpinner) e.getSource();
                GraphicsSetting.getInstance().setZoom((double) s.getModel().getValue());
                controller.Controller.getInstance().getUI().draw();
            }
        }

    }

    private class ChangeListenerSpawn implements ChangeListener {

        @Override
        public void stateChanged(ChangeEvent e) {
            if (e.getSource() instanceof JSpinner) {
                JSpinner s = (JSpinner) e.getSource();
                int value = (int) s.getModel().getValue();
                if (value != SimulationSettings.getInstance().getNumberOfCarsToSpawn()) {
                    SimulationSettings.getInstance().setNumberOfCarsToSpawn(value);
                    controller.Controller.getInstance().getUI().draw();
                }
            }
        }

    }

}

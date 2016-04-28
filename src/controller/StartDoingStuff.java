package controller;

import java.util.ArrayList;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import algorithms.pathfinding.AStar;
import controller.threads.ThreadController;
import helper.Logger;
import map.Intersection;
import map.Map;
import map.Road;
import map.intersection.DefaultIntersection;
import map.road.NormalRoad;
import models.IntelligentDriver;
import statistics.Stats;
import ui.ControllerUI;
import vehicle.VehicleFactory;

/**
 * A start class for the rest of the system
 *
 * @author Kareem
 */
public class StartDoingStuff {

    /**
     * Start of everything. Of our mini-world muwahahahaha
     *
     * @param args For the first string inputted - is the number of threads you
     * want to be inputted - if left blank it would make the same number of as
     * core that you have, if you put "no" it means you don't care thus it would
     * consider it the same as if you left it blank
     */
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(
                    UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            Logger.LogError(ex);
        }

        /* Set what model to use for the rest of the applicatio */
        SimulationSettings.getInstance().setModel(new IntelligentDriver());

        /* Set what path finding AI to use for the rest of the application */
        SimulationSettings.getInstance().setPathFindingAI(new AStar());

        /* Set the number of ticks the simulation waits until it spawns new vehicles */
        SimulationSettings.getInstance().setTimeUntilSpawn(500);

        SimulationSettings.getInstance().setNumberOfCarsToSpawn(10);

        /* Creates the controller */
        Controller control = Controller.getInstance();

        /* A temporary holder for the intersections */
        ArrayList<DefaultIntersection> a = new ArrayList<>();
        ArrayList<Road> b = new ArrayList<>();

        /* Creates a ticker with the value of 100 ms between each tick which represent 0.1 second */
        Ticker t = new Ticker(0.1, 100);

        //Add the item to the controller
        b.stream().forEach((b1) -> {
            control.getMap().addRoad(b1);
        });
        a.stream().forEach((as) -> {
            control.getMap().addIntersection(as);
        });

        //Add the ticker to the controller
        control.setTicker(t);//so we can get the ticker later on
        
        /* Makes a proper grid to allow easier "debugging" */
//        control.setMap(standardGridRoads(t));
        
        //Need to move the if statements around but im too lazy now
        if (args.length != 0) {
            //Tries to parse the string to int and input that as the number of threads wanted
            try {
                int cores = Integer.parseInt(args[0]);
                new ThreadController(cores, t);
            } catch (Exception ex) {
                //Set it to 0 (to be safe) if parsing fails
                Logger.LogError(ex);
                System.err.println("An error has occured - check logs");
                new ThreadController(1, t);
            }
            //if you don't care about what number it has, it will be put as the default
        } else if (args.length != 0 && args[0].matches("([No][Oo])")) {
            int cores = Runtime.getRuntime().availableProcessors();
            new ThreadController(cores, t);
        } else {
            //The default
            //Set the number of threads to same number of cores you have
            int cores = Runtime.getRuntime().availableProcessors();
            new ThreadController(cores, t);
        }

        //Starts the UI
        control.setUI(new ControllerUI());
        control.getUI().update();

    }
    private static boolean start = false;

    public static void start() {
        if (!start) {
            Controller.getInstance().setStats(new Stats.StatsDynamic(Controller.getInstance()));
            Controller.getInstance().getTicker().start("tick");
            VehicleFactory.getFactory().createVehicle();
            start = true;
        }
    }

    private static Map standardGridRoads(Ticker t) {
        return standardGridRoads(false, t);
    }

    private static Map standardGridRoads(boolean twoWayStreet, Ticker t) {
        Map m = new Map(new ArrayList<>(), new ArrayList<>());
        ArrayList<Intersection> x = new ArrayList<>();
        ArrayList<Intersection> y = new ArrayList<>();
        for (int xi = 0; xi < 10; xi++) {
            for (int yi = 0; yi < 10; yi++) {
                DefaultIntersection tmp = new DefaultIntersection(50 + xi * 200, 50 + yi * 200, t);
                x.add(tmp);
                m.addIntersection(tmp);
            }
        }
        for (int i = 0; i < 9; i++) {
            Road r = new NormalRoad(x.get(i * 10), x.get(10 * (i + 1)));
            m.addRoad(r);
            r = new NormalRoad(x.get(90 + i), x.get(90 + i + 1));
            m.addRoad(r);
            r = new NormalRoad(x.get(99 - i * 10), x.get(99 - (i + 1) * 10));
            m.addRoad(r);
            r = new NormalRoad(x.get(i + 1), x.get(i));
            m.addRoad(r);
        }
        for (int xi = 1; xi < 9; xi++) {
            for (int yi = 0; yi < 9; yi++) {
                if (xi % 2 != 0) {
                    Road r = new NormalRoad(x.get(xi * 10 + yi), x.get(xi * 10 + yi + 1));
                    m.addRoad(r);
                } else {
                    Road r = new NormalRoad(x.get(xi * 10 + yi + 1), x.get(xi * 10 + yi));
                    m.addRoad(r);

                }

            }
        }

        for (int yi = 1; yi < 9; yi++) {
            for (int xi = 0; xi < 9; xi++) {
                if (yi % 2 != 0) {
                    Road r = new NormalRoad(x.get(yi + xi * 10), x.get(yi + xi * 10 + 10));
                    m.addRoad(r);
                } else {
                    Road r = new NormalRoad(x.get(yi + xi * 10 + 10), x.get(yi + xi * 10));
                    m.addRoad(r);
                }
            }
        }

//        for (int i = 0; i < y.size(); i++) {
//            Intersection ycurrent = y.get(i);
//            Intersection yadhead;
//            if (i == y.size()) {
//                yadhead = null;
//            } else {
//                yadhead = y.get(i + 1);
//            }
//            for (int j = 0; j < x.size(); j++) {
//                Intersection xcurrent = x.get(j);
//                Intersection xahdead;
//                if (j == x.size()) {
//                    xahdead = null;
//                } else {
//                    xahdead = x.get(i + 1);
//                }
//                Road r = new Road(ycurrent, xahdead, j) {
//                }
//
//            }
//        }
        return m;
    }

}

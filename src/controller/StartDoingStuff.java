package controller;

import java.util.ArrayList;
import java.util.Random;

import algorithms.TestAl;
import controller.threads.ThreadController;
import helper.Logger;
import map.Road;
import map.intersection.DefaultIntersection;
import map.road.NormalRoad;
import models.IntelligentDriver;
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
        /* Set what model to use for the rest of the application */
        SimulationSettings.getInstance().setModel(new IntelligentDriver());

        /* Set what path finding AI to use for the rest of the application */
        SimulationSettings.getInstance().setPathFindingAI(new TestAl());

        /* Set the number of ticks the simulation waits until it spawns new vehicles */
        SimulationSettings.getInstance().setTimeUntilSpawn(150);

        /* Creates the controller */
        Controller control = Controller.getInstance();

        /* A tempory of holder of for the intersection */
        ArrayList<DefaultIntersection> a = new ArrayList<>();
        ArrayList<Road> b = new ArrayList<>();

        /* A random gen for location of a vehicle */
        Random r = new Random();
        /* Creates a ticker with the value of 100 ms between each tick which represent 1 second */
        Ticker t = new Ticker(1, 100);

        /* New intersection */
        DefaultIntersection a1 = new DefaultIntersection(100, 100, t);
        DefaultIntersection a2 = new DefaultIntersection(100, 500, t);
        DefaultIntersection a3 = new DefaultIntersection(500, 500, t);
        DefaultIntersection a4 = new DefaultIntersection(500, 100, t);

        /* New roads */
        NormalRoad r1 = new NormalRoad(a1, a2);
        NormalRoad r2 = new NormalRoad(a2, a3);
        NormalRoad r3 = new NormalRoad(a3, a4);
        NormalRoad r4 = new NormalRoad(a4, a1);
        NormalRoad r5 = new NormalRoad(a1, a3);

        /* Adds it to the controller */
        a.add(a4);
        a.add(a3);
        a.add(a2);
        a.add(a1);
        b.add(r1);
        b.add(r2);
        b.add(r3);
        b.add(r4);
        b.add(r5);

        //Add the item to the controller
        b.stream().forEach((b1) -> {
            control.getMap().addRoad(b1);
        });
        a.stream().forEach((as) -> {
            control.getMap().addIntersection(as);
        });

        //Creates n number of cars
        for (int i = 0; i < 100; i++) {
            VehicleFactory.getFactory().createVehicle(VehicleFactory.SPORT_CAR);
        }
        //Add the ticker to the controller
        control.setTicker(t);//so we can get the ticker later on

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
            Controller.getInstance().getTicker().start("tick");
            start = true;
        }
        
    }
    
}
package controller;


import java.util.ArrayList;
import java.util.Random;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import algorithms.AStar;
import controller.threads.ThreadController;
import helper.Logger;
import map.Road;
import map.intersection.DefaultIntersection;
import models.Pipes;
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
        
        
        /* Set what model to use for the rest of the application */
        SimulationSettings.getInstance().setModel(new Pipes());

        /* Set what path finding AI to use for the rest of the application */
        SimulationSettings.getInstance().setPathFindingAI(new AStar());

        /* Set the number of ticks the simulation waits until it spawns new vehicles */
        SimulationSettings.getInstance().setTimeUntilSpawn(200);

        SimulationSettings.getInstance().setNumberOfCarsToSpawn(10);

        /* Creates the controller */
        Controller control = Controller.getInstance();

        /* A temporary holder for the intersections */
        ArrayList<DefaultIntersection> a = new ArrayList<>();
        ArrayList<Road> b = new ArrayList<>();

        /* A random generator for location of a vehicle */ //TODO: We must make this ZONE based, not random
        Random r = new Random();

        /* Creates a ticker with the value of 100 ms between each tick which represent 0.1 second */
        Ticker t = new Ticker(0.01, 10);


        //Add the item to the controller
        b.stream().forEach((b1) -> {
            control.getMap().addRoad(b1);
        });
        a.stream().forEach((as) -> {
            control.getMap().addIntersection(as);
        });


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
            VehicleFactory.getFactory().createVehicle();
            start = true;
        }
        
    }

}

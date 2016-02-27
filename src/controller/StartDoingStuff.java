package controller;

import algorithms.AStar;
import controller.threads.ThreadController;
import helper.Logger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import map.Intersection;
import map.Road;
import map.intersection.SimpleTrafficLight;
import map.road.NormalRoad;
import ui.ControllerUI;
import vehicle.forbe.SportCar;

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
        //Creates test items
        Controller control = Controller.getInstance();
        ArrayList<SimpleTrafficLight> a = new ArrayList<>();
        ArrayList<Road> b = new ArrayList<>();
        Random r = new Random();
        //Creates a ticker with the value of 100 ms between each tick which represent 1 second
        Ticker t = new Ticker(1, 10);
        SimpleTrafficLight a1 = new SimpleTrafficLight(100, 100, t);
        SimpleTrafficLight a2 = new SimpleTrafficLight(100, 1000, t);
        SimpleTrafficLight a3 = new SimpleTrafficLight(1000, 1000, t);
        SimpleTrafficLight a4 = new SimpleTrafficLight(1000, 100, t);
        a.add(a4);
        a.add(a3);
        a.add(a2);
        a.add(a1);
        b.add(new NormalRoad(a1, a2));
        b.add(new NormalRoad(a2, a3));
        b.add(new NormalRoad(a3, a4));
        b.add(new NormalRoad(a4, a1));
        //Creates n number of cars
        for (int i = 0; i < 1; i++) {
            new SportCar((NormalRoad) getRandom(b), r.nextDouble(), new AStar(), (Intersection) getRandom(a));
        }
        b.stream().forEach((b1) -> {
            control.getMap().addRoad(b1);
        });
        a.stream().forEach((as) -> {
            control.getMap().addIntersection(as);
        });

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

        new ControllerUI(t);

        t.start("tick");

    }

    /**
     * Stupid helper thingy, randomly chooses a item of a collection
     *
     * @param c
     * @return
     */
    private static Object getRandom(Collection c) {
        Random r = new Random();
        return c.toArray()[r.nextInt(c.size())];
    }

}

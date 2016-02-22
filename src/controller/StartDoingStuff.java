package controller;

import controller.threads.ThreadController;
import helper.Logger;
import helper.Timer;
import java.util.ArrayList;
import map.Intersection;
import map.Road;
import ui.ControllerUI;
import vehicle.Vehicle;

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
        TestInter a = new TestInter(100, 100);
        TestInter b = new TestInter(200, 100);
        TestInter c = new TestInter(100, 200);
        TestRoad d = new TestRoad(a, b, 100);
        TestRoad e = new TestRoad(a, c, 100);

        //Creates n number of cars
        for (int i = 0; i < 1; i++) {
            new TestCar(d, 0.20);
        }

        control.getMap().addIntersection(a);
        control.getMap().addIntersection(b);
        control.getMap().addIntersection(c);
        control.getMap().addRoad(d);
        control.getMap().addRoad(e);
        //Creates a ticker with the value of 1 ms between each tick which represent 1 second
        Ticker t = new Ticker(1, 1);

        //Need to move the if statements around but im too lazy now
        if (args.length == 0) {
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
        } else if (args[0].matches("([No][Oo])")) {
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

    /*
     * Test classes. Don't actually use for anything
     */
    public static class TestCar extends Vehicle {

        public TestCar(Road start, double percentage) {
            super(start, percentage);
        }

        @Override
        public boolean update() {
            return true;
        }

    }

    public static class TestRoad extends Road {

        public TestRoad(Intersection start, Intersection end, double length) {
            super(start, end, length);
        }

    }

    public static class TestInter extends Intersection {

        public TestInter(int x, int y) {
            super(x, y);
        }

    }

}

package controller;

import controller.threads.ThreadController;
import helper.Logger;
import helper.Timer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;
import javax.swing.plaf.basic.BasicBorders;
import map.Intersection;
import map.Road;
import models.Forbe;
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
        ArrayList<TestInter> a = new ArrayList<>();
        ArrayList<TestRoad> b = new ArrayList<>();
        Random r = new Random();

        for (int i = 0; i < 100; i++) {
            a.add(new TestInter(r.nextInt(1000), r.nextInt(1000)));
        }

        for (int i = 0; i < 100; i++) {
            b.add(new TestRoad((TestInter) getRandom(a), (TestInter) getRandom(a), i));
//            b.add(new TestRoad(a.get(0), a.get(1), i));
        }

        //Creates n number of cars
        for (int i = 0; i < 1; i++) {
            new TestCar((TestRoad) getRandom(b), 0.20);
        }
        for (TestRoad b1 : b) {
            control.getMap().addRoad(b1);
        }
        for (TestInter a1 : a) {
            control.getMap().addIntersection(a1);
        }
        //Creates a ticker with the value of 100 ms between each tick which represent 1 second
        Ticker t = new Ticker(10, 100);
        control.setTicker(t);//so we can get the ticker later on
        System.out.println(args.length);

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

    private static Object getRandom(Collection c) {
        Random r = new Random();
        return c.toArray()[r.nextInt(c.size())];
    }

    /*
     * Test classes. Don't actually use for anything
     */
    public static class TestCar extends Vehicle {

        public TestCar(Road start, double percentage) {
            super(start, percentage, new Forbe(), null);
        }

        @Override
        public boolean update() {
            getModel().calculate(this);
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

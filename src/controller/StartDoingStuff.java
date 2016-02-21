package controller;

import controller.threads.ThreadController;
import helper.Timer;
import java.util.ArrayList;
import map.Intersection;
import map.Road;
import ui.ControllerUI;
import vehicle.Vehicle;

public class StartDoingStuff {

    public static void main(String[] args) {
        Controller control = Controller.getInstance();
        ArrayList<Object> ar = new ArrayList<>();
        TestInter a = new TestInter(100, 100);
        TestInter b = new TestInter(200, 100);
        TestInter c = new TestInter(100, 200);
        TestRoad d = new TestRoad(a, b, 100);
        TestRoad e = new TestRoad(a, c, 100);
        for (int i = 0; i < 100; i++) {
            ar.add(new TestCar(d, 0.20));

        }

        control.getMap().addIntersection(a);
        control.getMap().addIntersection(b);
        control.getMap().addIntersection(c);
        control.getMap().addRoad(d);
        control.getMap().addRoad(e);
        Ticker t = new Ticker(1, 1);
        new ThreadController(100, t);
//        new ControllerUI(t);

        t.start("tick");
        Timer.start();

        while (true) {
            if (Timer.milli() > 10000) {
                Timer.milliPrint();
                System.out.println("F = " + f);
                break;
            }
        }
        System.exit(0);
    }
    public static int f = 0;

    public static class TestCar extends Vehicle {

        public TestCar(Road start, double percentage) {
            super(start, percentage);
        }

        @Override
        public boolean update() {
            f++;
            long a = 0;
            for (int i = 0; i < 1000000000; i++) {
                a += i;
            }
            System.out.println(a);
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

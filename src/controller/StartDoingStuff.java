package controller;

import map.Intersection;
import map.Road;
import map.TrafficLight;
import ui.ControllerUI;

public class StartDoingStuff {
    
    public static void main(String[] args) {
        Controller control = Controller.getInstance();
        TestInter a = new TestInter(100,100);
        TestInter b = new TestInter(200,100);
        TestInter c = new TestInter(100,200);
        TestRoad d = new TestRoad(a, b, 100);
        TestRoad e = new TestRoad(a, c, 100);
        
        
        control.getMap().addIntersection(a);
        control.getMap().addIntersection(b);
        control.getMap().addIntersection(c);
        control.getMap().addRoad(d);
        control.getMap().addRoad(e);
        Ticker t = new Ticker(20);

        new ControllerUI(t);
        t.start("tick");
        
    }
    
    public static class TestRoad extends Road{

        public TestRoad(Intersection start, Intersection end, double length) {
            super(start, end, length);
        }
        
    }
    
    public static class TestInter extends Intersection{

        public TestInter(int x, int y) {
            super(x, y);
        }
        
    }
    
}

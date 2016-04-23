package algorithms.trafficlights.greedy;

import controller.Ticker;
import java.util.ArrayList;
import map.Intersection;
import map.Road;
import map.TrafficLight;
import map.intersection.DefaultIntersection;
import map.intersection.detectors.Detector;

/**
 * Hopefully a intersection which is smart that act all greedy like
 *
 * @author Kareem Horstink
 */
public class GreedyIntersection extends DefaultIntersection {
    
    private static Detector type;

    public GreedyIntersection(int x, int y, Ticker t) {
        super(x, y, t);
    }

    @Override
    public void updateLight(double elapsed) {
        super.updateLight(elapsed); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Road addRoad(Road r) {
        getRoads().add(r);
        boolean flag = false;
        if (r.getStart() == this) {
            getOut().add(r);
            flag = true;
        } else {
            getIn().add(r);
        }

        if (flag) {
            getTrafficLights().stream().forEach((trafficLight) -> {
                trafficLight.addOut(r);
            });
        } else {
            getTrafficLights().add(new GreedyTrafficLight(this, r, getOut()));
        }

        return r;
    }

    private static class GreedyTrafficLight extends TrafficLight {

        public GreedyTrafficLight(Intersection i, Road in, ArrayList<Road> out) {
            super(i, in, out);
        }

    }

}

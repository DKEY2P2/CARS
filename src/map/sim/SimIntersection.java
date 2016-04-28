package map.sim;

import algorithms.trafficlights.SmartTrafficLight;
import map.Intersection;
import map.Road;
import map.TrafficLight;

/**
 * A simulated intersection. Meant for benchmarking
 *
 * @author Kareem
 */
public class SimIntersection extends Intersection {

    public SimIntersection() {
        super(0, 0);
    }

    @Override
    public Road addRoad(Road r) {
        getTrafficLights().add(new SimTrafficLight(new SimDetector()));
        return null;
    }

    @Override
    public TrafficLight addTrafficLight(TrafficLight tl) {
        return null;
    }

}

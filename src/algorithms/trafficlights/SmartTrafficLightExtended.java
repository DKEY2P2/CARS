package algorithms.trafficlights;

import algorithms.trafficlights.greedy.*;
import algorithms.trafficlights.SmartTrafficLight;
import java.util.ArrayList;
import map.Intersection;
import map.Road;
import map.intersection.detectors.Detector;

/**
 * Adds one property to the smart traffic light which is that it now
 * measures how long it has been green for
 */
public class SmartTrafficLightExtended extends SmartTrafficLight {
    /**
     * How long the light has been green for
     */
    double currentGreenTime;

    /**
     * Get the value of currentGreenTime
     *
     * @return the value of currentGreenTime
     */
    public double getCurrentGreenTime() {
        return currentGreenTime;
    }

    /**
     * Set the value of currentGreenTime
     *
     * @param currentGreenTime new value of currentGreenTime
     */
    public void setCurrentGreenTime(double currentGreenTime) {
        this.currentGreenTime = currentGreenTime;
    }

    /**
     * Increments the value of currentGreenTime
     *
     * @param currentGreenTime how to be added
     */
    public void incrementCurrentGreenTime(double currentGreenTime) {
        this.currentGreenTime += currentGreenTime;
    }

    /**
     * Creates the smart traffic light
     *
     * @param i The intersection the traffic light should be made one
     * @param in The road going into the traffic light
     * @param out All the roads going out a traffic light
     * @param detector The detector to be used ~ Going to be cloned
     */
    public SmartTrafficLightExtended(Intersection i, Road in, ArrayList<Road> out, Detector detector) {
        super(i, in, out, detector);
    }
    
}

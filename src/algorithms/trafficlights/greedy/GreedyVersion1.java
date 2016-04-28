package algorithms.trafficlights.greedy;

import algorithms.trafficlights.SmartTrafficLight;
import controller.Ticker;
import java.util.ArrayList;
import map.Road;
import map.TrafficLight;
import map.intersection.DefaultIntersection;
import map.intersection.detectors.Detector;

/**
 * Hopefully a intersection which is smart that act all greedy like
 * <p>
 * Ignores everything and only depends on the number cars currently waiting at a
 * light
 *
 * @author Kareem Horstink
 */
public class GreedyVersion1 extends DefaultIntersection {

    private static Detector type;

    /**
     * Gets the type of detector that the intersection would use
     *
     * @return An empty detector
     */
    public static Detector getType() {
        return type;
    }

    /**
     * Set the type of detector that the intersection should use
     *
     * @param type The type of detector the intersection should use. Note that
     * it is going to use an empty clone of the detector
     */
    public static void setType(Detector type) {
        GreedyVersion1.type = type;
    }

    /**
     * The default constructor of the Greedy intersection. Same as the default
     * intersection
     *
     * @param x The x location of the intersection
     * @param y The y location of the intersection
     * @param t The ticker which would update the intersection
     */
    public GreedyVersion1(int x, int y, Ticker t) {
        super(x, y, t);
    }

    @Override
    public void updateLight(double elapsed) {
        ArrayList<TrafficLight> tLights = getTrafficLights();
        //Don't bother when there is no traffic light
        if (tLights.isEmpty()) {
            return;
        }

        if (tLights.size() > 1) {
            //Get the index of the traffic light with the most cars waiting via the detector of the smart traffic light
            int indexWithMostCars = -1;
            for (int i = 0; i < tLights.size(); i++) {
                SmartTrafficLight light = (SmartTrafficLight) tLights.get(i);
                indexWithMostCars = light.getDetector().getNumberOfCarsDetected() > indexWithMostCars ? i : indexWithMostCars;
                light.setLight(false);//Sets all the light to red to ensure that there is only one light which is green
            }
            //Set the traffic light with the most cars to be green
            tLights.get(indexWithMostCars).setLight(true);
        } else {
            //Set it green if there is only one light
            tLights.get(0).setLight(true);
        }

    }

    @Override
    public Road addRoad(Road r) {
        //Only change is that it now uses smart traffic lights instead of the default ones
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
            getTrafficLights().add(new SmartTrafficLight(this, r, getOut(), type));
        }

        return r;
    }

}

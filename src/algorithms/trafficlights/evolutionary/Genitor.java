package algorithms.trafficlights.evolutionary;

import algorithms.trafficlights.SmartTrafficLight;
import controller.Ticker;
import java.util.ArrayList;
import map.Road;
import map.TrafficLight;
import map.intersection.DefaultIntersection;
import map.intersection.detectors.Detector;

/**
 *
 * @author Kareem Horstink
 */
public class Genitor extends DefaultIntersection {

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
        Genitor.type = type;
    }

    public Genitor(int x, int y, Ticker t) {
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

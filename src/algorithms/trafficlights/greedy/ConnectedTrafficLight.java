package algorithms.trafficlights.greedy;

import controller.Controller;
import controller.SimulationSettings;
import java.util.ArrayList;
import map.Intersection;
import map.Road;
import map.intersection.detectors.Detector;

/**
 *
 * @author Imray
 */
public class ConnectedTrafficLight extends SmartTrafficLightExtended {

    public ConnectedTrafficLight(Intersection i, Road in, ArrayList<Road> out, Detector detector) {
        super(i, in, out, detector);
    }

    /**
     * Time needed to for a car to travel from the previous traffic light to the
     * current traffic light in ticks
     *
     * @return Time needed in ticks
     */
    public int getTimeNeeded() {
        double length = getIn().getLength();
        double speedLimit = getIn().getSpeedLimit();
        //Maybe
        return (int) (length / speedLimit / Controller.getInstance().getTicker().getTickTimeInS());
    }

}

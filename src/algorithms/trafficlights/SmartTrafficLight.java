package algorithms.trafficlights;

import java.util.ArrayList;
import map.Intersection;
import map.Road;
import map.TrafficLight;
import map.intersection.detectors.Detector;

/**
 * A traffic light with a detector
 *
 * @author Kareem Horstink
 */
public class SmartTrafficLight extends TrafficLight {

    private final Detector detector;

    /**
     * Creates the smart traffic light
     * @param i The intersection the traffic light should be made one
     * @param in The road going into the traffic light
     * @param out All the roads going out a traffic light
     * @param detector The detector to be used ~ Going to be cloned
     */
    public SmartTrafficLight(Intersection i, Road in, ArrayList<Road> out, Detector detector) {
        super(i, in, out);
        this.detector = detector.clone(this);
    }

    /**
     * Get the detector
     * @return The detector for this traffic light
     */
    public Detector getDetector() {
        return detector;
    }

}

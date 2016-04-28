package map.intersection.detectors;

import helper.Logger;
import map.TrafficLight;

/**
 * A detector that is perfect and has no error. More as debugging and test
 * purpose rather than a real detector though this would become the case the
 * moment that every car becomes autonomous cars.
 * <p>
 * Note, this won't work when there is a traffic jam on the roads ~ or its less
 * accurate
 *
 * @author Kareem Horstink
 */
public class PerfectDetector implements Detector, Cloneable {
    //The traffic light that the detector is attached to
    private TrafficLight trafficLight;

    public PerfectDetector() {
    }

    @Override
    public int getNumberOfCarsDetected() {
        return (int) trafficLight.
                getIn().
                getVehicles().
                stream().
                filter((vehicle) -> vehicle.getSpeed() < 1).
                count();
    }

    @Override
    public double getProbablityOfBeingCorrect() {
        return 1;
    }

    @Override
    public int getMaximumAmountOfCarThatCanBeDetected() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Detector clone() {
        PerfectDetector detector = null;
        try {
            detector = (PerfectDetector) super.clone();
            System.err.println("You should call PerfectDetector.clone(trafficLight) not this method");
        } catch (CloneNotSupportedException ex) {
            Logger.LogError(ex);
        }
        return detector;
    }

    @Override
    public Detector clone(TrafficLight t) {
        PerfectDetector detector = null;
        try {
            detector = (PerfectDetector) super.clone();
            detector.trafficLight = t;
        } catch (CloneNotSupportedException ex) {
            Logger.LogError(ex);
        }
        return detector;
    }

}

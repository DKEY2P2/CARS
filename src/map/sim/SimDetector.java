package map.sim;

import map.TrafficLight;
import map.intersection.detectors.Detector;
import map.intersection.detectors.PerfectDetector;

/**
 * Simulates a perfect detector
 *
 * @author Kareem
 */
public class SimDetector extends PerfectDetector {

    private int wantedOutput;

    /**
     * Get the value of wantedOutput
     *
     * @return the value of wantedOutput
     */
    public int getWantedOutput() {
        return wantedOutput;
    }

    /**
     * Set the value of wantedOutput
     *
     * @param wantedOutput new value of wantedOutput
     */
    public void setWantedOutput(int wantedOutput) {
        this.wantedOutput = wantedOutput;
    }

    public SimDetector() {
    }

    @Override
    public int getNumberOfCarsDetected() {
        return wantedOutput;
    }

    
    
    @Override
    public Detector clone() {
        return new SimDetector();
    }

    @Override
    public Detector clone(TrafficLight t) {
        return new SimDetector();
    }
    
    

}

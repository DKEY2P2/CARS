package map.intersection.detectors;

import map.TrafficLight;

/**
 * A simple framework for detectors to use and implement
 * <p>
 * Currently only has two methods, please add more if you can think of anything
 *
 * @author Kareem Horstink
 */
public interface Detector {

    /**
     * Get the number of cars that the detector detects
     *
     * @return The number of cars that it detects
     */
    public int getNumberOfCarsDetected();

    /**
     * The probability that the detector is correct
     *
     * @return A number between 0 and 1
     *
     */
    public double getProbablityOfBeingCorrect();//@TODO Make a better name

    /**
     * Get the maximum amount of cars that the detector can detects
     *
     * @return The maximum amount of cars that the detector can detect (to
     * accurate level)
     */
    public int getMaximumAmountOfCarThatCanBeDetected();

    /**
     * So we are able to clone the detector, makes it easier to dictate what
     * detector to use when in start doing stuff
     *
     * @return A <b>empty</b> clone of the detector
     */
    public Detector clone();

    /**
     * So we are able to clone the detector, makes it easier to dictate what
     * detector to use when in start doing stuff
     *
     * @param t The traffic light the detector has to detect on
     * @return A <b>empty</b> clone of the detector
     */
    public Detector clone(TrafficLight t);

}

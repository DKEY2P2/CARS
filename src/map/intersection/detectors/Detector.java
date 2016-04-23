package map.intersection.detectors;

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
     * @TODO Make a better name
     */
    public double getProbablityOfBeingCorrect();

    /**
     * Get the maximum amount of cars that the detector can detects
     *
     * @return The maximum amount of cars that the detector can detect (to accurate level)
     */
    public int getMaximumAmountOfCarThatCanBeDetected();

}

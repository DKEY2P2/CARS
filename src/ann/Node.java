package ann;

/**
 * A class that just contains a simple double. Don't really need it to be honest
 * but I made it just so the other class are <I>"easier"</I> to understand
 *
 * @author Kareem Horstink
 */
public class Node {

    /**
     * Bla
     */
    protected Node() {
    }

    /**
     * The value of the node
     */
    private double currentValue;

    /**
     * Get the value of currentValue
     *
     * @return the value of currentValue
     */
    public double getCurrentValue() {
        return currentValue;
    }

    /**
     * Set the value of currentValue
     *
     * @param currentValue new value of currentValue
     */
    public void setCurrentValue(double currentValue) {
        this.currentValue = currentValue;
    }

}

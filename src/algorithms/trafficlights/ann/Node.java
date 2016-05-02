package algorithms.trafficlights.ann;

/**
 * A class that just contains 2 doubles. Don't really need it to be honest but I
 * made it just so the other class are <I>"easier"</I> to understand
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

    private double delta = 0;

    /**
     * Get the value of delta
     *
     * @return the value of delta
     */
    public double getDelta() {
        return delta;
    }

    /**
     * Set the value of delta
     *
     * @param delta new value of delta
     */
    public void setDelta(double delta) {
        this.delta = delta;
    }

    @Override
    public String toString() {
        return Double.toString(getCurrentValue());
    }
    
    
    public String getValueString(){
        return Double.toString(getCurrentValue());
    }
    public String getErrorString(){
        return Double.toString(getDelta());
    }

}

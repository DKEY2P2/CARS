package algorithms.trafficlights.evolutionary;

import java.util.BitSet;

/**
 * The abstract class that is meant to allow two or more parents to create a
 * child
 *
 * @author Kareem Horstink
 */
public abstract class BreedingMethod {

    public abstract BitSet breed(Individual... parenent);

    private float mutationRate = 0.1f;

    /**
     * Get the value of mutationRate
     *
     * @return the value of mutationRate
     */
    public float getMutationRate() {
        return mutationRate;
    }

    /**
     * Set the value of mutationRate
     *
     * @param mutationRate new value of mutationRate
     */
    public void setMutationRate(float mutationRate) {
        this.mutationRate = mutationRate;
    }

}

package algorithms.trafficlights.evolutionary;

import java.util.BitSet;

/**
 * What an individual should have
 *
 * @author Kareem Horstink
 */
public abstract class Individual {

    public abstract void encode(double... data);

    public abstract BitSet getBit();

    public abstract double getFitness();

}

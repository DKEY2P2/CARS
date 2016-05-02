package algorithms.trafficlights.evolutionary;

import helper.Logger;
import java.util.BitSet;

/**
 * A individual that is defined by this paper
 * http://paper.ijcsns.org/07_book/200902/20090212.pdf which is just the
 * encoding of how long the light has been green and red
 *
 * @author Kareem Horstink
 */
public class SimpleIndividual extends Individual {

    @Override
    public void encode(double... data) {
        Logger.LogError("Not supported yet.", this);
    }

    @Override
    public BitSet getBit() {
        Logger.LogError("Not supported yet.", this);
        return null;
    }

    @Override
    public double getFitness() {
        Logger.LogError("Not supported yet.", this);
        return Double.NaN;
    }

}

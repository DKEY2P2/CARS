package algorithms.trafficlights.evolutionary;

import java.util.List;

/**
 * The abstract class that is bases of how we select the population
 *
 * @author Kareem Horstink
 */
public abstract class SelectionMethod {

    public abstract Individual[] select(int number, Individual[] population);

    public Individual[] select(int number, List<Individual> population) {
        return select(number, population.toArray(new Individual[1]));
    }

}

package algorithms;

import map.Intersection;
/**
 * An interface for swapping out what the heuristic for calculating the hcost is
 * @author jvacek
 *
 */
public interface AStarHeuristicCriteria {
    /**
     * The value specific to the heuristic that determines the algorithm's outcome multiplied by an appropriate scaling factor
     */
    public double scalingfactor=0;
    /**
     * The method that calculates the value of H for the node that requests it
     * @param start
     * @param end
     * @return a double of the "approximate" distance it takes from start to end
     */
    public double calculateHCost(Intersection start, Intersection end);
}

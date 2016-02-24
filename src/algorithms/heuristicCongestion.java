package algorithms;

import map.Intersection;

public class heuristicCongestion implements AStarHeuristicCriteria{

    //TODO determine a better value for this
    public static double scalingFactor = 1;
    public static double calculateHCost(Intersection start, Intersection end) {
	//TODO determine what's a good way to give this.
	return scalingFactor*0;
    }

}

package algorithms;

import map.Intersection;
/**
 * Calculates the hCost based on the 
 * @author jvacek
 *
 */
public class heuristicXY implements AStarHeuristicCriteria {
    
    @Override
    public double calculateHCost(Intersection start, Intersection end) {
	//TODO check if my pythagoras actually works
	return ((start.getX()-end.getX())*(start.getX()-end.getX()) + (start.getY()-end.getY())*(start.getY()-end.getY()) );
    }
    
}

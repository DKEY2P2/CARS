package algorithms.pathfinding;

import map.Intersection;

import java.util.ArrayList;

/**
 * This is an interface for the different implementations of the path finding algorithms we will use
 * @author Lucas Vanparijs
 * @since 22-02-16
 */
public interface Algorithm {

    public ArrayList<Intersection> findShortestPath(Intersection start, Intersection end);
}

package map;

import java.util.ArrayList;

/**
 * This Class represents a road network on which we can simulate traffic A map
 * is very similar to a directed graph
 *
 * @author Lucas Vanparijs
 * @since 18-02-2016
 */
public class Map {
    
    /**
     * ArrayList storing all the roads in the map
     */
    private ArrayList<Road>	    ar = new ArrayList<Road>();
    /**
     * ArrayList storing all the Intersection in the map
     */
    private ArrayList<Intersection> ai = new ArrayList<Intersection>();
				       
    /**
     * Constructor of the map, PRONE TO CHANGE
     *
     * @param ar The roads that make up the map
     * @param ai The intersections that make up the map
     */
    public Map(ArrayList<Road> ar, ArrayList<Intersection> ai) {
	this.ar = ar;
	this.ai = ai;
    }
    
    /**
     * Returns true if there is a Road in between the two intersections
     *
     * @param i1 Intersection
     * @param i2 Intersection
     * @return True if there is a Road in between the 2 Intersection, false if
     *         there isn't
     */
    public boolean adjacent(Intersection i1, Intersection i2) {
	for (Road r : ar) {
	    if ((r.getStart() == i1 && r.getEnd() == i2) || (r.getStart() == i2 && r.getEnd() == i1)) {
		return true;
	    }
	}
	return false;
    }
    
    /**
     * Returns all the neighbouring Intersections(The ones that share a Road)
     * with the input
     *
     * @param i The intersection of which we want to get the neighbours
     * @return A list of all neighbouring intersections
     */
    public ArrayList<Intersection> neighbours(Intersection i) {
	ArrayList<Intersection> neighbours = new ArrayList<Intersection>();
	for (Road r : ar) {
	    if (r.getStart() == i) {
		neighbours.add(r.getEnd());
	    } else if (r.getEnd() == i) {
		neighbours.add(r.getStart());
	    }
	}
	return neighbours;
    }
    
    /**
     * Adds an intersection to the map
     *
     * @param i the Intersection to be added
     * @return the added Intersections
     */
    public Intersection addIntersection(Intersection i) {
	ai.add(i);
	return i;
    }
    
    /**
     * Removes a certain intersection from the map
     *
     * @param i the intersection to be removed
     * @return the removed intersection
     */
    public Intersection removeIntersection(Intersection i) {
	ai.remove(i);
	return i;
    }
    
    /**
     * Returns all the intersections on the map
     *
     * @return All intersections
     */
    public ArrayList<Intersection> getIntersections() {
	return ai;
    }
    
    /**
     * Adds a road to the map
     *
     * @param r the road to be added
     * @return the added road
     */
    public Road addRoad(Road r) {
	ar.add(r);
	return r;
    }
    
    /**
     * Removes a certain road from the map
     *
     * @param r the road to be removed
     * @return the removed road
     */
    public Road removeRoad(Road r) {
	ar.remove(r);
	return r;
    }
    
    /**
     * Returns all the roads on the map
     *
     * @return All the roads on the map
     */
    public ArrayList<Road> getRoads() {
	return ar;
    }
    
    /**
     * Finds the closet intersection
     *
     * @param x
     * @param y
     * @return
     */
    public Intersection findClosestIntersection(int x, int y) {
	
	ArrayList<Intersection> r = getIntersections();
	double min = +Double.MAX_VALUE;
	int index = 0;
	int i = 0;
	for (Intersection r1 : r) {
	    double tmp = Math.sqrt(Math.pow(r1.getX() - x, 2) + Math.pow(y - r1.getY(), 2));
	    if (tmp < min) {
		min = tmp;
		index = i;
	    }
	    i++;
	}
	return r.get(index);
    }
    
    /**
     * Generates a coordinate which returns the average x and y coordinate of
     * all the intersections present on the map
     * 
     * @return Array with two numbers, being the x and y coordinate
     */
    public int[] midpoint() {
	int sumX = 0;
	int sumY = 0;
	for (Intersection i : this.ai) {
	    sumX += i.getX();
	    sumY += i.getY();
	}
	int[] ret = {(sumX / this.ai.size()), sumY / this.ai.size()};
	return ret;
	
    }
}

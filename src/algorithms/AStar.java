package algorithms;

import java.util.ArrayList;

import controller.Controller;
import map.Intersection;
import map.Map;
import map.Road;

/**
 * Uses the A* Algorithm to find the shortest path in the graph.
 * 
 * @author jvacek
 * @see <a href="http://web.mit.edu/eranki/www/tutorials/search/">web.mit.edu
 *      </a>
 */
public class AStar implements Algorithm {
    
    /**
     * Little object that stores my A* data
     * 
     * @author jvacek
     */
    public class Twinkle {
	private double	     gCost = 0;
	private double	     hCost = 0;
	private double	     fCost = 0;
	private Intersection element;
			     
	public Twinkle(Intersection e) {
	    setElement(e);
	    
	}
	
	public double getgCost() {
	    return gCost;
	}
	
	public void setgCost(double gCost) {
	    this.gCost = gCost;
	}
	
	public double gethCost() {
	    return hCost;
	}
	
	public void sethCost(double hCost) {
	    this.hCost = hCost;
	}
	
	public double getfCost() {
	    return fCost;
	}
	
	public void setfCost(double fCost) {
	    this.fCost = fCost;
	}
	
	public Intersection getElement() {
	    return element;
	}
	
	public void setElement(Intersection element) {
	    this.element = element;
	}
    }
    
    @Override
    public ArrayList<Intersection> findShortestPath(Intersection start, Intersection end) {
	//Setting up what we'll be inputting the final result into
	ArrayList<Intersection> result = new ArrayList<Intersection>();
	//Retrieving the map through the Controller singleton
	Map map = Controller.getInstance().getMap();
	
	// A*
	//initialize the open list
	ArrayList<Twinkle> open = new ArrayList<Twinkle>();
	//initialize the closed list
	ArrayList<Twinkle> closed = new ArrayList<Twinkle>();
	//put the starting node on the open list (you can leave its f at zero)
	open.add(new Twinkle(start));
	//while the open list is not empty
	while (open.isEmpty() == false) {
	    //    find the node with the least f on the open list, call it "q"
	    double min = Double.MAX_VALUE;
	    // TODO check if this null is "OK". THis shouldn't be empty as I just checked for that, and all fCosts must be lower than Double.Max but who the fuck knows what goes on inside these things
	    Twinkle q = null;
	    for (Twinkle t : open) {
		if (t.getfCost() < min) {
		    min = t.getfCost();
		    q = t;
		}
	    }
	    //    pop q off the open list
	    open.remove(q);
	    //    generate q's 8 successors and set their parents to q
	    for (Road r :q.getElement().getRoads()){
		System.out.println();
	    }
	    //    for each successor
	    //    	if successor is the goal, stop the search
	    //        successor.g = q.g + distance between successor and q
	    //        successor.h = distance from goal to successor
	    //        successor.f = successor.g + successor.h
	    //
	    //        if a node with the same position as successor is in the OPEN list \
	    //            which has a lower f than successor, skip this successor
	    //        if a node with the same position as successor is in the CLOSED list \ 
	    //            which has a lower f than successor, skip this successor
	    //        otherwise, add the node to the open list
	    //    end
	}
	//    push q on the closed list
	//end
	
	return result;
    }
    
}

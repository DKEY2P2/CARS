package algorithms;

import java.util.ArrayList;

import map.Intersection;
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
     * The heuristic that will be used to determine the shortest distance, see
     * heuristicXY for more
     */
    private AStarHeuristicCriteria heuristic = new heuristicXY();
    
    /**
     * This object holds the costs, parent and children of an intersection. It
     * is used for calculating the shortest path
     * 
     * @author jvacek
     */
    public class Twinkle {

        @Override
        public String toString() {
            return element.toString();
        }
        
	private double		   gCost = 0;
	private double		   hCost = 0;
	private double		   fCost = 0;
	private Intersection	   element;
	private Twinkle		   parent;
	private ArrayList<Twinkle> children;
				   
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
	
	public Twinkle getParent() {
	    return parent;
	}
	
	public void setParent(Twinkle parent) {
	    this.parent = parent;
	}
	
	public void addChild(Twinkle t) {
	    this.children.add(t);
	}
	
	public void removeChild(Twinkle t) {
	    try {
		this.children.remove(t);
	    } catch (NullPointerException e) {
		System.out.println(e);
	    }
	}
	
	public ArrayList<Twinkle> getChildren() {
	    return this.children;
	}
	
    }
    
    @Override
    public ArrayList<Intersection> findShortestPath(Intersection start, Intersection end) {
	
	/**
	 * A list of intersection that sequentially describes how the vehicle
	 * needs to go
	 */
	ArrayList<Intersection> result = new ArrayList<Intersection>();
	
	/**
	 * The map of the road retrieved from the singleton controller
	 */
//	Map map = Controller.getInstance().getMap();
	
	/**
	 * The open list, this contains all the currently discovered nodes still
	 * to be evaluated. It contains the node where the car starts initially
	 */
	ArrayList<Twinkle> open = new ArrayList<Twinkle>();
	open.add(new Twinkle(start));
	
	/**
	 * The set of nodes that are already evaluated
	 */
	ArrayList<Twinkle> closed = new ArrayList<Twinkle>();
	
	while (open.isEmpty() == false) {
	    double min = Double.MAX_VALUE; //find the node with the least f on the open list, call it "q"
	    Twinkle q = null; //TODO check if this null is "OK". THis shouldn't be empty as I just checked for that, and all fCosts must be lower than Double.Max but who the fuck knows what goes on inside these things
	    for (Twinkle t : open) {
		if (t.getfCost() < min) {
		    min = t.getfCost();
		    q = t;
		}
	    }
	    
	    open.remove(q); //pop q off the open list
	    
	    ArrayList<Twinkle> successor = new ArrayList<Twinkle>(); //generate q's 8 successors and set their parents to q
	    for (Intersection i : q.getElement().getNeighbours()) {
		Twinkle t_successor = new Twinkle(i);
		t_successor.setParent(q);
		successor.add(t_successor);
	    }
    	    for (Twinkle t_successor : successor) { //for each successor
		
		if (t_successor.getElement() == end) { //if successor is the goal, stop the search
		    Twinkle next = t_successor;
		    while (next.getParent() !=null){
			result.add(0, next.getElement());
			next = next.getParent();
		    }
		    //SUCCESS!
		    return result;
		}
		
		double qtD = 0;//successor.g = q.g + distance between successor and q
		for (Road r1 : t_successor.getElement().getRoads()) {
		    for (Road r2 : q.getElement().getRoads()) {
			if (r1 == r2) {
			    qtD = r1.getLength();
			    //TODO check this break only breaks these two forloops and not the whole thing
			    break;
			}
		    }
		}
		
		t_successor.setgCost(q.getgCost() + qtD); //successor.h = distance from goal to successor
		
		t_successor.sethCost(heuristicXY.calculateHCost(t_successor.getElement(), end)); 
		
		t_successor.setfCost(t_successor.getgCost() + t_successor.gethCost()); //successor.f = successor.g + successor.h
		
		boolean skip = false;
		for (Twinkle u : open) {//if a node with the same position as successor is in the OPEN list which has a lower f than successor, skip this successor
		    if ((u.getElement() == t_successor.getElement()) && (u.getfCost() < t_successor.getfCost())) { //TODO is this what they mean by skip?
			skip = true;
		    }
		}
		
		for (Twinkle u : closed) {//if a node with the same position as successor is in the CLOSED list which has a lower f than successor, skip this successor
		    if (u.getElement() == t_successor.getElement()) { //TODO is this what they mean by skip?
			skip = true;
		    }
		}
		if (skip == false) { //otherwise, add the node to the open list
		    open.add(t_successor);
		}
	    }
	    closed.add(q);
	}
	return result;
    }
}

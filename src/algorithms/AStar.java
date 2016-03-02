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
	 * The heuristic that will be used to determine the shortest distance, see heuristicXY for more
	 */
    private AStarHeuristicCriteria heuristic = new heuristicXY(); 

    /**
     * This object holds the costs, parent and children of an intersection. It is used for calculating the shortest path
     * @author jvacek
     */
    public class Twinkle {
	private double		   		gCost      = 0;
	private double		   		hCost      = 0;
	private double		   		fCost      = 0;
	private Intersection	  	element;
	private Twinkle		   		parent;
	private ArrayList<Twinkle> 	children;
				   
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
		 * A list of intersection that sequentially describes how the vehicle needs to go
		 */
		ArrayList<Intersection> result = new ArrayList<Intersection>();

		/**
		 * The map of the road retrieved from the singleton controller
		 */
		Map map = Controller.getInstance().getMap();

		/**
		 * The open list, this contains all the currently discovered nodes still to be evaluated. It contains the node where the car starts initially
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
				Twinkle t = new Twinkle(i);
				t.setParent(q);
				successor.add(t);
			}
			for (Twinkle t : successor) { //for each successor

				if (t.getElement() == end) { //if successor is the goal, stop the search
					return result;
				}
				double qtD = 0;//successor.g = q.g + distance between successor and q
				for (Road r1 : t.getElement().getRoads()){
					for (Road r2 : q.getElement().getRoads()){
						if (r1==r2){
							qtD = r1.getLength();
							//TODO check this break only breaks these two forloops and not the whole thing
							break;
						}
					}
				}
				t.setgCost(q.getgCost()+ qtD); //successor.h = distance from goal to successor

				t.sethCost(heuristicXY.calculateHCost(t.getElement(), end)); //successor.f = successor.g + successor.h

				t.setfCost(t.getgCost() + t.gethCost());//if a node with the same position as successor is in the OPEN list which has a lower f than successor, skip this successor

				boolean skip = false;
				for (Twinkle u : open) {
					if ((u.getElement() == t.getElement()) && (u.getfCost() < t.getfCost())) { //TODO is this what they mean by skip?
						skip = true;
					}
				}

				for (Twinkle u : closed) {//if a node with the same position as successor is in the CLOSED list which has a lower f than successor, skip this successor
					if (u.getElement() == t.getElement()) { //TODO is this what they mean by skip?
						skip = true;
					}
				}
				if (skip==false){ //otherwise, add the node to the open list
					open.add(t);
				}
			}
			closed.add(q);
		}
		return result;
	}
}

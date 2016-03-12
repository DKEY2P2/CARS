package algorithms;


import controller.Controller;
import map.Intersection;
import map.Map;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author Lucas Vanparijs, Jonas Vacek
 *  _    _ _   _ _____  ______ _____     _____ ____  _   _  _____ _______ _____  _    _  _____ _______ _____ ____  _   _
 * | |  | | \ | |  __ \|  ____|  __ \   / ____/ __ \| \ | |/ ____|__   __|  __ \| |  | |/ ____|__   __|_   _/ __ \| \ | |
 * | |  | |  \| | |  | | |__  | |__) | | |   | |  | |  \| | (___    | |  | |__) | |  | | |       | |    | || |  | |  \| |
 * | |  | | . ` | |  | |  __| |  _  /  | |   | |  | | . ` |\___ \   | |  |  _  /| |  | | |       | |    | || |  | | . ` |
 * | |__| | |\  | |__| | |____| | \ \  | |___| |__| | |\  |____) |  | |  | | \ \| |__| | |____   | |   _| || |__| | |\  |
 *  \____/|_| \_|_____/|______|_|  \_\  \_____\____/|_| \_|_____/   |_|  |_|  \_\\____/ \_____|  |_|  |_____\____/|_| \_|
 * @since 09/03/16
 *
 * I use the Euclidean distance as heuristic
 */

public class AStar implements Algorithm {

	ArrayList<Quadruplet<Intersection, Double, Double, Intersection>> aT = new ArrayList<Quadruplet<Intersection, Double, Double, Intersection>>();		//All the Intersections in the Map
	Queue<Quadruplet<Intersection,Double,Double,Intersection>> closedSet;	//Already evaluated nodes
	Queue<Quadruplet<Intersection,Double,Double,Intersection>> openSet;	//Currently discovered nodes still to be evaluated

	@Override
	public ArrayList<Intersection> findShortestPath(Intersection start, Intersection end) {

		Map m = Controller.getInstance().getMap();
		closedSet = new PriorityQueue<>(m.getIntersections().size(),new QuadrupletComparator());
		openSet = new PriorityQueue<>(m.getIntersections().size(),new QuadrupletComparator());

		for (int i = 0; i < m.getIntersections().size(); i++) {
			if (m.getIntersections().get(i) == start) {
				Quadruplet<Intersection, Double, Double, Intersection> t = new Quadruplet<>(start, 0d, euclideanDistance(start, end),start);
				aT.add(t);
				openSet.add(t);
			} else {
				aT.add(new Quadruplet<>(m.getIntersections().get(i), Double.MAX_VALUE, Double.MAX_VALUE,null));
			}
		}

		while(!openSet.isEmpty()){
			Quadruplet<Intersection, Double, Double, Intersection> current = openSet.poll();
			closedSet.add(current);

			if(current.getA() == end){
				return reconstructPath(current);
			}

			for(Quadruplet<Intersection, Double, Double, Intersection> neighbour : getNeighbours(aT,current)){
				double tmpG = current.getB() + euclideanDistance(current.getA(),neighbour.getA());
				if(closedSet.contains(neighbour))
					if(tmpG >= neighbour.getB())
						continue;

				if(!openSet.contains(neighbour) || tmpG < neighbour.getB()){
					neighbour.setD(current.getA());
					neighbour.setB(tmpG);
					neighbour.setC(neighbour.getB() + euclideanDistance(neighbour.getA(),end));
					if(!openSet.contains(neighbour)){
						openSet.add(neighbour);
					}
				}
			}
		}
		return null;

	}

	/*@Override
	public ArrayList<Intersection> findShortestPath(Intersection start, Intersection end) {

		s = start;

		if(start == end){
			return null; //If it fails
		}else {

			Map m = Controller.getInstance().getMap();
			aT = new ArrayList<Quadruplet<Intersection, Double, Double, Intersection>>();
			closedSet = new PriorityQueue<>(m.getIntersections().size());
			openSet = new PriorityQueue<>(m.getIntersections().size());

			for (int i = 0; i < m.getIntersections().size(); i++) {
				if (m.getIntersections().get(i) == start) {
					Quadruplet<Intersection, Double, Double, Intersection> t = new Quadruplet<>(start, 0d, euclideanDistance(start, end),null);
					aT.add(t);
					openSet.add(t);
				} else {
					aT.add(new Quadruplet<>(m.getIntersections().get(i), Double.MAX_VALUE, Double.MAX_VALUE,null));
				}
			}

			while (!openSet.isEmpty()) {
				Quadruplet<Intersection, Double, Double, Intersection> current = getLowest(openSet, "f");
				if (current.getA() == end) {
					return reconstructPath(current);
				}

				openSet.remove(current);
				closedSet.add(current);

				for (Quadruplet<Intersection, Double, Double, Intersection> n : getNeighbours(aT, current)) {
					if (closedSet.contains(n))
						continue;

					double tmpG = current.getB() + euclideanDistance(current.getA(), n.getA());

					if (!openSet.contains(n))
						openSet.add(n);
					else if (tmpG >= n.getB())
						continue;

					n.setD(current.getA());
					n.setB(tmpG);
					n.setC(n.getB() + euclideanDistance(n.getA(), end));
				}

			}
		}
		return null; //If it fails
	}*/

	public ArrayList<Intersection> reconstructPath(Quadruplet<Intersection,Double,Double,Intersection> cur){
		ArrayList<Intersection> totalPath = new ArrayList<>();
		Quadruplet<Intersection,Double,Double,Intersection> parent = cur;

		for (Quadruplet<Intersection, Double, Double, Intersection> q : aT) {
			if (q.getA() == cur.getD()) {
				parent = q;
			}
		}

		if(aT.contains(parent) && parent != cur){
			totalPath = reconstructPath(parent);
			totalPath.add(cur.getA());
			return totalPath;
		}else{
			totalPath.add(cur.getA());
			return totalPath;
		}
	}

	public double euclideanDistance(Intersection i1, Intersection i2){
		return Math.sqrt(Math.pow(i1.getX()-i2.getX(),2)+Math.pow(i1.getY()-i2.getY(),2));
	}

	public Quadruplet<Intersection,Double,Double,Intersection> getLowest(Queue<Quadruplet<Intersection,Double,Double,Intersection>> pq, String s){
		Double min = Double.MAX_VALUE;
		Quadruplet<Intersection,Double,Double,Intersection> mindex = null;

		if(s == "g"){
			for(Quadruplet<Intersection,Double,Double,Intersection> t : pq) {
				double d = t.getB();
				if (d < min) {
					min = d;
					mindex = t;
				}
			}
		}else if(s == "f"){
			for(Quadruplet<Intersection,Double,Double,Intersection> t : pq) {
				double d = t.getC();
				if (d < min) {
					min = d;
					mindex = t;
				}
			}
		}else{
			System.err.println("There is no such thing as a "+s+" cost");
		}

		return  mindex;
	}

	public ArrayList<Quadruplet<Intersection,Double,Double,Intersection>> getNeighbours(ArrayList<Quadruplet<Intersection,Double,Double,Intersection>> a, Quadruplet<Intersection,Double,Double,Intersection> t){
		ArrayList<Intersection> ai = Controller.getInstance().getMap().neighbours(t.getA());
		ArrayList<Quadruplet<Intersection,Double,Double,Intersection>> aL = new ArrayList<Quadruplet<Intersection,Double,Double,Intersection>>();

		for(Quadruplet<Intersection,Double,Double,Intersection> ti : a)
			for(Intersection i : ai)
				if(i == ti.getA())
					aL.add(ti);

		return aL;
	}
}

class Quadruplet<T, U, V, W>
{
	T a;
	U b;
	V c;
	W d;

	Quadruplet(T a, U b, V c, W d)
	{
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	T getA(){ return a;}
	U getB(){ return b;}
	V getC(){ return c;}
	W getD(){ return d;}

	void setA(T t){ a = t;}
	void setB(U u){ b = u;}
	void setC(V v){ c = v;}
	void setD(W w){ d = w;}

}

class  QuadrupletComparator implements Comparator< Quadruplet<Intersection,Double,Double,Intersection>> {

	public  QuadrupletComparator(){}

	@Override
	public int compare( Quadruplet<Intersection,Double,Double,Intersection> o1, Quadruplet<Intersection,Double,Double,Intersection> o2) {
		if(o1.getC() < o2.getC())
			return -1;
		else if(o1.getC() == o2.getC())
			return 0;
		else
			return 1;
	}
}
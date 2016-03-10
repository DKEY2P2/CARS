package algorithms;


import controller.Controller;
import map.Intersection;
import map.Map;

import java.util.*;

/**
 * @author Lucas Vanparijs, Jonas Vacek
 *
 * @since 09/03/16
 *
 * I use the Euclidean distance as heuristic
 */

public class AStar implements Algorithm {

	ArrayList<Triplet<Intersection,Double,Double>> aT = new ArrayList<Triplet<Intersection,Double,Double>>();		//All the Intersections in the Map
	ArrayList<Triplet<Intersection,Double,Double>> cameFrom = new ArrayList<Triplet<Intersection,Double,Double>>();	//This will eventually contain the most efficient steps

	Queue<Triplet<Intersection,Double,Double>> closedSet = new ArrayDeque<Triplet<Intersection, Double, Double>>();	//Already evaluated nodes
	Queue<Triplet<Intersection,Double,Double>> openSet = new ArrayDeque<Triplet<Intersection, Double, Double>>();	//Currently discovered nodes still to be evaluated

	@Override
	public ArrayList<Intersection> findShortestPath(Intersection start, Intersection end) {

		Map m = Controller.getInstance().getMap();

		for(int i = 0; i < m.getIntersections().size(); i++){
			if(m.getIntersections().get(i) == start){
				Triplet<Intersection,Double,Double> t = new Triplet<>(start,0d,euclideanDistance(start,end));
				aT.add(t);
				openSet.add(t);
			}else if(m.getIntersections().get(i) == start){
				aT.add(new Triplet<>(end,Double.MAX_VALUE,Double.MAX_VALUE));
			}else{
				aT.add(new Triplet<>(m.getIntersections().get(i),Double.MAX_VALUE,Double.MAX_VALUE));
			}
		}
		cameFrom = aT;

		while(!openSet.isEmpty()){
			Triplet<Intersection,Double,Double> current = getLowest(openSet,"f");
			if(current.getA() == end){
				return reconstructPath(cameFrom, current);
			}

			openSet.remove(current);
			closedSet.add(current);
			for(Triplet<Intersection,Double,Double> n : getNeighbours(aT,current)) {
				if (closedSet.contains(n))
					continue;

				double tmpG = current.getB() + euclideanDistance(current.getA(), n.getA());
				if(!openSet.contains(n))
					openSet.add(n);
				else if(tmpG >= n.getB())
					continue;

				cameFrom.set(aT.indexOf(n),current);
				n.setB(tmpG);
				n.setC(n.getB() + euclideanDistance(n.getA(), end));
			}

		}

		return null; //If it fails
	}

	public ArrayList<Intersection> reconstructPath(ArrayList<Triplet<Intersection,Double,Double>> a, Triplet<Intersection,Double,Double> cur){
		ArrayList<Intersection> totalPath = new ArrayList<Intersection>();
		totalPath.add(cur.getA());

		while(a.contains(cur)){
			cur = a.get(aT.indexOf(cur));
			totalPath.add(cur.getA());
		}
		return totalPath;
	}

	public double euclideanDistance(Intersection i1, Intersection i2){
		return Math.sqrt(Math.pow(i1.getX()-i2.getX(),2)+Math.pow(i1.getY()-i2.getY(),2));
	}

	public Triplet<Intersection,Double,Double> getLowest(Queue<Triplet<Intersection,Double,Double>> pq, String s){
		Double min = Double.MAX_VALUE;
		Triplet<Intersection,Double,Double> mindex = null;

		if(s == "g"){
			for(Triplet<Intersection,Double,Double> t : pq) {
				double d = t.getB();
				if (d < min) {
					min = d;
					mindex = t;
				}
			}
		}else if(s == "f"){
			for(Triplet<Intersection,Double,Double> t : pq) {
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

	public ArrayList<Triplet<Intersection,Double,Double>> getNeighbours(ArrayList<Triplet<Intersection,Double,Double>> a, Triplet<Intersection,Double,Double> t){
		ArrayList<Intersection> ai = Controller.getInstance().getMap().neighbours(t.getA());
		ArrayList<Triplet<Intersection,Double,Double>> aT = new ArrayList<Triplet<Intersection,Double,Double>>();

		for(Triplet<Intersection,Double,Double> ti : a)
			for(Intersection i : ai)
				if(i == ti.getA())
					aT.add(ti);

		return aT;
	}
}

class Triplet<T, U, V>
{
	T a;
	U b;
	V c;

	Triplet(T a, U b, V c)
	{
		this.a = a;
		this.b = b;
		this.c = c;
	}

	T getA(){ return a;}
	U getB(){ return b;}
	V getC(){ return c;}

	void setA(T t){ a = t;}
	void setB(U u){ b = u;}
	void setC(V v){ c = v;}

}
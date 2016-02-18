package map;

import java.util.ArrayList;
import java.util.Queue;

public abstract class Road {
	
	private double maxSpeed;
	/**
	 * The speed limit in km/h
	 */
	private double kilometer;
	/**
	 * length of the road in kilometers
	 */
	private Intersection start;
	/**
	 * starting intersection of the road
	 */
	private Intersection end;
	/**
	 * ending intersection of the road
	 */
	private ArrayList<Road> neighbor;
	/**
	 * The roads that share the same start and end intersection
	 */
	
	public void setMaxSpeed(double speed ) {
		maxSpeed = speed;
	}
	
	public void setLength(double kilometer) {
		this.kilometer=kilometer;
	}
	
	public void Start(Intersection start) {
		this.start = start;
	}
	
	public void End(Intersection end) {
		this.end = end;
	}
	
	public void AddNeighbor(Road road) {
		if(this.start == road.getStart() && this.end == road.getEnd()) {
			neighbor.add(road);
		}
			
	}
	
	public double getMaxSpeed() {
		return this.maxSpeed;
	}

	public double getLength() {
		return this.kilometer;
	}
	
	public Intersection getStart() {
		return this.start;
	}

	public Intersection getEnd() {
		return this.end;
	}
	
	public Queue getQueue() {
		return this.cars;
	}
	
	public ArrayList <Road> getNeighbor() {
		return this.neighbor;
	}
}

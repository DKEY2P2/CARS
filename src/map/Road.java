package map;

import java.util.ArrayList;
import java.util.Queue;

import vehicle.Vehicle;
/**
 * 
 * @author Gianni
 *
 *The abstract class for the roads of the simulation.
 */

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
	
	private Queue<Vehicle> vehicles;
	/**
	 * The number of vehicles on the road
	 */
	
	/**
	 * 
	 * 
	 * setters
	 * 
	 */
	
	/**
	 * @param maxSpeed sets the speed limit of the road 
	 */
	public void setMaxSpeed(double speed ) {
		maxSpeed = speed;
	}
	
	public void Start(Intersection start) {
		this.start = start;
	}
	
	public void End(Intersection end) {
		this.end = end;
	}
	
	public void AddNeighbor(Road road) {
		/*
		 * @param road will only be added to the ArrayList if it shares a start and end node of this road 
		 */
		if(this.start == road.getStart() && this.end == road.getEnd()) {
			neighbor.add(road);
		}
			
	}
	
	/**
	 * 
	 * Getters
	 * 
	 */
	
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
	
	public Queue<Vehicle> getQueue() {
		return this.vehicles;
	}
	
	public ArrayList <Road> getNeighbor() {
		return this.neighbor;
	}
	
	/*
	 * adds a vehicle to the queue
	 */
	public void addVehicle(Vehicle vehicle) {
		vehicles.add(vehicle);
	}
	/*
	 * removes a vehicle from the queue
	 */
	public void removeVehicle(Vehicle vehicle) {
		vehicles.remove();
	}
}

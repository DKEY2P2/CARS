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
	/**
	 * 
	 * @param kilometer sets the length of the road
	 */
	public void setLength(double kilometer) {
		this.kilometer=kilometer;
	}
	/**
	 * 
	 * @param start the beginning intersection of the road
	 */
	public void Start(Intersection start) {
		this.start = start;
	}
	/**
	 * 
	 * @param end the end intersection of the road
	 */
	
	public void End(Intersection end) {
		this.end = end;
	}
	
	public void AddNeighbor(Road road) {
		/**
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
	
	/**
	 * 
	 * @return maxSpeed
	 */
	public double getMaxSpeed() {
		return this.maxSpeed;
	}
	/**
	 * 
	 * @return getLength
	 */
	public double getLength() {
		return this.kilometer;
	}
	/**
	 * 
	 * @return start
	 */
	public Intersection getStart() {
		return this.start;
	}

	
	/**
	 * 
	 * @return end
	 */
	public Intersection getEnd() {
		return this.end;
	}
	/**
	 * 
	 * @return vehicles
	 */
	public Queue<Vehicle> getQueue() {
		return this.vehicles;
	}
	/**
	 * 
	 * @return neighbor
	 */
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

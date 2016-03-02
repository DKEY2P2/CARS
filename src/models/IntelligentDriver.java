package models;

import java.util.AbstractMap.SimpleImmutableEntry;

import controller.Controller;
import map.Road;
import vehicle.Vehicle;

/**
 * http://home2.fvcc.edu/~dhicketh/DiffEqns/Spring11projects/Scott_Miller/
 * Project.pdf
 *
 * @author B
 *
 */
public class IntelligentDriver implements Model{

	private double distanceCars;
	private double[] pcnt;
	double[] positionsRoad;
	double[] lengthCars;
	double[] speedCars;
	double[] timeHeadway;
	double[] desiredVelocity;
	double maxAcceleration;
	double desiredDeceleration;
	double desiredDistance;
	double approachingRate;
	double delta;
	
	/*
	 * x0 (v) denotes position of vehicle 1 
	 * l0 (lengthCars) gives length of vehicle 1 
	 * a -1 (inFrontVehicle) position of vehicle 2 (Infront of vehicle 1) 
	 * v0 (desiredVelocity) Desired velocity 
	 * va (speedCars) velocity of vehicle a 
	 * delta va (approachingRate) is the approaching rate / velocity difference(va - va-1)
	 * s0 (desiredDistance) is minimum desired distance (at least s0 between two cars) 
	 * T is desired time headway (use distance and speed to calculate this desired time between two cars) 
	 * a is (maxVehicleAcceleration) maximum vehicle acceleration 
	 * b is (desiredDeceleration) desired breaking deceleration
	 * delta is acceleration exponent (usually set to 4)
	 * 
	 */

	public void calculate(Vehicle v) {// TODO: Find a better way to find car infront
		Road r = v.getPosition().getKey();
		Vehicle inFrontVehicle = r.getVehicles().getFirst();
		int i = 0;
		for (Vehicle vehicle : r.getVehicles()) {
			if (vehicle == v) {
				break;
			} else {
				inFrontVehicle = vehicle;
			}

		}
		if (inFrontVehicle != null) {
			pcnt = new double[] { v.getPosition().getValue(), inFrontVehicle.getPosition().getValue() };
		} else {
			pcnt = new double[] { v.getPosition().getValue(), 0 };
		}

		if (inFrontVehicle != null) {
			desiredVelocity = new double[] { v.getDesiredSpeed(), inFrontVehicle.getDesiredSpeed() };
			positionsRoad = new double[] { pcnt[0] * r.getLength(), pcnt[1] * r.getLength() };
			distanceCars = positionsRoad[1] - positionsRoad[0];
			lengthCars = new double[] { v.getLength(), inFrontVehicle.getLength() };
			speedCars = new double[] { v.getSpeed(), inFrontVehicle.getSpeed() };
			//maxVehicleAcceleration = new double[] { v.getMaxAcceleration(), inFrontVehicle.getMaxAcceleration() };
			maxAcceleration = v.getMaxAcceleration();
			desiredDeceleration = v.getDesiredBraking();
			//desiredDeceleration = new double[] { v.getDesiredBraking(), inFrontVehicle.getDesiredBraking() };
			delta = 4;
			desiredDistance = v.getDesiredDistance();

			/* !http://nexus.umn.edu/Courses/ce3201/CE3201-L2-02.pdf */
			timeHeadway = new double[] { (speedCars[0] * lengthCars[0]) * distanceCars,(speedCars[1] * lengthCars[1]) * distanceCars };
			// TODO:Supposed to be Calculating the time headway (Check if I'm being blond..)

			approachingRate = speedCars[1] - speedCars[0];
			double s = desiredDistance + (speedCars[0]*timeHeadway[0]) + ((speedCars[0]*approachingRate)/2*(Math.sqrt(maxAcceleration*desiredDeceleration)));
			speedCars[0] = maxAcceleration*(1 - Math.pow((speedCars[0]/ desiredVelocity[0]),delta))- Math.pow((s/ distanceCars),2);
			
			double newPosition = r.getLength() * 1000 / (positionsRoad[0] + speedCars[0] * Controller.getInstance().getTicker().getTickTimeInS());
			v.setPosition(new SimpleImmutableEntry<>(r, newPosition));
			v.setSpeed(speedCars[0]);
           
            
			
		} else {
			//??????????????????????????//
			
			speedCars = new double[] { v.getSpeed() * 0.277777778};
			positionsRoad = new double[]{pcnt[0] * r.getLength()};
		
			speedCars[0] = Math.min(Math.min(v.getDesiredSpeed(), r.getSpeedLimit()) * 0.277777778,	speedCars[0] + v.getMaxAcceleration());
			double newPosition = r.getLength() * 1000 / (positionsRoad[0] + speedCars[0] * Controller.getInstance().getTicker().getTickTimeInS());
			v.setSpeed(speedCars[0] * 3.6);
			v.setPosition(new SimpleImmutableEntry<>(r, newPosition));

		}
	}
}

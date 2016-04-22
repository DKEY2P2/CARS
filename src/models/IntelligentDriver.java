package models;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

/**
 * http://home2.fvcc.edu/~dhicketh/DiffEqns/Spring11projects/Scott_Miller/
 * Project.pdf
 *
 * @author B
 *
 */
public class IntelligentDriver implements Model {

	/*private double distanceCars;
	private double[] positionsRoad;
	private double[] velCars;
	private double timeHeadway;
	private double desiredVelocity;
	private double maxAcceleration;
	private double desiredDeceleration;
	private double approachingRate;
	private double delta;
	private double acceleration;
	private double jamDistance;*/

	/*
	 * x0 (v) denotes position of vehicle 1 
	 * l0 (lengthCars) gives length of vehicle 1 
	 * a -1 (inFrontVehicle) position of vehicle 2 (Infront of vehicle 1) 
	 * v0 (desiredVelocity) Desired velocity 
	 * va (speedCars) speed of vehicle a 
	 * delta va (approachingRate) is the approaching rate / velocity difference(va - va-1) 
	 * s0 (desiredDistance) is minimum desired distance(at least s0 between two cars) 
	 * T is desired time headway (use distance and speed to calculate this desired time between two cars) 
	 * a is(maxVehicleAcceleration) maximum vehicle acceleration 
	 * b is(desiredDeceleration) desired breaking deceleration 
	 * delta is acceleration exponent (usually set to 4)
	 * 
	 */

	@Override
	public void calculate(Vehicle veh) {

		Vehicle inFront = veh.getPredecessor();
		Road r = veh.getPosition().getKey();
		TrafficLight trl = r.getEnd().getTrafficLight(r);

		double speed, acc = 0, diffSpeed = 0,sStar,a,b,T, dist = 0;
		speed = veh.getSpeed();
		int delta = 4;
		a = veh.getMaxAcceleration();
		b = veh.getDesiredDeceleration();
		T = speed / b * 0.05;

		double minDist = (veh.getSpeed()/6000) * veh.getLength();// Two-second rule from WIKI //Picked randomly

/*		if(inFront == null){
			if(!trl.isGreen()){
				diffSpeed = speed;
				dist = (1 - veh.getPosition().getValue()) * r.getLength();
			}else{
				diffSpeed = -speed;
				dist = minDist * 2;
			}
		}else{
			diffSpeed = speed - inFront.getSpeed();
			dist = (inFront.getPosition().getValue() - veh.getPosition().getValue()) * r.getLength() - inFront.getLength();
		}

		if(dist < minDist) {
			acc = 0;
			speed = 0;
		}else {
			sStar = minDist + speed*T+(speed*diffSpeed)/(2*Math.sqrt(a*b));
			acc = a * (1-Math.pow(veh.getSpeed()/Math.min(r.getSpeedLimit(),veh.getDesiredSpeed()),delta)-Math.pow(sStar/dist,2));
		}

		veh.updateAll(speed + acc * Controller.getInstance().getTicker().getTickTimeInS(),acc,r);

		*/
		
		if (inFront == null) {
			if (trl != null) {
				if (!trl.isGreen()) {
					//diffSpeed = -speed;
					dist = (1 - veh.getPosition().getValue()) * r.getLength();
					diffSpeed = speed;
				} else {
				//	diffSpeed = speed;
				  dist = minDist * 2;
				  diffSpeed = - speed;
				}
			} else {
				speed = Math.max(speed, r.getSpeedLimit());
				dist = 0.1;
			}

		} else {
			diffSpeed = speed - inFront.getSpeed();
			dist = (inFront.getPosition().getValue() - veh.getPosition().getValue()) * r.getLength()
					- inFront.getLength();
			System.out.println(veh.getPosition().getValue() + " inFront : " + inFront.getPosition().getValue());
		}

		if (dist < minDist) {
			if (inFront != null){
			acc = veh.getMaxDecceleration();
			speed = inFront.getSpeed()/2;
			} else {
				if(!trl.isGreen()){
					speed = 0; 
					acc = veh.getMaxDecceleration();
				}
			}
		} else {
			sStar = minDist + speed * T + (speed * diffSpeed) / (2 * Math.sqrt(a * b));
			acc = a * (1 - Math.pow(veh.getSpeed() / Math.min(r.getSpeedLimit(), veh.getDesiredSpeed()), delta)
					- Math.pow(sStar / dist, 2));
		}

		veh.updateAll(speed + acc * Controller.getInstance().getTicker().getTickTimeInS(), acc, r);

	}
	
}

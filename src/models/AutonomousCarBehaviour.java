package models;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

public class AutonomousCarBehaviour implements Model {

	@Override
	public void calculate(Vehicle v) {

		Vehicle leader = v.getPredecessor();
		Road r = v.getPosition().getKey();
		TrafficLight trl = r.getEnd().getTrafficLight(r);
		double acc; 												// Acceleration
		double maxDec, maxAcc, desDec;								// Maximum Deceleration, Acceleration
		double propPar1, propPar2;									// Proportional Parameters 1 and 2
		double actFollDist, desiFollDist, minFollDist; 				// Actual , desired and minimum following distance
		double speed, lSpeed, desSpeed, rLimit;					// Follower, leader, desired speed and speed limit of road.
		double posV, posL, posT;									// Position follower, leader, position Trafficlight
		double rangeToLeader;										// The range of distance to leader, so whether or not to take them into consideration yet.
		double lRoad;												// Obviously the length of the road
		boolean drive = true;
		double bla =0;

		acc = 0;
		maxAcc = v.getMaxAcceleration();
		maxDec = -(v.getMaxDecceleration());
		propPar1 = 1.3;
		propPar2 = (2 * Math.sqrt(propPar1));
		speed = v.getSpeed();
		desSpeed = v.getDesiredSpeed();
		lRoad = r.getLength();
		rLimit = r.getSpeedLimit();

		if (leader == null) {
			if (trl != null) {
				if (trl.isGreen()) {
					System.out.println("GREEN TRAFFIC LIGHT \n");
					acc = cruisingBehaviour(v, r, propPar2);
				} else { // Traffic light is RED
					desDec = -(v.getDesiredDeceleration());
					
					actFollDist = ((1 - v.getPosition().getValue()) * lRoad);
					
					// ***************************** SOMETHING IS WRONG HERE *********************************//
													//|||||||||||||||||||||//
												//vvvvvvvvvvvvvvvvvvvvvvvvvvvvvv//
					
					
					acc = -(Math.pow(speed, 2) / (2 * actFollDist));
					System.out.println("Acceleration : " + acc + "   desired Decel: " + desDec + "\n   MaxDec : " + maxDec);
					if (maxDec > acc && acc < desDec && drive) {
						// Successful stopping action can be applied
						System.out.println("This is the acceleration right now : " + acc);
						acc = -(Math.pow(speed, 2) / (2 * actFollDist));
						System.out.println("RED TRAFFIC LIGHT, inside range \n");
					} else if (acc > desDec && drive) {
						// Desired stopping position is still too far away.
						System.out.println("RED TRAFFIC LIGHT, outside range (positive) \n");
						acc = cruisingBehaviour(v, r, propPar2);
					} else if (acc <= maxDec) {
						// You are too close. It is impossible to stop the vehicle at the desired stopping position.
						System.err.println("RED TRAFFIC LIGHT, outside  range (negative!!) \n");
						drive = false;
						acc = 0;
						speed = 0;
					}
				}
				System.out.println("Speed before : " + speed);
				bla = speed + acc * Controller.getInstance().getTicker().getTickTimeInS();
				v.updateAll(bla, acc, r);
				System.err.println("Speed after : " + speed);
			} else {
				System.out.println("NO TRAFFIC LIGHT \n");
				acc = cruisingBehaviour(v, r, propPar2);
			}

		} else {
			System.out.println(" \n Leader takes care of everything.... \n");
			rangeToLeader = Math.max(30, speed * 2);
			posL = leader.getPosition().getValue();
			posV = v.getPosition().getValue();
			if (((posL - posV) * lRoad) <= rangeToLeader) {
				lSpeed = leader.getSpeed();
				actFollDist = ((posL - posV) * lRoad) - leader.getLength();
				minFollDist = (v.getSpeed() / 2.2222222) * v.getLength() * 2;// Two-second rule 
				desiFollDist = Math.max(minFollDist, speed * 2);    // where 2 is some constant k
				if (actFollDist <= minFollDist) {
					acc = -(Math.pow(speed, 2) / (2 * actFollDist)) * 3;
					acc = Math.min(acc, maxDec);
				} else {
					acc = Math.max(maxDec, propPar1 * (actFollDist - desiFollDist) - (propPar2) * (speed - lSpeed));
				}
			} else {
				acc = cruisingBehaviour(v, r, propPar2);
			}
		}
		 bla = speed + acc * Controller.getInstance().getTicker().getTickTimeInS();
		v.updateAll(bla, acc, r);
	}
	
	public static double cruisingBehaviour(Vehicle v, Road r, double prop){
		double speed = v.getSpeed();
		double desSpeed = v.getDesiredSpeed();
		double rLimit = r.getSpeedLimit();
		double maxDec = -(v.getMaxDecceleration());
		double maxAcc = v.getMaxAcceleration();
		double acc;
		if (speed < desSpeed) {
			if (overLimit(speed, rLimit)) {
				acc = -(Math.max(maxDec, prop * (desSpeed - speed)));
			} else
				acc = Math.min(maxAcc, prop * (desSpeed - speed));
		} else if (speed == desSpeed) {
			if (overLimit(speed, rLimit)) {
				acc = -(Math.max(maxDec, prop * (desSpeed - speed)));
			} else
				acc = 0;
		} else {
				acc = -(Math.max(maxDec, prop * (desSpeed - speed)));
		}
		return acc;
	}

	public static boolean overLimit(double speed, double sLimit) {
		boolean tooFast;
		if (speed > sLimit) {
			tooFast = true;
		} else if (speed == sLimit) {
			tooFast = false;
		} else {
			tooFast = false;
		}
		return tooFast;
	}
}

package models;

import java.awt.Color;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

public class AutonomousCarBehaviour implements Model {

	private boolean drive = true;
	private double acc; 												// Acceleration
	private double maxDec, desDec;										// Maximum Deceleration, desired Dec
	private double propPar1, propPar2;									// Proportional Parameters 1 and 2
	private double actFollDist, desiFollDist, minFollDist; 				// Actual , desired and minimum following distance
	private double speed, lSpeed;										// Follower, leader, desired speed
	private double posV, posL;											// Position follower, leader
	private double rangeToLeader;										// The range of distance to leader, so whether or not to take them into consideration yet.
	private double lRoad;												// Obviously the length of the road
	@Override
	public void calculate(Vehicle v) {

		Vehicle leader = v.getPredecessor();
		Road r = v.getPosition().getKey();
		TrafficLight trl = r.getEnd().getTrafficLight(r);
		
		double bla = 0;
		

		maxDec = -(v.getMaxDecceleration());
		propPar1 = 1.3;
		propPar2 = (2 * Math.sqrt(propPar1));
		speed = v.getSpeed();
		lRoad = r.getLength();
		
		if (leader == null) {
			if (trl != null) {
				if (trl.isGreen()) {
					acc = cruisingBehaviour(v, r, propPar2);
				} else { // Traffic light is RED
					desDec = -(v.getDesiredDeceleration());

					actFollDist = ((1 - v.getPosition().getValue()) * lRoad);
					acc = -(Math.pow(speed, 2) / (2 * actFollDist));
					if (acc > desDec && drive) {
						acc = cruisingBehaviour(v, r, propPar2);
					} else if (acc < maxDec && acc < desDec) {
						drive = false;
						acc = -10*(Math.pow(speed, 2) / (2 * actFollDist));
					}
				}
				bla = speed + acc * Controller.getInstance().getTicker().getTickTimeInS();
				v.updateAll(bla, acc, r);
				
				if (bla <= 0) {
					v.setColor(Color.BLACK);
				} else
					v.setColor(Color.ORANGE);
			} else {
				acc = cruisingBehaviour(v, r, propPar2);
			}

		} else {
			rangeToLeader = Math.max(20, speed * 2);
			posL = leader.getPosition().getValue();
			posV = v.getPosition().getValue();
			if (((posL - posV) * lRoad) <= rangeToLeader) {
				lSpeed = leader.getSpeed();
				actFollDist = ((posL - posV) * lRoad) - leader.getLength();
				minFollDist = (v.getSpeed() / 2.2222222) * v.getLength() * 1.1;// Two-second rule 
				desiFollDist = Math.max(minFollDist, speed * 1.2);    // where 2 is some constant k
				if (actFollDist <= minFollDist) {
					acc = -(Math.pow(speed, 2) / (2 * actFollDist)) ;
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

	public static double cruisingBehaviour(Vehicle v, Road r, double prop) {
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

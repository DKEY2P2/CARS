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
		double acc; 									// Acceleration
		double maxDec, maxAcc;							// Maximum Deceleration, Acceleration
		double propPar1, propPar2;						// Proportional Parameters 1 and 2
		double actFollDist, desiFollDist, minFollDist;	// Actual , desired and minimum following distance
		double vSpeed, lSpeed, desSpeed;				// Follower, leader and desired speed
		double posV, posL, posT;						// Position follower, leader, position Trafficlight
		double rangeToLeader;							// The range of distance to leader, so whether or not to take them into consideration yet.
		double lRoad;									// Obviously the length of the road
		
		
		acc = 0;
		maxAcc = v.getMaxAcceleration();
		maxDec = v.getMaxDecceleration();
		propPar1 = 1.3;
		propPar2 = (2 * Math.sqrt(propPar1));
		vSpeed = v.getSpeed();
		desSpeed = v.getDesiredSpeed();
		lRoad = r.getLength();

		if (leader == null) {
			if (trl != null) {
				if (trl.isGreen()) {
					if (vSpeed < desSpeed)
						acc = Math.min(maxAcc, propPar2 * (desSpeed - vSpeed));
					else if (vSpeed == desSpeed)
						acc = 0;
					else
						acc = Math.max(maxDec, propPar2 * (desSpeed - vSpeed));
				} else {
					posV = v.getPosition().getValue();
					posT = 1;
					actFollDist = (posT - posV);
					desiFollDist = 0.3;
					if(actFollDist <= desiFollDist){
						acc = -6*(Math.pow(vSpeed, 2) / (2 * ((actFollDist)* lRoad)));
					}else {
						acc = 0;
					}
				}
			} else {
				if (vSpeed < desSpeed)
					acc = Math.min(maxAcc, propPar2 * (desSpeed - vSpeed));
				else if (vSpeed == desSpeed)
					acc = 0;
				else
					acc = Math.max(maxDec, propPar2 * (desSpeed - vSpeed));
			}

		} else {
			rangeToLeader = Math.max(30, vSpeed * 2);
			posL = leader.getPosition().getValue();
			posV = v.getPosition().getValue();
			if (((posL - posV) * lRoad) <= rangeToLeader) {
				lSpeed = leader.getSpeed();
				actFollDist = ((posL - posV) * lRoad)
						- leader.getLength();
				minFollDist = (v.getSpeed() / 2.2222222) * v.getLength()*2;// Two-second rule 
				desiFollDist = Math.max(minFollDist, vSpeed * 2);    // where 2 is some constant k
				System.out.println(" LEADER POS :      " + leader.getPosition().getValue() + " FOLLOWER      "
						+ v.getPosition().getValue());
				System.out.println("Actual : " + actFollDist + " Allowed : " + minFollDist);
				if (actFollDist <= minFollDist) {
					System.out.println("Went into IF");
					acc = -(Math.pow(vSpeed, 2) / (2 * actFollDist)) * 3;
					acc = Math.min(acc, -maxDec);
					System.err.println(acc);
				} else {
					acc = Math.max(maxDec, propPar1 * (actFollDist - desiFollDist) - (propPar2) * (vSpeed - lSpeed));
				}
			} else {
				if (vSpeed < desSpeed)
					acc = Math.min(maxAcc, propPar2 * (desSpeed - vSpeed));
				else if (vSpeed == desSpeed)
					acc = 0;
				else
					acc = -Math.max(maxDec, propPar2 * (desSpeed - vSpeed));
			}
		}

		v.updateAll(vSpeed + acc * Controller.getInstance().getTicker().getTickTimeInS(), acc, r);
	}
}

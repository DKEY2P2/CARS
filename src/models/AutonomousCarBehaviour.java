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
		double vSpeed, lSpeed, desSpeed, sRoad;						// Follower, leader, desired speed and speed limit of road.
		double posV, posL, posT;									// Position follower, leader, position Trafficlight
		double rangeToLeader;										// The range of distance to leader, so whether or not to take them into consideration yet.
		double lRoad;												// Obviously the length of the road

		acc = 0;
		maxAcc = v.getMaxAcceleration();
		maxDec = -(v.getMaxDecceleration());
		propPar1 = 1.3;
		propPar2 = (2 * Math.sqrt(propPar1));
		vSpeed = v.getSpeed();
		desSpeed = v.getDesiredSpeed();
		lRoad = r.getLength();
		sRoad = r.getSpeedLimit();

		if (leader == null) {
			if (trl != null) {
				if (trl.isGreen()) {
					System.out.println("GREEN TRAFFIC LIGHT \n");
					System.out.println(desSpeed + " " + vSpeed + "  " + maxAcc);
					if (vSpeed < desSpeed)
						acc = Math.min(maxAcc, propPar2 * (desSpeed - vSpeed));
					else if (vSpeed == desSpeed) {
						acc = 0;
					} else
						acc = -(Math.max(maxDec, propPar2 * (desSpeed - vSpeed)));// TODO: Should this be negative??
				} else { // Traffic light is RED
					System.out.println("RED TRAFFIC LIGHT \n");
					desDec = -(v.getDesiredDeceleration());
					posV = v.getPosition().getValue();
					posT = 1;
					actFollDist = (posT - posV) * lRoad;
					// ***************************** SOMETHING IS WRONG HERE *********************************
					acc = -(Math.pow(vSpeed, 2) / (2 * actFollDist));
					//	System.out.println("Acc : "+ acc);
					if (maxDec >= acc && acc <= desDec) {
						// Successful stopping action can be applied
						System.out.println("This is the acceleration right now : " + acc);
						acc = -(Math.pow(vSpeed, 2) / (2 * actFollDist));
						System.out.println("RED TRAFFIC LIGHT, inside range \n");
					} else if (acc > desDec) {
						// Desired stopping position is still too far away.
						System.out.println("RED TRAFFIC LIGHT, outside range (positive) \n");
						acc = Math.min(maxAcc, sRoad);
					} else if (acc < maxDec) {
						// You are too close. It is impossible to stop the vehicle at the desired stopping position.
						System.out.println("RED TRAFFIC LIGHT, outside  range (negative!!) \n");
						acc = 0;
						v.updateAll(vSpeed + acc * Controller.getInstance().getTicker().getTickTimeInS(), acc, r);
					}
				}
			} else {
				System.out.println("NO TRAFFIC LIGHT \n");
				if (vSpeed < desSpeed)
					acc = Math.min(maxAcc, propPar2 * (desSpeed - vSpeed));
				else if (vSpeed == desSpeed)
					acc = 0;
				else
					acc = -(Math.max(maxDec, propPar2 * (desSpeed - vSpeed)));
			}

		} else {
			System.out.println(" \n Leader takes care of everything.... \n");
			rangeToLeader = Math.max(30, vSpeed * 2);
			posL = leader.getPosition().getValue();
			posV = v.getPosition().getValue();
			if (((posL - posV) * lRoad) <= rangeToLeader) {
				lSpeed = leader.getSpeed();
				actFollDist = ((posL - posV) * lRoad) - leader.getLength();
				minFollDist = (v.getSpeed() / 2.2222222) * v.getLength() * 2;// Two-second rule 
				desiFollDist = Math.max(minFollDist, vSpeed * 2);    // where 2 is some constant k
				if (actFollDist <= minFollDist) {
					acc = -(Math.pow(vSpeed, 2) / (2 * actFollDist)) * 3;
					acc = Math.min(acc, maxDec);
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
		double bla = vSpeed + acc * Controller.getInstance().getTicker().getTickTimeInS();
		v.updateAll(bla, acc, r);
	}
}

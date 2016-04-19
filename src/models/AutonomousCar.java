package models;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

// http://www.sciencedirect.com/science/article/pii/S1405774315000311
// https://en.wikipedia.org/wiki/Two-second_rule
// http://homepage.cs.uiowa.edu/~kearney/pubs/SteeringBehaviorsIEEEVR05.pdf    Page 157
public class AutonomousCar implements Model {

	@Override
	public void calculate(Vehicle v) {

		Vehicle leader = v.getPredecessor();
		Road r = v.getPosition().getKey();
		TrafficLight trl = r.getEnd().getTrafficLight(r);

		double acc, maxDec, dist, actDist, minDist, desiDist, par1, par2, speed, diffSpeed = 0;
		
		//minDist = (v.getSpeed()/8000) * v.getSpeed();// Two-second rule from WIKI
		minDist = v.getLength()*2;
		speed = v.getSpeed();
		maxDec = v.getMaxDecceleration();
		par1 = 1; // Do NOT know what this is supposed to be. Defined as "a
					// proportional parameter".
		par2 = (2 * Math.sqrt(par1));

		desiDist = Math.max(minDist, speed * 2); // kd is a constant ?!!!

		if (leader == null) {
			if (trl != null) {
				if (!trl.isGreen()) {
				//	dist = (1 - v.getPosition().getValue()) * r.getLength();
					diffSpeed = speed;
					actDist = (1 - v.getPosition().getValue()) * r.getLength();
					
				} else {
				//	dist = minDist *2;
					diffSpeed = - speed;
					actDist = minDist *2;
				}
			} else {
				//dist = minDist*2;
				diffSpeed =  - speed;
				actDist = minDist *2;
			}
		} else {
			actDist = v.getPosition().getValue() - leader.getPosition().getValue() * r.getLength() - leader.getLength();
			diffSpeed = v.getSpeed() - leader.getSpeed();

		}
		dist = (actDist - desiDist);
		if (dist < minDist) {
			speed = 0;
			acc = 0;
		} else {
			acc = Math.max(maxDec, (par1 * dist - par2 * diffSpeed));
		}
		System.out.println("Acceleration : " +acc);
		System.out.println("DISTANCE : " + dist);
		System.out.println(par2 * diffSpeed + "     " + diffSpeed);
		v.updateAll(speed + acc * Controller.getInstance().getTicker().getTickTimeInS(), acc, r);

	}

}

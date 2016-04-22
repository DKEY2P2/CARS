package models;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

// http://www.sciencedirect.com/science/article/pii/S1405774315000311
// https://en.wikipedia.org/wiki/Two-second_rule
// http://homepage.cs.uiowa.edu/~kearney/pubs/SteeringBehaviorsIEEEVR05.pdf    Page 157
// http://www.cedr.fr/home/fileadmin/user_upload/Publications/2010/e_Distance_between_vehicles.pdf
public class AutonomousCar implements Model {

	@Override
	public void calculate(Vehicle v) {

		Vehicle leader = v.getPredecessor();
		Road r = v.getPosition().getKey();
		TrafficLight trl = r.getEnd().getTrafficLight(r);

		double acc, maxDec, dist, actDist, minDist, desiDist, par1, par2, speed, diffSpeed = 0;

		minDist = v.getLength() * 2;
		speed = v.getSpeed();
		maxDec = v.getMaxDecceleration();
		par1 = 1; // Do NOT know what this is supposed to be. Defined as "a
					// proportional parameter".
		par2 = (2 * Math.sqrt(par1));

		desiDist = Math.max(minDist, speed * 2); // kd is a constant ?!!!

		if (leader == null) {
			if (trl != null) {
				//	System.out.println("We have a traffic light.");
				if (!trl.isGreen()) {
					diffSpeed = speed;
					actDist = (1 - v.getPosition().getValue()) * r.getLength();
					dist = (actDist - desiDist);
					if (dist < minDist) {
						speed = 0;
						acc = 0;
					} else {
						acc = Math.max(maxDec, (par1 * dist - par2 * diffSpeed));
					}

				} else {
					if (speed < v.getDesiredSpeed()) {
						acc = Math.min(v.getMaxAcceleration(), 2 * (v.getDesiredSpeed() - speed));
					} else if (speed == v.getDesiredSpeed()) {
						acc = speed;
					} else {
						acc = Math.max(v.getMaxDecceleration(), 2 * (v.getDesiredDeceleration() - speed));
					}
				}
			} else {
				if (speed < v.getDesiredSpeed()) {
					acc = Math.min(v.getMaxAcceleration(), 2 * (v.getDesiredSpeed() - speed));
				} else if (speed == v.getDesiredSpeed()) {
					acc = speed;
				} else {
					acc = Math.max(v.getMaxDecceleration(), 2 * (v.getDesiredDeceleration() - speed));
				}
			}
		} else {
			minDist = (v.getSpeed() / 8000) * v.getLength();// Two-second rule from WIKI
			actDist = ((leader.getPosition().getValue() - v.getPosition().getValue()) * r.getLength())
					- leader.getLength();
			diffSpeed = v.getSpeed() - leader.getSpeed();
			dist = (actDist - desiDist);
			if (dist < minDist) {
				acc = - Math.pow(speed, 2)/(2*dist);
			} else {
				acc = Math.max(maxDec, (par1 * dist - par2 * diffSpeed));
			}
		}
		v.updateAll(speed + acc * Controller.getInstance().getTicker().getTickTimeInS(), acc, r);

	}

}

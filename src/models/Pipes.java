package models;

import java.util.AbstractMap.SimpleImmutableEntry;

import controller.Controller;
import map.Road;
import vehicle.Vehicle;

public class Pipes implements Model {

	/**
	 * http://people.umass.edu/ndh/TFT/Ch17%20Pipes.pdf
	 *
	 * @author B
	 *
	 */

	private double distanceCars;
	private double safeDistanceMIN;
	private double[] positionsRoad;
	private double length;
	private double speed;
	private double speedLimit;

	@Override
	public void calculate(Vehicle follower) {
		Road r = follower.getPosition().getKey();
		Vehicle inFrontVehicle = follower.getPredecessor(); //I made a method that does this, it's called getPredecessor and it's in Vehicle. - Lucas
		int i = 0;
		for (Vehicle vehicle : r.getVehicles()) {
			if (vehicle == follower) {
				break;
			} else {
				inFrontVehicle = vehicle;
			}
		}
		/* 
		 * @param pcntI percentage of location of first car (the hind car)
		 * @param pcntII same for second car (the front car)
		 */

		if (inFrontVehicle != null) {
			if (!inFrontVehicle.getPosition().getKey().getTrafficlight().getWaiting().contains(inFrontVehicle)) {
				length = follower.getLength();
				speedLimit = r.getSpeedLimit();
				speed = follower.getSpeed();
				positionsRoad = new double[] { follower.getPosition().getValue() * r.getLength(),
						inFrontVehicle.getPosition().getValue() * r.getLength() };
				distanceCars = positionsRoad[1] - positionsRoad[0] - length;

				safeDistanceMIN = length * (speed / (0.447 * 10) + 1);
				if (distanceCars < safeDistanceMIN) { // This might be wrong/ right in the text??
					speed = (Math.max(0, speed - (follower.getMaxDecceleration()
							* Controller.getInstance().getTicker().getTickTimeInS())));
				} else {
					speed = Math.min(
							Math.min(follower.getDesiredSpeed(),
									speed + (follower.getMaxAcceleration()
											* Controller.getInstance().getTicker().getTickTimeInS())),
							r.getSpeedLimit());
				}
				double newPosition = (positionsRoad[0] + speed * Controller.getInstance().getTicker().getTickTimeInS())
						/ r.getLength();
				follower.setSpeed(speed);
				follower.setPosition(new SimpleImmutableEntry<>(r, newPosition));
			}else{singleCar(follower, r);}
		} else {

			singleCar(follower, r);

		}

	}

	private void singleCar(Vehicle follower, Road r) {
		length = follower.getLength();
		speedLimit = r.getSpeedLimit();
		positionsRoad = new double[] { follower.getPosition().getValue() * r.getLength(), 0 };
		double trafficLightDistance = r.getLength() - positionsRoad[0];//Distance between car and the traffic light
		safeDistanceMIN = length * ((speed / (0.447 * 10)) + 1);

		if (trafficLightDistance < safeDistanceMIN) { // This might be wrong/ right in the text??
			speed = (Math.max(0, speed
					- (follower.getMaxDecceleration() * Controller.getInstance().getTicker().getTickTimeInS())));
		} else {
			speed = Math.min((Math.min(follower.getDesiredSpeed(), speed
					+ (follower.getMaxAcceleration() * Controller.getInstance().getTicker().getTickTimeInS()))),
					speedLimit);
		}
		double newPosition = (positionsRoad[0] + speed * Controller.getInstance().getTicker().getTickTimeInS())
				/ r.getLength();
		follower.setSpeed(speed);
		follower.setPosition(new SimpleImmutableEntry<>(r, newPosition));
	}
}
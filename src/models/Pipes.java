package models;

import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

public class Pipes implements Model {

	/**
	 * http://people.umass.edu/ndh/TFT/Ch17%20Pipes.pdf
	 *
	 * @author B
	 *
	 */

	/*private double distanceCars;
	private double safeDistanceMIN;
	private double[] positionsRoad;
	private double length;
	private double speed;
	private double speedLimit;*/

	private final double SCALINGFACTOR = 2;

	@Override
	public void calculate(Vehicle follower) {

		Vehicle inFront = follower.getPredecessor();
		Road r = follower.getPosition().getKey();
		TrafficLight trl = r.getEnd().getTrafficLight(r);

		double speed, acc, dist, minDist;

		if(inFront == null){
			minDist = (follower.getLength() * follower.getSpeed()/(0.447 * 10))/SCALINGFACTOR;
			if(!trl.isGreen()){
				dist = (1 - follower.getPosition().getValue()) * r.getLength();
			}else{
				dist = minDist * 2;
			}
		}else{
			minDist = (follower.getLength() * (follower.getSpeed()/(0.447 * 10))+inFront.getLength())/SCALINGFACTOR;
			dist = (inFront.getPosition().getValue() - follower.getPosition().getValue()) * r.getLength();
		}

		if(dist < minDist) {
			acc = follower.getMaxDecceleration();
			speed = Math.max(0, follower.getSpeed() - acc);
		}else {
			acc = follower.getMaxAcceleration();
			speed = Math.min(follower.getDesiredSpeed(), follower.getSpeed() + acc);
		}

		follower.updateAll(speed,acc,r);
	/*
		if(follower.getPosition().getKey().getTrafficlight().getWaiting().contains(this)){
			follower.setSpeed(0);
			follower.setAcceleration(0);
			return;
		}
		Road r = follower.getPosition().getKey();
//		Vehicle inFrontVehicle = follower.getPredecessor(); //I made a method that does this, it's called getPredecessor and it's in Vehicle. - Lucas
		int i = 0;
		 Vehicle inFrontVehicle = null;
	        double currentCarPos = follower.getPosition().getValue();
	        double compare = Double.MAX_VALUE;
	        for (Vehicle vehicle : r.getVehicles()) {
	        	double tmp = vehicle.getPosition().getValue();
	        	if(tmp<currentCarPos){
	        		if(compare>tmp){
	        			inFrontVehicle = vehicle;
	        		}
	        	}
	        }
	         


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
					speed = (Math.max(2, speed - (follower.getMaxDecceleration()
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
		*/


	}

	/*private void singleCar(Vehicle follower, Road r) {
		length = follower.getLength();
		speedLimit = r.getSpeedLimit();
		positionsRoad = new double[] { follower.getPosition().getValue() * r.getLength(), 0 };
		double trafficLightDistance = r.getLength() - positionsRoad[0];//Distance between car and the traffic light
		safeDistanceMIN = length * ((speed / (0.447 * 10)) + 1);

		if (trafficLightDistance < safeDistanceMIN) { // This might be wrong/ right in the text??
			speed = (Math.max(2, speed
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
	}*/
}
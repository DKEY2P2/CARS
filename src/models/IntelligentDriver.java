package models;

import java.util.AbstractMap.SimpleImmutableEntry;

import controller.Controller;
import helper.Logger;
import map.Road;
import vehicle.Vehicle;
import vehicle.VehicleHolder;

/**
 * http://home2.fvcc.edu/~dhicketh/DiffEqns/Spring11projects/Scott_Miller/
 * Project.pdf
 *
 * @author B
 *
 */
public class IntelligentDriver implements Model {

	private double distanceCars;
	private double[] positionsRoad;
	private double[] velCars;
	private double timeHeadway;
	private double desiredVelocity;
	private double maxAcceleration;
	private double desiredDeceleration;
	private double approachingRate;
	private double delta;
	private double acceleration;
	private double jamDistance;

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
	/**
	 * @param v
	 * @param r
	 * @return
	 */
	private Vehicle getInFront(Vehicle follower, Road r) {// TODO: Find a better way to do this

		Vehicle inFrontVehicle = follower.getPredecessor();
		//	        double currentCarPos = follower.getPosition().getValue();
		//	        double compare = Double.MAX_VALUE;
		//	        for (Vehicle vehicle : r.getVehicles()) {
		//	        	if(vehicle == follower){
		//	        		continue;
		//	        	}
		//	        	double tmp = vehicle.getPosition().getValue();
		//	        	if(tmp<currentCarPos){
		//	        		if(compare>tmp){
		//	        			inFrontVehicle = vehicle;
		//	        		}
		//	        	}
		//	        }
		if (follower.getPosition().getKey().getTrafficlight().getWaiting().contains(inFrontVehicle)|| !VehicleHolder.getInstance().contains(inFrontVehicle)) {
			return null;
		} else {
			return inFrontVehicle;
		}

	}

	@SuppressWarnings("unused")
	@Override
	public void calculate(Vehicle follower) {

		Road r = follower.getPosition().getKey();
		Vehicle inFrontVehicle = getInFront(follower, r);
		jamDistance = 4;

		desiredVelocity = follower.getDesiredSpeed(); //  Desired Speed that front car wants to drive at max.
		maxAcceleration = follower.getMaxAcceleration();
		desiredDeceleration = follower.getDesiredDeceleration();
		delta = 4; // set Delta (acceleration exponent) to 4 : http://home2.fvcc.edu/~dhicketh/DiffEqns/Spring11projects/Scott_Miller/Project.pdf

		if (inFrontVehicle != null) {
			double s =12213621873621d;

			acceleration = follower.getAcceleration();
			velCars = new double[] { follower.getSpeed(), inFrontVehicle.getSpeed() }; // Velocity of the two cars
//			if (velCars[0] <= 0) {
//				velCars[0] = 0.1;
//				follower.setSpeed(velCars[0]);
//			}
//			if (velCars[1] <= 0) {
//				velCars[1] = 0.1;
//				inFrontVehicle.setSpeed(velCars[1]);
//			}
			positionsRoad = new double[] { follower.getPosition().getValue() * r.getLength(),
					inFrontVehicle.getPosition().getValue() * r.getLength() };
			approachingRate = velCars[0] - velCars[1];
			distanceCars = positionsRoad[1] - positionsRoad[0] - inFrontVehicle.getLength(); // Distance between the two cars
			timeHeadway = velCars[0]/desiredDeceleration*0.05;
			// Headway between two cars in time. (seconds)
			// Time Headway (distance/speed)
						if (distanceCars <= 0.1) {
				Logger.LogAny("Crash", "I crashed: " + follower.getIndex());
				velCars[0] = (Math.max(0, velCars[0]
						- (follower.getMaxDecceleration() * Controller.getInstance().getTicker().getTickTimeInS())));
				//velCars[0] = velCars[0] - desiredDeceleration;
//				if (velCars[0] <= 0) {
//					velCars[0] = 0.1;
//					follower.setSpeed(velCars[0]);
//				}
//				if (velCars[1] <= 0) {
//					velCars[1] = 0.1;
//					inFrontVehicle.setSpeed(velCars[1]);
//				}

			} else {

				s = jamDistance + timeHeadway * velCars[0]
						+ ((velCars[0] * approachingRate) / (2 * Math.sqrt(maxAcceleration * desiredDeceleration)));
				acceleration = maxAcceleration
						* (1 - Math.pow((velCars[0] / desiredVelocity), delta) - Math.pow((s / distanceCars), 2));

				velCars[0] = velCars[0] + acceleration * Controller.getInstance().getTicker().getTickTimeInS();
				if (velCars[0] < 0) {
					velCars[0] = 0;
				}
				if (velCars[1] < 0) {
					velCars[1] = 0;
				}
			}
			velCars[0] = Math.min(velCars[0], r.getSpeedLimit());
			double newPosition = (positionsRoad[0] + velCars[0] * Controller.getInstance().getTicker().getTickTimeInS())
					/ r.getLength();

			if(follower.getIndex()==3){
//				System.out.println("s" + s);
//				System.out.println("d" +distanceCars );
				
			}
			follower.setAcceleration(acceleration);
			follower.setSpeed(velCars[0]);
			follower.setPosition(new SimpleImmutableEntry<>(r, newPosition));

		} else {
			double acceleration = follower.getAcceleration();

			double positionRoad = follower.getPosition().getValue() * r.getLength(); // Position (in meters from the start of the road) of the car relative to road																											// Position
			double velCar = follower.getSpeed(); // Velocity of the car
//			if (velCar <= 0) {
//				velCar = 0.1;
//				follower.setSpeed(velCar);
//			}
			approachingRate = velCar;
			double trafficLightDistance = r.getLength() - positionRoad;//Distance between car and the traffic light
			timeHeadway = velCar/desiredDeceleration*0.05;

			/**
			 * double s = jamDistance + jamDistanceB * Math.sqrt((velCar /
			 * desiredVelocity)) + (velCar * timeHeadway) + +((velCar *
			 * approachingRate) / (2 * Math.sqrt(maxAcceleration *
			 * desiredDeceleration)));
			 **/

			// Braking should only happen when actual distance between two cars is smaller than s
			//			}

			//acceleration = maxAcceleration * Math.abs((1 - Math.pow((velCar / desiredVelocity), delta) - Math.pow((s / trafficLightDistance), 2)));

			/**
			 * velCar = maxAcceleration Math.abs((1 - Math.pow((velCar /
			 * desiredVelocity), delta) - Math.pow((s / distanceCars), 2)));
			 **/
		
			double s = jamDistance + timeHeadway * velCar
					+ ((velCar * approachingRate) / (2 * Math.sqrt(maxAcceleration * desiredDeceleration)));
			acceleration = maxAcceleration
					* (1 - Math.pow((velCar / desiredVelocity), delta) - Math.pow((s / trafficLightDistance), 2));

			velCar = velCar + acceleration * Controller.getInstance().getTicker().getTickTimeInS();
			if (velCar < 0) {
				velCar = 2;
			}

			velCar = Math.min(velCar, r.getSpeedLimit());
			double newPosition = (positionRoad + velCar * Controller.getInstance().getTicker().getTickTimeInS())
					/ r.getLength();

			follower.setAcceleration(acceleration);
			follower.setSpeed(velCar);
			follower.setPosition(new SimpleImmutableEntry<>(r, newPosition));

		}
	}
}

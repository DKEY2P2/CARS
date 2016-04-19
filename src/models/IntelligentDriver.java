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

		double speed, acc, dist,diffSpeed,sStar,a,b,T;
		speed = veh.getSpeed();
		int delta = 4;
		a = veh.getMaxAcceleration();
		b = veh.getDesiredDeceleration();
		T = speed / b * 0.05;

		double minDist = veh.getLength()*2; //Picked randomly

		if(inFront == null){
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

		/*

		Road r = follower.getPosition().getKey();
		Vehicle inFrontVehicle = getInFront(follower, r);
		jamDistance = 4;

		desiredVelocity = follower.getDesiredSpeed(); //  Desired Speed that front car wants to drive at max.
		maxAcceleration = follower.getMaxAcceleration();
		desiredDeceleration = follower.getDesiredDeceleration();
		delta = 4; // set Delta (acceleration exponent) to 4 : http://home2.fvcc.edu/~dhicketh/DiffEqns/Spring11projects/Scott_Miller/Project.pdf

		if (inFrontVehicle != null) {
			double s = 1;

			acceleration = follower.getAcceleration();
			velCars = new double[] { follower.getSpeed(), inFrontVehicle.getSpeed() }; // Velocity of the two cars
			positionsRoad = new double[] { follower.getPosition().getValue() * r.getLength(),
					inFrontVehicle.getPosition().getValue() * r.getLength() };
			approachingRate = velCars[0] - velCars[1];
			distanceCars = positionsRoad[1] - positionsRoad[0] - inFrontVehicle.getLength(); // Distance between the two cars
			timeHeadway = velCars[0] / desiredDeceleration * 0.05;
			// Headway between two cars in time. (seconds)
			// Time Headway (distance/speed)
			if (distanceCars <= 0.1) {
				Logger.LogAny("Crash", "I crashed: " + follower.getIndex());
				velCars[0] = (Math.max(0, velCars[0]
						- (follower.getMaxDecceleration() * Controller.getInstance().getTicker().getTickTimeInS())));
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
			follower.setAcceleration(acceleration);
			follower.setSpeed(velCars[0]);
			follower.setPosition(new SimpleImmutableEntry<>(r, newPosition));

		} else {
			double acceleration = follower.getAcceleration();

			double positionRoad = follower.getPosition().getValue() * r.getLength(); // Position (in meters from the start of the road) of the car relative to road																											// Position
			double velCar = follower.getSpeed(); // Velocity of the car
			approachingRate = velCar;
			double trafficLightDistance = r.getLength() - positionRoad;//Distance between car and the traffic light
			timeHeadway = velCar / desiredDeceleration * 0.05;

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

		}*/
	}
}

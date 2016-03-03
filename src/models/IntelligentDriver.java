package models;

import java.util.AbstractMap.SimpleImmutableEntry;

import controller.Controller;
import map.Road;
import vehicle.Vehicle;

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
	private double[] brakingCars;
	private double timeHeadway;
	private double desiredVelocity;
	private double maxAcceleration;
	private double desiredDeceleration;
	private double[] desiredDistance;
	private double approachingRate;
	private double delta;
	private double acceleration;

	/*
	 * x0 (v) denotes position of vehicle 1 
	 * l0 (lengthCars) gives length of vehicle 1 
	 * a -1 (inFrontVehicle) position of vehicle 2 (Infront of vehicle 1) 
	 * v0 (desiredVelocity) Desired velocity 
	 * va (speedCars) velocity of vehicle a 
	 * delta va (approachingRate) is the approaching rate / velocity difference(va - va-1) 
	 * s0 (desiredDistance) is minimum desired distance(at least s0 between two cars) 
	 * T is desired time headway (use distance and speed to calculate this desired time between two cars) 
	 * a is(maxVehicleAcceleration) maximum vehicle acceleration 
	 * b is(desiredDeceleration) desired breaking deceleration 
	 * delta is acceleration exponent (usually set to 4)
	 * 
	 */
	
	
	/**
	 * 
	 * @param v
	 * @param r
	 * @return
	 */
	private Vehicle getInFront(Vehicle v, Road r) {// TODO: Find a better way to do this
		Vehicle inFrontVehicle = null;
		for (Vehicle vehicle : r.getVehicles()) {
			if (vehicle == v) {
				break;
			} else {
				inFrontVehicle = vehicle;
			}
		}
		// Get the car ahead of you
		return inFrontVehicle;
	}

	@SuppressWarnings("unused")
	@Override
	public void calculate(Vehicle v) {
		//Waiting at the traffic light
        if (v.getPosition().getKey() == null) {
            return;
        }
		
		Road r = v.getPosition().getKey();
		Vehicle inFrontVehicle = getInFront(v, r);
		desiredVelocity = v.getDesiredSpeed(); //  Desired Speed that front car wants to drive at max.
		maxAcceleration = v.getMaxAcceleration();
		desiredDeceleration = v.getDesiredBraking();
		desiredDistance = new double[]{v.getDesiredDistance(), inFrontVehicle.getDesiredDistance()};
		delta = 4; // set Delta (acceleration exponent) to 4 : http://home2.fvcc.edu/~dhicketh/DiffEqns/Spring11projects/Scott_Miller/Project.pdf
		acceleration = v.getAcceleration();
		
		if (inFrontVehicle != null) {
			positionsRoad = new double[] { v.getPosition().getValue() * r.getLength(), inFrontVehicle.getPosition().getValue() * r.getLength() }; // Position (in percentage) of the two cars relative to road																											// Position
			velCars = new double[] { v.getSpeed(), inFrontVehicle.getSpeed() }; // Velocity of the two cars
			
			if(velCars[0] == 0){
				velCars[0] = Math.min(desiredVelocity, maxAcceleration);
				v.setSpeed(velCars[0]);
				double newPosition = (positionsRoad[0] + velCars[0] * Controller.getInstance().getTicker().getTickTimeInS()/ r.getLength());
				v.setPosition(new SimpleImmutableEntry<>(r, newPosition));
			}
			
			//TODO: What happens when the cars are standing still?! Velocity is 0, giving timeHeadway infinity!!!
			
			approachingRate = Math.abs(velCars[1] - velCars[0]);
			
			distanceCars = positionsRoad[1] - positionsRoad[0]; // Distance in metres per second between the two cars
			timeHeadway = distanceCars / velCars[0]; // TODO : Is this correct ?
			// Headway between two cars in time. (seconds)
			// Time Headway (distance/speed)
			
			double s = desiredDistance[0] + desiredDistance[1]*Math.sqrt(velCars[0]/desiredVelocity)+(velCars[0] * timeHeadway)
					+ ((velCars[0] * approachingRate) / (2 * (Math.sqrt(maxAcceleration * desiredDeceleration))));
			
			velCars[0] = Math.abs(maxAcceleration * (1 - Math.pow((velCars[0] / desiredVelocity), delta)
					- Math.pow((s / distanceCars), 2)));
			brakingCars[0] = -maxAcceleration * Math.pow((s / distanceCars), 2);
			acceleration = maxAcceleration*(1- Math.pow((velCars[0]/desiredVelocity),2));

			double newPosition = (positionsRoad[0] + velCars[0] * Controller.getInstance().getTicker().getTickTimeInS()/ r.getLength());
			v.setSpeed(velCars[0]);
			v.setBraking(brakingCars[0]);
			v.setAcceleration(acceleration);
			v.setPosition(new SimpleImmutableEntry<>(r, newPosition));

		} else {

			double positionRoad = v.getPosition().getValue() * r.getLength(); // Position (in percentage) of the car relative to road																											// Position
			double velCar = v.getSpeed(); // Velocity of the car
			if(velCar == 0){
				velCar = Math.min(desiredVelocity, maxAcceleration);
				v.setSpeed(velCar);
				double newPos = r.getLength() * 1000/ (positionRoad + velCar * Controller.getInstance().getTicker().getTickTimeInS());
				v.setPosition(new SimpleImmutableEntry<>(r, newPos));
			}
			approachingRate = velCar;
            double x = r.getLength() * positionRoad;
            double trafficLightDistance = Math.abs(x - r.getLength());//Distance between car and the traffic light
            timeHeadway =  trafficLightDistance / velCar;
            
            double s = desiredDistance[0] + desiredDistance[1]*Math.sqrt(velCar/desiredVelocity)+ (velCar * timeHeadway)
					+ ((velCar * approachingRate) / (2 * (Math.sqrt(maxAcceleration * desiredDeceleration))));
                       
            velCar = Math.abs(maxAcceleration * (1 - Math.pow((velCar / desiredVelocity), delta)
					- Math.pow((s / trafficLightDistance), 2)));
            double brakingCar = -maxAcceleration * Math.pow((s / trafficLightDistance), 2);
            System.out.println(brakingCar);
			acceleration = maxAcceleration*(1- Math.pow((velCar/desiredVelocity),2));


			double newPosition = (positionsRoad[0] + velCars[0] * Controller.getInstance().getTicker().getTickTimeInS()/ r.getLength());
			v.setSpeed(velCar);
			v.setBraking(brakingCar);
			v.setAcceleration(acceleration);
			v.setPosition(new SimpleImmutableEntry<>(r, newPosition));
			
		}
	}
}

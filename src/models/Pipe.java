package models;

import java.util.ArrayList;
import java.util.AbstractMap.SimpleImmutableEntry;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;
/**
 * http://people.umass.edu/ndh/TFT/Ch17%20Pipes.pdf
 *
 * @author B
 *
 */
public class Pipe implements Model {

	private double distanceCars;
	private double safeDistance;
	private double safeDistanceMIN;
	private double[] pcnt;
	double[] positionsRoad;
	double[] lengthCars;
	double speed;

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

	public void calculate(Vehicle v) {
		Road r = v.getPosition().getKey();
		Vehicle inFrontVehicle = getInFront(v, r);
		speed = v.getSpeed();
		//System.out.println(speed);
		if (v.getPosition().getKey() == null) {
			return;
		}

		if (inFrontVehicle != null) {
			pcnt = new double[] { v.getPosition().getValue(), inFrontVehicle.getPosition().getValue() };
			positionsRoad = new double[] { pcnt[0] * r.getLength(), pcnt[1] * r.getLength() };
			distanceCars = positionsRoad[1] - positionsRoad[0];
			lengthCars = new double[] { v.getLength(), inFrontVehicle.getLength() };

			safeDistance = Math.abs(distanceCars);
			safeDistanceMIN = lengthCars[0] * speed / ((0.447 * 10) + 1);
			if (safeDistance < safeDistanceMIN) {
				speed = (Math.max(0,
						speed - v.getMaxDecceleration() * Controller.getInstance().getTicker().getTickTimeInS()));
			} else {
				speed = Math.min(Math.min(v.getDesiredSpeed(), r.getSpeedLimit()),
						speed + v.getMaxAcceleration() * Controller.getInstance().getTicker().getTickTimeInS());
			}
			double newPosition = (positionsRoad[0] + speed*Controller.getInstance().getTicker().getTickTimeInS())/ r.getLength();
			v.setSpeed(speed);
			v.setPosition(new SimpleImmutableEntry<>(r, newPosition));
		} else {
			
			pcnt = new double[] { v.getPosition().getValue(), 0 };
			positionsRoad = new double[] { pcnt[0] * r.getLength() };
			double distanceLight = Math.abs(positionsRoad[0] - r.getLength());
			if (distanceLight < safeDistanceMIN) {
				speed = (Math.max(0,
						speed - v.getMaxDecceleration() * Controller.getInstance().getTicker().getTickTimeInS()));
			} else {
				speed = Math.min(Math.min(v.getDesiredSpeed(), r.getSpeedLimit()),
						speed + v.getMaxAcceleration() * Controller.getInstance().getTicker().getTickTimeInS());
			}
			double newPosition = (positionsRoad[0] + speed*Controller.getInstance().getTicker().getTickTimeInS())/ r.getLength();
			v.setSpeed(speed);
			v.setPosition(new SimpleImmutableEntry<>(r, newPosition));

		}
		ArrayList<TrafficLight> atl = v.getPosition().getKey().getEnd().getTrafficLights();
		TrafficLight tl = null;
		for (TrafficLight t : atl) {
			if (t.getIn() == v.getPosition().getKey())
				; // ONLY CONSIDERS 1 ROAD INCOMING
			tl = t;

			if (tl.isGreen()) {
				// Updates the speed
				v.setDistance(v.getDistance() + speed * Controller.getInstance().getTicker().getTickTimeInS());
				double newPosition = (positionsRoad[0] + speed * Controller.getInstance().getTicker().getTickTimeInS())
						/ r.getLength();
				v.setSpeed(speed);
				// Update the position
				v.setPosition(new SimpleImmutableEntry<>(r, newPosition));
			} else {
				v.setPosition(new SimpleImmutableEntry<>(r, v.getPosition().getValue()));
			}

		}
	}
}
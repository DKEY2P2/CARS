package models;

import java.util.AbstractMap.SimpleImmutableEntry;

import controller.Controller;
import map.Road;
import vehicle.Vehicle;

public class Pipe implements Model {

	/**
	 * http://people.umass.edu/ndh/TFT/Ch17%20Pipes.pdf
	 *
	 * @author B
	 *
	 */

	private double distanceCars;
	private double safeDistance;
	private double safeDistanceMIN;
	private double[] pcnt;
	double[] positionsRoad;
	double[] lengthCars;
	double[] speedCars;

	public void calculate(Vehicle v) {
		Road r = v.getPosition().getKey();
		Vehicle inFrontVehicle = r.getVehicles().getFirst();
		int i = 0;
		for (Vehicle vehicle : r.getVehicles()) {
			if (vehicle == v) {
				break;
			} else {
				inFrontVehicle = vehicle;
			}

		}
		if (inFrontVehicle != null) {
			pcnt = new double[] { v.getPosition().getValue(), inFrontVehicle.getPosition().getValue() };
		} else {
			pcnt = new double[] { v.getPosition().getValue(), 0 };
		}

		if (inFrontVehicle != null) {
			positionsRoad = new double[] { pcnt[0] * r.getLength(), pcnt[1] * r.getLength()};
			distanceCars = positionsRoad[1] - positionsRoad[0];
			lengthCars = new double[] { v.getLength(), inFrontVehicle.getLength() };
			speedCars = new double[] { v.getSpeed() * 0.277777778, inFrontVehicle.getSpeed()* 0.277777778};

			safeDistance = distanceCars;
			safeDistanceMIN = (lengthCars[0] * speedCars[0]) / ((0.447 * 10) + 1);
			if (safeDistance > safeDistanceMIN) { // This might be wrong/ right
													// in the text??
				speedCars[0] = (Math.max(0, speedCars[0] - v.getMaxDecceleration()));
			} else {
				speedCars[0] = Math.min(Math.min(v.getDesiredSpeed(), r.getSpeedLimit()) * 0.277777778,
						speedCars[0] + v.getMaxAcceleration());
			}
			double newPosition = r.getLength() * 1000 / (positionsRoad[0] + speedCars[0] * Controller.getInstance().getTicker().getTickTimeInS());
			v.setSpeed(speedCars[0] * 3.6);
			v.setPosition(new SimpleImmutableEntry<>(r, newPosition));
		} else {
		
			speedCars = new double[] { v.getSpeed() * 0.277777778};
			positionsRoad = new double[]{pcnt[0] * r.getLength()};
		
			speedCars[0] = Math.min(Math.min(v.getDesiredSpeed(), r.getSpeedLimit()) * 0.277777778,	speedCars[0] + v.getMaxAcceleration());
			double newPosition = r.getLength() * 1000 / (positionsRoad[0] + speedCars[0] * Controller.getInstance().getTicker().getTickTimeInS());
			v.setSpeed(speedCars[0] * 3.6);
			v.setPosition(new SimpleImmutableEntry<>(r, newPosition));

		}
	}
}
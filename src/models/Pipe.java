package models;

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

    @Override
    public void calculate(Vehicle v) {
        Road r = v.getPosition().getKey();
        Vehicle inFrontVehicle = v.getPredecessor(); //I made a method that does this, it's called getPredecessor and it's in Vehicle. - Lucas
        int i = 0;
        for (Vehicle vehicle : r.getVehicles()) {
            if (vehicle == v) {
                break;
            } else {
                inFrontVehicle = vehicle;
            }

        }

        /* 
         * @param pcntI percentage of location of first car (the hind car)
         * @param pcntII same for second car (the front car)
         */
        double pcntII = inFrontVehicle.getPosition().getValue();
        double pcntI = v.getPosition().getValue();
        double[] positionsRoad = new double[]{pcntI * r.getLength(), pcntII * r.getLength()};
        distanceCars = positionsRoad[1] - positionsRoad[0];
        double[] lengthCars = new double[]{v.getLength(), inFrontVehicle.getLength()};
        double[] speedCars = new double[]{v.getSpeed(), inFrontVehicle.getSpeed()};

        safeDistance = distanceCars;
        safeDistanceMIN = (lengthCars[0] * speedCars[0]) / ((0.447 * 10) + 1);
        if (safeDistance > safeDistanceMIN) { // This might be wrong/ right in the text??
            speedCars[0] = (Math.max(0, speedCars[0] - v.getMaxDecceleration()));
        } else {
            speedCars[0] = (Math.min(v.getDesiredSpeed(), speedCars[0] + v.getMaxAcceleration()));
        }
        positionsRoad[0] = positionsRoad[0] + speedCars[0] * Controller.getInstance().getTicker().getTickTimeInS();

    }
}
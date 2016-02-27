package models;

import controller.Controller;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import map.Road;
import vehicle.Vehicle;

/**
 *
 * @author Kareem
 */
public class Forbe implements Model {

    @Override
    public void calculate(Vehicle veh) {
        //Waiting at the traffic light
        if (veh.getPosition().getKey() == null) {
            return;
        }
        //If you are in the end of the road it shouldn't bother
        if (veh.getPosition().getValue() > 1) {
            return;
        }
        //Get the road your on
        Road r = veh.getPosition().getKey();
        Vehicle inFrontVehicle = null;
        int i = 0;
        for (Vehicle vehicle : r.getVehicles()) {
            if (vehicle == veh) {
                break;
            } else {
                inFrontVehicle = vehicle;
            }
        }
        //Get the car ahead of you

        //Get the percentage dunno why I split it when I split later on
        double[] percentage;
        if (inFrontVehicle != null) {
            percentage = new double[]{veh.getPosition().getValue(), inFrontVehicle.getPosition().getValue()};
        } else {
            percentage = new double[]{veh.getPosition().getValue(), 0};
        }

        //If there is a car ahead of you
        if (inFrontVehicle != null) {
            //Get various of data
            double roadLength = r.getLength();
            double[] x = new double[]{roadLength * percentage[0], roadLength * percentage[1]};
            double[] l = new double[]{veh.getLength(), inFrontVehicle.getLength()};
            double[] v = new double[]{veh.getSpeed(), inFrontVehicle.getSpeed()};
            double[] reactionTime = new double[]{veh.getReactionTime(), inFrontVehicle.getReactionTime()};
            //Distance between the two cars
            double distance = x[1] - x[0];
            double s = Math.abs(distance);
            //Time it takes you to react
            double sMin = reactionTime[0] * v[0] + l[0];

            //If you can react fast enough
            if (s < sMin) {
                v[0] = Math.max(0,
                        v[0] - veh.getMaxDecceleration() * Controller.getInstance().getTicker().getTickTimeInS());
            } else {
                v[0] = Math.min(
                        Math.min(veh.getDesiredSpeed(), r.getSpeedLimit()),
                        v[0] + veh.getMaxAcceleration() * Controller.getInstance().getTicker().getTickTimeInS());
            }
            //Updates the speed
            double newPercentage = (x[0] + v[0] * Controller.getInstance().getTicker().getTickTimeInS()) / roadLength;
            veh.setSpeed(v[0]);
            //Update the position
            veh.setPosition(new SimpleImmutableEntry<>(r, newPercentage));
        } else {
            //Get various of data

            double roadLength = r.getLength();
            double[] v = new double[]{veh.getSpeed()};
            double[] x = new double[]{roadLength * percentage[0]};
            double[] l = new double[]{veh.getLength()};
            double[] reactionTime = new double[]{veh.getReactionTime()};

            //Distance between you and the traffic light
            double s = Math.abs(x[0] - roadLength);
            //Time it takes you to react
            double sMin = reactionTime[0] * v[0] + l[0];
            //If you can react fast enough
            if (s < sMin) {
                v[0] = Math.max(0,
                        v[0] - veh.getMaxDecceleration() * Controller.getInstance().getTicker().getTickTimeInS());
            } else {
                v[0] = Math.min(
                        Math.min(veh.getDesiredSpeed(), r.getSpeedLimit()),
                        v[0] + veh.getMaxAcceleration() * Controller.getInstance().getTicker().getTickTimeInS());
            }
            //Updates the speed
            veh.setDistance(veh.getDistance() + v[0] * Controller.getInstance().getTicker().getTickTimeInS());
            double newPercentage = (x[0] + v[0] * Controller.getInstance().getTicker().getTickTimeInS()) / roadLength;
            veh.setSpeed(v[0]);
            //Update the position
            veh.setPosition(new SimpleImmutableEntry<>(r, newPercentage));
        }
    }

}

package models;

import controller.Controller;
import helper.Timer;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import map.Road;
import vehicle.Vehicle;

/**
 *
 * @author Kareem
 */
public class Forbe implements Model {

    private Vehicle getInFront(Vehicle veh, Road r) {
        Vehicle inFrontVehicle = null;
//        int i = 0;
//        i = r.getVehicles().indexOf(veh);
//        if(i == r.getVehicles().size()-1){
//            return null;
//        }
//        return r.getVehicles().get(i+1);
        for (Vehicle vehicle : r.getVehicles()) {
            if (vehicle == veh) {
                break;
            } else {
                inFrontVehicle = vehicle;
            }
        }
        //Get the car ahead of you
        return inFrontVehicle;
    }

    @Override
    public void calculate(Vehicle veh) {
        if (veh.getIndex() == 1) {
//            Timer.start();
        }
        Road r = veh.getPosition().getKey();
        double roadLength = r.getLength();
        double v = veh.getSpeed();
        double l = veh.getLength();
        double reactionTime = veh.getReactionTime();
        double sMin = reactionTime * v + l;

        Vehicle inFrontVehicle = getInFront(veh, r);

        //Waiting at the traffic light
        if (veh.getPosition().getKey() == null) {
            return;
        }
        //If you are in the end of the road it shouldn't bother
        if (veh.getPosition().getValue() > 1) {
            return;
        }
        //Get the road your on

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
            double[] x = new double[]{roadLength * percentage[0], roadLength * percentage[1]};
            //Distance between the two cars
            double distance = x[1] - x[0];
            double s = Math.abs(distance);
            //Time it takes you to react

            //If you can react fast enough
            if (s < sMin) {
                v = Math.max(0,
                        v - veh.getMaxDecceleration() * Controller.getInstance().getTicker().getTickTimeInS());
            } else {
                v = Math.min(
                        Math.min(veh.getDesiredSpeed(), r.getSpeedLimit()),
                        v + veh.getMaxAcceleration() * Controller.getInstance().getTicker().getTickTimeInS());
            }
            //Updates the speed
            double newPercentage = (x[0] + v * Controller.getInstance().getTicker().getTickTimeInS()) / roadLength;
            veh.setSpeed(v);
            //Update the position
            veh.setPosition(new SimpleImmutableEntry<>(r, newPercentage));
        } else {
            //Get various of data
            double x = roadLength * percentage[0];

            //Distance between you and the traffic light
            double s = Math.abs(x - roadLength);
            //Time it takes you to react
            //If you can react fast enough
            if (s < sMin) {
                v = Math.max(0,
                        v - veh.getMaxDecceleration() * Controller.getInstance().getTicker().getTickTimeInS());
            } else {
                v = Math.min(
                        Math.min(veh.getDesiredSpeed(), r.getSpeedLimit()),
                        v + veh.getMaxAcceleration() * Controller.getInstance().getTicker().getTickTimeInS());
            }
            //Updates the speed
            veh.setDistance(veh.getDistance() + v * Controller.getInstance().getTicker().getTickTimeInS());
            double newPercentage = (x + v * Controller.getInstance().getTicker().getTickTimeInS()) / roadLength;
            veh.setSpeed(v);
            //Update the position
            veh.setPosition(new SimpleImmutableEntry<>(r, newPercentage));
        }
        if (veh.getIndex() == 1) {
//            Timer.nanoPrint();
        }
    }

}

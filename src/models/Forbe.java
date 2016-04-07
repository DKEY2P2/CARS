package models;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

/**
 *
 * @author Kareem, Lucas
 */
public class Forbe implements Model {

    private final double SCALINGFACTOR = 1;

    @Override
    public void calculate(Vehicle veh) {

        Vehicle inFront = veh.getPredecessor();
        Road r = veh.getPosition().getKey();
        TrafficLight trl = r.getEnd().getTrafficLight(r);

        double speed, acc, dist, minDist;

        if(inFront == null){
            minDist = (veh.getReactionTime() * veh.getSpeed())/SCALINGFACTOR;
            if(!trl.isGreen()){
                dist = (1 - veh.getPosition().getValue()) * r.getLength();
            }else{
                dist = minDist * 2;
            }
        }else{
            minDist = (veh.getReactionTime() * veh.getSpeed() + inFront.getLength())/SCALINGFACTOR;
            dist = (inFront.getPosition().getValue() - veh.getPosition().getValue()) * r.getLength();
        }

        if(dist < minDist) {
            acc = veh.getMaxDecceleration();
            speed = Math.max(0, veh.getSpeed() - acc * Controller.getInstance().getTicker().getTickTimeInS());
        }else {
            acc = veh.getMaxAcceleration();
            speed = Math.min(veh.getDesiredSpeed(), veh.getSpeed() + acc * Controller.getInstance().getTicker().getTickTimeInS());
        }

        veh.updateAll(speed,acc,r);
/*
        //Waiting at the traffic light
        if (veh.getPosition().getKey() == null) {
            return;
        }
        //If you are in the end of the road it shouldn't bother
        if (veh.getPosition().getValue() > 1) {
            return;
        }

        Road r = veh.getPosition().getKey();
        double roadLength = r.getLength();
        double v = veh.getSpeed();
        double l = veh.getLength();
        double reactionTime = veh.getReactionTime();
        double sMin = reactionTime * v + l;

        Vehicle inFrontVehicle = veh.getPredecessor();

        //Get the road your on
        //Get the percentage dunno why I split it when I split later on
        double[] percentage;
        //If there is a car ahead of you
        if (inFrontVehicle != null) {
            percentage = new double[]{veh.getPosition().getValue(), inFrontVehicle.getPosition().getValue()};
            //Get various of data
            double[] x = new double[]{roadLength * percentage[0], roadLength * percentage[1]};
            //Distance between the two cars
            double distance = x[1] - x[0] - veh.getLength();
            double s = Math.abs(distance);
            //Time it takes you to react

            //If you can react fast enough
            if (s < sMin) {
                v = Math.max(2,
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
            percentage = new double[]{veh.getPosition().getValue(), 0};
            //Get various of data
            double x = roadLength * percentage[0];

            //Distance between you and the traffic light
            double s = Math.abs(x - roadLength);

            sMin = reactionTime * v + veh.getDesiredSpeed() / (veh.getMaxDecceleration());
            //Time it takes you to react
            //If you can react fast enough
            if (s < sMin) {
                v = Math.max(2,
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

            //veh.updateAll();
        }*/
    }

}

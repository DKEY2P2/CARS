package models;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

/**
 *
 * @author Kareem
 */
public class Forbe implements Model {

    @Override
    public void calculate(Vehicle veh) {

        Road r = veh.getPosition().getKey();
        Vehicle inFrontVehicle = veh.getPredecessor();
        TrafficLight trl = r.getEnd().getTrafficLight(r);
        double acc = veh.getAcceleration();
        double dist;
        double curSpeed = veh.getSpeed();
        double newSpeed;
        double sMin = 15;
        double sMin2 = 10;
        //veh.getReactionTime() * curSpeed + 1;// + veh.getDesiredSpeed() / (veh.getMaxDecceleration()); // REACTIONTIME

        if(inFrontVehicle == null){//if no one in front
            if(trl.isGreen()){
                acc = veh.getMaxAcceleration();
                newSpeed = Math.min(Math.min(veh.getDesiredSpeed(), r.getSpeedLimit()), curSpeed + acc * Controller.getInstance().getTicker().getTickTimeInS());
            }else{
                dist = (1 - veh.getPosition().getValue()) * r.getLength();

                if (dist < sMin) {
                    acc = -veh.getMaxDecceleration();
                    newSpeed = Math.min(10, curSpeed + acc * Controller.getInstance().getTicker().getTickTimeInS());
                    if(dist < sMin2){
                        newSpeed = 0;
                    }
                } else {
                    if(acc >= veh.getMaxAcceleration()){
                        acc = veh.getMaxAcceleration();
                    }else if(acc <= veh.getMaxDecceleration()){
                        acc = veh.getMaxAcceleration();
                    }
                    newSpeed = Math.min(Math.min(veh.getDesiredSpeed(), r.getSpeedLimit()), curSpeed + acc * Controller.getInstance().getTicker().getTickTimeInS());
                }
            }

        }else{
            dist = (inFrontVehicle.getPosition().getValue() - veh.getPosition().getValue()) * r.getLength();
            newSpeed = (dist - inFrontVehicle.getLength())/1.34d;
            acc = (newSpeed - curSpeed)/Controller.getInstance().getTicker().getTickTimeInS();

            if (dist < sMin) {
                acc = -veh.getMaxDecceleration();
                newSpeed = Math.min(inFrontVehicle.getSpeed(), newSpeed);
                if(dist < sMin2){
                    newSpeed = inFrontVehicle.getSpeed() + acc;
                }
            } else {
                if(acc >= veh.getMaxAcceleration()){
                    acc = veh.getMaxAcceleration();
                }else if(acc <= veh.getMaxDecceleration()){
                    acc = veh.getMaxAcceleration();
                }
                newSpeed = Math.min(Math.min(veh.getDesiredSpeed(), r.getSpeedLimit()), newSpeed);
            }

        }
        veh.updateAll(newSpeed,acc,r);
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

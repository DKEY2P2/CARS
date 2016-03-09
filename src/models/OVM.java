package models;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

import java.util.AbstractMap;

/**
 * http://traffic.phys.cs.is.nagoya-u.ac.jp/~mstf/sample/ov/src.html
  _    _ _   _ _____  ______ _____     _____ ____  _   _  _____ _______ _____  _    _  _____ _______ _____ ____  _   _
 | |  | | \ | |  __ \|  ____|  __ \   / ____/ __ \| \ | |/ ____|__   __|  __ \| |  | |/ ____|__   __|_   _/ __ \| \ | |
 | |  | |  \| | |  | | |__  | |__) | | |   | |  | |  \| | (___    | |  | |__) | |  | | |       | |    | || |  | |  \| |
 | |  | | . ` | |  | |  __| |  _  /  | |   | |  | | . ` |\___ \   | |  |  _  /| |  | | |       | |    | || |  | | . ` |
 | |__| | |\  | |__| | |____| | \ \  | |___| |__| | |\  |____) |  | |  | | \ \| |__| | |____   | |   _| || |__| | |\  |
  \____/|_| \_|_____/|______|_|  \_\  \_____\____/|_| \_|_____/   |_|  |_|  \_\\____/ \_____|  |_|  |_____\____/|_| \_|
 * @author Lucas Vanparijs
 * @since 24-02-16
 */
public class OVM implements Model{

    private final double CONSTANT = 2;

    private Vehicle getInFront(Vehicle veh, Road r) {
        Vehicle inFrontVehicle = null;
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
    public void calculate(Vehicle v) {
        Road r = v.getPosition().getKey();

        TrafficLight trl = v.getNextLight();

        double t = Controller.getInstance().getTicker().getTickTimeInS();
        double speed = v.getSpeed();
        double speedLimit = r.getSpeedLimit();
        double dv;

        Vehicle prev = getInFront(v,r);

        if(v.getPosition().getValue()>1.0)
            v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(r,1d));

        if (v.getPosition().getKey() == null) {
            return;
        }
        if(prev == null) {
            if (!trl.isGreen()) {
                double dist = (1 - v.getPosition().getValue()) * r.getLength(); //distance to the traffic light
                dv = v.getReactionTime() * (optimalVelocity(dist) - speed);
            } else {
                if (speed >= speedLimit) {
                    dv = 0;
                } else {
                    dv = v.getMaxAcceleration();
                }
            }
        } else {
            double dist = (prev.getPosition().getValue() - v.getPosition().getValue()) * r.getLength();
            dv = v.getReactionTime() * (optimalVelocity(dist) - speed);

        }
        v.setAcceleration(dv);
        if(v.getAcceleration()<0)
            v.setSpeed(0);
        else
            v.setSpeed(speed + v.getAcceleration());
        if(v.getPosition().getValue()==100) {
            v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(v.nextPlaceToGo().hasRoad(trl.getIntersection()), 0d));
        }else {
            v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(r, v.getPosition().getValue() + ((v.getSpeed() * t / r.getLength()))));
        }
    }



    public double optimalVelocity(double dist){
        return Math.tanh(dist-CONSTANT) + Math.tanh(CONSTANT);
}

}

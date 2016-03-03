package models;

import controller.Controller;
import jdk.internal.org.objectweb.asm.util.TraceFieldVisitor;
import map.Intersection;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

import java.util.AbstractMap;
import java.util.Iterator;

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

    private final double CONSTANT = 15;

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
    public void calculate(Vehicle v) {
        Road r = v.getPosition().getKey();

        TrafficLight trl = v.getNextLight();

        double t = Controller.getInstance().getTicker().getTickTimeInS();
        double speed = v.getSpeed();
        double speedLimit = r.getSpeedLimit();
        double dv;

        Vehicle prev = getInFront(v,r);

        System.out.println(v.getAcceleration());
        System.out.println(v.getSpeed());

        if (v.getPosition().getValue() > 1) {
            return;
        }

        if(prev == null) {
            if (!trl.isGreen()) {
                if ((v.getBreakingDistance() <= ((1 - v.getPosition().getValue()) * r.getLength())))
                    dv = -v.getMaxDecceleration();
                else
                    dv = v.getMaxAcceleration();
            } else {
                if (speed >= speedLimit) {
                    dv = 0;
                } else {
                    dv = v.getMaxAcceleration();
                }
            }
                v.setAcceleration(dv);
            } else {
                double dist = (prev.getPosition().getValue() - v.getPosition().getValue()) * r.getLength();
                dv = v.getReactionTime() * (optimalVelocity(dist) - speed);
                v.setAcceleration(dv);
            }
            if(v.getPosition().getValue() >= 99) {
                if (v.getAcceleration() >= 0) {
                    v.setSpeed(speed + v.getAcceleration() * t);
                    v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(trl.getOut(), 0d));
                } else {
                    if (speed <= v.getAcceleration()) {
                        v.setSpeed(0);
                    } else {
                        v.setSpeed(speed + v.getAcceleration() * t);
                    }
                    v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(trl.getOut(), 0d));
                }
            }else{
                if (v.getAcceleration() >= 0) {
                    v.setSpeed(speed + v.getAcceleration());
                    v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(r, v.getPosition().getValue() + (speed) / r.getLength()));
                    v.setDistance(v.getDistance() + (speed));
                } else {
                    if (speed <= v.getAcceleration()) {
                        v.setSpeed(0);
                    } else {
                        v.setSpeed(speed + v.getAcceleration());
                    }
                    v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(r, v.getPosition().getValue() + (speed) / r.getLength()));
                    v.setDistance(v.getDistance() + (speed * t));
                }
            }
        }



    public double optimalVelocity(double dist){
        return Math.tanh(dist-CONSTANT) + Math.tanh(CONSTANT);
}

}

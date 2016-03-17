package models;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * http://traffic.phys.cs.is.nagoya-u.ac.jp/~mstf/sample/ov/src.html
 * @author Lucas Vanparijs
 * @since 24-02-16
 */
public class OVM implements Model{

    private final double CONSTANT = 5;

    private Vehicle getInFront(Vehicle veh, Road r) {
        Vehicle inFrontVehicle = null;

        PriorityQueue<Vehicle> pq = r.getVehicles();
        Iterator<Vehicle> iv = pq.iterator();

        while(iv.hasNext()){
            Vehicle c = iv.next();
            if(c == veh){
                return inFrontVehicle;
            }else{
                inFrontVehicle = c;
            }
        }

        return inFrontVehicle;
    }

    @Override
    public void calculate(Vehicle v) {

        Road r = v.getPosition().getKey();

        TrafficLight trl = r.getEnd().getTrafficLight(r);

        double optVel = v.getDesiredSpeed()/2;
        double t = Controller.getInstance().getTicker().getTickTimeInS();
        double speed = v.getSpeed();
        double speedLimit = r.getSpeedLimit();
        double dv;

        Vehicle prev = getInFront(v,r);

        if (v.getPosition().getKey().getEnd() == v.getDestination() && v.getPosition().getValue()>1) {
            v = null;
            return;
        }

        if(prev == null) {
            if (!trl.isGreen()) {
                double dist = (1 - v.getPosition().getValue()) * r.getLength(); //distance to the traffic light
                //dv = v.getReactionTime() * (optimalVelocity(dist) - speed);
                dv = (optimalVelocity(dist) - speed);
            } else {
                if (speed >= speedLimit) {
                    dv = speedLimit-speed;
                } else {
                    double dif = speedLimit-speed;
                    if(v.getMaxAcceleration() > dif)
                        dv = dif;
                    else
                        dv = v.getMaxAcceleration();
                }
            }
        } else {
            double dist = (prev.getPosition().getValue() - v.getPosition().getValue()) * r.getLength() - v.getLength();
            //dv = v.getReactionTime() * (optimalVelocity(dist) - speed);
            dv = (optimalVelocity(dist) - speed);

        }
        v.setAcceleration(dv);
        if(v.getAcceleration()<0) {
            double newSpeed = speed + dv;
            if(newSpeed < 0)
                v.setSpeed(0);
            else
                v.setSpeed(newSpeed);
        }else{
            v.setSpeed(speed + v.getAcceleration());
        }

        if((1-v.getPosition().getValue())*r.getLength()<CONSTANT) {
            v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(v.nextPlaceToGo().hasRoad(trl.getIntersection()), 0d));
        }else {
            double d = v.getPosition().getValue() + ((v.getSpeed() * t / r.getLength()));
            if(d>1)
                v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(v.nextPlaceToGo().hasRoad(trl.getIntersection()), 0d));
            else
                v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(r, v.getPosition().getValue() + ((v.getSpeed() * t / r.getLength()))));
        }
    }



    public double optimalVelocity(double dist){
        double result = Math.tanh(dist-CONSTANT) + Math.tanh(CONSTANT);
        if(result < 1){
            return 0;
        }else{
            return result;
        }
}

}

package models;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;
import vehicle.VehicleHolder;

import java.util.AbstractMap;
import java.util.Iterator;
import java.util.PriorityQueue;

/**
 * http://traffic.phys.cs.is.nagoya-u.ac.jp/~mstf/sample/ov/src.html
 * @author Lucas Vanparijs
 * @since 24-02-16
 */
public class OVM implements Model{

    private double maxSpeed = 0;
    private double safety = 3;

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
        maxSpeed = speedLimit/2;
        double dv;
        double safetyPercent = 1-(safety/r.getLength());
        double minDist = safety/r.getLength();
        
        Vehicle prev = getInFront(v,r);
        /*if (v.getPosition().getKey().getEnd() == v.getDestination() && v.getPosition().getValue()>=1) {
            VehicleHolder.getInstance().remove(v);
            return;
        }*/

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

        double p = v.getPosition().getValue() + ((v.getSpeed() * t / r.getLength()));
        if(p>=1d){
            if(!trl.isGreen()){
                v.setSpeed(0);
                v.setAcceleration(0);
            }
            //v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(r, 1d));
            r.getVehicles().remove(v);
            if(v.getDestination() == r.getEnd()){
                VehicleHolder.getInstance().remove(v);
            }else{
                v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(v.nextPlaceToGo().hasRoad(r.getEnd()), 0d));
                v.getPosition().getKey().addCar(v);
            }
            //VehicleHolder.getInstance().remove(v);
        }else{
            v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(r, v.getPosition().getValue() + ((v.getSpeed() * t / r.getLength()))));

            /*if(prev != null && v.getPosition().getValue() >= prev.getPosition().getValue()-minDist){
                System.out.println("HERE");
                v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(r, prev.getPosition().getValue()-safetyPercent));
                v.setSpeed(prev.getSpeed());
                v.setAcceleration(prev.getAcceleration());
            }*/
        }

        /*
        if((1-v.getPosition().getValue())*r.getLength()<CONSTANT) {
            v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(v.nextPlaceToGo().hasRoad(trl.getIntersection()), 0d));
        }else {
            double d = v.getPosition().getValue() + ((v.getSpeed() * t / r.getLength()));
            if(d>1)
                v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(v.nextPlaceToGo().hasRoad(trl.getIntersection()), 0d));
            else
                v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(r, v.getPosition().getValue() + ((v.getSpeed() * t / r.getLength()))));
        }*/
    }



    public double optimalVelocity(double dist){
        double result = Math.tanh(dist-safety-2) + Math.tanh(2);
        return result * maxSpeed;
}

}
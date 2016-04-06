package models;

import controller.Controller;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

/**
 * http://traffic.phys.cs.is.nagoya-u.ac.jp/~mstf/sample/ov/src.html
 * @author Lucas Vanparijs
 * @since 24-02-16
 */
public class OVM implements Model{

    private double maxSpeed = 0;
    private double safety = 3;

    @Override
    public void calculate(Vehicle v) {

        Road r = v.getPosition().getKey();
        TrafficLight trl = r.getEnd().getTrafficLight(r);
        Vehicle prev = v.getPredecessor();

        double speed = v.getSpeed();
        double speedLimit = r.getSpeedLimit();
        double dv;

        maxSpeed = speedLimit/2;

        if(prev == null) {
            if (!trl.isGreen()) {
                double dist = (1 - v.getPosition().getValue()) * r.getLength(); //distance to the traffic light
                dv = (1/v.getReactionTime()) * (optimalVelocity(dist) - speed);
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
            dv = (1/v.getReactionTime()) * (optimalVelocity(dist) - speed);

        }

        v.updateAll(speed + dv * Controller.getInstance().getTicker().getTickTimeInS(), dv,r);

        /*v.setAcceleration(dv);
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
            r.getVehicles().remove(v);
            if(v.getDestination() == r.getEnd()){
                VehicleHolder.getInstance().remove(v);
            }else{
                v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(v.nextPlaceToGo().hasRoad(r.getEnd()), 0d));
                v.getPosition().getKey().addCar(v);
            }
        }else{
            v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(r, v.getPosition().getValue() + ((v.getSpeed() * t / r.getLength()))));
        }*/
    }



    public double optimalVelocity(double dist){
        double result = Math.tanh(dist-safety-2) + Math.tanh(2);
        return result * maxSpeed;
}

}
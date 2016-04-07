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
    private double safety = 10; //minimum distance in meters

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
                dv = v.getReactionTime() * (optimalVelocity(dist) - speed);
            } else {
                dv = v.getReactionTime() * (optimalVelocity(safety+10) - speed);
            }
        } else {
            double dist = (prev.getPosition().getValue() - v.getPosition().getValue()) * r.getLength() - v.getLength();
            dv = v.getReactionTime() * (optimalVelocity(dist) - speed);
        }

        v.updateAll(speed + dv * Controller.getInstance().getTicker().getTickTimeInS(),dv,r);
    }



    public double optimalVelocity(double dist){
        double result = Math.tanh(dist-safety-2) + Math.tanh(2);
        if(result < 0)
            result = 0;
        return result * maxSpeed;
}

}
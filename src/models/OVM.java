package models;

import controller.Controller;
import map.Road;
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

    private final double CONSTANT = 2;

    @Override
    public void calculate(Vehicle v) {
        Vehicle prev = v.getPredecessor();
        double t = Controller.getInstance().getTicker().getTickTimeInS();
        double speed = v.getSpeed();
        double speedLimit = v.getPosition().getKey().getSpeedLimit();
        double dv;

        if(prev == null){
            //go towards maximum speed
            if(speed >= speedLimit){
                dv = 0;
            }else{
                dv = v.getMaxAcceleration() * t;
            }
        }else{
            double dist = (prev.getPosition().getValue() - v.getPosition().getValue()) * v.getPosition().getKey().getLength();
            dv = v.getReactionTime()*(optimalVelocity(dist)-speed);
        }
        v.setSpeed(speed + dv * t);
        v.setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(v.getPosition().getKey(), v.getPosition().getValue() + speed*v.getPosition().getKey().getLength()));
    }



    public double optimalVelocity(double dist){
        return Math.tanh(dist-CONSTANT) + Math.tanh(CONSTANT);
    }

}

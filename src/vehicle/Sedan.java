package vehicle;

import algorithms.Algorithm;
import helper.Logger;
import java.awt.Color;
import java.util.AbstractMap;
import java.util.Random;
import map.Intersection;
import map.Road;
import models.Model;

/**
 * Model after 300c
 *
 * @author Kareem
 */
public class Sedan extends Vehicle {

    protected Sedan(Road start, double percentage, Model m, Algorithm a, Intersection destination) {
        super(start, percentage, m, a);
        Random r = new Random();
        setDesiredSpeed(27.777777778 + r.nextInt(3));//100kmh
        setDesiredDeceleration(1.67 + r.nextInt(2));
        setMaxAcceleration(27.78 / 5.6);// Crystler 300c v8 edition 2015
        setMaxDecceleration(10.175);//100kmh to 0 takes 36.63m thus 10ms^2 from bmw  
        setReactionTime(2.3);//average human - http://copradar.com/redlight/factors/
        setLength(4.004);//Crystler 300c v8 edition 2015
        setDestination(destination);
        setColor(Color.cyan);
    }

    @Override
    public boolean update() {
        if (VehicleHolder.getInstance().contains(this)) {
            if (getPosition().getValue() > 1) {
                if (getPosition().getKey().getEnd() == getDestination()) {
                    //Kills the car
                    boolean a = VehicleHolder.getInstance().remove(this);
                    if (!a) {
                        Logger.LogError("Failed to delete object", this);
                    }
                }
                //Adds it to the queue if not already in there
                if (!getPosition().getKey().getEnd().getTrafficLight(getPosition().getKey()).getWaiting()
                        .contains(this)) {
                    getPosition().getKey().getEnd().getTrafficLight(getPosition().getKey()).getWaiting().add(this);
                    setPosition(new AbstractMap.SimpleImmutableEntry<Road, Double>(getPosition().getKey(), 1d));
                    setSpeed(0);
                    setAcceleration(0);
                }
            } else {
                setTimeOnRoad(getTimeOnRoad() + 1);//add one tick
                getModel().calculate(this);
            }
            return true;
        }

        return false;
    }
}

package vehicle;

import algorithms.Algorithm;
import controller.SimulationSettings;
import helper.Logger;
import map.Intersection;
import map.Road;
import models.Model;

import java.awt.*;
import java.util.AbstractMap;
import java.util.Random;

/**
 * Model after 300c
 *
 * @author Kareem
 */
public class Sedan extends Vehicle {

    //Volkswagen Passat

    protected Sedan(Road start, double percentage, Model m, Algorithm a, Intersection destination) {
        super(start, percentage, m, a);
        Random r = new Random(SimulationSettings.getInstance().getRandomSeed());
        setDesiredSpeed(66.66);//100kmh
        setDesiredDeceleration(1.67 + r.nextInt(2));
        setMaxAcceleration(4.55373406193078);
        setMaxDecceleration(21.73);//http://blog.esurance.com/stopping-distance-is-the-3-second-rule-wrong/#.VwailPkrKUk
        setReactionTime(2.3);//average human - http://copradar.com/redlight/factors/
        setLength(4.866);
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

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package vehicle;

import algorithms.pathfinding.Algorithm;
import controller.SimulationSettings;
import helper.Logger;
import map.Intersection;
import map.Road;
import models.Model;

import java.awt.*;
import java.util.AbstractMap;
import java.util.Random;

/**
 *
 * @author Kareem
 */
public class Minibus extends Vehicle {

    //Volkswagen Sharan

    protected Minibus(Road start, double percentage, Model m, Algorithm a, Intersection destination) {
        super(start, percentage, m, a);
Random r = SimulationSettings.getInstance().getRandom();
        setDesiredSpeed(55.55);
        setDesiredDeceleration(1.67 + r.nextInt(2));
        setMaxAcceleration(2.80583613916947);
        setMaxDecceleration(21.73 - 2);http://blog.esurance.com/stopping-distance-is-the-3-second-rule-wrong/#.VwailPkrKUk - THIS IS FOR THE AVERAGE CAR so i subtracted a bit for the minibus
        setReactionTime(2.3);//average human - http://copradar.com/redlight/factors/
        setLength(4.854);
        setDestination(destination);
        setColor(Color.yellow);
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

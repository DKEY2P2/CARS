package vehicle.forbe;

import java.util.ArrayList;

import algorithms.Algorithm;
import map.Intersection;
import map.Road;
import map.TrafficLight;
import models.IntelligentDriver;
import vehicle.Vehicle;

/**
 * A sport modelled after Jaguar XK coupe 2007
 * <p>
 * Using Forbe's model
 *
 * @author Kareem Horstink
 */
public class SportCarF extends Vehicle {

    public SportCarF(Road start, double percentage, Algorithm a, Intersection destination) {
        super(start, percentage, new IntelligentDriver(), a);
        setDesiredSpeed(27.777777778);//100kmh
        setDesiredBraking(1.6777); //http://home2.fvcc.edu/~dhicketh/DiffEqns/Spring11projects/Scott_Miller/Project.pdf
        setMaxAcceleration(4.5);// Jaguar XK Coupe 2007 - http://hypertextbook.com/facts/2001/MeredithBarricella.shtml
        setMaxDecceleration(2.98704);//Traffic Engineering Handbook, 5th ed. (J. L. Prine, ed.). ITE, Washington, D.C., 1999.
        setReactionTime(2.3);//average human - http://copradar.com/redlight/factors/
        setLength(4.7904400000000002535);//Jaguar Xk Coupe 2007 - http://www.edmunds.com/jaguar/xk-series/2007/features-specs/
        setDestination(destination);
    }

    @Override
    public boolean update() {
        if (getPosition().getValue() > 1) {
            if (getPosition().getKey().getEnd() == getDestination()) {
                //TODO do something??
            }
            //Adds it to the queue if not already in there
            if (!getPosition().getKey().getEnd().getQueue(getPosition().getKey()).contains(this)) {
                getPosition().getKey().getEnd().getQueue(getPosition().getKey()).offer(this);
            }
        } else {
            ArrayList<TrafficLight> atl = getPosition().getKey().getEnd().getTrafficLights();
            for(TrafficLight tl : atl)
                if(tl.getIn() == getPosition().getKey())
                    if(!tl.isGreen() && getPosition().getValue()>0.95) {
                        setSpeed(0);
                    }else{
                        setTimeOnRoad(getTimeOnRoad() + 1);//add one tick
                        getModel().calculate(this);
                    }
        }


        return true;
    }

}

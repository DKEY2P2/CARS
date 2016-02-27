package vehicle.forbe;

import algorithms.Algorithm;
import java.util.AbstractMap;
import map.Intersection;
import map.Road;
import models.Forbe;
import vehicle.Vehicle;

/**
 *
 * @author Kareem
 */
public class SportCar extends Vehicle {

    public SportCar(Road start, double percentage, Algorithm a, Intersection destination) {
        super(start, percentage, new Forbe(), a);
        setDesiredSpeed(27.777777778);//100kmh
        setMaxAcceleration(4.5);// Jaguar XK Coupe 2007 - http://hypertextbook.com/facts/2001/MeredithBarricella.shtml
        setMaxDecceleration(2.98704);//Traffic Engineering Handbook, 5th ed. (J. L. Prine, ed.). ITE, Washington, D.C., 1999.
        setReactionTime(2.3);//average human - http://copradar.com/redlight/factors/
        setLength(4.7904400000000002535);//Jaguar Xk Coupe 2007 - http://www.edmunds.com/jaguar/xk-series/2007/features-specs/
        setDestination(destination);
    }

    @Override
    public boolean update() {
        if (getPosition().getValue() > 1) {
            if(getPosition().getKey().getEnd() == getDestination()){
                System.out.println("done");
            }

            //Adds it to the queue if not already in there
            if(!getPosition().getKey().getEnd().getQueue(getPosition().getKey()).contains(this)){
                getPosition().getKey().getEnd().getQueue(getPosition().getKey()).offer(this);
            }
        } else {
            setTimeOnRoad(getTimeOnRoad() + 1);//add one tick
            getModel().calculate(this);
        }
        return true;
    }

}

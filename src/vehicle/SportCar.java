package vehicle;

import algorithms.Algorithm;
import java.util.Random;

import map.Intersection;
import map.Road;
import models.Forbe;
import models.Model;

/**
 * A sport modelled after Jaguar XK coupe 2007
 *
 * @author Kareem Horstink
 */
public class SportCar extends Vehicle {

    protected SportCar(Road start, double percentage, Model m, Algorithm a, Intersection destination) {
        super(start, percentage, m, a);
        Random r = new Random();
        setDesiredSpeed(27.777777778 + r.nextInt(10));//100kmh
        setDesiredDeceleration(1.67 + r.nextInt(2));
        setMaxAcceleration(4.5 + r.nextInt(3));// Jaguar XK Coupe 2007 - http://hypertextbook.com/facts/2001/MeredithBarricella.shtml
        setMaxDecceleration(2.98704 + r.nextInt(1));//Traffic Engineering Handbook, 5th ed. (J. L. Prine, ed.). ITE, Washington, D.C., 1999.
        setReactionTime(2.3 + r.nextDouble());//average human - http://copradar.com/redlight/factors/
        setLength(4.7904400000000002535);//Jaguar Xk Coupe 2007 - http://www.edmunds.com/jaguar/xk-series/2007/features-specs/
        setDestination(destination);
    }

    @Override
    public boolean update() {
    	if(VehicleHolder.getInstance().contains(this)){
            if (getPosition().getValue() > 1) {
            if (getPosition().getKey().getEnd() == getDestination()) {
                //Kills the car
                boolean a = vehicle.VehicleHolder.getInstance().remove(this);
            }
            //Adds it to the queue if not already in there
            if (!getPosition().getKey().getEnd().getTrafficLight(getPosition().getKey()).getWaiting().contains(this)) {
                getPosition().getKey().getEnd().getTrafficLight(getPosition().getKey()).getWaiting().offer(this);
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

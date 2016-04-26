package vehicle.pipe;

import algorithms.Algorithm;
import controller.SimulationSettings;
import map.Intersection;
import map.Road;
import models.OVM;
import vehicle.Vehicle;

import java.util.Random;

/**
 * A sport modelled after Jaguar XK coupe 2007
 * <p>
 * Using Forbe's model
 *
 * @author Kareem Horstink
 */
public class SportCarP extends Vehicle {

    public SportCarP(Road start, double percentage, Algorithm a, Intersection destination) {
        super(start, percentage, new OVM(), a);
        Random r = new Random(SimulationSettings.getInstance().getRandomSeed());
        setDesiredSpeed(27.777777778 + r.nextInt(10));//100kmh
        setMaxAcceleration(4.5 + r.nextInt(3));// Jaguar XK Coupe 2007 - http://hypertextbook.com/facts/2001/MeredithBarricella.shtml
        setMaxDecceleration(2.98704 + r.nextInt(1));//Traffic Engineering Handbook, 5th ed. (J. L. Prine, ed.). ITE, Washington, D.C., 1999.
        setReactionTime(2.3 + r.nextDouble());//average human - http://copradar.com/redlight/factors/
        setLength(4.7904400000000002535);//Jaguar Xk Coupe 2007 - http://www.edmunds.com/jaguar/xk-series/2007/features-specs/
        setDestination(destination);
    }

    @Override
    public boolean update() {
        if (getPosition().getValue() > 1) {
            if (getPosition().getKey().getEnd() == getDestination()) {
                //Kills the car
                boolean a = vehicle.VehicleHolder.getInstance().remove(this);
            }
            //Adds it to the queue if not already in there
            if (!getPosition().getKey().getEnd().getTrafficLight(getPosition().getKey()).getWaiting().contains(this)) {
                getPosition().getKey().getEnd().getTrafficLight(getPosition().getKey()).getWaiting().offer(this);
                //setSpeed(0);
                //setAcceleration(0);
            }
        } else {
//            ArrayList<TrafficLight> atl = getPosition().getKey().getEnd().getTrafficLights();
//            for (TrafficLight tl : atl) {
//                if (tl.getIn() == getPosition().getKey()) {
            setTimeOnRoad(getTimeOnRoad() + 1);//add one tick
//                }
//            }
            getModel().calculate(this);
            /*if(!tl.isGreen() && getPosition().getValue()>0.95) {
             setSpeed(0);
             setAcceleration(0);
             }else{

             }*/
        }

        return true;
    }

}

package vehicle;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Random;

import algorithms.Algorithm;
import helper.Logger;
import map.Intersection;
import map.Road;
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
		setMaxAcceleration(4.5 );// Jaguar XK Coupe 2007 - http://hypertextbook.com/facts/2001/MeredithBarricella.shtml
		setMaxDecceleration(2.98704 );//Traffic Engineering Handbook, 5th ed. (J. L. Prine, ed.). ITE, Washington, D.C., 1999.
		setReactionTime(2.3);//average human - http://copradar.com/redlight/factors/
		setLength(4.7904400000000002535);//Jaguar Xk Coupe 2007 - http://www.edmunds.com/jaguar/xk-series/2007/features-specs/
		setDestination(destination);
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
					setPosition(new SimpleImmutableEntry<Road, Double>(getPosition().getKey(), 1d));
					//setSpeed(0);
					//setAcceleration(0);
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

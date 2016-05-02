package algorithms.trafficlights.greedy;

import controller.Ticker;
import java.util.ArrayList;
import map.Road;
import map.TrafficLight;
import map.intersection.detectors.Detector;

/**
 * An extension of the {@link GreedyVersion1} which hopes to makes it more
 * realistic.<p>
 * Not necessarily better in measurement of performance but better in terms of
 * realistic-ness
 *
 * @author Kareem Horstink
 */
public class GreedyVersion2 extends GreedyVersion1 {

    //The lower bound of how long the light should stay green
    //In seconds
    private static double lowerBound = 10;

    //The upper bound of how long the light can stay green
    //In seconds
    private static double upperBound = 60;

    /**
     * Get the value of lowerBound
     *
     * @return the value of lowerBound
     */
    public static double getLowerBound() {
        return lowerBound;
    }

    /**
     * Set the value of lowerBound
     *
     * @param lowerBound new value of lowerBound
     */
    public static void setLowerBound(double lowerBound) {
        GreedyVersion2.lowerBound = lowerBound;
    }

    /**
     * Get the value of upperBound
     *
     * @return the value of upperBound
     */
    public static double getUpperBound() {
        return upperBound;
    }

    /**
     * Set the value of upperBound
     *
     * @param upperBound new value of upperBound
     */
    public static void setUpperBound(double upperBound) {
        GreedyVersion2.upperBound = upperBound;
    }

    /**
     * The default constructor of the Greedy intersection. Same as the default
     * intersection
     *
     * @param x The x location of the intersection
     * @param y The y location of the intersection
     * @param t The ticker which would update the intersection
     */
    public GreedyVersion2(int x, int y, Ticker t) {
        super(x, y, t);
    }

    @Override
    public void updateLight(double elapsed) {
        ArrayList<TrafficLight> tLights = getTrafficLights();
        //Don't bother when there is no traffic light
        if (tLights.isEmpty()) {
            return;
        }

        if (tLights.size() > 1) {

            //Get the index of traffic light with largest amount of cars waiting
            //with some conditions on it
            int indexWithMostCars = -1;
            for (int i = 0; i < tLights.size(); i++) {
                //The current light being checked
                SmartTrafficLightExtended light = (SmartTrafficLightExtended) tLights.get(i);
                if (light.isGreen()) {
                    //If the current light is green, we how long the light is
                    //green for and check if its within the bounds.

                    //If the light is below the lower bound, we stop checking as
                    //the light has to be green until the lower bound is reached
                    //If the light is above the lower bound but lower than upper
                    //bound, we do the same check as GreedyVersion1
                    //If the light is above the upper bound then we set the light
                    //to be red and continue searching for a new light to change
                    //to green with considering the current light
                    light.incrementCurrentGreenTime(0.1);
                    if (light.getCurrentGreenTime() < lowerBound) {
                        return;
                    } else if (light.getCurrentGreenTime() > upperBound) {
                        light.setLight(false);
                        light.setCurrentGreenTime(0);
                        continue;
                    }
                }
                indexWithMostCars = light.getDetector().getNumberOfCarsDetected() > indexWithMostCars ? i : indexWithMostCars;
                light.setLight(false);
            }
            //Switches the lights
            if (((SmartTrafficLightExtended) tLights.get(indexWithMostCars)).getCurrentGreenTime() == 0) {
                ((SmartTrafficLightExtended) tLights.get(indexWithMostCars)).
                        incrementCurrentGreenTime(0.1);
                for (int i = 0; i < tLights.size(); i++) {
                    if (i == indexWithMostCars) {
                        continue;
                    }
                    SmartTrafficLightExtended light = (SmartTrafficLightExtended) tLights.get(i);
                    light.setCurrentGreenTime(0);
                }
            }
            tLights.get(indexWithMostCars).setLight(true);
        } else {
            //If there is always one light, we always set it to green
            tLights.get(0).setLight(true);
        }
    }

    public Road addRoad(Road r, Detector d) {
        //Only change is that it now uses smart traffic light extended instead of the default ones

        getTrafficLights().add(new SmartTrafficLightExtended(this, r, getOut(), d));

        return r;
    }

    @Override
    public Road addRoad(Road r) {
        //Only change is that it now uses smart traffic light extended instead of the default ones
        getRoads().add(r);
        boolean flag = false;
        if (r.getStart() == this) {
            getOut().add(r);
            flag = true;
        } else {
            getIn().add(r);
        }

        if (flag) {
            getTrafficLights().stream().forEach((trafficLight) -> {
                trafficLight.addOut(r);
            });
        } else {
            getTrafficLights().add(new SmartTrafficLightExtended(this, r, getOut(), getType()));
        }

        return r;
    }

}

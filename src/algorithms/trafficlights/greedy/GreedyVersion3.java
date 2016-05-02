package algorithms.trafficlights.greedy;

import controller.Ticker;
import java.util.ArrayList;
import map.Road;
import map.TrafficLight;

/**
 * A greedy (version 2) with a heurstic to 
 *
 * @author Kareem Horstink
 */
public class GreedyVersion3 extends GreedyVersion2 {

    public GreedyVersion3(int x, int y, Ticker t) {
        super(x, y, t);
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
            getTrafficLights().add(new ConnectedTrafficLight(this, r, getOut(), getType()));
        }

        return r;
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
                    light.incrementCurrentGreenTime(getTicker().getTickTimeInS());
                    if (light.getCurrentGreenTime() < getLowerBound()) {
                        return;
                    } else if (light.getCurrentGreenTime() > getUpperBound()) {
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
                ((SmartTrafficLightExtended) tLights.get(indexWithMostCars)).incrementCurrentGreenTime(getTicker().getTickTimeInS());
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

}

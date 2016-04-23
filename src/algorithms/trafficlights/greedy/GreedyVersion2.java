package algorithms.trafficlights.greedy;

import controller.Ticker;
import java.util.ArrayList;
import map.Intersection;
import map.Road;
import map.TrafficLight;
import map.intersection.detectors.Detector;

/**
 * An extension of the {@link SimpleGreedyIntersection} which hopes to makes it
 * more realistic.<p>
 * Not necessarily better more in measure of performance but better in terms of
 * realistic
 *
 * @author Kareem Horstink
 */
public class GreedyVersion2 extends SimpleGreedyIntersection {

    private static double lowerBound = 10;

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

    public GreedyVersion2(int x, int y, Ticker t) {
        super(x, y, t);
    }

    @Override
    public void updateLight(double elapsed) {
        ArrayList<TrafficLight> tLights = getTrafficLights();
        if (tLights.isEmpty()) {
            return;
        }
        if (tLights.size() > 1) {
            int indexWithMostCars = -1;
            for (int i = 0; i < tLights.size(); i++) {
                GreedyVersion2.GreedyTrafficLightExtended light = (GreedyVersion2.GreedyTrafficLightExtended) tLights.get(i);
                if (light.isGreen()) {
                    light.incrementCurrentGreenTime(getTicker().getTickTimeInS());
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
            tLights.get(indexWithMostCars).setLight(true);
        } else {
            tLights.get(0).setLight(true);
        }
    }

    @Override
    public Road addRoad(Road r) {
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
            getTrafficLights().add(new GreedyTrafficLightExtended(this, r, getOut(), getType()));
        }

        return r;
    }

    protected class GreedyTrafficLightExtended extends GreedyTrafficLight {

        /**
         * How long the light has been green for
         */
        private double currentGreenTime;

        /**
         * Get the value of currentGreenTime
         *
         * @return the value of currentGreenTime
         */
        public double getCurrentGreenTime() {
            return currentGreenTime;
        }

        /**
         * Set the value of currentGreenTime
         *
         * @param currentGreenTime new value of currentGreenTime
         */
        public void setCurrentGreenTime(double currentGreenTime) {
            this.currentGreenTime = currentGreenTime;
        }

        /**
         * Increments the value of currentGreenTime
         *
         * @param currentGreenTime how to be added
         */
        public void incrementCurrentGreenTime(double currentGreenTime) {
            this.currentGreenTime += currentGreenTime;
        }

        public GreedyTrafficLightExtended(Intersection i, Road in, ArrayList<Road> out, Detector detector) {
            super(i, in, out, detector);
        }

    }

}

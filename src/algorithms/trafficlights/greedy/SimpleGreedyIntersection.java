package algorithms.trafficlights.greedy;

import controller.Ticker;
import java.util.ArrayList;
import map.Intersection;
import map.Road;
import map.TrafficLight;
import map.intersection.DefaultIntersection;
import map.intersection.detectors.Detector;

/**
 * Hopefully a intersection which is smart that act all greedy like
 * <p>
 * Ignores everything and only depends on the number cars currently waiting at a
 * light
 *
 * @author Kareem Horstink
 */
public class SimpleGreedyIntersection extends DefaultIntersection {

    private static Detector type;

    public static Detector getType() {
        return type;
    }

    public static void setType(Detector type) {
        SimpleGreedyIntersection.type = type;
    }

    public SimpleGreedyIntersection(int x, int y, Ticker t) {
        super(x, y, t);
    }

    @Override
    public void updateLight(double elapsed) {
        ArrayList<TrafficLight> tLights = getTrafficLights();
        if (tLights.isEmpty()) {
            return;
        }
        //Check which use case to use

        if (tLights.size() > 1) {
            int indexWithMostCars = -1;
            for (int i = 0; i < tLights.size(); i++) {
                GreedyTrafficLight light = (GreedyTrafficLight) tLights.get(i);
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
            getTrafficLights().add(new GreedyTrafficLight(this, r, getOut(), type));
        }

        return r;
    }

    protected static class GreedyTrafficLight extends TrafficLight {

        private Detector detector;

        public GreedyTrafficLight(Intersection i, Road in, ArrayList<Road> out, Detector detector) {
            super(i, in, out);
            this.detector = detector.clone();
        }

        public Detector getDetector() {
            return detector;
        }

    }

}

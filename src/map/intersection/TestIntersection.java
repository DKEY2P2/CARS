package map.intersection;

import controller.Observer;
import controller.Ticker;
import helper.Logger;
import map.Intersection;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

import java.util.AbstractMap;

/**
 * Created by lvanp on 01/03/2016.
 */
public class TestIntersection extends Intersection implements Observer {

    private Ticker ticker;

    public TestIntersection(int x, int y, Ticker t) {
        super(x, y);
        t.addObserver(this);
        ticker = t;
    }

    public void update() {
        updateLight(ticker.getTimeBetweenTick());
        for (TrafficLight tl : getTrafficLights()) {
            Vehicle v = tl.getWaiting().poll();
            if (v == null) {
                return;
            }
            v.addToTraceLog(this);
            Intersection placeToGo = v.nextPlaceToGo();
            Road r = null;
            for (Road road : getRoads()) {
                if (road.getStart() == placeToGo) {
                    r = road;
                }
            }
            if (r == null) {
                for (Road road : getRoads()) {
                    if (road.getEnd() != this) {
                        r = road;
                        break;
                    }
                }
            }
            v.setPosition(new AbstractMap.SimpleImmutableEntry<>(r, 0d));
        }
    }

    @Override
    public void update(String args) {
        update();
    }

}

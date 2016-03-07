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
        updateLight(ticker.getTickTimeInS());
        for (TrafficLight tl : getTrafficLights()) {
            if (tl.isGreen()) {
                for (int i = 0; i < tl.getMaxFlow(); i++) {
                    Vehicle veh = tl.getWaiting().poll();
                    if (veh == null) {
                        return;
                    }
                    veh.addToTraceLog(this);
                    Intersection placeToGo = veh.nextPlaceToGo();
                    Road r = null;
                    for (Road road : getRoads()) {
                        if (road.getEnd() == placeToGo) {
                            r = road;
                        }
                    }
                    if (r == null) {
                        Logger.LogError("Can't find a place to go to reach destination", veh);
                        for (Road road : getRoads()) {
                            if (road.getEnd() != this) {
                                r = road;
                                break;
                            }
                        }
                    }
                    veh.setPosition(new AbstractMap.SimpleImmutableEntry<>(r, 0d));
                }
            }
        }
    }

    @Override
    public void update(String args) {
        update();
    }

}

package map.intersection;

import controller.Observer;
import controller.Ticker;
import helper.Logger;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import map.Intersection;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

/**
 *
 * @author Kareem Horstink
 */
public class SimpleTrafficLight extends Intersection implements Observer {

    private Ticker ticker;

    public SimpleTrafficLight(int x, int y, Ticker t) {
        super(x, y);
        t.addObserver(this);
        ticker = t;
    }

    @Override
    public void update() {
        updateLights(ticker.getTimeBetweenTick() * 1000);
        TrafficLight tl = getCurrentlyGreen();
        int i = 0;
        while (i++ < tl.getMaxFlow()) {
            if (tl.getQueue().isEmpty()) {
                return;
            }
            Vehicle v = tl.getQueue().poll();
            if(v ==null){
                return;
            }
            v.addToTraceLog(this);
            Intersection placeToGo = v.nextPlaceToGo();
            if(placeToGo == null){
                Logger.LogHigh("The car doesn't know where it wants to go");
            }
            Road r = null;
            for (Road road : getRoads()) {
                if (road.getStart() == placeToGo) {
                    r = road;
                }
            }
            if (r == null) {
                Logger.LogError("Vehicle has no where to go", this);
                for (Road road : getRoads()) {
                    if(road.getEnd() != this){
                        r = road;
                        break;
                    }
                }
            }

            v.setPosition(new SimpleImmutableEntry<>(r, 0d));
        }

    }

    @Override
    public void update(String args) {
        update();
    }

}

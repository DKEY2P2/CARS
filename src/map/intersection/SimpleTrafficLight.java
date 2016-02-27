package map.intersection;

import controller.Observer;
import controller.Ticker;
import helper.Logger;
import java.util.AbstractMap;
import java.util.AbstractMap.SimpleImmutableEntry;
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
            }else{
                System.out.println("Green");
            }
            Vehicle v = tl.getQueue().poll();
            v.addToTraceLog(this);
            Road r = null;
            for (Road road : getRoads()) {
                if (road.getEnd() == v.nextPlaceToGo()) {
                    r = road;
                }
            }
            Logger.LogError("Vehicle has no where to go", this);
            v.setPosition(new SimpleImmutableEntry<>(r, 0d));
        }

    }

    @Override
    public void update(String args) {
        update();
    }

}

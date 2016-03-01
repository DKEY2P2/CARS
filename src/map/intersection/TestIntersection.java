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
public class TestIntersection extends Intersection implements Observer{

    private Ticker ticker;

    public TestIntersection(int x, int y, Ticker t){
        super(x, y);
        t.addObserver(this);
        ticker = t;
    }

    public void update(){
        updateLight(ticker.getTimeBetweenTick());
        for(TrafficLight tl : getTrafficLights()){
            Vehicle v = tl.getIn().getVehicles().poll();
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
            v.setPosition(new AbstractMap.SimpleImmutableEntry<>(r, 0d));
        }
    }

    @Override
    public void update(String args) {
        update();
    }

}

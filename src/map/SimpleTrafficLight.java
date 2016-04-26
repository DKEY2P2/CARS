package map;

import controller.Observer;
import controller.Ticker;
import helper.Logger;
import map.Intersection;
import map.Road;
import map.TrafficLight;
import vehicle.Vehicle;

import java.util.AbstractMap.SimpleImmutableEntry;

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

    //TODO: This class might be completely useless
    @Override
    public void update() {
        System.out.println("HERE");
        //updateLights((int) (ticker.getTimeBetweenTick() * ticker.getTickTimeInMS()));
        TrafficLight tl = getCurrentlyGreen();
        int i = 0;
        while (i++ < tl.getMaxFlow()) {
         if (tl.getWaiting().isEmpty()) {
         return;
         }
         Vehicle v = tl.getWaiting().poll();
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
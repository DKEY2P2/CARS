package map.intersection;

import controller.Observer;
import controller.Ticker;
import map.Intersection;
import map.Road;
import map.TrafficLight;

import java.util.ArrayList;

/**
 * Created by lvanp on 01/03/2016.
 *
 * @author lvanp, Kareem
 * @since 07.03.16
 */
public class DefaultIntersection extends Intersection implements Observer {

    private Ticker ticker;

    public DefaultIntersection(int x, int y, Ticker t) {
        super(x, y);
        t.addObserver(this);
        ticker = t;
    }

    public void update() {
        updateLight(ticker.getTimeBetweenTick()); //this should be the only thing updated in the intersection, the intersection is only responsible for its traffic lights, the cars should be responible for their location
        /*for (TrafficLight tl : getTrafficLights()) {
                if (tl.isGreen()) {
                    for (int i = 0; i < tl.getMaxFlow(); i++) {
                        Vehicle veh = tl.getWaiting().poll();
                        if (veh == null) {
                            return;
                        }
                        Road prev = veh.getPosition().getKey();
                        prev.getVehicles().remove(veh);
                        veh.addToTraceLog(this);
                        if (veh.getPosition().getKey().getEnd() == veh.getDestination()) {
                            VehicleHolder.getInstance().remove(veh);
                        } else {
                            Intersection placeToGo = veh.nextPlaceToGo();
                            Road r = null;
                            for (Road road : getRoads()) {
                                if (road.getEnd() == placeToGo) {
                                    r = road;
                                }
                            }
                            if (r == null) {
                                Logger.LogError("Can't find a place to go to reach destination", veh);
                                /*for (Road road : getRoads()) {
                                    if (road.getEnd() != this) {
                                        r = road;
                                        break;
                                    }
                                }
                                VehicleHolder.getInstance().remove(veh);
                            }else{
                                veh.setPosition(new AbstractMap.SimpleImmutableEntry<>(r, 0d));
                                r.addCar(veh);
                            }
                        }
                    }
                }
            }*/
    }

    @Override
    public Road addRoad(Road r) {
        getRoads().add(r);
        boolean flag = false;
        if (r.getStart() == this) {
            out.add(r);
            flag = true;
        } else {
            in.add(r);
        }

        if (flag) {
            getTrafficLights().stream().forEach((trafficLight) -> {
                trafficLight.addOut(r);
            });
        } else {
            if(in.size() >= 2 ){ //This is American style (says Kareem :P)
            	if(getTrafficLights().isEmpty()){
            		getTrafficLights().add(new TrafficLight(this,in.get(0), out));
            	}
            		getTrafficLights().add(new TrafficLight(this, r, out));
            }
        }

        return r;
    }

    ArrayList<Road> in = new ArrayList<>();
    ArrayList<Road> out = new ArrayList<>();

    @Override
    public void update(String args) {
        update();
    }

}

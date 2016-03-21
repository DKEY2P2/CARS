package map.intersection;

import controller.Observer;
import controller.SimulationSettings;
import controller.Ticker;
import helper.Logger;
import map.Intersection;
import map.Road;
import map.TrafficLight;
import models.OVM;
import vehicle.Vehicle;
import vehicle.VehicleHolder;

import java.util.AbstractMap;
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
        updateLight(ticker.getTimeBetweenTick());
            for (TrafficLight tl : getTrafficLights()) {
                if (tl.isGreen()) {
                    for (int i = 0; i < tl.getMaxFlow(); i++) {
                        Vehicle veh = tl.getWaiting().poll();
                        if (veh == null) {
                            return;
                        }
                        veh.addToTraceLog(this);
                        if (veh.getPosition().getKey().getEnd() == veh.getDestination()) {
                            VehicleHolder.getInstance().remove(veh);
                        } else {
                            Intersection placeToGo = veh.nextPlaceToGo();
                            Road r = null;
                            for (Road road : getRoads()) {
                                if (road.getEnd() == placeToGo&& road.getStart()==this) {
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
                            veh.getPosition().getKey().getVehicles().remove(veh);
                            r.getVehicles().offer(veh);// TODO : Is this correct ???
                            veh.setPosition(new AbstractMap.SimpleImmutableEntry<>(r, 0d));
                        }
                    }
                }
            }
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
            getTrafficLights().add(new TrafficLight(this, r, out));
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
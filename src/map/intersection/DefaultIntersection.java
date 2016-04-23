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
        updateLight(ticker.getTimeBetweenTick());
        //this should be the only thing updated in the intersection, the intersection is only responsible for its traffic lights, the cars should be responible for their location

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

    private ArrayList<Road> in = new ArrayList<>();
    private ArrayList<Road> out = new ArrayList<>();

    public ArrayList<Road> getIn() {
        return in;
    }

    public ArrayList<Road> getOut() {
        return out;
    }

    @Override
    public void update(String args) {
        update();
    }

}
